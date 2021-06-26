package com.vku.lethanhan.utcshop.main_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vku.lethanhan.utcshop.R;
import com.vku.lethanhan.utcshop.adapter.CategoryAdapter;
import com.vku.lethanhan.utcshop.api.ApiService;
import com.vku.lethanhan.utcshop.model.Category;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //rv_phone
    RecyclerView rv_phone;
    ArrayList<Category> phone_categories;
    CategoryAdapter phoneAdapter;
    //rv_accessory
    RecyclerView rv_accessory;
    ArrayList<Category> accessory_categories;
    CategoryAdapter accessoryAdapter;

    public CategoryFragment() {
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
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.category_fragment, container, false);

        //rv_phone
        rv_phone = view.findViewById(R.id.rv_phone);
        phone_categories = new ArrayList<>();
        phoneAdapter = new CategoryAdapter(getActivity(), phone_categories);
        rv_phone.setAdapter(phoneAdapter);
        rv_phone.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        //rv_category;
        rv_accessory = view.findViewById(R.id.rv_accessory);
        accessory_categories = new ArrayList<>();
        accessoryAdapter = new CategoryAdapter(getActivity(), accessory_categories);
        rv_accessory.setAdapter(accessoryAdapter);
        rv_accessory.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        getCategory();

        return view;
    }

    public void getCategory() {
        ApiService.apiService.getCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    for (Category item : response.body()) {
                        if (item.getLevel() == 1){
                            phone_categories.add(item);
                            phoneAdapter.notifyDataSetChanged();
                        }else{
                            accessory_categories.add(item);
                            accessoryAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }
}