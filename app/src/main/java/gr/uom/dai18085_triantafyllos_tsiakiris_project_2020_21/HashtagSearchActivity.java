package gr.uom.dai18085_triantafyllos_tsiakiris_project_2020_21;

import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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
    private SearchForPosts searcher;
    private Object trendsLock,searchLock;
    private postSearchAdapter postSearchAdapter;
    private List<RecyclerPost> recyclerPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtag_search);

        trendsLock = new Object();
        searchLock= new Object();

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
        trending.add("#covid19");
        trending.add("#whoCaresAboutAmericanPolitics");
        trending.add("#someRandomHashtag");

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
                hashtagSearchText.setText("");
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



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedHashtag = hashtagSearchText.getText().toString();
                Toast.makeText(HashtagSearchActivity.this, "Searching for "+selectedHashtag,Toast.LENGTH_LONG).show();
                searcher = new SearchForPosts(twitter,selectedHashtag, searchLock);
                Thread searchThread = new Thread(searcher);
                searchThread.start();
                synchronized(searchLock) {
                    if (!searcher.done) {
                        try {
                            searchLock.wait();
                            QueryResult searchResult = searcher.getResult();
                            recyclerPosts = new ArrayList<>();
                            for(Status s: searchResult.getTweets())
                                recyclerPosts.add(new RecyclerPost(s.getUser().getName(),s.getText(),"twitter",s.getMediaEntities()));

                            postSearchAdapter = new postSearchAdapter(HashtagSearchActivity.this,recyclerPosts);
                            resultRecyclerView.setAdapter(postSearchAdapter);
                            resultRecyclerView.setLayoutManager(new LinearLayoutManager(HashtagSearchActivity.this));
                        } catch (InterruptedException e) {
                        }
                    }
                }

            }
        });

    }


}

