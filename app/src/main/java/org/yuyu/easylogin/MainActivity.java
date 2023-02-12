package org.yuyu.easylogin;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigationrail.NavigationRailView;

import org.yuyu.easylogin.adapter.ViewAdapter;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomBar;
    private NavigationRailView railBar;
    private ViewAdapter adapter;
    private ViewPager2 pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = findViewById(R.id.pager);
        String config = getApplicationContext().getResources().getConfiguration().toString();
        Log.d("Config", config);
        if (BuildConfig.DEBUG) {
            if (config.contains("zui-magic-windows")) {
                Toast.makeText(this, R.string.debug_app_run_magic, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.debug_app_not_run_magic, Toast.LENGTH_SHORT).show();
            }
        }
        adapter = new ViewAdapter(this);
        initBottomNav();
        initActivity();
    }

    private void initActivity() {
        pager.setAdapter(adapter);
        adapter.addFragment(new LoginFragment());
        adapter.addFragment(new PreferenceFragment());
        pager.setCurrentItem(0,true);
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    //若为横屏
                    railBar.getMenu().getItem(position).setChecked(true);
                } else {
                    //若为竖屏
                    bottomBar.getMenu().getItem(position).setChecked(true);
                }
            }
        });
    }

    private void initBottomNav() {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //若为横屏
            railBar = findViewById(R.id.rail_bar);
            railBar.setOnItemSelectedListener(item -> {
                switch (item.getItemId()){
                    case R.id.bar_login:
                        pager.setCurrentItem(0);
                        return true;
                    case R.id.bar_option:
                        pager.setCurrentItem(1);
                        return true;
                }
                return false;
            });
        } else {
            //若为竖屏
            bottomBar = findViewById(R.id.bar);
            bottomBar.setOnItemSelectedListener(item -> {
                switch (item.getItemId()){
                    case R.id.bar_login:
                        pager.setCurrentItem(0);
                        return true;
                    case R.id.bar_option:
                        pager.setCurrentItem(1);
                        return true;
                }
                return false;
            });
        }

    }
}