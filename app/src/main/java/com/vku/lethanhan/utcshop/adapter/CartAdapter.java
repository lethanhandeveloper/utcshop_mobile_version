package com.vku.lethanhan.utcshop.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vku.lethanhan.utcshop.activity.CartActitvity;
import com.vku.lethanhan.utcshop.api.ApiService;
import com.vku.lethanhan.utcshop.data_local.AccessTokenManager;
import com.vku.lethanhan.utcshop.model.ApiResponse;
import com.vku.lethanhan.utcshop.model.Cart;
import com.vku.lethanhan.utcshop.R;
import com.vku.lethanhan.utcshop.util.Server;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartProductViewHolder> {

    Context context;
    ArrayList<Cart> carts = new ArrayList<>();
    public CartAdapter(Context context, ArrayList<Cart> carts) {
        this.context = context;
        this.carts = carts;
    }

    @NonNull
    @Override
    public CartProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductViewHolder holder, int position) {
        Cart cart = carts.get(position);
        holder.txt_name.setText(cart.getName());
        holder.txt_quantity_number.setText(String.valueOf(cart.getQuantity()));

        if(cart.isChecked()){
            holder.checkbox.setChecked(true);
        }else{
            holder.checkbox.setChecked(false);
        }
        Log.e("Cartinfo", cart.toString());
//        if(cart.getDiscount()>0){
//            int current_price = (cart.getPrice()-(cart.getPrice()*cart.getDiscount()/100))*cart.getQuantity();
//
//            holder.txt_new_price.setText(new DecimalFormat("###,###,###").format(current_price)+ " đ");
//
//            holder.txt_old_price.setText(new DecimalFormat("###,###,###").format(cart.getPrice()*cart.getQuantity())+ " đ");
//            holder.txt_old_price.setVisibility(View.VISIBLE);
//            holder.txt_old_price.setPaintFlags(holder.txt_old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        }else{
//            holder.txt_new_price.setText(new DecimalFormat("###,###,###").format(cart.getPrice()*cart.getQuantity())+ " đ");
//        }

        if(cart.getDiscount()>0){
            holder.txt_new_price.setText(new DecimalFormat("###,###,###").format(cart.getcurrentPrice())+ " đ");

            holder.txt_old_price.setText(new DecimalFormat("###,###,###").format(cart.getPrice())+ " đ");
            holder.txt_old_price.setVisibility(View.VISIBLE);
            holder.txt_old_price.setPaintFlags(holder.txt_old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.txt_new_price.setText(new DecimalFormat("###,###,###").format(cart.getPrice()*cart.getQuantity())+ " đ");
        }
        Glide.with(context).load(Server.productFolderUrl+cart.getImage().trim()).into(holder.img_product);

    }

    @Override
    public int getItemCount() {
        return carts.size();
    }



    public class CartProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_name;
        TextView txt_price;
        ImageView img_product;
        Button btn_quantity_desc;
        Button btn_quantity_asc;
        TextView txt_quantity_number;
        TextView txt_old_price;
        TextView txt_new_price;
        CheckBox checkbox;
        RelativeLayout layout_delete;

        public CartProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_product_name);
            txt_price = itemView.findViewById(R.id.txt_price_product);
            img_product = itemView.findViewById(R.id.img_product);
            btn_quantity_desc = itemView.findViewById(R.id.btn_quantity_desc);
            btn_quantity_asc = itemView.findViewById(R.id.btn_quantity_asc);
            txt_quantity_number = itemView.findViewById(R.id.txt_quantity);
            txt_old_price = itemView.findViewById(R.id.txt_old_price);
            txt_new_price = itemView.findViewById(R.id.txt_new_price);
            checkbox = itemView.findViewById(R.id.checkbox);
            checkbox.setOnClickListener(this);
            btn_quantity_desc.setOnClickListener(this);
            btn_quantity_asc.setOnClickListener(this);
            layout_delete = itemView.findViewById(R.id.layout_delete);
            layout_delete.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            int quantity = Integer.parseInt(String.valueOf(txt_quantity_number.getText()));
//            layout_delete
            switch (v.getId()){
                case R.id.btn_quantity_desc:
                    if (quantity == 1) return;
                    txt_quantity_number.setText(String.valueOf(--quantity));
                    carts.get(getAdapterPosition()).setQuantity(quantity);
                    notifyDataSetChanged();
                    CartActitvity.updatetotalPrice();
                    CartActitvity.updateLayout();
                    updateCart(carts.get(getLayoutPosition()).getId(), Integer.parseInt(txt_quantity_number.getText().toString()));
                    break;
                case R.id.btn_quantity_asc:
                    txt_quantity_number.setText(String.valueOf(++quantity));
                    carts.get(getAdapterPosition()).setQuantity(quantity);
                    notifyDataSetChanged();
                    CartActitvity.updatetotalPrice();
                    CartActitvity.updateLayout();
                    updateCart(carts.get(getLayoutPosition()).getId(), Integer.parseInt(txt_quantity_number.getText().toString()));
                    break;
                case R.id.layout_delete:
                    Cart cart = carts.get(getLayoutPosition());
                    ApiService.apiService.deleteCart(AccessTokenManager.getAccess_token(), cart.getId())
                            .enqueue(new Callback<ApiResponse>() {
                                @Override
                                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                    if (response.isSuccessful()){
                                        carts.remove(getAdapterPosition());
                                        notifyDataSetChanged();
                                        CartActitvity.updatetotalPrice();
                                        CartActitvity.updateLayout();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiResponse> call, Throwable t) {

                                }
                            });
                    break;
                case R.id.checkbox:
                    carts.get(getLayoutPosition()).setChecked(true);
                    break;
            }


        }

        public void updateCart(int id, int quantity){
            ApiService.apiService.updateCart(AccessTokenManager.getAccess_token(), id, quantity)
                    .enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.isSuccessful()){

                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {

                        }
                    });
        }
    }
}
