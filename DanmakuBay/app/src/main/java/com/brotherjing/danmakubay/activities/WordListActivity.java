package com.brotherjing.danmakubay.activities;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.brotherjing.danmakubay.R;
import com.brotherjing.danmakubay.utils.WordDBManager;
import com.brotherjing.danmakubay.utils.views.UniversalAdapter;
import com.brotherjing.danmakubay.utils.views.UniversalViewHolder;
import com.greendao.dao.Word;

import java.util.List;

public class WordListActivity extends Activity {

    List<Word> wordList;
    WordDBManager wordDBManager;

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        lv = (ListView)findViewById(R.id.lv);
        wordDBManager = new WordDBManager(this);
        wordList = wordDBManager.getList();
        lv.setAdapter(new UniversalAdapter<Word>(this,wordList,R.layout.item_word_list) {
            @Override
            public void convert(UniversalViewHolder viewHolder, Word item, int position) {
                TextView tvWord = viewHolder.getView(R.id.tvWord);
                TextView tvPron = viewHolder.getView(R.id.tvPronounce);
                TextView tvDesc = viewHolder.getView(R.id.tvDesc);
                tvWord.setText(item.getWord());
                tvPron.setText(item.getPronounce());
                tvDesc.setText(item.getDefinition());
            }
        });
    }

}
