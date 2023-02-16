package org.yuyu.easylogin.util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class UiUtils {

    public static boolean isLargeScreen(Context context){
        boolean isPad = (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y); // 屏幕尺寸
        return isPad || screenInches >= 7.0;
    }
}
