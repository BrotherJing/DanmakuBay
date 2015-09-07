package com.brotherjing.danmakubay.utils;

import android.app.ActionBar;
import android.content.Context;
import android.util.TypedValue;

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
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    }

    public static int dp2px(Context context,int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context,int sp){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,context.getResources().getDisplayMetrics());
    }

}
