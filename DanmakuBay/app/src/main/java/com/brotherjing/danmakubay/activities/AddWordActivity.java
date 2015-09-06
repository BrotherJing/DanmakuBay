package com.brotherjing.danmakubay.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.brotherjing.danmakubay.App;
import com.brotherjing.danmakubay.R;
import com.brotherjing.danmakubay.utils.Result;
import com.brotherjing.danmakubay.utils.SoundManager;
import com.brotherjing.danmakubay.utils.TextUtil;
import com.brotherjing.danmakubay.utils.ViewUtil;
import com.brotherjing.danmakubay.utils.WordDBManager;
import com.brotherjing.danmakubay.utils.beans.SentenceBean;
import com.brotherjing.danmakubay.utils.beans.WordBean;
import com.brotherjing.danmakubay.utils.providers.ShanbayProvider;
import com.greendao.dao.Word;

import java.util.List;

public class AddWordActivity extends Activity {

    ShanbayProvider provider;
    WordDBManager wordDBManager;

    EditText et;
    TextView tvSearch,tvDesc,btnAdd,tvSentences;

    WordBean searchResult;
    List<SentenceBean> sentenceBeanList;

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
        tvSentences = f(R.id.tv_sentences);
        et = f(R.id.et_search);

        btnAdd.setVisibility(View.GONE);
        tvDesc.setVisibility(View.GONE);
        tvSentences.setVisibility(View.GONE);

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
                new AddWordTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    if (TextUtils.isEmpty(et.getText())) return false;
                    new SearchWordTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, et.getText().toString());
                }
                return false;
            }
        });
    }

    private void initData(){
        provider = new ShanbayProvider();
        wordDBManager = App.getWordDBManager();
        searchResult = null;
        sentenceBeanList = null;
    }

    private class AddWordTask extends AsyncTask<Void,Void,Result>{
        @Override
        protected Result doInBackground(Void... params) {
            if(provider.addNewWord(searchResult.getId())){
                Word word = provider.from(searchResult);
                if(!wordDBManager.ifExist(word)){
                    String file_dir = SoundManager.downloadSound(word);
                    if(!TextUtils.isEmpty(file_dir)){
                        word.setAudio_local(file_dir);
                        wordDBManager.addWord(word);
                        wordDBManager.addSentences(sentenceBeanList, word);
                        return new Result(true,"");
                    }
                }
            }
            return new Result(false,"");
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            if(result.isSuccess()){
                Toast.makeText(AddWordActivity.this,R.string.add_success,Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(AddWordActivity.this,R.string.add_fail,Toast.LENGTH_SHORT).show();
        }
    }

    private class SearchWordTask extends AsyncTask<String,Void,Result> {
        WordBean wordBean = null;
        @Override
        protected Result doInBackground(String... params) {
            try {
                wordBean = provider.getWord(params[0]);
                if(wordBean!=null) {
                    sentenceBeanList = provider.getSentences(wordBean.getId());
                    if(sentenceBeanList!=null)
                        return new Result(true, "");
                }
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
                tvDesc.setText("["+wordBean.getPronunciation() + "]\n" + wordBean.getDefinition());
                btnAdd.setVisibility(View.VISIBLE);
                searchResult = wordBean;
                if(!sentenceBeanList.isEmpty()){
                    tvSentences.setVisibility(View.VISIBLE);
                    String str = "";
                    for(SentenceBean sb:sentenceBeanList){
                        str+= TextUtil.removeTags(sb.getSentence())+"\n"+sb.getTranslation()+"\n\n";
                    }
                    tvSentences.setText(str);
                }
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
