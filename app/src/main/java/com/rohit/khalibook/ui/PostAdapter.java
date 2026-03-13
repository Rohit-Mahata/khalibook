package com.rohit.khalibook.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rohit.khalibook.R;
import com.rohit.khalibook.data.local.entity.PostEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<PostEntity> posts;
    private OnPostActionListener listener;
    private int currentUserId;

    public interface OnPostActionListener {
        void onLikeClick(PostEntity post);
        void onCommentClick(PostEntity post);
        void onDeleteClick(PostEntity post);
    }

    public PostAdapter(List<PostEntity> posts, int currentUserId, OnPostActionListener listener) {
        this.posts = posts;
        this.currentUserId = currentUserId;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostEntity post = posts.get(position);
        holder.tvUsername.setText(post.username);
        holder.tvContent.setText(post.content);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault());
        holder.tvTimestamp.setText(sdf.format(new Date(post.timestamp)));

        holder.btnLike.setText("Like (" + post.likeCount + ")");
        holder.btnComment.setText("Comment (" + post.commentCount + ")");

        holder.btnDelete.setVisibility(post.userId == currentUserId ? View.VISIBLE : View.GONE);

        holder.btnLike.setOnClickListener(v -> listener.onLikeClick(post));
        holder.btnComment.setOnClickListener(v -> listener.onCommentClick(post));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(post));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setPosts(List<PostEntity> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvContent, tvTimestamp;
        Button btnLike, btnComment, btnDelete;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnComment = itemView.findViewById(R.id.btnComment);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
