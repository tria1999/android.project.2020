package gr.uom.dai18085_triantafyllos_tsiakiris_project_2020_21;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import twitter4j.Status;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PostSearchAdapter extends RecyclerView.Adapter<PostSearchAdapter.postSearchViewHolder> {


    private Context context;
    private List<RecyclerPost> recyclerPosts;
    public List<ArrayList<String>> imageUrls;
    private ArrayList<Status> replies;

    public PostSearchAdapter(Context context, List<RecyclerPost> recyclerPosts){

        this.context = context;
        this.recyclerPosts = recyclerPosts;
        this.imageUrls = new ArrayList<>();
        this.replies = new ArrayList<>();

    }

    @NonNull
    @Override
    public postSearchViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_post, parent, false);
        View view =  inflater.inflate(R.layout.layout_post, parent, false);
        return new postSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull postSearchViewHolder holder, int position) {
        holder.profileNameView.setText(recyclerPosts.get(position).getUsername());
        holder.postTextView.setText(recyclerPosts.get(position).getText());


        Picasso.get()
                .load(recyclerPosts.get(position).getProfileImage().replace("http:", "https:"))
                .into(holder.profileImageView);
            //holder.postImageView.setImageURI(uri);

        switch (recyclerPosts.get(position).getSmn()){
            case "twitter": holder.smnImageView.setImageResource(R.drawable.twitter_logo);
                break;
            case "facebook": holder.smnImageView.setImageResource(R.drawable.facebook_logo);
                break;
            case "instagram": holder.smnImageView.setImageResource(R.drawable.instagram_logo);
                break;
        }

        holder.detailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, PostDetailsActivity.class);
                intent.putExtra("username",recyclerPosts.get(position).getUsername());
                intent.putExtra("text",recyclerPosts.get(position).getText());
                intent.putExtra("smn",recyclerPosts.get(position).getSmn());
                intent.putExtra("profileImage", recyclerPosts.get(position).getProfileImage());
                if(!imageUrls.isEmpty())
                    intent.putStringArrayListExtra("imageUrls",imageUrls.get(position));
                else
                {
                    ArrayList<String> emptyList = new ArrayList<>();
                    emptyList.add("empty");
                    intent.putStringArrayListExtra("imageUrls",emptyList);
                }
                if(!replies.isEmpty())
                {
                    ArrayList<String> rNames = new ArrayList<>();
                    for (Status s: replies){
                        rNames.add(s.getUser().getName());
                    }
                    ArrayList<String> rText = new ArrayList<>();
                    for (Status s: replies){
                        rNames.add(s.getText());
                    }
                    ArrayList<String> rSmn = new ArrayList<>();
                    for (Status s: replies){
                        rNames.add("twitter");
                    }
                    ArrayList<String> rProfileImage = new ArrayList<>();
                    for (Status s: replies){
                        rNames.add(s.getUser().get400x400ProfileImageURLHttps());
                    }
                    intent.putStringArrayListExtra("rNames",rNames);
                    intent.putStringArrayListExtra("rText",rText);
                    intent.putStringArrayListExtra("rSmn",rSmn);
                    intent.putStringArrayListExtra("rProfileImage",rProfileImage);

                }

                context.startActivity(intent);
            }
        });
    }

    public void passImages(List<ArrayList<String>> imageUrls){
        this.imageUrls = imageUrls;
    }
    public void passReplies(ArrayList<Status> replies){
        this.replies = replies;
    }
    @Override
    public int getItemCount() {
        return recyclerPosts.size();
    }

    public class postSearchViewHolder extends RecyclerView.ViewHolder{

        TextView profileNameView, postTextView;
        ImageView profileImageView, smnImageView;
        ConstraintLayout detailsLayout;
        public postSearchViewHolder(@NonNull  View itemView) {
            super(itemView);
            profileNameView = itemView.findViewById(R.id.profileNameView);
            postTextView = itemView.findViewById(R.id.postTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            smnImageView = itemView.findViewById(R.id.smnImageView);
            detailsLayout = itemView.findViewById(R.id.detailsLayout);
        }
    }
}
