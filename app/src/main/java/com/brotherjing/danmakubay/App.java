package com.brotherjing.danmakubay;

import android.app.Application;

import com.brotherjing.danmakubay.api.API_SPF;
import com.brotherjing.danmakubay.utils.DataUtil;
import com.brotherjing.danmakubay.utils.ImageUtil;
import com.brotherjing.danmakubay.utils.WordDBManager;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by Brotherjing on 2015/8/15.
 */
public final class App extends Application {

    private static App appContext;
    private static WordDBManager wordDBManager;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;

        DataUtil.init(this);
        ImageUtil.init(this);
        GlobalEnv.init();
        wordDBManager = new WordDBManager(this);
        CrashReport.initCrashReport(appContext, "900008968", true);

        checkFirstUse();
    }

    public static App getInstance(){return appContext;}

    public static WordDBManager getWordDBManager(){return wordDBManager;}

    private void checkFirstUse(){
        if(!DataUtil.getBoolean(API_SPF.SPF_SETTING,API_SPF.ITEM_FIRST_USE,false)){
            DataUtil.putBoolean(API_SPF.SPF_SETTING,API_SPF.ITEM_FIRST_USE,true);
            DataUtil.putInt(API_SPF.SPF_SETTING, API_SPF.ITEM_DANMAKU_SPEED, 50);
            DataUtil.putBoolean(API_SPF.SPF_SETTING,API_SPF.ITEM_SHOW_BG,true);
            DataUtil.putBoolean(API_SPF.SPF_SETTING,API_SPF.ITEM_DISPLAY_AREA,true);//all app
        }
    }

}
