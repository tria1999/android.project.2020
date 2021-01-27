package gr.uom.dai18085_triantafyllos_tsiakiris_project_2020_21;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class PostDetailsActivity extends AppCompatActivity {

    ImageView profileImageView,postImageView;
    TextView usernameView, postTextView;

    String username,text, smn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        profileImageView = findViewById(R.id.dProfileImageView);
        postImageView = findViewById(R.id.dPostImageView);
        usernameView = findViewById(R.id.dUsernameView);
        postTextView = findViewById(R.id.dPostTextView);

        getData();
        setData();
    }

    private void getData(){
        if(getIntent().hasExtra("username")&&getIntent().hasExtra("text")&&
                getIntent().hasExtra("smn")){
            username = getIntent().getStringExtra("username");
            text = getIntent().getStringExtra("text");
            smn= getIntent().getStringExtra("smn");

        }
        else{
            Toast.makeText(this,"No data.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(){
        usernameView.setText(username);
        postTextView.setText(text);

    }

}