package com.vku.lethanhan.utcshop.viewpager;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.vku.lethanhan.utcshop.activity.MainActivity;
import com.vku.lethanhan.utcshop.main_fragment.AccountFragment;
import com.vku.lethanhan.utcshop.main_fragment.CategoryFragment;
import com.vku.lethanhan.utcshop.main_fragment.ChatFragment;
import com.vku.lethanhan.utcshop.main_fragment.HomeFragment;
import com.vku.lethanhan.utcshop.main_fragment.LoginFragment;
import com.vku.lethanhan.utcshop.main_fragment.NotificationFragment;
import com.vku.lethanhan.utcshop.main_fragment.RegisterFragment;

//import com.vku.lethanhan.utcshop.fragment.HomeFragment;

public class MainViewPagerAdapter extends FragmentStatePagerAdapter {
    Context context;

    SharedPreferences sharedPref;
    public MainViewPagerAdapter(Context context, @NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new CategoryFragment();
            case 2:
                return new ChatFragment();
            case 3:
                return new NotificationFragment();
            case 4:
                if(MainActivity.isLogin()){
                    return new AccountFragment();
                }else{
                    return new LoginFragment();
                }
            case 5:
                return new RegisterFragment();
            default:
                return new AccountFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
