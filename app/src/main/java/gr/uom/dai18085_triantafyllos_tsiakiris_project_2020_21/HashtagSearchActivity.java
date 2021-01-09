package gr.uom.dai18085_triantafyllos_tsiakiris_project_2020_21;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HashtagSearchActivity extends AppCompatActivity {

    private Button searchButton, setButton;
    private EditText hashtagSearchText;
    private RecyclerView resultRecyclerView;
    private RecyclerView trendingRecyclerView;
    public String selectedHashtag;
    private String trending[];
    private String postProfileName[];
    private String postText[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtag_search);

        setButton = findViewById(R.id.setButton);
        searchButton = findViewById(R.id.searchButton);
        hashtagSearchText = findViewById(R.id.hashtagSearchText);
        resultRecyclerView = findViewById(R.id.resultRecyclerView);
        trendingRecyclerView = findViewById(R.id.trendingRecyclerView);


        //test
        postProfileName = new String[]{"john doe", "jane doe", "mary poe", "mario poe"};
        postText = new String[]{"hi, im john","hi, im jane","hi, im mary","hi, im mario"};
        trending = new String[]{"#covid19","#whoCaresAboutAmericanPolitics","#someRandomHashtag"};


        //test


        postSearchAdapter postSearchAdapter = new postSearchAdapter(this,postProfileName,postText);
        resultRecyclerView.setAdapter(postSearchAdapter);
        resultRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        final trendingAdapter trendingAdapter = new trendingAdapter(this,trending);
        trendingRecyclerView.setAdapter(trendingAdapter);
        trendingRecyclerView.setLayoutManager(new LinearLayoutManager(this));



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
            }
        });

    }

}