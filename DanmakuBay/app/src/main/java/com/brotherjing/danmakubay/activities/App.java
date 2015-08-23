package com.brotherjing.danmakubay.activities;

import android.app.Application;

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
    }
}
