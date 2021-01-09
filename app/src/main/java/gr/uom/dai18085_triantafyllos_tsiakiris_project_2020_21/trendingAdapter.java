package gr.uom.dai18085_triantafyllos_tsiakiris_project_2020_21;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class trendingAdapter extends RecyclerView.Adapter<trendingAdapter.trendingViewHolder>{
    Context context;
    String trendingHashtags[];
    String selectedHashtag;

    public trendingAdapter(Context context, String trendingHashtags[]){

        this.context = context;
        this.trendingHashtags = trendingHashtags;
        this.selectedHashtag = trendingHashtags[0];
    }

    @NonNull
    @Override
    public trendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_trending, parent, false);
        View view =  inflater.inflate(R.layout.layout_trending, parent, false);
        return new trendingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull trendingViewHolder holder, int position) {
        holder.trendingView.setText(trendingHashtags[position]);
        final int pos = position;
        holder.innerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedHashtag = trendingHashtags[pos];
                //Intent i = new Intent(context,HashtagSearchActivity.class);
                //i.putExtra("selectedHashtag", selectedHashtag);
            }
        });

    }

    @Override
    public int getItemCount() {
        return trendingHashtags.length;
    }

    public class trendingViewHolder extends RecyclerView.ViewHolder{

        TextView trendingView;
        ConstraintLayout innerLayout;

        public trendingViewHolder(@NonNull  View itemView) {
            super(itemView);
            trendingView = itemView.findViewById(R.id.trendingView);
            innerLayout = itemView.findViewById(R.id.layoutInner);

        }
    }
}
