package com.brotherjing.danmakubay.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brotherjing.danmakubay.R;
import com.brotherjing.danmakubay.api.API_SPF;
import com.brotherjing.danmakubay.services.FloatToolService;
import com.brotherjing.danmakubay.services.FloatWindowService;
import com.brotherjing.danmakubay.utils.CheckNetwork;
import com.brotherjing.danmakubay.utils.DataUtil;
import com.brotherjing.danmakubay.utils.Result;
import com.brotherjing.danmakubay.utils.ViewUtil;
import com.brotherjing.danmakubay.utils.beans.UserInfo;
import com.brotherjing.danmakubay.utils.providers.ShanbayProvider;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class MainActivity extends Activity {

    private final int NO_NETWORK = 0x1;
    private final int WINDOW_OPENED = 0x02;
    private final int FLOAT_TOOL_OPENED = 0x04;

    private EditText et;
    private Button btn;
    private ImageView ivAvatar;
    private TextView tvName;
    private TextView tvOpenWindow,tvOpenFindWindow,tvSetDanmaku,tvAddWord,tvWordList;

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
        ActionBar actionBar = getActionBar();
        ViewUtil.customizeActionBar(actionBar, R.layout.actionbar_message_index);
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
        if(userinforaw==null&&(flag&NO_NETWORK)==0){
            new GetUserInfoTask().execute();
        }else if(userinforaw!=null){
            userInfo = new Gson().fromJson(userinforaw,UserInfo.class);
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
        tvName.setText(userInfo.getUsername());
        ImageLoader.getInstance().displayImage(userInfo.getAvatar(), ivAvatar);
    }

    private void initListener(){
        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SearchWordTask().execute(et.getText().toString());
            }
        });
        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(MainActivity.this,FloatWindowService.class);
                startService(intent);
                MainActivity.this.finish();
                return true;
            }
        });*/
        tvOpenWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        tvSetDanmaku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,DanmakuSettingActivity.class));
            }
        });
        tvAddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddWordActivity.class));
            }
        });
        tvWordList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,WordListActivity.class));
            }
        });
        tvOpenFindWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
    }

    private <T extends View>T f(int resId){
        return (T)super.findViewById(resId);
    }

    private class GetUserInfoTask extends AsyncTask<Void,Void,Result>{
        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            if(result.isSuccess()){
                //Toast.makeText(MainActivity.this,userInfo.getUsername(),Toast.LENGTH_SHORT).show();
                refreshView();
            }else{
                Toast.makeText(MainActivity.this,"not login",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Result doInBackground(Void... params) {
            userInfo = provider.getUserInfo();
            return userInfo==null?new Result(false,""):new Result(true,"");
        }
    }
}
