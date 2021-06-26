package com.vku.lethanhan.utcshop.viewpager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.vku.lethanhan.utcshop.status_order_fragment.CancelStatusFragment;
import com.vku.lethanhan.utcshop.status_order_fragment.DeliveredStatusFragment;
import com.vku.lethanhan.utcshop.status_order_fragment.DeliveringStatusFragment;
import com.vku.lethanhan.utcshop.status_order_fragment.HandlingStatusFragment;
import com.vku.lethanhan.utcshop.status_order_fragment.PackedStatusFragment;

public class OrderStatusViewPager extends FragmentStatePagerAdapter {
    public OrderStatusViewPager(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HandlingStatusFragment();
            case 1:
                return new PackedStatusFragment();
            case 2:
                return new DeliveringStatusFragment();
            case 3:
                return new DeliveredStatusFragment();
            case 4:
                return new CancelStatusFragment();
            default:
                return new HandlingStatusFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Đang xử lý";
                break;
            case 1:
                title = "Đã đóng gói";
                break;
            case 2:
                title = "Đang vận chuyển";
                break;
            case 3:
                title = "Đã giao hàng";
                break;
            case 4:
                title = "Đã hủy";
                break;
        }

        return title;
    }
}
