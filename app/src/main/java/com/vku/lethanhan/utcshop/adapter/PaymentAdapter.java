package com.vku.lethanhan.utcshop.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ramotion.foldingcell.FoldingCell;
import com.vku.lethanhan.utcshop.R;
import com.vku.lethanhan.utcshop.api.ApiService;
import com.vku.lethanhan.utcshop.data_local.AccessTokenManager;
import com.vku.lethanhan.utcshop.model.Cart;
import com.vku.lethanhan.utcshop.model.Payment;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    Context context;
    ArrayList<Payment> payments;


    public PaymentAdapter(Context context, ArrayList<Payment> payments) {
        this.context = context;
        this.payments = payments;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.status_order_item, null);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Payment payment = payments.get(position);

        holder.txt_order_code.setText("Mã đơn hàng : "+payment.getId());
        holder.txt_address.setText("Địa chỉ : " + payment.getAddress());
        holder.txt_customer_name.setText("Tên khách hàng :" + payment.getName());
        holder.txt_phone.setText("Số điện thoại : "+payment.getPhone());
        holder.txt_email.setText("Địa chỉ email : " +payment.getEmail());
        holder.txt_time.setText("Thời gian đặt hàng : "+ payment.getTime());

        ArrayList<Cart> bill_details = holder.bill_details = payments.get(position).getCarts();
        if (bill_details != null){
            holder.billDetailAdapter = new BillDetailAdapter(context, holder.bill_details);
            holder.recyclerView.setAdapter(holder.billDetailAdapter);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        }
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }


    public class PaymentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        FoldingCell foldingCell;
        TextView txt_order_code;
        TextView txt_customer_name;
        TextView txt_address;
        TextView txt_phone;
        TextView txt_email;
        TextView txt_time;
        TextView txt_expand;
        TextView txt_collapse;


        RecyclerView recyclerView;
        ArrayList<Cart> bill_details = new ArrayList<>();
        BillDetailAdapter billDetailAdapter;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);

            foldingCell = itemView.findViewById(R.id.folding_cell);

            txt_order_code = itemView.findViewById(R.id.txt_order_code);
            txt_customer_name = itemView.findViewById(R.id.txt_customer_name);
            txt_address = itemView.findViewById(R.id.txt_address);
            txt_phone = itemView.findViewById(R.id.txt_phone);
            txt_email = itemView.findViewById(R.id.txt_email);
            txt_time = itemView.findViewById(R.id.txt_time);
            txt_expand = itemView.findViewById(R.id.txt_expand);
            txt_collapse = itemView.findViewById(R.id.txt_collapse);

            recyclerView = itemView.findViewById(R.id.recycler_view);

            txt_collapse.setOnClickListener(this);
            txt_expand.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.txt_expand:
                    try {
                        foldingCell.toggle(true);
                        getBillDetail();
                    }catch (Exception e){
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.txt_collapse:
                    try {
                        foldingCell.toggle(false);
                    }catch (Exception e){
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }

        private void getBillDetail() {
            int bill_id = payments.get(getLayoutPosition()).getId();

            ApiService.apiService.getBillDetail(AccessTokenManager.getAccess_token(), bill_id).enqueue(new Callback<ArrayList<Cart>>() {
                @Override
                public void onResponse(Call<ArrayList<Cart>> call, Response<ArrayList<Cart>> response) {

                    if (response.isSuccessful()){

                        payments.get(getLayoutPosition()).setCarts(response.body());
                        Log.e("paymentdetail", payments.get(getLayoutPosition()).toString());
                        notifyDataSetChanged();

                    }else{
                        Toast.makeText(context, "có lỗi", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Cart>> call, Throwable t) {
                    Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
