package org.yuyu.easylogin.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.yuyu.easylogin.login.Constant;

import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NetworkState {
    public static boolean isWifi(Context context){
        context = context.getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
            return networkInfo.isAvailable();
        }else{
            return false;
        }
    }

    public static boolean isInternetAvailable(){
        HttpsURLConnection captiveUrlConnection = null;
        try{
            URL url = new URL(Constant.CAPTIVE_HTTPS_204_URL);
            captiveUrlConnection = (HttpsURLConnection) url.openConnection();
            captiveUrlConnection.setInstanceFollowRedirects(false);
            captiveUrlConnection.setConnectTimeout(Constant.CAPTIVE_SOCKET_TIMEOUT_MS);
            captiveUrlConnection.setReadTimeout(Constant.CAPTIVE_SOCKET_TIMEOUT_MS);
            captiveUrlConnection.setUseCaches(false);
            captiveUrlConnection.getInputStream();
            Log.d("getResponseCode", String.valueOf(captiveUrlConnection.getResponseCode()));
            return captiveUrlConnection.getResponseCode() == 204;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }finally {
            if (captiveUrlConnection != null){
                captiveUrlConnection.disconnect();
            }
        }
    }

    public static String getWLANName(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID();
    }

    public static String getWifiBSSID(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getBSSID();
    }


}
