package com.vku.lethanhan.utcshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vku.lethanhan.utcshop.R;
import com.vku.lethanhan.utcshop.model.Cart;
import com.vku.lethanhan.utcshop.model.Comment;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    Context context;
    ArrayList<Comment> comments;

    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }


    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, null);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.txt_name.setText(comment.getName());
        holder.txt_content.setText(comment.getContent());
        holder.txt_time.setText(comment.getTime());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name;
        TextView txt_time;
        TextView txt_content;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_name);
            txt_time = itemView.findViewById(R.id.txt_time);
            txt_content = itemView.findViewById(R.id.txt_content);
        }
    }
}
