package com.vku.lethanhan.utcshop.main_fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.google.gson.JsonElement;
import com.vku.lethanhan.utcshop.R;
import com.vku.lethanhan.utcshop.api.ApiService;
import com.vku.lethanhan.utcshop.model.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView img_cancel;
    //form attributes
    private EditText edt_password;
    private EditText edt_fullname;
    private EditText edt_date_of_birth;
    private EditText edt_phone_number;
    private EditText edt_email;
    private EditText edt_address;
    private RadioButton radio_male;
    private Button btn_register;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mapping(view);

        return view;
    }

    private void mapping(View view) {
        img_cancel = view.findViewById(R.id.img_cancel);
        img_cancel.setOnClickListener(this);

        //form mapping
        edt_email    = view.findViewById(R.id.edt_email);
        edt_password = view.findViewById(R.id.edt_password);
        edt_fullname = view.findViewById(R.id.edt_fullname);
        edt_date_of_birth = view.findViewById(R.id.edt_date_of_birth);
        edt_date_of_birth.setOnClickListener(this);
        edt_phone_number = view.findViewById(R.id.edt_phone_number);
        edt_address = view.findViewById(R.id.edt_address);
        radio_male = view.findViewById(R.id.radio_male);
        radio_male.setChecked(true);
        btn_register = view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edt_date_of_birth:
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int month, int day) {
                        String dayStr = String.valueOf(day);
                        String monthStr = String.valueOf(month);
                        String yearStr = String.valueOf(year);
                        if (dayStr.length() <2){
                            dayStr = "0" + dayStr;
                        }

                        if (monthStr.length() <2){
                            monthStr = "0" + monthStr;
                        }
                        edt_date_of_birth.setText(yearStr +"-" +monthStr +"-" +dayStr);
                    }
                };

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        dateSetListener, 2020, 11, 05);
                datePickerDialog.setTitle("Chọn ngày sinh");
                datePickerDialog.show();
                break;
            case R.id.img_cancel:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_register:
                String email = edt_email.getText().toString();
                String name = edt_fullname.getText().toString();
                String password = edt_password.getText().toString();
                String date_of_birth = edt_date_of_birth.getText().toString();
                String phone_number = edt_phone_number.getText().toString();
                String address = edt_address.getText().toString();
                int gender = 0;
                if(radio_male.isChecked()){
                    gender = 1;
                }

                String error = null;

                if (name.length() >100){
                    error = "Tên quá dài (tối đa 100 kí tự)";
                }

                if(password.length() <6 || password.length() >100){
                    error = "Mật khẩu không hợp lệ(từ 6 đến 100 kí tự)";
                }

                if(!email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")){
                    error = "Email không đúng định dạng";
                }

                if (!phone_number.matches("^0\\d{9,10}$")){
                    error = "Số điện thoại không đúng định dạng";
                }

                if(address.length() <10){
                    error = "Địa chỉ quá ngắn";
                }

                if(email.equals("") || password.equals("") || name.equals("") || date_of_birth.equals("")
                        ||phone_number.equals("") || address.equals("")){
                    error = "Các trường không được để trống";
                }

                if (error != null){
                    Account account = new Account(email, password, name, date_of_birth, gender, phone_number, address);
                    register(account);
                }else{
//                    Toast.makeText(getActivity(), "Oke", Toast.LENGTH_SHORT).show();
                    Account account = new Account(email, password, name, date_of_birth, gender, phone_number, address);
                    register(account);
                }
                break;
        }
    }

    private void register(Account account) {
        ApiService.apiService.register(account).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }
}