package gr.uom.dai18085_triantafyllos_tsiakiris_project_2020_21;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import twitter4j.*;
import twitter4j.api.TrendsResources;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class HashtagSearchActivity extends AppCompatActivity {

    private Button searchButton, setButton;
    private EditText hashtagSearchText;
    private RecyclerView resultRecyclerView;
    private RecyclerView trendingRecyclerView;
    public String selectedHashtag;
    private List<String> trending;
    private String postProfileName[];
    private String postText[];
    private Uri authUri;
    private trendingAdapter trendingAdapter;
    private Trends trends;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtag_search);

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
/*
        Random random = ThreadLocalRandom.current();
        byte[] r = new byte[256]; //Means 2048 bit
        random.nextBytes(r);
        String nonce = new String(r);
        authUri = Uri.parse(authorizationUrl)
                .buildUpon()
                .appendQueryParameter("oauth_consumer_key", "yopTn4IEXy4OVuEt2V2iinOyP")
                .appendQueryParameter("oauth_nonce", nonce)
                .appendQueryParameter("oauth_signature", "code")
                .appendQueryParameter("oauth_signature_method", scopes.joinToString(separator = " "))
                .appendQueryParameter("oauth_timestamp", uniqueState)
                .appendQueryParameter("oauth_token",safafafa)
                .appendQueryParameter("oauth_version", afaf)
                .build()*/
        //test
        postProfileName = new String[]{"john doe", "jane doe", "mary poe", "mario poe"};
        postText = new String[]{"hi, im john","hi, im jane","hi, im mary","hi, im mario"};
        trending = new ArrayList<>();
        trending.add("#covid19");
        trending.add("#whoCaresAboutAmericanPolitics");
        trending.add("#someRandomHashtag");

        trendingAdapter = new trendingAdapter(this,trending);
        trendingRecyclerView.setAdapter(trendingAdapter);
        trendingRecyclerView.setLayoutManager(new LinearLayoutManager(this));


            //List<Status> status = twitter.getHomeTimeline();

        //test
        //Get trends from Twitter
/*
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.twitter.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //TrendsResources trendingHashtags = retrofit.create(TrendsResources.class);
       */

        try{
            // The factory instance is re-useable and thread safe.
            //twitter = TwitterFactory.getSingleton();
            Query query = new Query("#google");
            query.setCount(50);
            QueryResult result = twitter.search(query);

        }catch(Exception e){
            System.out.println(e);
        }

       /* try {
            trends = twitter.getPlaceTrends(1);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
*/



/*
        Call<List<Trend>> call = getTrendingHashtagsInterface.getTrends();

        call.enqueue(new Callback<List<Trend>>() {
            @Override
            public void onResponse(Call<List<Trend>> call, Response<List<Trend>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(HashtagSearchActivity.this,"Code: "+response.code(),Toast.LENGTH_LONG);
                    Log.e("call","Error code " + response.code());
                    return;
                }

                trending = response.body();
                trendingAdapter = new trendingAdapter(HashtagSearchActivity.this,trending);
                trendingRecyclerView.setAdapter(trendingAdapter);

            }

            @Override
            public void onFailure(Call<List<Trend>> call, Throwable t) {
                Toast.makeText(HashtagSearchActivity.this,"Failed to load trends!",Toast.LENGTH_LONG);
                Log.e("call","Failed to load trends!");

            }
        });
        */
        //Put trends on the RecyclerView
        postSearchAdapter postSearchAdapter = new postSearchAdapter(this,postProfileName,postText);
        resultRecyclerView.setAdapter(postSearchAdapter);
        resultRecyclerView.setLayoutManager(new LinearLayoutManager(this));





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
                try {
                    Query query = new Query(hashtagSearchText.getText().toString());
                    query.setCount(10);
                    QueryResult result = twitter.search(query);

                } catch (TwitterException e) {
                    e.printStackTrace();
                }

            }
        });

    }

}

