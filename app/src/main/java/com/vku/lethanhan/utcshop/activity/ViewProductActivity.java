package com.vku.lethanhan.utcshop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vku.lethanhan.utcshop.R;
import com.vku.lethanhan.utcshop.adapter.ViewProductAdapter;
import com.vku.lethanhan.utcshop.api.ApiService;
import com.vku.lethanhan.utcshop.model.Category;
import com.vku.lethanhan.utcshop.model.Filter;
import com.vku.lethanhan.utcshop.adapter.FilterAdapter;
import com.vku.lethanhan.utcshop.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewProductActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    ArrayList<Product> list_product;
    private ViewProductAdapter viewProductAdapter;
    Toolbar toolbar;
    ImageView img_filter;
    Button btn_apply;
    ImageView img_empty_filter;
    TextView txt_empty_filter;
    TextView edt_search;
    private static TextView txt_price_start;
    private static TextView txt_price_end;

    DrawerLayout drawer_layout;

    //data for filter
    private static String queryName;
    private static String queryPrice;
    private static ArrayList<String> list_queryCategory = new ArrayList<>();

    //recylerview price
    RecyclerView rv_price;
    RecyclerView rv_category;
    FilterAdapter categoryfilterAdapter;
    FilterAdapter pricefilterAdapter;
    ArrayList<Filter> category_filters = new ArrayList<>();
    ArrayList<Filter> price_filters = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        mapping();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Xem sản phẩm");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetValueFilter();

                startActivity(new Intent(ViewProductActivity.this , MainActivity.class));
            }
        });

        rv_price = findViewById(R.id.rv_price);
        rv_category = findViewById(R.id.rv_category);

        categoryfilterAdapter = new FilterAdapter(this, category_filters);
        rv_category.setAdapter(categoryfilterAdapter);
        rv_category.setLayoutManager(new GridLayoutManager(this, 2));

        pricefilterAdapter =new FilterAdapter(this, price_filters);
        rv_price.setAdapter(pricefilterAdapter);
        rv_price.setLayoutManager(new GridLayoutManager(this, 2));

        getDataFilter();

        Intent intent = getIntent();

        if (intent.hasExtra("name")){
            queryName = intent.getStringExtra("name").trim();
            edt_search.setText(queryName);
        }

        if (intent.hasExtra("price")){
            queryPrice = intent.getStringExtra("price");
        }

        if (intent.hasExtra("category_id")){
            list_queryCategory.add(intent.getStringExtra("category_id"));
        }

        recyclerView = findViewById(R.id.recycler_view);
        list_product = new ArrayList<>();
        viewProductAdapter = new ViewProductAdapter(this, list_product);
        recyclerView.setAdapter(viewProductAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        filter();
    }

    private void getDataFilter() {
        //get price filter
        price_filters.add(new Filter("Dưới 1 triệu", "0-1000000", "price", false));
        price_filters.add(new Filter("1-3 Triệu", "1000000-3000000", "price", false));
        price_filters.add(new Filter("3-5 Triệu", "3000000-5000000", "price", false));
        price_filters.add(new Filter("5-8 triệu", "5000000-8000000", "price", false));
        price_filters.add(new Filter("8-10 triệu", "8000000-10000000", "price", false));
        price_filters.add(new Filter("Trên 10 triệu", "10000000-"+Integer.MAX_VALUE, "price", false));

        pricefilterAdapter.notifyDataSetChanged();

        //get category filter
        ApiService.apiService.getCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()){
                    for (Category category : response.body()){
                        Filter filter = new Filter(category.getName(), String.valueOf(category.getId()), "category", false);
                        category_filters.add(filter);
                        categoryfilterAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbar);
        edt_search = findViewById(R.id.edt_search);
        edt_search.setOnClickListener(this);
        img_filter = findViewById(R.id.img_filter);
        btn_apply = findViewById(R.id.btn_apply);
        btn_apply.setOnClickListener(this);
        txt_price_start = findViewById(R.id.txt_price_start);
        txt_price_end = findViewById(R.id.txt_price_end);
        img_empty_filter = findViewById(R.id.img_empty_filter);
        txt_empty_filter = findViewById(R.id.txt_empty_filter);
        drawer_layout = findViewById(R.id.drawer_layout);

        img_filter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_filter:
                drawer_layout.openDrawer(GravityCompat.END);
                break;
            case R.id.btn_apply:
                filter();
                drawer_layout.closeDrawer(GravityCompat.END);
                break;
            case R.id.edt_search:
                resetValueFilter();
                startActivity(new Intent(this, SearchActivity.class));
                break;
        }
    }

    private void filter() {

        list_product.clear();
        viewProductAdapter.notifyDataSetChanged();


        ApiService.apiService.filterProduct(queryName, queryPrice, toCategoryString()).enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {

                if (response.isSuccessful()){
                    for(Product item : response.body()){
                        list_product.add(item);
                        viewProductAdapter.notifyDataSetChanged();
                    }

                    if (list_product.isEmpty()){
                        img_empty_filter.setVisibility(View.VISIBLE);
                        txt_empty_filter.setVisibility(View.VISIBLE);
                    }else{
                        img_empty_filter.setVisibility(View.GONE);
                        txt_empty_filter.setVisibility(View.GONE);
                    }
                }else{
                    Toast.makeText(ViewProductActivity.this, "Lỗi tải", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {

            }
        });


    }

    public static void setValueforPrice(String priceRange){
        queryPrice = priceRange;

        String arrPriceRange[] = priceRange.split("-");

        txt_price_start.setText(arrPriceRange[0]);
        txt_price_end.setText(arrPriceRange[1]);
    }
    
    
    public static void addNewValueforCategory(String categoryId){
        if (list_queryCategory.contains(categoryId)) return;
        list_queryCategory.add(categoryId);
        Log.e("listquery", list_queryCategory.toString());
    }

    public static void removeValueforCategory(String categoryId){
        for(int i =0; i<=list_queryCategory.size(); i++){
            if (list_queryCategory.get(i).equals(categoryId)){
                list_queryCategory.remove(i);
                return;
            }

        }
    }
    
    public String toCategoryString(){
        String queries = "";

       if (!list_queryCategory.isEmpty()){
           if (list_queryCategory.size() == 1){
               queries = list_queryCategory.get(0);
               return queries;
           }

           for (String string : list_queryCategory){

               queries+= string+"-";
           }

           queries = queries.substring(0, queries.length() - 1);
           return queries;

       }

        return null;
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        resetValueFilter();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(this, MainActivity.class));

        resetValueFilter();
    }

    public void resetValueFilter(){
        queryName = null;
        queryPrice = null;
        if (list_queryCategory != null){
            list_queryCategory.clear();
        }
    }
}