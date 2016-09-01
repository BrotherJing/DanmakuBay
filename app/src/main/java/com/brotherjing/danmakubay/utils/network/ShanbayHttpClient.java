package com.brotherjing.danmakubay.utils.network;

import android.text.TextUtils;

import com.brotherjing.danmakubay.App;
import com.brotherjing.danmakubay.utils.Logger;
import com.brotherjing.danmakubay.utils.TextUtil;
import com.brotherjing.danmakubay.utils.beans.Account;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by jingyanga on 2016/9/1.
 */
public class ShanbayHttpClient {

    private static OkHttpClient client;
    private static Account sAccount;

    public static OkHttpClient getInstance(Account account){
        if(client==null||!account.equals(sAccount)){
            synchronized (ShanbayClient.class){
                if(client==null||!account.equals(sAccount)){
                    sAccount = account;
                    client = new OkHttpClient();
                    client.setConnectTimeout(10, TimeUnit.SECONDS);
                    client.interceptors().add(new HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT));
                    client.interceptors().add(mErrorInterceptor);
                    client.networkInterceptors().add(new AuthenticationInterceptor(account));
                }
            }
        }
        return client;
    }

    private static final class AuthenticationInterceptor implements Interceptor{

        private Account account;

        public AuthenticationInterceptor(Account account){
            this.account = account;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder requestBuilder = request.newBuilder();
            if(!TextUtils.isEmpty(account.getCookie())){
                requestBuilder.addHeader("Cookie",account.getCookie());
            }
            if(!TextUtils.isEmpty(account.getAccessToken())){
                requestBuilder.url(request.httpUrl().newBuilder().addQueryParameter("access_token",account.getAccessToken()).build());
            }
            request = requestBuilder.build();
            Logger.i(request.urlString());
            return chain.proceed(request);
        }
    }

    private static final Interceptor mErrorInterceptor = chain -> {

        Request request = chain.request();
        if(!ConnectivityHelper.isConnected(App.getInstance())){
            //RxBus.getBus().post(new NoNetworkEvent());
            //Log.i("yj",mContext.getResources().getString(R.string.error_not_connected));
            return null;
        }
        Response response = chain.proceed(request);
        if(!response.isSuccessful()){
            String msg = response.message();
            Logger.i(msg);
            //APIError error = GsonProvider.getInstance().fromJson(response.body().string(),APIError.class);
            //RxBus.getBus().post(new APIErrorEvent(response.code(),error));
            return null;
        }
        return response;
    };
}
