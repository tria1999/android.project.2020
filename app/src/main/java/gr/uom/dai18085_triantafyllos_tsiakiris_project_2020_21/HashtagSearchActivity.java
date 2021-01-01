package gr.uom.dai18085_triantafyllos_tsiakiris_project_2020_21;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URI;

public class HashtagSearchActivity extends AppCompatActivity {

    private Button searchButton;
    private EditText hashtagSearchText;
    private RecyclerView resultRecyclerView;
    private String hashtag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtag_search);

        searchButton = findViewById(R.id.searchButton);
        hashtagSearchText = findViewById(R.id.hashtagSearchText);
        resultRecyclerView = findViewById(R.id.resultRecyclerView);
//https://graph.facebook.com/v9.0/ig_hashtag_search?user_id=17841405309211844&q="+hashtag+"&access_token="+MainActivity.getAccessToken()
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            @GET
            public void onClick(View v) {
                hashtag = hashtagSearchText.getText().toString();



            }
        });

    }
}