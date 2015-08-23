package com.brotherjing.danmakubay.activities;

import android.app.Application;

import com.brotherjing.danmakubay.api.API_SPF;
import com.brotherjing.danmakubay.utils.CrossyHttpClient;
import com.brotherjing.danmakubay.utils.DataUtil;
import com.brotherjing.danmakubay.utils.ImageUtil;

/**
 * Created by Brotherjing on 2015/8/15.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DataUtil.init(this);
        CrossyHttpClient.init();
        ImageUtil.init(this);

        checkFirstUse();
    }

    private void checkFirstUse(){
        if(!DataUtil.getBoolean(API_SPF.SPF_SETTING,API_SPF.ITEM_FIRST_USE,false)){
            DataUtil.putBoolean(API_SPF.SPF_SETTING,API_SPF.ITEM_FIRST_USE,true);
            DataUtil.putInt(API_SPF.SPF_SETTING, API_SPF.ITEM_DANMAKU_SPEED, 50);
            DataUtil.putBoolean(API_SPF.SPF_SETTING,API_SPF.ITEM_SHOW_BG,true);
            DataUtil.putBoolean(API_SPF.SPF_SETTING,API_SPF.ITEM_DISPLAY_AREA,false);//home only
        }
    }
}
