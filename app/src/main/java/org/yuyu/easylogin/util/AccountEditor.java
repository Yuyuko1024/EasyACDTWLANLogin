package org.yuyu.easylogin.util;

import android.content.Context;
import android.content.SharedPreferences;

public class AccountEditor {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor sharedEditor;
    private static final String SHARED_STRING = "LOGIN_INFO";
    public static void saveAccount(String account,String password,long carrierId,Context context){
        sharedPreferences= context.getSharedPreferences(SHARED_STRING,Context.MODE_PRIVATE);
        sharedEditor=sharedPreferences.edit();
        sharedEditor.putString("username",account);
        sharedEditor.putString("password",password);
        sharedEditor.putLong("carrier",carrierId);
        sharedEditor.commit();
    }

    public static String readAccount(Context context){
        sharedPreferences= context.getSharedPreferences(SHARED_STRING,Context.MODE_PRIVATE);
        return sharedPreferences.getString("username",null);
    }

    public static String readPassword(Context context){
        sharedPreferences= context.getSharedPreferences(SHARED_STRING,Context.MODE_PRIVATE);
        return sharedPreferences.getString("password",null);
    }

    public static long readCarrierId(Context context){
        sharedPreferences= context.getSharedPreferences(SHARED_STRING,Context.MODE_PRIVATE);
        return sharedPreferences.getLong("carrier",0);
    }

    public static void setRememberPassowrd(boolean isRemember,Context context){
        sharedPreferences= context.getSharedPreferences(SHARED_STRING,Context.MODE_PRIVATE);
        sharedEditor=sharedPreferences.edit();
        sharedEditor.putBoolean("is_remember_password",isRemember);
        sharedEditor.commit();
    }

    public static boolean isRememberPassword(Context context){
        sharedPreferences= context.getSharedPreferences(SHARED_STRING,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("is_remember_password",false);
    }

    public static String readCustomAuthIP(Context context){
        sharedPreferences= context.getSharedPreferences(SHARED_STRING,Context.MODE_PRIVATE);
        return sharedPreferences.getString("auth_server_ip",null);
    }
}
