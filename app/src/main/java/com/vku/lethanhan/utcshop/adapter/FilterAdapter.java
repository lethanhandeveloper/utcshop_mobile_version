package com.vku.lethanhan.utcshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vku.lethanhan.utcshop.R;
import com.vku.lethanhan.utcshop.activity.DetailActivity;
import com.vku.lethanhan.utcshop.activity.ViewProductActivity;
import com.vku.lethanhan.utcshop.model.Filter;

import java.util.ArrayList;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

    Context context;
    ArrayList<Filter> filters;

    public FilterAdapter(Context context, ArrayList<Filter> filters) {
        this.context = context;
        this.filters = filters;
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.filter_item, null);
        return new FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
        Filter filter = filters.get(position);
        holder.btn_filter.setText(filter.getName());

        if (filter.isCheck()){
            holder.btn_filter.setBackgroundResource(R.color.colormainApp);
            holder.btn_filter.setTextColor(Color.WHITE);
        }else{
            holder.btn_filter.setBackgroundResource(R.drawable.custom_button_add_cart);
            holder.btn_filter.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    public class FilterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Button btn_filter;

        public FilterViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_filter = itemView.findViewById(R.id.btn_filter);
            btn_filter.setOnClickListener(this);



        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_filter:
                    clickFilter();
                    notifyDataSetChanged();
                    break;
            }
        }

        private void clickFilter() {
            if (filters.get(getLayoutPosition()).getType() == "price"){
                for (Filter filter : filters){
                    if (filter == filters.get(getLayoutPosition()) && !filter.isCheck()){
                        filter.setCheck(true);
                        ViewProductActivity.setValueforPrice(filters.get(getLayoutPosition()).getValue());
                    }else{
                        filter.setCheck(false);
                    }
                }

            }else {

                String currentCategoryId = filters.get(getLayoutPosition()).getValue();

                if (!filters.get(getLayoutPosition()).isCheck()){
                    filters.get(getLayoutPosition()).setCheck(true);
                    ViewProductActivity.addNewValueforCategory(currentCategoryId);

                    return;
                }

                filters.get(getLayoutPosition()).setCheck(false);
                ViewProductActivity.removeValueforCategory(currentCategoryId);
            }
        }
    }
}
