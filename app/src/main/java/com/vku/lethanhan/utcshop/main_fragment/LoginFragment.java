package com.vku.lethanhan.utcshop.main_fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vku.lethanhan.utcshop.R;
import com.vku.lethanhan.utcshop.api.ApiService;
import com.vku.lethanhan.utcshop.data_local.AccessTokenManager;
import com.vku.lethanhan.utcshop.model.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView edt_username;
    TextView edt_password;
    Button btn_login;
    private TextView txt_register;

    AccessTokenManager accessTokenManager;

    static FragmentTransaction fr;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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

        accessTokenManager = new AccessTokenManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        mapping(view);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = edt_username.getText().toString().trim();
                String password = (String) edt_password.getText().toString().trim();

                ApiService.apiService.login(username, password).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()){
                            String status = response.body().getStatus();
                            String message = response.body().getMessage();

                            if (status.equals("success")){
                                AccessTokenManager.setAccess_token(response.body().getData());
                                getActivity().finish();
                                startActivity(getActivity().getIntent());
                            }

                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {

                    }
                });

                //hide keyboard
                edt_username.onEditorAction(EditorInfo.IME_ACTION_DONE);
                edt_password.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });

        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveRegisterFragment();
            }
        });

        return view;
    }

    private void mapping(View view) {
        edt_username = view.findViewById(R.id.edt_username);
        edt_password = view.findViewById(R.id.edt_password);
        btn_login = view.findViewById(R.id.btn_login);
        txt_register = view.findViewById(R.id.txt_register);
    }

    private void moveRegisterFragment() {
        fr = getFragmentManager().beginTransaction().addToBackStack("Login");
        fr.replace(R.id.login_fragment , new RegisterFragment());
        fr.commit();
    }
}