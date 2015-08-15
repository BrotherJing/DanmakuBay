package com.brotherjing.danmakubay.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.brotherjing.danmakubay.R;
import com.brotherjing.danmakubay.utils.Result;
import com.brotherjing.danmakubay.utils.beans.UserInfo;
import com.brotherjing.danmakubay.utils.beans.Word;
import com.brotherjing.danmakubay.utils.providers.ShanbayProvider;


public class MainActivity extends ActionBarActivity {

    private EditText et;
    private Button btn;

    private UserInfo userInfo;

    private ShanbayProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = f(R.id.et_word);
        btn = f(R.id.btn_search);

        provider = new ShanbayProvider();

        initListener();

        new GetUserInfoTask().execute();
    }

    private void initListener(){
        btn.setOnClickListener(new View.OnClickListener() {
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
        });
    }

    private <T extends View>T f(int resId){
        return (T)super.findViewById(resId);
    }

    private class SearchWordTask extends AsyncTask<String,Void,Result>{
        Word word;
        @Override
        protected Result doInBackground(String... params) {
            try {
                word = provider.getWord(params[0]);
                if(provider.addNewWord(word.getId()))
                    return new Result(true, "");
            }catch (Exception ex) {
                ex.printStackTrace();
            }
            return new Result(false, "");
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            if(result.isSuccess()){
                Toast.makeText(MainActivity.this,word.getDefinition(),Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(MainActivity.this,"fail",Toast.LENGTH_SHORT).show();
        }
    }

    private class GetUserInfoTask extends AsyncTask<Void,Void,Result>{
        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            if(result.isSuccess()){
                Toast.makeText(MainActivity.this,userInfo.getUsername(),Toast.LENGTH_SHORT).show();
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
