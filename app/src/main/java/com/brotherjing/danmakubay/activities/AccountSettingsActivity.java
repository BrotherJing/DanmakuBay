package com.brotherjing.danmakubay.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brotherjing.danmakubay.R;
import com.brotherjing.danmakubay.api.API_SPF;
import com.brotherjing.danmakubay.base.BasicActionBarActivity;
import com.brotherjing.danmakubay.utils.DataUtil;
import com.brotherjing.danmakubay.utils.TextUtil;
import com.brotherjing.danmakubay.utils.ViewUtil;
import com.brotherjing.danmakubay.utils.beans.UserInfo;
import com.brotherjing.danmakubay.utils.providers.AccountProvider;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AccountSettingsActivity extends BaseActivity {

    public static int RES_CODE_LOGOUT = 2;

    private ImageView ivAvatar;
    private TextView tvName,tvNickname;
    private Button btnLogout;

    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        initToolbar();

        ivAvatar = f(R.id.iv_avatar);
        tvName = f(R.id.tv_username);
        tvNickname = f(R.id.tv_nickname);
        btnLogout = f(R.id.btn_logout);

        initData();

        btnLogout.setOnClickListener(v -> {
            AccountProvider.logout();
            Toast.makeText(AccountSettingsActivity.this,R.string.logout,Toast.LENGTH_SHORT).show();
            setResult(RES_CODE_LOGOUT);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    private void initToolbar(){
        Toolbar toolbar = f(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewUtil.initStatusBar(this);
    }

    private void initData(){
        String rawInfo = DataUtil.getString(API_SPF.SPF_TOKEN, API_SPF.ITEM_USERINFO, null);
        if(TextUtils.isEmpty(rawInfo))finish();
        userInfo = new Gson().fromJson(rawInfo, UserInfo.class);
        refreshView();
    }

    private void refreshView(){
        tvName.setText(userInfo.getUsername());
        ImageLoader.getInstance().displayImage(userInfo.getAvatar(), ivAvatar);
        tvNickname.setText(userInfo.getNickname());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onOptionsItemSelected(item)) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
