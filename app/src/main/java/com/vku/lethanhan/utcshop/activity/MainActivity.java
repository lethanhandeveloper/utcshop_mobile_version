package com.vku.lethanhan.utcshop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.vku.lethanhan.utcshop.R;
import com.vku.lethanhan.utcshop.data_local.AccessTokenManager;
import com.vku.lethanhan.utcshop.util.CheckConnection;
import com.vku.lethanhan.utcshop.viewpager.MainViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static ViewPager mviewPager;
    private BottomNavigationView bottomNavigationView;
    private long backPress;

//    public static AccessTokenManager accessTokenManager; commment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mviewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);


//        accessTokenManager = new AccessTokenManager(getApplicationContext()); commment

        MainViewPagerAdapter viewPagerAdapter = new MainViewPagerAdapter(this, getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mviewPager.setAdapter(viewPagerAdapter);
        mviewPager.setOffscreenPageLimit(5);

        mviewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.menu_tab_1).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menu_tab_2).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.menu_tab_3).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.menu_tab_4).setChecked(true);
                        break;
                    case 4:
                        bottomNavigationView.getMenu().findItem(R.id.menu_tab_5).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_tab_1:
                        mviewPager.setCurrentItem(0);
                        break;
                    case R.id.menu_tab_2:
                        mviewPager.setCurrentItem(1);
                        break;
                    case R.id.menu_tab_3:
                        mviewPager.setCurrentItem(2);
                        break;
                    case R.id.menu_tab_4:
                        mviewPager.setCurrentItem(3);
                        break;
                    case R.id.menu_tab_5:
                        mviewPager.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });

        try {
            new CheckConnection(this).execute();
        } catch (Exception e) {
            Log.e("Lỗi", e.toString());
        }
    }

    @Override
    public void onBackPressed() {


        if (backPress + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 1) {
                super.onBackPressed();
            }else{
                Toast.makeText(this, "Nhấn back lần nữa để thoát", Toast.LENGTH_SHORT).show();
            }
        }

        backPress = System.currentTimeMillis();
    }

    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public static boolean isLogin() {
        if (AccessTokenManager.getAccess_token() != null && !AccessTokenManager.getAccess_token().isEmpty()) {
            return true;
        }

        return false;
    }

    public void logout(){
        AccessTokenManager.delAccess_token();
    }
}