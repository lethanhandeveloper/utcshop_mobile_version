package com.vku.lethanhan.utcshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vku.lethanhan.utcshop.activity.DetailActivity;
import com.vku.lethanhan.utcshop.model.Product;
import com.vku.lethanhan.utcshop.R;
import com.vku.lethanhan.utcshop.util.Server;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.NewProductViewHolder> {

    Context context;
    List<Product> products;


    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;


    }

    @NonNull
    @Override
    public NewProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, null);
        return new NewProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewProductViewHolder holder, int position) {
        Product product = products.get(position);

        Glide.with(context).load(Server.productFolderUrl+product.getImage().trim()).into(holder.img_product);

        Log.e("Link images", Server.productFolderUrl+product.getImage().trim());

        holder.txt_name_product.setText(product.getName());
        holder.txt_price_product.setText(new DecimalFormat("###,###,###").format(product.getPrice())+ " Đ");
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class NewProductViewHolder extends RecyclerView.ViewHolder{

        ImageView img_product;
        TextView txt_name_product;
        TextView txt_price_product;

        public NewProductViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            txt_name_product = itemView.findViewById(R.id.txt_name_product);
            txt_price_product = itemView.findViewById(R.id.txt_price_product);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("product_info", products.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
