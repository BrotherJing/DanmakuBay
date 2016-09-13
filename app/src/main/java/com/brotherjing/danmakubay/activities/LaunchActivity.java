package com.brotherjing.danmakubay.activities;

import android.content.Intent;
import android.os.Bundle;

import com.brotherjing.danmakubay.GlobalEnv;
import com.brotherjing.danmakubay.R;
import com.brotherjing.danmakubay.api.API_SPF;
import com.brotherjing.danmakubay.utils.CheckNetwork;
import com.brotherjing.danmakubay.utils.DataUtil;
import com.brotherjing.danmakubay.utils.beans.UserInfo;
import com.brotherjing.danmakubay.utils.network.BaseSubscriber;
import com.brotherjing.danmakubay.utils.network.ShanbayClient;
import com.google.gson.Gson;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        checkLogin();
    }

    private void checkLogin(){
        if(new CheckNetwork(this).getConnectionType()==0){
            //offline scenario
            GlobalEnv.setLogin(false);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else if(DataUtil.getString(API_SPF.SPF_TOKEN, API_SPF.ITEM_ACCOUNT, null)!=null){
            //new GetUserInfoTask().execute();
            addSubscription(ShanbayClient.getInstance().getUserInfo().subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new BaseSubscriber<UserInfo>() {
                                @Override
                                public void onNext(UserInfo userInfo) {
                                    DataUtil.putString(API_SPF.SPF_TOKEN,API_SPF.ITEM_USERINFO,new Gson().toJson(userInfo));
                                    GlobalEnv.setLogin(true);
                                    startActivity(new Intent(LaunchActivity.this,MainActivity.class));
                                    finish();
                                }
                            }));
        }else{
            startActivity(new Intent(this, ChooseLoginType.class));
            finish();
        }
    }

}
