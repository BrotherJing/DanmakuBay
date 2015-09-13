package com.brotherjing.danmakubay.base;

import android.app.ActionBar;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.brotherjing.danmakubay.R;
import com.brotherjing.danmakubay.utils.ViewUtil;

/**
 * Created by Brotherjing on 2015/9/13.
 */
public class BasicActionBarActivity extends Activity {

    protected ActionBar actionBar;

    protected void initActionBar(int layoutId){
        actionBar = getActionBar();
        ViewUtil.customizeActionBar(actionBar, layoutId);
    }

    protected  <T extends View>T f(int resId){
        return (T)super.findViewById(resId);
    }

}
