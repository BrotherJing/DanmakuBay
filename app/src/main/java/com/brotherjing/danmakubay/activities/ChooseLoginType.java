package com.brotherjing.danmakubay.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.brotherjing.danmakubay.GlobalEnv;
import com.brotherjing.danmakubay.R;

public class ChooseLoginType extends AppCompatActivity {

    private static int REQ_CODE_LOGIN = 1;

    TextView btn_noaccount,btn_shanbay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_type);
        btn_noaccount = f(R.id.btn_login_noaccount);
        btn_shanbay = f(R.id.btn_login_shanbay);

        btn_noaccount.setOnClickListener(v -> {
            GlobalEnv.setLogin(false);
            startActivity(new Intent(ChooseLoginType.this,MainActivity.class));
            finish();
        });
        btn_shanbay.setOnClickListener(v -> startActivityForResult(new Intent(ChooseLoginType.this, AuthLoginActivity.class), REQ_CODE_LOGIN));
    }

    private <T extends View>T f(int resId){
        return (T)super.findViewById(resId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_CODE_LOGIN){
            if(resultCode==RESULT_OK){
                startActivity(new Intent(ChooseLoginType.this,MainActivity.class));
                finish();
            }
        }
    }
}
