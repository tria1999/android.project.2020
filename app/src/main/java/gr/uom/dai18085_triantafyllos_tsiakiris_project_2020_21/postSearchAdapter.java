package gr.uom.dai18085_triantafyllos_tsiakiris_project_2020_21;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class postSearchAdapter extends RecyclerView.Adapter<postSearchAdapter.postSearchViewHolder> {

    Context context;
    String profileName[];
    String postText[];
    public postSearchAdapter(Context context, String profileName[], String postText[]){

        this.context = context;
        this.profileName = profileName;
        this.postText = postText;
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
        holder.profileNameView.setText(profileName[position]);
        holder.postTextView.setText(postText[position]);
        //holder.profileImageView.setImageResource();
        //holder.smnImageView.setImageResource();
    }

    @Override
    public int getItemCount() {
        return profileName.length;
    }

    public class postSearchViewHolder extends RecyclerView.ViewHolder{

        TextView profileNameView, postTextView;
        ImageView profileImageView, smnImageView;

        public postSearchViewHolder(@NonNull  View itemView) {
            super(itemView);
            profileNameView = itemView.findViewById(R.id.profileNameView);
            postTextView = itemView.findViewById(R.id.postTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            smnImageView = itemView.findViewById(R.id.smnImageView);

        }
    }
}
