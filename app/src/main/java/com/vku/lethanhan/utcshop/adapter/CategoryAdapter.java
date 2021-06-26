package com.vku.lethanhan.utcshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vku.lethanhan.utcshop.R;
import com.vku.lethanhan.utcshop.activity.ViewProductActivity;
import com.vku.lethanhan.utcshop.model.Category;
import com.vku.lethanhan.utcshop.util.Server;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    ArrayList<Category> list_categories;

    public CategoryAdapter(Context context, ArrayList<Category> list_categories) {
        this.context = context;
        this.list_categories = list_categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.categories_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = list_categories.get(position);
        Glide.with(context).load(Server.categoryFolderUrl+category.getImage()).into(holder.img_category);
        holder.txt_category.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return list_categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView img_category;
        TextView txt_category;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            img_category = itemView.findViewById(R.id.img_category);
            txt_category = itemView.findViewById(R.id.txt_category);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewProductActivity.class);
                    intent.putExtra("category_id", String.valueOf(list_categories.get(getAdapterPosition()).getId()));

                    context.startActivity(intent);
                }
            });
        }
    }
}
