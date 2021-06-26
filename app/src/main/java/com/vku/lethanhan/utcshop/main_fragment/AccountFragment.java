package com.vku.lethanhan.utcshop.main_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.vku.lethanhan.utcshop.R;
import com.vku.lethanhan.utcshop.activity.MainActivity;
import com.vku.lethanhan.utcshop.activity.OrderingStatementActivity;
import com.vku.lethanhan.utcshop.api.ApiService;
import com.vku.lethanhan.utcshop.data_local.AccessTokenManager;
import com.vku.lethanhan.utcshop.model.Account;
import com.vku.lethanhan.utcshop.model.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vku.lethanhan.utcshop.data_local.AccessTokenManager.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView txt_name;
    TextView txt_email;
    TextView txt_name_info;
    TextView txt_email_info;
    TextView txt_phone_info;
    TextView txt_address_info;
    TextView txt_view_order_status;
    Button btn_logout;

    public AccountFragment() {
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
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        View view = inflater.inflate(R.layout.account_fragment, container, false);

        mapping(view);

        ApiService.apiService.getAccountData(getAccess_token()).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()){
                    Account account = response.body();
                    txt_name.setText(account.getName());
                    txt_email.setText(account.getEmail());

                    txt_name_info.setText(account.getName());
                    txt_email_info.setText(account.getEmail());
                    txt_phone_info.setText(account.getPhone_number());
                    txt_address_info.setText(account.getAddress());
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {

            }
        });
        btn_logout.setOnClickListener(this);
        return view;
    }

    private void mapping(View view) {
        txt_name = view.findViewById(R.id.txt_name);
        txt_email = view.findViewById(R.id.txt_email);
        txt_name_info = view.findViewById(R.id.txt_name_info);
        txt_email_info = view.findViewById(R.id.txt_email_info);
        txt_phone_info = view.findViewById(R.id.txt_phone_info);
        txt_address_info = view.findViewById(R.id.txt_address_info);
        txt_view_order_status = view.findViewById(R.id.txt_view_order_status);
        txt_view_order_status.setOnClickListener(this);
        btn_logout = view.findViewById(R.id.btn_logout);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_view_order_status:
                startActivity(new Intent(getActivity(), OrderingStatementActivity.class));
                break;
            case R.id.btn_logout:
                logout();
                break;
        }
    }

    private void logout() {
        AccessTokenManager.delAccess_token();

        ApiService.apiService.logout(getAccess_token());

        getActivity().finish();
        startActivity(getActivity().getIntent());
        MainActivity.mviewPager.setCurrentItem(R.layout.account_fragment);

        Toast.makeText(getActivity(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();
    }
}