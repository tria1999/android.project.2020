package gr.uom.dai18085_triantafyllos_tsiakiris_project_2020_21;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;

public class HashtagSearchActivity extends AppCompatActivity {

    private Button searchButton, setButton;
    private EditText hashtagSearchText;
    private RecyclerView resultRecyclerView;
    private RecyclerView trendingRecyclerView;
    public String selectedHashtag;
    private List<String> trending;
    private trendingAdapter trendingAdapter;
    private Trends trends;
    private TrendsPasser trendsPasser;
    private TwitterSearchForPosts twitterSearcher;
    private Object trendsLock,searchLock,repliesLock;
    private PostSearchAdapter postSearchAdapter;
    private List<RecyclerPost> recyclerPosts;
    public ArrayList<Status> replies;
    private boolean searchPerformed = false;
    private boolean done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtag_search);

        trendsLock = new Object();
        searchLock= new Object();
        repliesLock = new Object();
        setButton = findViewById(R.id.setButton);
        searchButton = findViewById(R.id.searchButton);
        hashtagSearchText = findViewById(R.id.hashtagSearchText);
        resultRecyclerView = findViewById(R.id.resultRecyclerView);
        trendingRecyclerView = findViewById(R.id.trendingRecyclerView);

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(this.getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY))
                .setOAuthConsumerSecret(this.getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET))
                .setOAuthAccessToken(this.getResources().getString(R.string.com_twitter_sdk_android_ACCESS_TOKEN))
                .setOAuthAccessTokenSecret(this.getResources().getString(R.string.com_twitter_sdk_android_ACCESS_TOKEN_SECRET));
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        //test

        //Get trends from Twitter
        trending = new ArrayList<>();

        trendsPasser = new TrendsPasser(twitter,trends,trending, trendsLock);
        Thread trendsThread = new Thread(trendsPasser);
        trendsThread.start();

        //Put trends on the RecyclerView
        synchronized(trendsLock) {
            if (!trendsPasser.done) {
                try {
                    trendsLock.wait();
                    trendingAdapter = new trendingAdapter(HashtagSearchActivity.this,trending);
                    trendingRecyclerView.setAdapter(trendingAdapter);
                    trendingRecyclerView.setLayoutManager(new LinearLayoutManager(HashtagSearchActivity.this));
                } catch (InterruptedException e) {
                }
            }
        }

        hashtagSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hashtagSearchText.setText("#");
            }
        });
//https://graph.facebook.com/v9.0/ig_hashtag_search?user_id=17841405309211844&q="+hashtag+"&access_token="+MainActivity.getAccessToken()
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedHashtag = trendingAdapter.selectedHashtag;
                hashtagSearchText.setText(selectedHashtag);
            }
        });


        //search
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedHashtag = hashtagSearchText.getText().toString();
                Toast.makeText(HashtagSearchActivity.this, "Searching for "+selectedHashtag,Toast.LENGTH_SHORT).show();
                twitterSearcher = new TwitterSearchForPosts(twitter,selectedHashtag, searchLock);
                Thread searchThread = new Thread(twitterSearcher);
                searchThread.start();
                synchronized(searchLock) {
                    if (!twitterSearcher.done) {
                        try {
                            searchLock.wait();
                        } catch (InterruptedException e) {
                        }
                            QueryResult searchResult = twitterSearcher.getResult();
                            recyclerPosts = new ArrayList<>();
                            replies = new ArrayList<>();
                            List<ArrayList<String>> imageUrls = new ArrayList<>();
                            for(Status s: searchResult.getTweets())
                            {   //make recycler post
                                recyclerPosts.add(new RecyclerPost(s.getUser().getName(),s.getText(),"twitter",s.getUser().get400x400ProfileImageURL()));
                                /*for(Status r: searchResult.getTweets())
                                    if (r.getInReplyToStatusId() == s.getId()&&r.getUser().isFollowRequestSent()&&(s.getId()!=r.getId()))
                                        replies.add(s);*/
                                //prepare to pass the photos to details activity
                                ArrayList<String> statusImageUrls = new ArrayList<String>();
                                for(int i=0;i< s.getMediaEntities().length;i++)
                                {
                                    if(s.getMediaEntities()[i].getType().equals("photo")){
                                        statusImageUrls.add(s.getMediaEntities()[i].getMediaURL());
                                    }
                                }
                                imageUrls.add(statusImageUrls);
                                done = false;
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        replies = getDiscussion(s, twitter);
                                        synchronized (repliesLock) {
                                            done=true;
                                            repliesLock.notify();
                                        }
                                    }
                                }).start();

                            }

                            postSearchAdapter = new PostSearchAdapter(HashtagSearchActivity.this,recyclerPosts);
                            postSearchAdapter.passImages(imageUrls);
                            synchronized (repliesLock){
                                if(!done){
                                    try{
                                        repliesLock.wait();
                                    }catch(InterruptedException e){
                                        e.printStackTrace();
                                    }
                                    postSearchAdapter.passReplies(replies);
                                }
                            }

                            resultRecyclerView.setAdapter(postSearchAdapter);
                            resultRecyclerView.setLayoutManager(new LinearLayoutManager(HashtagSearchActivity.this));


                    }
                }
                searchPerformed = true;

            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull @org.jetbrains.annotations.NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("searchPerformed",searchPerformed);
        outState.putString("selectedHashtag",selectedHashtag);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        hashtagSearchText.setText(savedInstanceState.getString("selectedHashtag"));
        if(savedInstanceState.getBoolean("searchPerformed"))
            searchButton.performClick();
    }

    public ArrayList<Status> getDiscussion(Status status, Twitter twitter) {
        ArrayList<Status> replies = new ArrayList<>();

        ArrayList<Status> all = null;

        try {
            long id = status.getId();
            String screenname = status.getUser().getScreenName();

            Query query = new Query("@" + screenname + " since_id:" + id);

            //System.out.println("query string: " + query.getQuery());

            try {
                query.setCount(100);
            } catch (Throwable e) {
                // enlarge buffer error?
                query.setCount(30);
            }

            QueryResult result = twitter.search(query);
            //System.out.println("result: " + result.getTweets().size());

            all = new ArrayList<Status>();

            do {
                //System.out.println("do loop repetition");

                List<Status> tweets = result.getTweets();

                for (Status tweet : tweets)
                    if (tweet.getInReplyToStatusId() == id)
                        all.add(tweet);

                if (all.size() > 0) {
                    for (int i = all.size() - 1; i >= 0; i--)
                        replies.add(all.get(i));
                    all.clear();
                }

                query = result.nextQuery();

                if (query != null)
                    result = twitter.search(query);

            } while (query != null);

        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        done = true;
        return replies;
    }
}

