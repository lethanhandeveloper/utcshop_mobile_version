package com.vku.lethanhan.utcshop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vku.lethanhan.utcshop.api.ApiService;
import com.vku.lethanhan.utcshop.data_local.AccessTokenManager;
import com.vku.lethanhan.utcshop.model.ApiResponse;
import com.vku.lethanhan.utcshop.model.Product;
import com.vku.lethanhan.utcshop.R;
import com.vku.lethanhan.utcshop.util.Server;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    //attributes of activity
    Toolbar toolbar;
    TextView txt_description;
    ImageView img_product;
    TextView txt_name;
    TextView txt_new_price;
    TextView txt_old_price;
    ImageView img_add_cart;
    ImageView img_cancel;
    WebView wv_description;
    ImageView img_cart;
    TextView txt_order_cart;
    ImageView img_comment;

    private Product product;

    //attributes of bottomsheet
    BottomSheetDialog bottomSheetDialog;
    View viewBottomSheetDialog;
    Button btn_quantity_desc_bottomsheet;
    Button btn_quantity_asc_bottomsheet;
    TextView txt_quantity_bottomsheet;
    TextView txt_price_total_bottomsheet;
    ImageView img_product_buttomsheet;
    TextView txt_sold_bottomsheet;
    TextView txt_name_bottomsheet;
    TextView txt_new_price_bottomsheet;
    TextView txt_old_price_bottomsheet;
    Button btn_add_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        try {
            mapping();

            Intent intent = getIntent();
            product = (Product) intent.getSerializableExtra("product_info");

//            txt_description.setText(product.getDescription());
            wv_description.loadData(product.getDescription(), "text/html", "UTF-8");
            wv_description.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            txt_name.setText(product.getName());


            setbottomsheetAtrributes();

            if (product.getDiscount() > 0) {
                //set for activity
                int current_price = product.getPrice() - (product.getPrice() * product.getDiscount() / 100);
                txt_new_price.setText(new DecimalFormat("###,###,###").format(current_price) + " đ");
                txt_old_price.setText(new DecimalFormat("###,###,###").format(product.getPrice()) + " đ");
                txt_old_price.setPaintFlags(txt_old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                txt_old_price.setVisibility(View.VISIBLE);
                //set for bottomsheet
                txt_new_price_bottomsheet.setText(new DecimalFormat("###,###,###").format(current_price) + " đ");
                txt_old_price_bottomsheet.setText(new DecimalFormat("###,###,###").format(product.getPrice()) + " đ");
                txt_old_price_bottomsheet.setPaintFlags(txt_old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                txt_old_price_bottomsheet.setVisibility(View.VISIBLE);
            } else {
                txt_new_price.setText(new DecimalFormat("###,###,###").format(product.getPrice()) + " đ");
                txt_new_price_bottomsheet.setText(new DecimalFormat("###,###,###").format(product.getPrice()) + " đ");
            }



            txt_sold_bottomsheet.setText(String.valueOf(product.getSold()));

            txt_name_bottomsheet.setText(product.getName());

            Glide.with(this).load(Server.productFolderUrl + product.getImage()).into(img_product);
            Glide.with(this).load(Server.productFolderUrl + product.getImage()).into(img_product_buttomsheet);

            updatetotalPrice();

            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            img_add_cart.setOnClickListener(this);
        } catch (Exception e) {
            Log.e("EEEEEEE", e.toString());
        }


    }
    private void updatetotalPrice(){
        txt_price_total_bottomsheet.setText(new DecimalFormat("###,###,###")
                .format(product.getPrice() *Integer.parseInt(txt_quantity_bottomsheet.getText().toString()) ) + " đ");
    }
    private void setbottomsheetAtrributes() {
        viewBottomSheetDialog = getLayoutInflater().inflate(R.layout.detail_choice_bottom_sheet, null);
        img_cancel = viewBottomSheetDialog.findViewById(R.id.img_cancel);

        img_cancel.setOnClickListener(this);

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(viewBottomSheetDialog);

        txt_sold_bottomsheet = viewBottomSheetDialog.findViewById(R.id.txt_product_sold);
        txt_name_bottomsheet = viewBottomSheetDialog.findViewById(R.id.txt_product_name);
        img_product_buttomsheet = viewBottomSheetDialog.findViewById(R.id.img_product);
        btn_quantity_asc_bottomsheet = viewBottomSheetDialog.findViewById(R.id.btn_quantity_asc);
        btn_quantity_asc_bottomsheet.setOnClickListener(this);
        btn_quantity_desc_bottomsheet = viewBottomSheetDialog.findViewById(R.id.btn_quantity_desc);
        btn_quantity_desc_bottomsheet.setOnClickListener(this);
        txt_quantity_bottomsheet = viewBottomSheetDialog.findViewById(R.id.txt_quantity);

        txt_new_price_bottomsheet = viewBottomSheetDialog.findViewById(R.id.txt_new_price);
        txt_old_price_bottomsheet = viewBottomSheetDialog.findViewById(R.id.txt_old_price);
        txt_price_total_bottomsheet = viewBottomSheetDialog.findViewById(R.id.txt_price_total);

        btn_add_cart = viewBottomSheetDialog.findViewById(R.id.btn_add_cart);
        btn_add_cart.setOnClickListener(this);

        txt_sold_bottomsheet.setText(String.valueOf(product.getSold()));
    }

    public void mapping() {
//        txt_description = findViewById(R.id.txt_description);
        img_product = findViewById(R.id.img_product);
        txt_new_price = findViewById(R.id.txt_new_price);
        txt_old_price = findViewById(R.id.txt_old_price);
        txt_name = findViewById(R.id.txt_name);
        img_add_cart = findViewById(R.id.img_add_cart);
        wv_description = findViewById(R.id.wv_description);
        img_cart = findViewById(R.id.img_cart);
        img_cart.setOnClickListener(this);
        img_comment = findViewById(R.id.img_comment);
        img_comment.setOnClickListener(this);
        txt_order_cart = findViewById(R.id.txt_order_cart);
        txt_order_cart.setOnClickListener(this);
    }

    private void showBottomSheetDialog() {
        bottomSheetDialog.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        int quantity = Integer.parseInt(String.valueOf(txt_quantity_bottomsheet.getText()));
        switch (id) {
            case R.id.img_add_cart:
                if (AccessTokenManager.getAccess_token().isEmpty()){
                    Toast.makeText(this, "Đăng nhập để tiếp tục", Toast.LENGTH_SHORT).show();
                    return;
                }
                showBottomSheetDialog();
                break;
            case R.id.btn_quantity_asc:
                txt_quantity_bottomsheet.setText(String.valueOf(++quantity));
                updatetotalPrice();
                break;
            case R.id.btn_quantity_desc:
                if (quantity == 1) return;
                txt_quantity_bottomsheet.setText(String.valueOf(--quantity));
                updatetotalPrice();
                break;
            case R.id.btn_add_cart:
                bottomSheetDialog.cancel();
                addProducttoCart(product.getId(), Integer.parseInt(txt_quantity_bottomsheet.getText().toString()));
                break;
            case R.id.img_cancel:
                bottomSheetDialog.cancel();
                break;
            case R.id.img_cart:
                if (!MainActivity.isLogin()){
                    Toast.makeText(this, "Đăng nhập để tiếp tục", Toast.LENGTH_SHORT).show();
                    return;
                }

                startActivity(new Intent(this, CartActitvity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case R.id.txt_order_cart:
                addProducttoCart(product.getId(), 1);
                break;
            case R.id.img_comment:
                if (AccessTokenManager.getAccess_token().isEmpty()){
                    Toast.makeText(this, "Đăng nhập để tiếp tục", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(this, CommentActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);

                break;
        }
    }

    private void showMessageDialog(String message) {

        bottomSheetDialog.cancel();

        int gravity = Gravity.BOTTOM;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.success_dialog);

        Window window = dialog.getWindow();
        if (window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;

        window.setAttributes(windowAttributes);

        TextView textView = dialog.findViewById(R.id.textView);
        textView.setText(message);

        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }



        dialog.show();
    }

    private void addProducttoCart(int product_id, int quantity){
        ApiService.apiService.addCart(AccessTokenManager.getAccess_token(), product_id,
                quantity).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus().equals("success")){
                        Toast toast = new Toast(DetailActivity.this);
                        toast.setView(LayoutInflater.from(DetailActivity.this).inflate(R.layout.toast_layout, null));
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.show();
                    }else{
//                        showMessageDialog("Đã hết hàng!Bạn vui lòng quay lại sau!");
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                showMessageDialog("Có lỗi khi thêm giỏ hàng");
            }
        });
    }
}