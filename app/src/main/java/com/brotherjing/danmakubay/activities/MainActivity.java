package com.brotherjing.danmakubay.activities;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brotherjing.danmakubay.GlobalEnv;
import com.brotherjing.danmakubay.R;
import com.brotherjing.danmakubay.api.API_SPF;
import com.brotherjing.danmakubay.base.BasicActionBarActivity;
import com.brotherjing.danmakubay.services.FloatToolService;
import com.brotherjing.danmakubay.services.FloatWindowService;
import com.brotherjing.danmakubay.utils.CheckNetwork;
import com.brotherjing.danmakubay.utils.DataUtil;
import com.brotherjing.danmakubay.utils.Result;
import com.brotherjing.danmakubay.utils.beans.UserInfo;
import com.brotherjing.danmakubay.utils.providers.ShanbayProvider;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.List;


public class MainActivity extends BasicActionBarActivity implements View.OnClickListener{

    public static final int REQ_CODE_LOGIN = 1;
    public static final int REQ_CODE_ACCOUNTSETTING = 2;

    private final int NO_NETWORK = 0x1;
    private final int WINDOW_OPENED = 0x02;
    private final int FLOAT_TOOL_OPENED = 0x04;

    private ImageView ivAvatar;
    private TextView tvName,tvOpenWindow,tvOpenFindWindow,tvSetDanmaku,tvAddWord,tvWordList;

    private UserInfo userInfo;

    private ShanbayProvider provider;

    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActionBar();

        initView();

        initData();

        initListener();
    }

    private void initActionBar(){
        super.initActionBar(R.layout.actionbar_message_index);
        View view = actionBar.getCustomView();
        ((TextView)view.findViewById(R.id.textViewTitle)).setText(getResources().getText(R.string.app_title));
    }

    private void initView(){
        ivAvatar = f(R.id.iv_avatar);
        tvName = f(R.id.tv_username);

        tvOpenWindow = f(R.id.tv_open_window);
        tvOpenFindWindow = f(R.id.tv_open_find_window);
        tvSetDanmaku = f(R.id.tv_set_danmaku);
        tvAddWord = f(R.id.tv_add_word);
        tvWordList = f(R.id.tv_word_list);
    }

    private void initData(){
        flag = 0;
        if(new CheckNetwork(this).getConnectionType()==0)flag|=NO_NETWORK;
        provider = new ShanbayProvider();

        String userinforaw = DataUtil.getString(API_SPF.SPF_TOKEN, API_SPF.ITEM_USERINFO, null);
        if(GlobalEnv.isLogin()&&userinforaw==null&&(flag&NO_NETWORK)==0){
            new GetUserInfoTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else if(userinforaw!=null){
            userInfo = new Gson().fromJson(userinforaw,UserInfo.class);
            refreshView();
        }else if(!GlobalEnv.isLogin()){
            refreshView();
        }

        checkServiceState();
    }

    private void checkServiceState(){
        ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfoList = manager.getRunningServices(100);
        for(int i=0;i<serviceInfoList.size();++i){
            if(serviceInfoList.get(i).service.getClassName().equals("com.brotherjing.danmakubay.services.FloatWindowService")){
                flag|=WINDOW_OPENED;
                tvOpenWindow.setText(R.string.close_window);
                break;
            }
        }
        for(int i=0;i<serviceInfoList.size();++i){
            if(serviceInfoList.get(i).service.getClassName().equals("com.brotherjing.danmakubay.services.FloatToolService")){
                flag|=FLOAT_TOOL_OPENED;
                tvOpenFindWindow.setText(R.string.close_find_window);
                break;
            }
        }
    }

    private void refreshView(){
        if(GlobalEnv.isLogin()) {
            tvName.setText(userInfo.getUsername());
            ImageLoader.getInstance().displayImage(userInfo.getAvatar(), ivAvatar, new AnimateListener());
        }else{
            ivAvatar.setImageBitmap(null);
            tvName.setText(R.string.not_login);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_open_window:
                if((flag&WINDOW_OPENED)==0) {
                    Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
                    startService(intent);
                    tvOpenWindow.setText(R.string.close_window);
                    Toast.makeText(MainActivity.this, R.string.window_opened, Toast.LENGTH_SHORT).show();
                    flag|=WINDOW_OPENED;
                }else{
                    Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
                    stopService(intent);
                    tvOpenWindow.setText(R.string.open_window);
                    Toast.makeText(MainActivity.this, R.string.window_closed, Toast.LENGTH_SHORT).show();
                    flag&=~WINDOW_OPENED;
                }
                break;
            case R.id.tv_set_danmaku:startActivity(new Intent(MainActivity.this,DanmakuSettingActivity.class));break;
            case R.id.tv_add_word:startActivity(new Intent(MainActivity.this,AddWordActivity.class));break;
            case R.id.tv_word_list:startActivity(new Intent(MainActivity.this,WordListActivity.class));break;
            case R.id.tv_open_find_window:
                if((flag&FLOAT_TOOL_OPENED)==0) {
                    Intent intent = new Intent(MainActivity.this, FloatToolService.class);
                    startService(intent);
                    Toast.makeText(MainActivity.this, R.string.tool_opened, Toast.LENGTH_SHORT).show();
                    tvOpenFindWindow.setText(R.string.close_find_window);
                    flag|=FLOAT_TOOL_OPENED;
                }else{
                    Intent intent = new Intent(MainActivity.this, FloatToolService.class);
                    stopService(intent);
                    Toast.makeText(MainActivity.this, R.string.tool_closed, Toast.LENGTH_SHORT).show();
                    tvOpenFindWindow.setText(R.string.open_find_window);
                    flag&=~FLOAT_TOOL_OPENED;
                }
                break;
            case R.id.iv_avatar:case R.id.tv_username:
                if(GlobalEnv.isLogin()){
                    startActivityForResult(new Intent(this, AccountSettingsActivity.class), REQ_CODE_ACCOUNTSETTING);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else{
                    startActivityForResult(new Intent(this,AuthLoginActivity.class),REQ_CODE_LOGIN);
                }
                break;
            default:break;
        }
    }

    private void initListener(){
        tvOpenWindow.setOnClickListener(this);
        tvSetDanmaku.setOnClickListener(this);
        tvAddWord.setOnClickListener(this);
        tvWordList.setOnClickListener(this);
        tvOpenFindWindow.setOnClickListener(this);
        tvName.setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_CODE_LOGIN){
            if(resultCode==RESULT_OK){
                initData();
            }
        }else if(requestCode==REQ_CODE_ACCOUNTSETTING){
            if(resultCode==AccountSettingsActivity.RES_CODE_LOGOUT){
                refreshView();
            }
        }
    }

    private class GetUserInfoTask extends AsyncTask<Void,Void,Result>{
        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            if(result.isSuccess()){
                DataUtil.putString(API_SPF.SPF_TOKEN, API_SPF.ITEM_USERINFO, new Gson().toJson(userInfo));
                //Toast.makeText(MainActivity.this,userInfo.getUsername(),Toast.LENGTH_SHORT).show();
                refreshView();
            }else{
                Toast.makeText(MainActivity.this,R.string.not_login,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Result doInBackground(Void... params) {
            userInfo = provider.getUserInfo();
            return userInfo==null?new Result(false,""):new Result(true,"");
        }
    }

    private static class AnimateListener extends SimpleImageLoadingListener {

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                    FadeInBitmapDisplayer.animate(imageView, 500);
            }
        }
    }
}