package com.brotherjing.danmakubay.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.brotherjing.danmakubay.R;
import com.brotherjing.danmakubay.utils.Result;
import com.brotherjing.danmakubay.utils.ViewUtil;
import com.brotherjing.danmakubay.utils.WordDBManager;
import com.brotherjing.danmakubay.utils.beans.WordBean;
import com.brotherjing.danmakubay.utils.providers.ShanbayProvider;
import com.greendao.dao.Word;

public class AddWordActivity extends Activity {

    ShanbayProvider provider;
    WordDBManager wordDBManager;

    EditText et;
    TextView tvSearch,tvDesc,btnAdd;

    WordBean searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        initActionBar();
        initView();
        initData();
    }

    private void initView(){
        tvSearch = f(R.id.btn_search);
        tvDesc = f(R.id.tv_description);
        btnAdd = f(R.id.btn_add_word);
        et = f(R.id.et_search);

        btnAdd.setVisibility(View.GONE);
        tvDesc.setVisibility(View.GONE);

        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et.getText())) return;
                new SearchWordTask().execute(et.getText().toString());
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddWordTask().execute();
            }
        });
    }

    private void initData(){
        provider = new ShanbayProvider();
        wordDBManager = new WordDBManager(this);
    }

    private class AddWordTask extends AsyncTask<Void,Void,Result>{
        @Override
        protected Result doInBackground(Void... params) {
            if(provider.addNewWord(searchResult.getId())){
                Word word = provider.from(searchResult);
                if(wordDBManager.addWord(word))return new Result(true,"");
            }
            return new Result(false,"");
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            if(result.isSuccess()){
                Toast.makeText(AddWordActivity.this,"success",Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(AddWordActivity.this,"fail",Toast.LENGTH_SHORT).show();
        }
    }

    private class SearchWordTask extends AsyncTask<String,Void,Result> {
        WordBean wordBean = null;
        @Override
        protected Result doInBackground(String... params) {
            try {
                wordBean = provider.getWord(params[0]);
                if(wordBean!=null)
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
                tvDesc.setVisibility(View.VISIBLE);
                tvDesc.setText(wordBean.getPronunciation() + "\n" + wordBean.getDefinition());
                btnAdd.setVisibility(View.VISIBLE);
                searchResult = wordBean;
            }
            else Toast.makeText(AddWordActivity.this,R.string.get_fail,Toast.LENGTH_SHORT).show();
        }
    }

    private <T extends View>T f(int resId){
        return (T)super.findViewById(resId);
    }

    private void initActionBar(){
        ActionBar actionBar = getActionBar();
        ViewUtil.customizeActionBar(actionBar, R.layout.actionbar_message_index);
        View view = actionBar.getCustomView();
        ((TextView)view.findViewById(R.id.textViewTitle)).setText(getResources().getText(R.string.add_word));
    }
}
