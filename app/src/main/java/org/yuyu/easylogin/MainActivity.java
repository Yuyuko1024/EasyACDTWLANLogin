package org.yuyu.easylogin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomBar;
    Fragment loginFragment = new LoginFragment();
    Fragment preferenceFragment = new PreferenceFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomBar=findViewById(R.id.bar);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container_view, loginFragment, "login_fragment").hide(preferenceFragment)
                    .add(R.id.fragment_container_view, preferenceFragment, "preference_fragment")
                    .commit();
        }else{
            loginFragment = getSupportFragmentManager().findFragmentByTag("login_fragment");
            preferenceFragment = getSupportFragmentManager().findFragmentByTag("preference_fragment");
            switch(bottomBar.getSelectedItemId()){
                case R.id.bar_login:
                    getSupportFragmentManager().beginTransaction().show(loginFragment).commit();
                    break;
                case R.id.bar_option:
                    getSupportFragmentManager().beginTransaction().show(preferenceFragment).commit();
                    break;
            }
        }
        bottomBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bar_login:
                    getSupportFragmentManager().beginTransaction()
                            .show(loginFragment).hide(preferenceFragment)
                            .commit();
                    return true;
                case R.id.bar_option:
                    getSupportFragmentManager().beginTransaction()
                            .show(preferenceFragment).hide(loginFragment)
                            .commit();
                    return true;
            }
            return false;
        });
    }


}