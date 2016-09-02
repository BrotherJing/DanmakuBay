package com.brotherjing.danmakubay.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.brotherjing.danmakubay.App;
import com.brotherjing.danmakubay.R;
import com.brotherjing.danmakubay.utils.SoundManager;
import com.brotherjing.danmakubay.utils.WordDBManager;
import com.brotherjing.danmakubay.utils.views.UniversalAdapter;
import com.brotherjing.danmakubay.utils.views.UniversalViewHolder;
import com.greendao.dao.Word;

import java.util.List;

public class WordListActivity extends BaseActivity {

    List<Word> wordList;
    WordDBManager wordDBManager;

    UniversalAdapter<Word> adapter;

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        initToolbar();

        SoundManager.prepare();

        lv = (ListView)findViewById(R.id.lv);
        wordDBManager = App.getWordDBManager();
        wordList = wordDBManager.getList();
        lv.setAdapter(adapter=new UniversalAdapter<Word>(this,wordList,R.layout.item_word_list) {
            @Override
            public void convert(UniversalViewHolder viewHolder, Word item, int position) {
                TextView tvWord = viewHolder.getView(R.id.tvWord);
                TextView tvPron = viewHolder.getView(R.id.tvPronounce);
                TextView tvDesc = viewHolder.getView(R.id.tvDesc);
                tvWord.setText(item.getWord());
                tvPron.setText("["+item.getPronounce()+"]");
                tvDesc.setText(item.getDefinition());
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                new AsyncTask<Void,Void,Void>(){
                    @Override
                    protected Void doInBackground(Void... params) {
                        SoundManager.playSound(wordList.get(pos));
                        return null;
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                PopupMenu popupMenu = new PopupMenu(WordListActivity.this,view);
                Menu menu = popupMenu.getMenu();
                popupMenu.getMenuInflater().inflate(R.menu.menu_word_list,menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.delete) {
                            SoundManager.deleteSound(wordList.get(pos));
                            wordDBManager.deleteWord(wordList.get(pos));
                            wordList.remove(pos);
                            refresh();
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return false;
            }
        });
    }

    private void initToolbar(){
        Toolbar toolbar = f(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void refresh(){
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SoundManager.release();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
