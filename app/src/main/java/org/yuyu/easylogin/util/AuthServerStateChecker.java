package org.yuyu.easylogin.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AuthServerStateChecker {

    public static boolean isAvailable(String ipAddress){
        return Ping(ipAddress);
    }

    private static boolean Ping(String str) {
        boolean result = false;
        Process p;
        try {
            //ping -c 3 -w 100  中  ，-c 是指ping的次数 3是指ping 3次 ，-w 10  以秒为单位指定超时间隔，是指超时时间为10秒
            p = Runtime.getRuntime().exec("ping -c 3 -w 10 " + str);
            int status = p.waitFor();
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null){
                buffer.append(line);
            }
            System.out.println("Return ============" + buffer);
            if (status == 0) {
                result = true;
                Log.d("PING_Status", "Auth server ping success");
            } else {
                Log.e("PING_Status", "Auth server ping faild");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
