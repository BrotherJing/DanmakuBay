package com.brotherjing.danmakubay.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.brotherjing.danmakubay.GlobalEnv;
import com.brotherjing.danmakubay.api.API_SPF;
import com.brotherjing.danmakubay.api.API_URL;
import com.brotherjing.danmakubay.R;
import com.brotherjing.danmakubay.utils.DataUtil;
import com.brotherjing.danmakubay.utils.ViewUtil;


public class AuthLoginActivity extends Activity {

    private WebView webView;
    private String login_url;

    private CookieManager cookieManager;

    private boolean login_once;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login);
        webView = (WebView)findViewById(R.id.webview_auth_login);

        initActionBar();

        login_once = false;

        MyWebViewClient client = new MyWebViewClient();
        webView.setWebViewClient(client);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        cookieManager = CookieManager.getInstance();

        login_url = String.format(API_URL.URL_AUTH, API_URL.APP_KEY, API_URL.URL_CALLBACK);
        //Log.i("yj", login_url);
        webView.loadUrl(login_url);
    }

    private void initActionBar(){
        ActionBar actionBar = getActionBar();
        ViewUtil.customizeActionBar(actionBar, R.layout.actionbar_message_index);
        View view = actionBar.getCustomView();
        ((TextView)view.findViewById(R.id.textViewTitle)).setText(getResources().getText(R.string.app_title));
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
            if(url.endsWith(API_URL.SUFFIX_DENIED)){
                Toast.makeText(AuthLoginActivity.this,R.string.login_cancel,Toast.LENGTH_SHORT).show();
                finish();
            }
            else if(url.startsWith(API_URL.URL_CALLBACK)){
                login_once = true;
                String args = url.split("#")[1];
                if(args.startsWith("access_token")) {
                    String access_token = args.split("&")[0].split("=")[1];
                    DataUtil.putString(API_SPF.SPF_TOKEN,API_SPF.ITEM_ACCESS_TOKEN,access_token);
                    String cookies = cookieManager.getCookie(url);
                    DataUtil.putString(API_SPF.SPF_TOKEN, API_SPF.ITEM_COOKIES, cookies);
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
