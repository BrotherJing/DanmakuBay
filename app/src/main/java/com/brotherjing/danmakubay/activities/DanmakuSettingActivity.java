package com.brotherjing.danmakubay.activities;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.brotherjing.danmakubay.R;
import com.brotherjing.danmakubay.api.API_SPF;
import com.brotherjing.danmakubay.utils.DataUtil;
import com.brotherjing.danmakubay.utils.ViewUtil;
import com.brotherjing.simpledanmakuview.Danmaku;
import com.brotherjing.simpledanmakuview.DanmakuView;

import java.lang.ref.WeakReference;

public class DanmakuSettingActivity extends AppCompatActivity {

    private static final int MESSAGE_SEND = 1;
    private static final int MESSAGE_FINISH = 2;

    SeekBar sb,sb_text_size,sb_height;
    RadioGroup rg,rg_speed;
    RadioButton rb1,rb2,rb01,rb02,rb03;
    CheckBox cb;

    FrameLayout ll;
    DanmakuView preview;
    View background;

    int speed,speed_level,text_size,danmaku_height;
    boolean all_app,show_bg;
    Danmaku.DanmakuSpeed danmakuSpeed;

    private final MyHandler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danmaku_setting);
        initToolbar();
        initView();
        initData();
        refreshView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(MESSAGE_SEND, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(MESSAGE_SEND);
        handler.sendEmptyMessage(MESSAGE_FINISH);
    }

    private void refreshView(){
        if(all_app) rg.check(rb2.getId());
        else rg.check(rb1.getId());

        switch (speed_level){
            case API_SPF.SPEED_LEVEL_SLOW:rg_speed.check(rb01.getId());break;
            case API_SPF.SPEED_LEVEL_NORMAL:rg_speed.check(rb02.getId());break;
            case API_SPF.SPEED_LEVEL_FAST:rg_speed.check(rb03.getId());break;
            default:break;
        }

        cb.setChecked(show_bg);

        sb.setProgress(speed);
        sb_text_size.setProgress(text_size);

        ViewGroup.LayoutParams params = ll.getLayoutParams();
        params.height = ViewUtil.dp2px(DanmakuSettingActivity.this,20+danmaku_height);
        ll.setLayoutParams(params);
        ll.requestLayout();
    }

    private void initData(){
        speed = DataUtil.getInt(API_SPF.SPF_SETTING, API_SPF.ITEM_DANMAKU_SPEED, 50);
        speed_level = DataUtil.getInt(API_SPF.SPF_SETTING,API_SPF.ITEM_DANMAKU_SPEED_LEVEL,API_SPF.SPEED_LEVEL_NORMAL);
        show_bg = DataUtil.getBoolean(API_SPF.SPF_SETTING, API_SPF.ITEM_SHOW_BG, true);
        all_app = DataUtil.getBoolean(API_SPF.SPF_SETTING, API_SPF.ITEM_DISPLAY_AREA,true);
        text_size = DataUtil.getInt(API_SPF.SPF_SETTING, API_SPF.ITEM_TEXT_SIZE, 50);
        danmaku_height = DataUtil.getInt(API_SPF.SPF_SETTING,API_SPF.ITEM_DANMAKU_HEIGHT,180);
    }

    private void initView(){
        sb = f(R.id.sb_danmaku_speed);
        sb_text_size = f(R.id.sb_text_size);
        sb_height = f(R.id.sb_danmaku_height);
        rg = f(R.id.rg_display_area);
        rg_speed = f(R.id.rg_danmaku_speed);
        cb = f(R.id.cb_show_bg);
        rb1 = f(R.id.rb1);
        rb2 = f(R.id.rb2);
        rb01 = f(R.id.rb01);
        rb02 = f(R.id.rb02);
        rb03 = f(R.id.rb03);
        ll = f(R.id.ll);
        preview = f(R.id.danmaku_view);
        background = f(R.id.background);

        preview.setMode(DanmakuView.MODE_NO_OVERDRAW);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speed = progress;
                preview.setMSPF(20 - (speed - 50) / 10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb_text_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text_size = progress;
                preview.setTextSize(18 + (progress - 50) / 10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb_height.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                danmaku_height = progress;
                ViewGroup.LayoutParams params = ll.getLayoutParams();
                params.height = ViewUtil.dp2px(DanmakuSettingActivity.this,20+danmaku_height);
                ll.setLayoutParams(params);
                ll.requestLayout();
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
                else if (checkedId == rb2.getId()) all_app = true;
            }
        });

        rg_speed.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==rb01.getId()){
                    speed_level = API_SPF.SPEED_LEVEL_SLOW;
                    danmakuSpeed = Danmaku.DanmakuSpeed.SLOW;
                }
                else if(checkedId==rb02.getId()){
                    speed_level = API_SPF.SPEED_LEVEL_NORMAL;
                    danmakuSpeed = Danmaku.DanmakuSpeed.NORMAL;
                }
                else if(checkedId==rb03.getId()){
                    speed_level = API_SPF.SPEED_LEVEL_FAST;
                    danmakuSpeed = Danmaku.DanmakuSpeed.FAST;
                }
            }
        });

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    show_bg = true;
                    background.setVisibility(View.VISIBLE);
                }
                else {
                    show_bg = false;
                    background.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initToolbar(){
        Toolbar toolbar = f(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(R.string.set_danmaku);
        toolbar.findViewById(R.id.tv_title).setOnClickListener(view -> {
            DataUtil.putInt(API_SPF.SPF_SETTING,API_SPF.ITEM_DANMAKU_SPEED,speed);
            DataUtil.putBoolean(API_SPF.SPF_SETTING, API_SPF.ITEM_DISPLAY_AREA, all_app);
            DataUtil.putBoolean(API_SPF.SPF_SETTING, API_SPF.ITEM_SHOW_BG, show_bg);
            DataUtil.putInt(API_SPF.SPF_SETTING, API_SPF.ITEM_TEXT_SIZE, text_size);
            DataUtil.putInt(API_SPF.SPF_SETTING,API_SPF.ITEM_DANMAKU_SPEED_LEVEL,speed_level);
            DataUtil.putInt(API_SPF.SPF_SETTING,API_SPF.ITEM_DANMAKU_HEIGHT,danmaku_height);
            finish();
        });
    }

    private <T extends View>T f(int resId){
        return (T)super.findViewById(resId);
    }

    final static class MyHandler extends Handler{
        private WeakReference<DanmakuSettingActivity> reference;

        public MyHandler(DanmakuSettingActivity activity){
            reference = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==MESSAGE_SEND) {
                Danmaku danmaku = new Danmaku("23333333");
                danmaku.setSpeed(reference.get().danmakuSpeed);
                reference.get().preview.addDanmaku(danmaku);
                sendEmptyMessageDelayed(MESSAGE_SEND, 500);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onOptionsItemSelected(item)) {
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
