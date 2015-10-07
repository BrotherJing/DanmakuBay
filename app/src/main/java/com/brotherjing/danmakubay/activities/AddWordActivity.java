package com.brotherjing.danmakubay.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.brotherjing.danmakubay.App;
import com.brotherjing.danmakubay.GlobalEnv;
import com.brotherjing.danmakubay.R;
import com.brotherjing.danmakubay.base.BasicActionBarActivity;
import com.brotherjing.danmakubay.utils.Result;
import com.brotherjing.danmakubay.utils.SoundManager;
import com.brotherjing.danmakubay.utils.TextUtil;
import com.brotherjing.danmakubay.utils.ViewUtil;
import com.brotherjing.danmakubay.utils.WordDBManager;
import com.brotherjing.danmakubay.utils.beans.SentenceBean;
import com.brotherjing.danmakubay.utils.beans.SentenceResponse;
import com.brotherjing.danmakubay.utils.beans.ShanbayResponse;
import com.brotherjing.danmakubay.utils.beans.WordBean;
import com.brotherjing.danmakubay.utils.beans.WordResponse;
import com.brotherjing.danmakubay.utils.providers.ShanbayProvider;
import com.greendao.dao.Word;

import java.util.List;

public class AddWordActivity extends BasicActionBarActivity {

    private static final String TAG = "AddWordActivity";

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
        initActionBar(R.layout.actionbar_with_title_back);
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
                //new SearchWordTask().execute(et.getText().toString());
                searchWord(et.getText().toString());
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new AddWordTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                addWord();
            }
        });
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (TextUtils.isEmpty(et.getText())) return false;
                    //new SearchWordTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, et.getText().toString());
                    searchWord(et.getText().toString());
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

    private void addWord(){
        if(GlobalEnv.isLogin()){
            provider.addNewWord(this, searchResult.getId(), new Response.Listener<ShanbayResponse>() {
                @Override
                public void onResponse(ShanbayResponse response) {
                    if(response.getStatus_code()==0){
                        saveWord();
                        return;
                    }
                    Toast.makeText(AddWordActivity.this,R.string.add_fail,Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AddWordActivity.this,R.string.add_fail,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void saveWord(){
        final Word word = provider.from(searchResult);
        if(!wordDBManager.ifExist(word)){
            SoundManager.downloadSound(this, word, new Response.Listener<String>() {
                @Override
                public void onResponse(String file_dir) {
                    if(!TextUtils.isEmpty(file_dir)){
                        word.setAudio_local(file_dir);
                        wordDBManager.addWord(word);
                        wordDBManager.addSentences(sentenceBeanList, word);
                        Toast.makeText(AddWordActivity.this,R.string.add_success,Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AddWordActivity.this,R.string.add_fail,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void searchWord(String word){
        provider.getWord(this, word, new Response.Listener<WordResponse>() {
            @Override
            public void onResponse(WordResponse response) {
                searchResult = response.getWordBean();
                if (GlobalEnv.isLogin()) {
                    getSentences(searchResult.getId());
                } else {
                    inflateView();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddWordActivity.this, R.string.get_fail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSentences(long word_id){
        provider.getSentences(this, word_id, new Response.Listener<SentenceResponse>() {
            @Override
            public void onResponse(SentenceResponse response) {
                sentenceBeanList = response.getList();
                inflateView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddWordActivity.this, R.string.get_fail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inflateView(){
        tvDesc.setVisibility(View.VISIBLE);
        tvDesc.setText("[" + searchResult.getPronunciation() + "]\n" + searchResult.getDefinition());
        btnAdd.setVisibility(View.VISIBLE);
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

    @Override
    protected void initActionBar(int layoutId) {
        super.initActionBar(layoutId);
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
