package com.vku.lethanhan.utcshop.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vku.lethanhan.utcshop.R;
import com.vku.lethanhan.utcshop.adapter.CartAdapter;
import com.vku.lethanhan.utcshop.api.ApiService;
import com.vku.lethanhan.utcshop.data_local.AccessTokenManager;
import com.vku.lethanhan.utcshop.model.Account;
import com.vku.lethanhan.utcshop.model.ApiResponse;
import com.vku.lethanhan.utcshop.model.Cart;
import com.vku.lethanhan.utcshop.model.Payment;
import com.vku.lethanhan.utcshop.model.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements LocationListener, View.OnClickListener {
    LocationManager locationManager;

    //views
    Toolbar toolbar;
    EditText edt_address;
    EditText edt_fullname;
    EditText edt_phone_number;
    EditText edt_email;
    ImageView img_pick_address;
    Button btn_payment;

    ArrayList<Cart> carts;
    Account account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        carts = (ArrayList<Cart>) getIntent().getSerializableExtra("carts");

        if (AccessTokenManager.getAccess_token() == null || carts.size() < 1){
            finish();
        }

        mapping();

        ApiService.apiService.getAccountData(AccessTokenManager.getAccess_token()).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()){
                    account = response.body();
                    edt_fullname.setText(account.getName());
                    edt_phone_number.setText(account.getPhone_number());
                    edt_email.setText(account.getEmail());
                    edt_address.setText(account.getAddress());
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {

            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }


    }



    private void mapping() {
        toolbar = findViewById(R.id.toolbar);
        edt_fullname = findViewById(R.id.edt_fullname);
        edt_address = findViewById(R.id.edt_address);
        edt_phone_number = findViewById(R.id.edt_phone_number);
        edt_email = findViewById(R.id.edt_email);
        img_pick_address = findViewById(R.id.img_pick_address);
        img_pick_address.setOnClickListener(this);
        btn_payment = findViewById(R.id.btn_payment);
        btn_payment.setOnClickListener(this);
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
//        Toast.makeText(this, "" + location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);

            edt_address.setText(address);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_pick_address:
                getLocation();
                break;
            case R.id.btn_payment:
                addPaymentInfo();
                break;
        }
    }

    private void addPaymentInfo() {

        String name = edt_fullname.getText().toString();
        String phone = edt_phone_number.getText().toString();
        String email = edt_email.getText().toString();
        String address = edt_address.getText().toString();


        Payment payment = new Payment(name, address, phone, email , carts);

        Log.e("payment_data", payment.toString());

        ApiService.apiService.addBill(AccessTokenManager.getAccess_token(), payment).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                   for (Cart cart : carts){
                       ApiService.apiService.deleteCart(AccessTokenManager.getAccess_token(), cart.getId()).enqueue(new Callback<ApiResponse>() {
                           @Override
                           public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                           }

                           @Override
                           public void onFailure(Call<ApiResponse> call, Throwable t) {

                           }
                       });
                   }
                    startActivity(new Intent(PaymentActivity.this, SuccessfulPaymentActivity.class));
                }else{
                    Log.e("LỗiPayment", response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(PaymentActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}