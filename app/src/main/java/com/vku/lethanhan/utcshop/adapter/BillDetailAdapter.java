package com.vku.lethanhan.utcshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vku.lethanhan.utcshop.R;
import com.vku.lethanhan.utcshop.model.Cart;
import com.vku.lethanhan.utcshop.util.Server;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BillDetailAdapter extends RecyclerView.Adapter<BillDetailAdapter.BillDetailViewHolder> {

    Context context;
    ArrayList<Cart> bill_details;

    public BillDetailAdapter(Context context, ArrayList<Cart> bill_details) {
        this.context = context;
        this.bill_details = bill_details;
    }

    @NonNull
    @Override
    public BillDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bill_detail_order_item, null);
        return new BillDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillDetailViewHolder holder, int position) {
        Cart cart = bill_details.get(position);

        Glide.with(context).load(Server.productFolderUrl+cart.getImage()).into(holder.img_product);
        holder.txt_name_product.setText(cart.getName());
        holder.txt_price.setText("Giá :" + new DecimalFormat("###,###,###").format(cart.getcurrentPrice()) + " đ");
        holder.txt_quantity.setText("Số lượng X" +cart.getQuantity());
        holder.txt_total_price_item.setText("Tổng tiền :" + new DecimalFormat("###,###,###").format(cart.getcurrentPrice()*cart.getQuantity()) + "đ");
    }

    @Override
    public int getItemCount() {
        return bill_details.size();
    }

    public class BillDetailViewHolder extends RecyclerView.ViewHolder {

        ImageView img_product;
        TextView txt_name_product;
        TextView txt_price;
        TextView txt_quantity;
        TextView txt_total_price_item;

        public BillDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            txt_name_product = itemView.findViewById(R.id.txt_name_product);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_quantity = itemView.findViewById(R.id.txt_quantity);
            txt_total_price_item = itemView.findViewById(R.id.txt_total_price_item);
        }
    }
}

