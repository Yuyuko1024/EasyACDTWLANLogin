package org.yuyu.easylogin.login;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.yuyu.easylogin.R;
import org.yuyu.easylogin.util.CallbackInterface;
import org.yuyu.easylogin.util.NetworkState;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginCore {

    public static CallbackInterface callbackInterface;
    private static final ExecutorService signalThreadPool = Executors.newSingleThreadExecutor();

    public void setCallbackInterface(CallbackInterface callbackInterface){
        LoginCore.callbackInterface =callbackInterface;
    }

    public static void LoginWithUsernamePwd(String username, String password, String carrier,String authIP, Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String myIpAddress = intToIp(wifiInfo.getIpAddress());
        Log.d("Get Wifi IP Address",myIpAddress);
        Log.d("Get Auth Server IP",authIP);
        String postDomain = context.getString(R.string.postDomain,authIP,"801",authIP,myIpAddress,myIpAddress);
        Log.d("Formated PostDomain:",postDomain);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("DDDDD",username+"@"+carrier)
                .add("upass",password)
                .add("R1",Constant.R1)
                .add("R2",Constant.R2)
                .add("R3",Constant.R3)
                .add("R6",Constant.R6)
                .add("para",Constant.para)
                .add("0MKKey",Constant.OMKKey)
                .build();
        Request request = new Request.Builder()
                .url(postDomain)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("Login Status","Failed, failed reason:"+ e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                Log.d("Get Response",response.toString());
                try{
                    String resp = response.toString();
                    int urlstart = resp.indexOf("url=");
                    int urlend =resp.indexOf("}");
                    int codes = resp.indexOf("code=");
                    int msgs = resp.indexOf("message=");
                    String url =resp.substring(urlstart+4,urlend);
                    int code = Integer.parseInt(resp.substring(codes+5,codes+8));
                    String msg = resp.substring(msgs+8,urlstart-2);
                    Log.d("Get Data","URL: "+url+", Msg: "+msg+", Code:"+code);
                    callbackInterface.resposeMsg(url,msg,authIP,code == 200);
                    isCaptiveSuccess();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private static void isCaptiveSuccess(){
        signalThreadPool.submit(() ->{
            try {
                callbackInterface.isCaptiveSuccess(NetworkState.isInternetAvailable());
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    private static String intToIp(int i) {
        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }
}
