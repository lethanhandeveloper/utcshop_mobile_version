package com.vku.lethanhan.utcshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vku.lethanhan.utcshop.R;
import com.vku.lethanhan.utcshop.activity.DetailActivity;
import com.vku.lethanhan.utcshop.activity.MainActivity;
import com.vku.lethanhan.utcshop.model.Product;
import com.vku.lethanhan.utcshop.util.FlyToCartAnimation;
import com.vku.lethanhan.utcshop.util.Server;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ViewProductAdapter extends RecyclerView.Adapter<ViewProductAdapter.ProductViewHolder>{

    Context context;
    ArrayList<Product> arrayList;

    public ViewProductAdapter(Context context, ArrayList<Product> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_product_item, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = arrayList.get(position);
        
        Glide.with(context).load(Server.productFolderUrl+product.getImage().trim()).into(holder.img_product);
        holder.txt_name_product.setText(product.getName());
        if(product.getDiscount()>0){
            int current_price = (product.getPrice()-(product.getPrice()*product.getDiscount()/100));
            holder.txt_price_view.setText(new DecimalFormat("###,###,###").format(current_price) +" Đ");
            holder.txt_old_price_view.setText(new DecimalFormat("###,###,###").format(product.getPrice()) +" Đ");
            holder.txt_old_price_view.setPaintFlags(holder.txt_old_price_view.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.txt_price_view.setText(String.valueOf(product.getPrice()));
        }

        if (product.isStock()){
            holder.txt_stock.setText("Còn hàng");
        }else {
            holder.txt_stock.setText("Hết hàng");
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img_product;
        TextView txt_name_product;
        TextView txt_price_view;
        TextView txt_old_price_view;
        TextView txt_stock;
        ImageView img_item_fly;
        Button btn_add_cart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            img_product = itemView.findViewById(R.id.img_product);
            txt_name_product = itemView.findViewById(R.id.txt_name_product);
            txt_price_view = itemView.findViewById(R.id.txt_price_view);
            txt_old_price_view = itemView.findViewById(R.id.txt_old_price_view);
            img_item_fly = itemView.findViewById(R.id.img_item_fly);
            txt_stock = itemView.findViewById(R.id.txt_stock);

            btn_add_cart = itemView.findViewById(R.id.btn_add_cart);
            btn_add_cart.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("product_info", arrayList.get(getLayoutPosition()));
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.btn_add_cart:
//                    FlyToCartAnimation.translateAnimation(img_item_fly, img_product, img_cart, new Animation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart(Animation animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animation animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animation animation) {
//
//                        }
//                    });
//                    break;
            }
        }
    }
}
