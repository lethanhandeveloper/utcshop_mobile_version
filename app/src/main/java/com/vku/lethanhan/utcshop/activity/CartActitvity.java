package com.vku.lethanhan.utcshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vku.lethanhan.utcshop.R;
import com.vku.lethanhan.utcshop.adapter.CartAdapter;
import com.vku.lethanhan.utcshop.api.ApiService;
import com.vku.lethanhan.utcshop.data_local.AccessTokenManager;
import com.vku.lethanhan.utcshop.model.Cart;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActitvity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txt_order_cart;
    static ArrayList<Cart> carts;
    static ArrayList<Cart> paymentCarts;
    RecyclerView recyclerView;
    static CartAdapter cartAdapter;
    static ImageView img_empty_cart;
    static TextView txt_empty_cart;
    CheckBox checkbox_all;
    private static LinearLayout linearlayout;
    static TextView txt_total_price;

    BottomSheetDialog bottomSheetDialog;
    View viewBottomSheetDialog;
    TextView txt_info_list_bottomsheet;
    TextView txt_total_price_bottomsheet;
    ImageView img_close_bottomsheet;
    Button btn_continue_bottomsheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_actitvity);

        if (AccessTokenManager.getAccess_token() == null){
            finish();
        }

        try {
            mapping();

            txt_order_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paymentCarts = new ArrayList<>();
                    for (Cart cart:carts){
                        if (cart.isChecked()){
                            paymentCarts.add(cart);
                        }
                    }

                    if (paymentCarts.size() < 1) return;
                    openBottomSheetDialog();
                }
            });

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }
            });
            carts = new ArrayList<>();

            cartAdapter = new CartAdapter(this, carts);

            recyclerView.setAdapter(cartAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            getCart();



        }catch (Exception e){
            Log.e("Loi", e.toString());
        }


        checkbox_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!carts.isEmpty()){
                    if(isChecked){
                        for (Cart item :carts) {
                            item.setChecked(true);
                        }
                    }else{
                        for (Cart item :carts) {
                            item.setChecked(false);
                        }
                    }
                    cartAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void openBottomSheetDialog() {
        String infoPayment = "";
        int totalPricePayment = 0 ;
        viewBottomSheetDialog = getLayoutInflater().inflate(R.layout.payment_item_list_bottomsheet, null);

        for (Cart cart : carts){
            if (cart.isChecked()){
                infoPayment+= cart.getName() +" X " + cart.getQuantity() +"\n";
                totalPricePayment += cart.getcurrentPrice() * cart.getQuantity();
            }
        }
        txt_info_list_bottomsheet = viewBottomSheetDialog.findViewById(R.id.txt_info_list);
        txt_total_price_bottomsheet = viewBottomSheetDialog.findViewById(R.id.txt_total_price);
        btn_continue_bottomsheet = viewBottomSheetDialog.findViewById(R.id.btn_continue);

        //set data
        txt_info_list_bottomsheet.setText(infoPayment);
        txt_total_price_bottomsheet.setText("Tổng tiền : "+new DecimalFormat("###,###,###").format(totalPricePayment)+" đ");
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(viewBottomSheetDialog);

        bottomSheetDialog.show();

        img_close_bottomsheet = viewBottomSheetDialog.findViewById(R.id.img_close);
        img_close_bottomsheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
            }
        });

        btn_continue_bottomsheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActitvity.this, PaymentActivity.class);
                intent.putExtra("carts", (Serializable) paymentCarts);
                startActivity(intent);
            }
        });
    }

    public static void updatetotalPrice(){
        //set text for txt_totalprice
        int total = 0;

        for (Cart item: carts){
            if(item.getDiscount()>0){
                int current_price = (item.getPrice()-(item.getPrice()*item.getDiscount()/100))*item.getQuantity();
                total+= current_price;
            }else{
                total += item.getPrice()*item.getQuantity();
            }
            Log.e("current_price", String.valueOf(total));
        }

        txt_total_price.setText(new DecimalFormat("###,###,###").format(total)+" đ");
    }

    private void getCart() {
        ApiService.apiService.getCart(AccessTokenManager.getAccess_token()).enqueue(new Callback<ArrayList<Cart>>() {
            @Override
            public void onResponse(Call<ArrayList<Cart>> call, Response<ArrayList<Cart>> response) {

                if(response.isSuccessful()){
//                    Log.e("statusheader", response.headers().get("status"));

                    for (Cart item : response.body()){
                        Log.e("data", item.toString());
                        carts.add(item);
                        Log.e("Number of cars inside", String.valueOf(carts.size()));
                        cartAdapter.notifyDataSetChanged();
                    }

                    updatetotalPrice();
                    updateLayout();
                }else{
                    Log.e("CartError", "CartError");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Cart>> call, Throwable t) {
                Log.e("CartError", t.toString());
            }
        });
    }

    private void mapping(){
        toolbar = findViewById(R.id.toolbar);
        txt_order_cart = findViewById(R.id.txt_order_cart);
        recyclerView = findViewById(R.id.recycler_view);

        img_empty_cart = findViewById(R.id.img_empty_cart);
        txt_empty_cart = findViewById(R.id.txt_empty_cart);
        txt_total_price = findViewById(R.id.txt_total_price);
        checkbox_all = findViewById(R.id.checkbox_all);
        linearlayout = findViewById(R.id.linearlayout);
    }

    public static void updateLayout(){
        if (cartAdapter.getItemCount() < 1 || carts.size() < 1){
            linearlayout.setVisibility(View.INVISIBLE);
            img_empty_cart.setVisibility(View.VISIBLE);
            txt_empty_cart.setVisibility(View.VISIBLE);
        }else{
            linearlayout.setVisibility(View.VISIBLE);
            img_empty_cart.setVisibility(View.INVISIBLE);
            txt_empty_cart.setVisibility(View.INVISIBLE);
        }
    }

}