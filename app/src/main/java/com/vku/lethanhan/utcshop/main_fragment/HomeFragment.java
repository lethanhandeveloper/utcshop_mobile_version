package com.vku.lethanhan.utcshop.main_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.vku.lethanhan.utcshop.R;
import com.vku.lethanhan.utcshop.activity.CartActitvity;
import com.vku.lethanhan.utcshop.activity.MainActivity;
import com.vku.lethanhan.utcshop.activity.SearchActivity;
import com.vku.lethanhan.utcshop.adapter.ProductAdapter;
import com.vku.lethanhan.utcshop.adapter.SlideAdapter;
import com.vku.lethanhan.utcshop.api.ApiService;
import com.vku.lethanhan.utcshop.model.Product;
import com.vku.lethanhan.utcshop.util.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    EditText edt_search_box;
    private SlideAdapter slideAdapter;
    List<String> slideImages;
    ImageView img_cart;
    EditText edt_search;
    Timer timer;
    //rv_new
    RecyclerView rv_new_product;
    ProductAdapter newproductAdapter;
    ArrayList<Product> list_newProduct;
    //rvhot
    RecyclerView rv_hot_product;
    ProductAdapter hotproductAdapter;
    ArrayList<Product> list_hotProduct;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        // Inflate the layout for this fragment
        viewPager = view.findViewById(R.id.view_pager);

        img_cart = view.findViewById(R.id.img_cart);
        img_cart.setOnClickListener(this);
        circleIndicator = view.findViewById(R.id.circleindicator);
        edt_search = view.findViewById(R.id.edt_search);
        edt_search.setOnClickListener(this);

        slideImages = new ArrayList<>();





        slideAdapter = new SlideAdapter(getActivity(), slideImages);
        viewPager.setAdapter(slideAdapter);

        circleIndicator.setViewPager(viewPager);
        slideAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        getSlideImage();

        //rv_new_product
        rv_new_product = view.findViewById(R.id.rv_new_product);
        list_newProduct = new ArrayList<>();
        newproductAdapter = new ProductAdapter(getActivity(), list_newProduct);
        rv_new_product.setAdapter(newproductAdapter);
        rv_new_product.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        getnewProduct();
        // rv_hotproduct
        rv_hot_product = view.findViewById(R.id.rv_hot_product);
        list_hotProduct = new ArrayList<>();
        hotproductAdapter = new ProductAdapter(getActivity(), list_hotProduct);
        rv_hot_product.setAdapter(hotproductAdapter);
        rv_hot_product.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        gethotProduct();
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edt_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.img_cart:
                if (!MainActivity.isLogin()){
                    Toast.makeText(getContext(), "Đăng nhập để tiếp tục", Toast.LENGTH_SHORT).show();
                    return;
                }

                startActivity(new Intent(getActivity(), CartActitvity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                break;
        }
    }

    private void getnewProduct() {
        ApiService.apiService.getNewProduct().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, retrofit2.Response<List<Product>> response) {
                if(!response.body().isEmpty()){
                    for (Product item : response.body()){
                        list_newProduct.add(item);
                        newproductAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("Thất bại", t.toString());
            }
        });
    }

    public void gethotProduct(){
        ApiService.apiService.gethotProduct().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, retrofit2.Response<List<Product>> response) {
                if(response.isSuccessful()){
                    for (Product item : response.body()){
                        list_hotProduct.add(item);
                        hotproductAdapter.notifyDataSetChanged();
                    }
                    Log.e("Print", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("Thất bại", t.toString());
            }
        });
    }

    private void startSlideAnimation() {
        if (slideImages == null || slideImages.isEmpty() || viewPager == null) {
            return;
        }

        if (timer == null) {
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItem = slideImages.size() - 1;

                        if (currentItem < totalItem) {
                            currentItem++;
                            viewPager.setCurrentItem(currentItem);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 500, 3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void getSlideImage() {
        ApiService.apiService.getSlide().enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if (response.isSuccessful()){
                    for (String slide : response.body()){
                        slideImages.add(Server.slideFolderUrl+slide);
                    }

                    slideAdapter.notifyDataSetChanged();
                    startSlideAnimation();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {

            }
        });

    }


}