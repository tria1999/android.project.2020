package gr.uom.dai18085_triantafyllos_tsiakiris_project_2020_21;

import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.squareup.picasso.Picasso;

public class PostDetailsActivity extends AppCompatActivity {

    ImageView profileImageView,postImageView;
    TextView usernameView, postTextView;

    String username,text, smn, profileImage;
    Uri profileImageUri;

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
                getIntent().hasExtra("smn")&&getIntent().hasExtra("profileImage")){
            username = getIntent().getStringExtra("username");
            text = getIntent().getStringExtra("text");
            smn= getIntent().getStringExtra("smn");
            profileImage = getIntent().getStringExtra("profileImage");
            profileImageUri = Uri.parse(profileImage);

        }
        else{
            Toast.makeText(this,"No data.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(){
        usernameView.setText(username);
        postTextView.setText(text);
        Picasso.with(this)
                .load(profileImageUri)
                .resize(profileImageView.getMaxWidth(), profileImageView.getMaxHeight())
                .centerInside()
                .into(profileImageView);

    }

}