package com.brotherjing.danmakubay.utils;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.brotherjing.danmakubay.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class ViewUtil {
    //actionbar的统一
    public static void customizeActionBar(ActionBar actionBar, int resId) {
        actionBar.setCustomView(resId);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    }

    public static void customizeActionBar(android.support.v7.app.ActionBar actionBar, int resId) {
        actionBar.setCustomView(resId);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
    }

    public static void initStatusBar(Activity activity){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    |WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(R.color.app_color_dark));
            window.setNavigationBarColor(activity.getResources().getColor(R.color.app_color));
        }else{
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setTintColor(activity.getResources().getColor(R.color.app_color_dark));
        }
    }

    public static int dp2px(Context context,int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context,int sp){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,context.getResources().getDisplayMetrics());
    }



}
