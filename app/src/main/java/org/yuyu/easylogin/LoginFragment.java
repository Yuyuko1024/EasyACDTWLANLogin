package org.yuyu.easylogin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.yuyu.easylogin.login.LoginCore;
import org.yuyu.easylogin.util.AccountEditor;
import org.yuyu.easylogin.util.AuthServerStateChecker;
import org.yuyu.easylogin.util.CallbackInterface;
import org.yuyu.easylogin.util.NetworkState;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.dmoral.toasty.Toasty;

public class LoginFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener, CallbackInterface {

    private final ExecutorService signalThreadPool = Executors.newSingleThreadExecutor();
    private static final int DELAY_TIMER_MILLIS = 500;
    private static final int ACTIVITY_TRIGGER_COUNT = 3;
    private final long[] mHits = new long[ACTIVITY_TRIGGER_COUNT];
    private final static int LOCATION_PERMISSION_TRUE = 1;

    MaterialButton btn_login;
    MaterialCheckBox remember_password,show_password;
    MaterialTextView auth_server_status,wifi_name;
    TextInputEditText username,password;
    AppCompatSpinner spin_carrier;
    String sharedUsername,sharedPassword,customAuthServer;
    ImageView app_banner;
    LoginCore loginCore;
    boolean is_remember_passwd,isCheckState=false,isCustomAuthServer,isInternetAvailable,isIgnoreGrant=false;
    long sharedCarrier;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        remember_password=requireView().findViewById(R.id.remember_password);
        btn_login=requireView().findViewById(R.id.login);
        username=requireView().findViewById(R.id.username);
        password=requireView().findViewById(R.id.password);
        spin_carrier=requireView().findViewById(R.id.carrier);
        auth_server_status=requireView().findViewById(R.id.server_status);
        app_banner=requireView().findViewById(R.id.app_banner);
        show_password=requireView().findViewById(R.id.show_passowrd);
        wifi_name=requireView().findViewById(R.id.wifi_name_text);
        loadSettings();
        btn_login.setOnClickListener(new LoginFunc());
        remember_password.setOnClickListener(new RememberFunc());
        auth_server_status.setOnClickListener(new ReCheckFunc());
        username.setText(AccountEditor.readAccount(getContext()));
        app_banner.setOnClickListener(new BannerFunc());
        btn_login.setClickable(false);
        show_password.setOnClickListener(new ShowPasswdFunc());
        loginCore = new LoginCore();
        requestPermissions();
        if(AccountEditor.isRememberPassword(getContext())) {
            remember_password.setChecked(true);
            password.setText(AccountEditor.readPassword(getContext()));
        }
        spin_carrier.setSelection((int) AccountEditor.readCarrierId(getContext()));
        checkWifiState(getContext());
        wifi_name.setText(NetworkState.getWLANName(getContext()));
        InternetStatusCheck();
    }

    private void requestPermissions(){
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED && !isIgnoreGrant){
            new MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(R.string.request_permissions_title)
                    .setMessage(R.string.request_permissions_desc)
                    .setCancelable(false)
                    .setPositiveButton(R.string.request_grant_btn, (dialogInterface, i) ->
                            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION_TRUE))
                    .setNegativeButton(R.string.request_denied_btn, (dialogInterface, i) -> AccountEditor.setIgnoreGrant(true,getContext()))
                    .show();
        }
    }

    private void checkWifiState(Context context){
        if(!NetworkState.isWifi(context)){
            new MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(R.string.dialog_denied_mobile_title)
                    .setMessage(R.string.dialog_denied_mobile_network)
                    .setCancelable(false)
                    .setView(R.layout.dialog_what)
                    .setPositiveButton(R.string.got_it, (dialogInterface, i) -> getActivity().finish())
                    .setNegativeButton(R.string.dialog_denied_mobile_btn, (dialogInterface, i) -> {
                        Intent intent = new Intent();
                        intent.setAction("android.settings.WIFI_SETTINGS");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    })
                    .show();
        }
    }

    private void loadSettings(){
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("LOGIN_INFO",Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        sharedUsername=AccountEditor.readAccount(getContext());
        sharedPassword=AccountEditor.readPassword(getContext());
        sharedCarrier=AccountEditor.readCarrierId(getContext());
        isCustomAuthServer =sharedPreferences.getBoolean("enable_custom_auth_server",false);
        customAuthServer=sharedPreferences.getString("auth_server_ip",null);
        isIgnoreGrant=sharedPreferences.getBoolean("ignore_grant",false);
        Log.e("isCustomAuthServer",String.valueOf(isCustomAuthServer));
        if(isCustomAuthServer){
            authStatusCheck(customAuthServer);
        }else{
            authStatusCheck(getString(R.string.auth_server));
        }
    }

    private void authStatusCheck(String authIP){
        signalThreadPool.submit(() -> {
            try {
                isCheckState=true;
                Thread.sleep(1000);
                if(AuthServerStateChecker.isAvailable(authIP)){
                    handler.sendEmptyMessage(0);
                    isCheckState=false;
                }else{
                    handler.sendEmptyMessage(1);
                    isCheckState=false;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    private void InternetStatusCheck(){
        signalThreadPool.submit(() -> {
            try {
                isInternetAvailable=NetworkState.isInternetAvailable();
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    btn_login.setClickable(true);
                    auth_server_status.setText(R.string.server_status_ok);
                    break;
                case 1:
                    btn_login.setClickable(false);
                    auth_server_status.setText(R.string.server_status_unavailable);
                    break;
            }
        }
    };

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        switch (s){
            case "username":
                sharedUsername=sharedPreferences.getString("username",null);
                break;
            case "password":
                sharedPassword=sharedPreferences.getString("password",null);
                break;
            case "is_remember_passwd":
                is_remember_passwd=sharedPreferences.getBoolean("is_remember_passwd",false);
                break;
            case "carrier":
                sharedCarrier=sharedPreferences.getLong("carrier",0);
                break;
            case "auth_server_ip":
                customAuthServer=sharedPreferences.getString("auth_server_ip",null);
                Log.d("Readed Custom IP",customAuthServer);
                break;
            case "enable_custom_auth_server":
                isCustomAuthServer =sharedPreferences.getBoolean("enable_custom_auth_server",false);
                break;
        }
    }

    @Override
    public void resposeMsg(String url, String msg, String authIP, boolean isOk) {
        //stub,not use in this fragment
    }

    @Override
    public void isCaptiveSuccess(boolean isAvailable) {
        isInternetAvailable=isAvailable;
    }

    class ShowPasswdFunc implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(show_password.isChecked()){
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    class ReCheckFunc implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(isCheckState){
                Snackbar.make(requireView(),R.string.is_checking_states,Snackbar.LENGTH_LONG).show();
            }else{
                auth_server_status.setText(R.string.server_status_default);
                if(isCustomAuthServer){
                    authStatusCheck(customAuthServer);
                }else{
                    authStatusCheck(getString(R.string.auth_server));
                }
            }
        }
    }

    class RememberFunc implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            AccountEditor.setRememberPassowrd(remember_password.isChecked(),getContext());
            if(!remember_password.isChecked()){
                password.setText("");
            }
        }
    }

    class BannerFunc implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            arrayCopy();
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            if (mHits[0] >= (SystemClock.uptimeMillis() - DELAY_TIMER_MILLIS)) {
                Snackbar.make(requireView(),R.string.not_implement_yet,Snackbar.LENGTH_LONG).show();
                showDialog("?????????",null,R.layout.dialog_picture,true);
            }
        }
    }

    class LoginFunc implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent login_intent= new Intent();
            login_intent.setClassName(BuildConfig.APPLICATION_ID,BuildConfig.APPLICATION_ID+".LoginPage");
            login_intent.setAction("android.intent.action.MAIN");
            Log.d("Is Internet Available? ", String.valueOf(isInternetAvailable));
            AccountEditor.setRememberPassowrd(remember_password.isChecked(),getContext());
            if(remember_password.isChecked()){
                if(!username.getText().toString().equals("")){
                    if(!password.getText().toString().equals("")){
                        Toasty.info(getContext(),getString(R.string.saved_username_passwd),Toasty.LENGTH_LONG,true).show();
                        AccountEditor.saveAccount(username.getText().toString(),
                                password.getText().toString(),spin_carrier.getSelectedItemId(),
                                getContext());
                        if (isInternetAvailable){
                            showDialog(getString(R.string.hotspot_available_block),getString(R.string.hotspot_available_block_text),0,true);
                        }else{
                            startActivity(login_intent);
                            delayPost(2000);
                        }
                    }else{
                        showDialog(getString(R.string.dialog_need_password),getString(R.string.dialog_need_password_text),R.layout.dialog_what,false);
                    }
                }else{
                    showDialog(getString(R.string.dialog_need_username),getString(R.string.dialog_need_username_text),R.layout.dialog_what,false);
                }
            }else{
                if(!username.getText().toString().equals("")){
                    if(!password.getText().toString().equals("")) {
                        Toasty.info(getContext(),getString(R.string.saved_username_passwd),Toasty.LENGTH_LONG,true).show();
                        AccountEditor.saveAccount(username.getText().toString(),
                                "",spin_carrier.getSelectedItemId(),
                                getContext());
                        if (isInternetAvailable){
                            showDialog(getString(R.string.hotspot_available_block),getString(R.string.hotspot_available_block_text),0,true);
                        }else{
                            startActivity(login_intent);
                            delayPost(2000);
                        }
                    }else{
                        showDialog(getString(R.string.dialog_need_password),getString(R.string.dialog_need_password_text),R.layout.dialog_what,false);
                    }
                }else{
                    showDialog(getString(R.string.dialog_need_username),getString(R.string.dialog_need_username_text),R.layout.dialog_what,false);
                }
            }
        }
    }

    private void delayPost(long time){
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(time);
                if(isCustomAuthServer){
                    LoginCore.LoginWithUsernamePwd(username.getText().toString(),password.getText().toString(),getCarrierTextId(spin_carrier.getSelectedItemId()),customAuthServer,getContext());
                }else{
                    LoginCore.LoginWithUsernamePwd(username.getText().toString(),password.getText().toString(),getCarrierTextId(spin_carrier.getSelectedItemId()),getString(R.string.auth_server),getContext());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private String getCarrierTextId(long carrierId){
        String carrier = "";
        switch ((int) carrierId){
            case 0:
                carrier="unicom";
                break;
            case 1:
                carrier="cmcc";
                break;
            case 2:
                carrier="telecom";
                break;
        }
        return carrier;
    }

    private void showDialog(String title, String text, int view, boolean cancelable){
        if (view==0){
            new MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(title)
                    .setMessage(text)
                    .setCancelable(cancelable)
                    .setPositiveButton(R.string.got_it, null)
                    .show();
        }else{
            new MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(title)
                    .setMessage(text)
                    .setCancelable(cancelable)
                    .setView(view)
                    .setPositiveButton(R.string.got_it, null)
                    .show();
        }
    }

    void arrayCopy() {
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        InternetStatusCheck();
    }
}
