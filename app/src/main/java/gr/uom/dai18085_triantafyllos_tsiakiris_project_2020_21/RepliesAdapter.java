package gr.uom.dai18085_triantafyllos_tsiakiris_project_2020_21;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import twitter4j.Status;

import java.util.List;

public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.repliesViewHolder>{
    Context context;
    List<Status> replies;

    public RepliesAdapter(Context context, List<Status> replies){

        this.context = context;
        this.replies = replies;

    }

    @NonNull
    @Override
    public RepliesAdapter.repliesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_trending, parent, false);
        View view =  inflater.inflate(R.layout.layout_trending, parent, false);
        return new RepliesAdapter.repliesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepliesAdapter.repliesViewHolder holder, int position) {
        holder.profileNameView.setText(replies.get(position).getUser().getName());
        holder.postTextView.setText(replies.get(position).getText());


    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    public class repliesViewHolder extends RecyclerView.ViewHolder{

        TextView profileNameView, postTextView;
        ImageView profileImageView, smnImageView;
        ConstraintLayout detailsLayout;

        public repliesViewHolder(@NonNull  View itemView) {
            super(itemView);
            profileNameView = itemView.findViewById(R.id.profileNameView);
            postTextView = itemView.findViewById(R.id.postTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            smnImageView = itemView.findViewById(R.id.smnImageView);
            detailsLayout = itemView.findViewById(R.id.detailsLayout);

        }


    }
}
