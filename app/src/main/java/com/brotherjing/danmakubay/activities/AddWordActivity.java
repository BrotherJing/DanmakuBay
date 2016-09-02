package com.brotherjing.danmakubay.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.brotherjing.danmakubay.App;
import com.brotherjing.danmakubay.GlobalEnv;
import com.brotherjing.danmakubay.R;
import com.brotherjing.danmakubay.utils.SoundManager;
import com.brotherjing.danmakubay.utils.TextUtil;
import com.brotherjing.danmakubay.utils.WordDBManager;
import com.brotherjing.danmakubay.utils.beans.SentenceBean;
import com.brotherjing.danmakubay.utils.beans.SentenceResponse;
import com.brotherjing.danmakubay.utils.beans.ShanbayResponse;
import com.brotherjing.danmakubay.utils.beans.WordBean;
import com.brotherjing.danmakubay.utils.beans.WordResponse;
import com.brotherjing.danmakubay.utils.network.BaseSubscriber;
import com.brotherjing.danmakubay.utils.network.ShanbayClient;
import com.brotherjing.danmakubay.utils.providers.ShanbayProvider;
import com.greendao.dao.Word;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddWordActivity extends BaseActivity {

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
        initToolbar();
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

        tvSearch.setOnClickListener(v -> {
            if (TextUtils.isEmpty(et.getText())) return;
            searchWord(et.getText().toString());
        });
        btnAdd.setOnClickListener(v -> addWord());
        et.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (TextUtils.isEmpty(et.getText())) return false;
                searchWord(et.getText().toString());
            }
            return false;
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
            addSubscription(ShanbayClient.getInstance().addNewWord(searchResult.getId())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ShanbayResponse>() {
                    @Override
                    public void onNext(ShanbayResponse shanbayResponse) {
                        if(shanbayResponse.getStatus_code()==0){
                            saveWord();
                            return;
                        }
                        Toast.makeText(AddWordActivity.this,R.string.add_fail,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Toast.makeText(AddWordActivity.this,R.string.add_fail,Toast.LENGTH_SHORT).show();
                    }
                }));
        }
    }

    private void saveWord(){
        final Word word = provider.from(searchResult);
        if(!wordDBManager.ifExist(word)){
            addSubscription(SoundManager.downloadAudio(word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onNext(String file_dir) {
                        if(!TextUtils.isEmpty(file_dir)){
                            word.setAudio_local(file_dir);
                            wordDBManager.addWord(word);
                            wordDBManager.addSentences(sentenceBeanList, word);
                            Toast.makeText(AddWordActivity.this,R.string.add_success,Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Toast.makeText(AddWordActivity.this,R.string.add_fail,Toast.LENGTH_SHORT).show();
                    }
                }));
        }
    }

    private void searchWord(String word){
        addSubscription(ShanbayClient.getInstance().getWord(word)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseSubscriber<WordResponse>() {
                @Override
                public void onNext(WordResponse wordResponse) {
                    searchResult = wordResponse.getWordBean();
                    if (GlobalEnv.isLogin()) {
                        getSentences(searchResult.getId());
                    } else {
                        inflateView();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    Toast.makeText(AddWordActivity.this, R.string.get_fail, Toast.LENGTH_SHORT).show();
                }
            }));
    }

    private void getSentences(long word_id){
        addSubscription(ShanbayClient.getInstance().getSentences(word_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseSubscriber<SentenceResponse>() {
                @Override
                public void onNext(SentenceResponse sentenceResponse) {
                    sentenceBeanList = sentenceResponse.getList();
                    inflateView();
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    Toast.makeText(AddWordActivity.this, R.string.get_fail, Toast.LENGTH_SHORT).show();
                }
            }));
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

    protected void initToolbar() {
        Toolbar toolbar = f(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
