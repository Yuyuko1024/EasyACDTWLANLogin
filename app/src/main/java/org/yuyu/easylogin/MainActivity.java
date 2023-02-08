package org.yuyu.easylogin;

import android.content.DialogInterface;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomBar;
    Fragment loginFragment = new LoginFragment();
    Fragment preferenceFragment = new PreferenceFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomBar=findViewById(R.id.bar);
        String config = getApplicationContext().getResources().getConfiguration().toString();
        Log.d("Config",config);
        if (config.contains("zui-magic-windows")){
            Toast.makeText(this, "App 运行于 平行视窗中", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "App 没有运行于 平行视窗中", Toast.LENGTH_SHORT).show();
        }
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

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            new MaterialAlertDialogBuilder(this)
                    .setTitle("退出应用")
                    .setMessage("是否退出？")
                    .setCancelable(true)
                    .setPositiveButton(R.string.got_it, (dialog, which) -> finish())
                    .setNegativeButton(R.string.cancel,null)
                    .show();
        }
        return true;
    }*/
}