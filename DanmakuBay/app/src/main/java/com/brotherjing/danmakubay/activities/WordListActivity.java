package com.brotherjing.danmakubay.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.brotherjing.danmakubay.App;
import com.brotherjing.danmakubay.R;
import com.brotherjing.danmakubay.utils.ViewUtil;
import com.brotherjing.danmakubay.utils.WordDBManager;
import com.brotherjing.danmakubay.utils.views.UniversalAdapter;
import com.brotherjing.danmakubay.utils.views.UniversalViewHolder;
import com.greendao.dao.Word;

import java.util.List;

public class WordListActivity extends Activity {

    List<Word> wordList;
    WordDBManager wordDBManager;

    UniversalAdapter<Word> adapter;

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        initActionBar();

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

    private void initActionBar(){
        ActionBar actionBar = getActionBar();
        ViewUtil.customizeActionBar(actionBar, R.layout.actionbar_message_index);
        View view = actionBar.getCustomView();
        ((TextView)view.findViewById(R.id.textViewTitle)).setText(getResources().getText(R.string.word_list));
    }

    private void refresh(){
        adapter.notifyDataSetChanged();
    }

}
