package com.vku.lethanhan.utcshop.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.collection.ArraySet;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.vku.lethanhan.utcshop.R;
import com.vku.lethanhan.utcshop.adapter.PaymentAdapter;
import com.vku.lethanhan.utcshop.api.ApiService;
import com.vku.lethanhan.utcshop.data_local.AccessTokenManager;
import com.vku.lethanhan.utcshop.model.Payment;
import com.vku.lethanhan.utcshop.viewpager.OrderStatusViewPager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderingStatementActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_order);

        mapping();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        OrderStatusViewPager orderStatusViewPager = new OrderStatusViewPager(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(orderStatusViewPager);

        tabLayout.setupWithViewPager(viewPager);
    }

    private void mapping() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        toolbar = findViewById(R.id.toolbar);
    }

    public static void getBill(String status, final ArrayList<Payment> payments, final PaymentAdapter paymentAdapter){
        ApiService.apiService.getBill(AccessTokenManager.getAccess_token(), status).enqueue(new Callback<ArrayList<Payment>>() {
            @Override
            public void onResponse(Call<ArrayList<Payment>> call, Response<ArrayList<Payment>> response) {
                if (response.isSuccessful()){
                    for (Payment payment : response.body()){
                        payments.add(payment);
                        paymentAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Payment>> call, Throwable t) {

            }
        });
    }
}