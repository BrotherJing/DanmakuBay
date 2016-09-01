package com.brotherjing.danmakubay.utils.network;

import android.text.TextUtils;

import com.brotherjing.danmakubay.api.API_SPF;
import com.brotherjing.danmakubay.utils.DataUtil;
import com.brotherjing.danmakubay.utils.beans.Account;
import com.google.gson.Gson;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by jingyanga on 2016/9/1.
 */
public class ShanbayClient {

    private static Account sAccount;
    private static ShanbayAPI shanbayAPI;

    public static ShanbayAPI getInstance(Account account){
        if(shanbayAPI==null||!account.equals(sAccount)){
            synchronized (ShanbayClient.class){
                if(shanbayAPI==null||!account.equals(sAccount)){
                    sAccount = account;
                    createInstance();
                }
            }
        }
        return shanbayAPI;
    }

    public static void setAccount(Account account){
        sAccount = account;
        shanbayAPI = null;
    }

    public static ShanbayAPI getInstance(){
        if(sAccount==null){
            String json = DataUtil.getString(API_SPF.SPF_TOKEN,API_SPF.ITEM_ACCOUNT,null);
            if(!TextUtils.isEmpty(json)){
                sAccount = new Gson().fromJson(json,Account.class);
            }else{
                sAccount = new Account();
            }
        }
        return getInstance(sAccount);
    }

    private static void createInstance(){
        Executor executor = Executors.newCachedThreadPool();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ShanbayAPI.BASE_URL)
                .callbackExecutor(executor)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(ShanbayHttpClient.getInstance(sAccount))
                .build();
        shanbayAPI = retrofit.create(ShanbayAPI.class);
    }

}
