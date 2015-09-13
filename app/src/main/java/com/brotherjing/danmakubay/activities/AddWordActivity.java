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
import com.brotherjing.danmakubay.GlobalEnv;
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
            if(GlobalEnv.isLogin()){
                if(!provider.addNewWord(searchResult.getId())){
                    return new Result(false,getString(R.string.word_not_added_remote));
                }
            }
            Word word = provider.from(searchResult);
            if(!wordDBManager.ifExist(word)){
                String file_dir = SoundManager.downloadSound(word);
                if(!TextUtils.isEmpty(file_dir)){
                    word.setAudio_local(file_dir);
                    wordDBManager.addWord(word);
                    wordDBManager.addSentences(sentenceBeanList, word);
                    return new Result(true,getString(R.string.add_success));
                }
            }
            return new Result(false,getString(R.string.add_fail));
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            Toast.makeText(AddWordActivity.this,result.getMsg(),Toast.LENGTH_SHORT).show();
        }
    }

    private class SearchWordTask extends AsyncTask<String,Void,Result> {
        WordBean wordBean = null;
        @Override
        protected Result doInBackground(String... params) {
            try {
                wordBean = provider.getWord(params[0]);
                /*if(wordBean!=null) {
                    sentenceBeanList = provider.getSentences(wordBean.getId());
                    if(sentenceBeanList!=null)
                }*/
                if(wordBean ==null)return new Result(false,getString(R.string.get_fail));
                if(GlobalEnv.isLogin()){
                    sentenceBeanList = provider.getSentences(wordBean.getId());
                    if(sentenceBeanList==null)return new Result(false,getString(R.string.get_fail));
                }
                return new Result(true, "");
            }catch (Exception ex) {
                ex.printStackTrace();
            }
            return new Result(false,getString(R.string.get_fail));
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            if(result.isSuccess()){
                tvDesc.setVisibility(View.VISIBLE);
                tvDesc.setText("["+wordBean.getPronunciation() + "]\n" + wordBean.getDefinition());
                btnAdd.setVisibility(View.VISIBLE);
                searchResult = wordBean;
                if(sentenceBeanList!=null&&!sentenceBeanList.isEmpty()){
                    tvSentences.setVisibility(View.VISIBLE);
                    String str = "";
                    for(SentenceBean sb:sentenceBeanList){
                        str+= TextUtil.removeTags(sb.getSentence())+"\n"+sb.getTranslation()+"\n\n";
                    }
                    tvSentences.setText(str);
                }else if(!GlobalEnv.isLogin()){
                    tvSentences.setVisibility(View.VISIBLE);
                    tvSentences.setText(R.string.login_to_get_sentences);
                }else{
                    tvSentences.setVisibility(View.GONE);
                }
            }
            else Toast.makeText(AddWordActivity.this,result.getMsg(),Toast.LENGTH_SHORT).show();
        }
    }

    private <T extends View>T f(int resId){
        return (T)super.findViewById(resId);
    }

    private void initActionBar(){
        ActionBar actionBar = getActionBar();
        ViewUtil.customizeActionBar(actionBar, R.layout.actionbar_with_title_back);
        View view = actionBar.getCustomView();
        ((TextView)view.findViewById(R.id.textViewTitle)).setText(getResources().getText(R.string.add_word));

        view.findViewById(R.id.layout_actionbar_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
