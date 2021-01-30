package gr.uom.dai18085_triantafyllos_tsiakiris_project_2020_21;

import android.content.Context;
import android.content.Intent;
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

import java.util.List;

public class postSearchAdapter extends RecyclerView.Adapter<postSearchAdapter.postSearchViewHolder> {

    Context context;
    List<RecyclerPost> recyclerPosts;
    public postSearchAdapter(Context context, List<RecyclerPost> recyclerPosts){

        this.context = context;
        this.recyclerPosts = recyclerPosts;

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

        if(!recyclerPosts.get(position).getImageUrls().isEmpty())
        {

            Uri uri = Uri.parse(recyclerPosts.get(position).getProfileImage());
            Picasso.with(context)
                    .load(uri)
                    .resize(holder.profileImageView.getMaxWidth(), holder.profileImageView.getMaxHeight())
                    .centerInside()
                    .into(holder.profileImageView);
            //holder.postImageView.setImageURI(uri);

        }
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
                context.startActivity(intent);
            }
        });
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
