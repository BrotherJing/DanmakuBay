package com.brotherjing.danmakubay.activities;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.brotherjing.danmakubay.GlobalEnv;
import com.brotherjing.danmakubay.api.API_SPF;
import com.brotherjing.danmakubay.R;
import com.brotherjing.danmakubay.utils.DataUtil;
import com.brotherjing.danmakubay.utils.ViewUtil;
import com.brotherjing.danmakubay.utils.beans.Account;
import com.brotherjing.danmakubay.utils.network.ShanbayAPI;
import com.brotherjing.danmakubay.utils.network.ShanbayClient;
import com.google.gson.Gson;


public class AuthLoginActivity extends AppCompatActivity {

    private WebView webView;
    private String login_url;

    private CookieManager cookieManager;

    private boolean login_once;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login);
        webView = (WebView)findViewById(R.id.webview_auth_login);

        initToolbar();

        login_once = false;

        MyWebViewClient client = new MyWebViewClient();
        webView.setWebViewClient(client);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        cookieManager = CookieManager.getInstance();

        login_url = String.format(ShanbayAPI.URL_AUTH, ShanbayAPI.APP_KEY, ShanbayAPI.URL_CALLBACK);
        //Log.i("yj", login_url);
        webView.loadUrl(login_url);
    }

    private void initToolbar(){
        Toolbar toolbar = f(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView)toolbar.findViewById(R.id.tv_title)).setText(R.string.app_title);

        ViewUtil.initStatusBar(this);
    }

    private <T extends View>T f(int resId){
        return (T)super.findViewById(resId);
    }

    private class MyWebViewClient extends WebViewClient{
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(login_once)finish();
            if(url.endsWith(ShanbayAPI.SUFFIX_DENIED)){
                Toast.makeText(AuthLoginActivity.this,R.string.login_cancel,Toast.LENGTH_SHORT).show();
                finish();
            }
            else if(url.startsWith(ShanbayAPI.URL_CALLBACK)){
                login_once = true;
                String args = url.split("#")[1];
                if(args.startsWith("access_token")) {
                    String access_token = args.split("&")[0].split("=")[1];
                    //DataUtil.putString(API_SPF.SPF_TOKEN,API_SPF.ITEM_ACCESS_TOKEN,access_token);
                    String cookies = cookieManager.getCookie(url);
                    //DataUtil.putString(API_SPF.SPF_TOKEN, API_SPF.ITEM_COOKIES, cookies);
                    Account account = new Account(cookies, access_token);
                    ShanbayClient.setAccount(account);
                    DataUtil.putString(API_SPF.SPF_TOKEN, API_SPF.ITEM_ACCOUNT, new Gson().toJson(account));
                    Toast.makeText(AuthLoginActivity.this,R.string.login_success,Toast.LENGTH_SHORT).show();
                    GlobalEnv.setLogin(true);
                    setResult(RESULT_OK);
                    //Log.i("yj","cookies in login page is "+cookies);
                }else{
                    Toast.makeText(AuthLoginActivity.this,R.string.unauthorized,Toast.LENGTH_SHORT).show();
                }

                //startActivity(new Intent(AuthLoginActivity.this,MainActivity.class));
                finish();
            }
        }

        /*@Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }*/
    }

}
