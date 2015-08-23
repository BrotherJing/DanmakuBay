package com.brotherjing.danmakubay.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.brotherjing.danmakubay.R;
import com.brotherjing.danmakubay.api.API_SPF;
import com.brotherjing.danmakubay.utils.DataUtil;
import com.brotherjing.danmakubay.utils.ViewUtil;

public class DanmakuSettingActivity extends Activity {

    SeekBar sb;
    RadioGroup rg;
    RadioButton rb1,rb2;
    CheckBox cb;

    int speed;
    boolean all_app,show_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danmaku_setting);
        initActionBar();
        initView();
        initData();
    }

    private void initData(){
        speed = DataUtil.getInt(API_SPF.SPF_SETTING, API_SPF.ITEM_DANMAKU_SPEED, 50);
        show_bg = DataUtil.getBoolean(API_SPF.SPF_SETTING, API_SPF.ITEM_SHOW_BG, true);
        all_app = DataUtil.getBoolean(API_SPF.SPF_SETTING, API_SPF.ITEM_DISPLAY_AREA,false);

        if(all_app) rg.check(rb2.getId());
        else rg.check(rb1.getId());

        cb.setChecked(show_bg);

        sb.setProgress(speed);
    }

    private void initView(){
        sb = f(R.id.sb_danmaku_speed);
        rg = f(R.id.rg_display_area);
        cb = f(R.id.cb_show_bg);
        rb1 = f(R.id.rb1);
        rb2 = f(R.id.rb2);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speed = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rb1.getId()) all_app = false;
                else if(checkedId==rb2.getId()) all_app = true;
            }
        });

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)show_bg = true;
                else show_bg = false;
            }
        });
    }

    private void initActionBar(){
        ActionBar actionBar = getActionBar();
        ViewUtil.customizeActionBar(actionBar, R.layout.actionbar_with_title_back);
        View view = actionBar.getCustomView();
        ((TextView)view.findViewById(R.id.textViewTitle)).setText(getResources().getText(R.string.set_danmaku));
        ((TextView)view.findViewById(R.id.textViewRight)).setText(getResources().getText(R.string.save));

        view.findViewById(R.id.layout_actionbar_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        view.findViewById(R.id.layout_actionbar_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataUtil.putInt(API_SPF.SPF_SETTING,API_SPF.ITEM_DANMAKU_SPEED,speed);
                DataUtil.putBoolean(API_SPF.SPF_SETTING, API_SPF.ITEM_DISPLAY_AREA, all_app);
                DataUtil.putBoolean(API_SPF.SPF_SETTING, API_SPF.ITEM_SHOW_BG, show_bg);
                finish();
            }
        });
    }

    private <T extends View>T f(int resId){
        return (T)super.findViewById(resId);
    }
}
