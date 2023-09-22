package com.aadevelopers.onlyscratchiapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.aadevelopers.onlyscratchiapp.R;
import com.aadevelopers.onlyscratchiapp.models.LeaderBoard;
import com.aadevelopers.onlyscratchiapp.utils.BaseUrl;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderBoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<LeaderBoard> leaderBoards;
    private final Context context;

    public LeaderBoardAdapter(List<LeaderBoard> leaderBoards, Context context) {
        this.leaderBoards = leaderBoards;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        viewHolder = new ImageHolder(inflater.inflate(R.layout.recyclerview_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ImageHolder holder1 = (ImageHolder) holder;
        LeaderBoard leaderBoard = leaderBoards.get(position);
        if (leaderBoard.getId() != null) {
            holder1.rank_textView.setText("#" + leaderBoard.getId());
        }
        if (leaderBoard.getName() != null) {
            holder1.name_textView.setText(leaderBoard.getName());
        }
        if (leaderBoard.getPoints() != null) {
            holder1.points_textView.setText(leaderBoard.getPoints());
        }
        Glide.with(context)
                .load(BaseUrl.LOAD_USER_IMAGE + leaderBoard.getImage())
                .centerCrop()
                .placeholder(R.drawable.profile)
                .into(holder1.profile_image);
    }

    @Override
    public int getItemCount() {
        return leaderBoards.size();
    }

    public static class ImageHolder extends RecyclerView.ViewHolder {
        private final CircleImageView profile_image;
        private final TextView rank_textView, name_textView, points_textView;

        public ImageHolder(View itemView) {
            super(itemView);
            this.rank_textView = itemView.findViewById(R.id.rank_textView);
            this.profile_image = itemView.findViewById(R.id.profile_image);
            this.name_textView = itemView.findViewById(R.id.name_textView);
            this.points_textView = itemView.findViewById(R.id.points_textView);

        }
    }
}
