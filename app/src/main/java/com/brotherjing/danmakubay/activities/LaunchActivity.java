package com.brotherjing.danmakubay.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.brotherjing.danmakubay.GlobalEnv;
import com.brotherjing.danmakubay.R;
import com.brotherjing.danmakubay.api.API_SPF;
import com.brotherjing.danmakubay.utils.CheckNetwork;
import com.brotherjing.danmakubay.utils.DataUtil;
import com.brotherjing.danmakubay.utils.Result;
import com.brotherjing.danmakubay.utils.beans.UserInfo;
import com.brotherjing.danmakubay.utils.providers.ShanbayProvider;
import com.google.gson.Gson;

public class LaunchActivity extends Activity {

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
        else if(DataUtil.getString(API_SPF.SPF_TOKEN, API_SPF.ITEM_COOKIES, null)!=null){
            new GetUserInfoTask().execute();
        }else{
            startActivity(new Intent(this, ChooseLoginType.class));
            finish();
        }
    }

    private class GetUserInfoTask extends AsyncTask<Void,Void,Result>{
        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            if(result.isSuccess()){
                GlobalEnv.setLogin(true);
                startActivity(new Intent(LaunchActivity.this,MainActivity.class));
                finish();
            }
        }

        @Override
        protected Result doInBackground(Void... params) {
            try{
                UserInfo userInfo = new ShanbayProvider().getUserInfo();
                DataUtil.putString(API_SPF.SPF_TOKEN,API_SPF.ITEM_USERINFO,new Gson().toJson(userInfo));
                if(userInfo!=null)return new Result(true,"");
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return new Result(false,"");
        }
    }

}