package gr.uom.dai18085_triantafyllos_tsiakiris_project_2020_21;

import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import twitter4j.MediaEntity;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

public class PostDetailsActivity extends AppCompatActivity {

    ImageView profileImageView,postImageView;
    TextView usernameView, postTextView;
    RecyclerView repliesRecyclerView;
    String username,text, smn, profileImage;
    PostSearchAdapter postSearchAdapter;
    List<String> imageUrls;
    ArrayList<Status> replies;
    List<RecyclerPost> recyclerPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        profileImageView = findViewById(R.id.dProfileImageView);
        postImageView = findViewById(R.id.dPostImageView);
        usernameView = findViewById(R.id.dUsernameView);
        postTextView = findViewById(R.id.dPostTextView);
        repliesRecyclerView = findViewById(R.id.repliesRecyclerView);
        replies = new ArrayList<>();
        recyclerPosts = new ArrayList<>();
        getData();
        setData();
    }

    private void getData(){
        if(getIntent().hasExtra("username")&&getIntent().hasExtra("text")&&
                getIntent().hasExtra("smn")&&getIntent().hasExtra("profileImage")
                &&getIntent().hasExtra("imageUrls")){
            username = getIntent().getStringExtra("username");
            text = getIntent().getStringExtra("text");
            smn= getIntent().getStringExtra("smn");
            profileImage = getIntent().getStringExtra("profileImage");
            imageUrls = getIntent().getStringArrayListExtra("imageUrls");
            if(getIntent().hasExtra("rNames")&&getIntent().hasExtra("rText")&&
                    getIntent().hasExtra("rSmn")&&getIntent().hasExtra("rProfileImage")){
                for(int i=0;i< getIntent().getStringArrayListExtra("rNames").size();i++)
                {
                    recyclerPosts.add(new RecyclerPost(
                            getIntent().getStringArrayListExtra("rNames").get(i),
                            getIntent().getStringArrayListExtra("rText").get(i),
                            getIntent().getStringArrayListExtra("rSmn").get(i),
                            getIntent().getStringArrayListExtra("rProfileImage").get(i)
                            ));
                }

            }
        }

        else{
            Toast.makeText(this,"No data.", Toast.LENGTH_LONG).show();
        }

    }

    private void setData(){
        usernameView.setText(username);
        postTextView.setText(text);
        Picasso.get()
                .load(profileImage.replace("http:", "https:"))
                .into(profileImageView);
        if(!imageUrls.isEmpty())
            if(!imageUrls.get(0).equals("empty"))
                Picasso.get()
                        .load(imageUrls.get(0).replace("http:", "https:"))
                        .into(postImageView);

        if(recyclerPosts.isEmpty())
            recyclerPosts.add(new RecyclerPost("Test User","This is a fake post, there are no replies!","facebook","@drawable/ic_launcher_foreground"));



        postSearchAdapter = new PostSearchAdapter(PostDetailsActivity.this,recyclerPosts);
        repliesRecyclerView.setAdapter(postSearchAdapter);
        repliesRecyclerView.setLayoutManager(new LinearLayoutManager(PostDetailsActivity.this));
    }

}