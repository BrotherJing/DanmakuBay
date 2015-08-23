package com.brotherjing.danmakubay.services;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brotherjing.danmakubay.R;
import com.brotherjing.danmakubay.utils.Result;
import com.brotherjing.danmakubay.utils.WordDBManager;
import com.brotherjing.danmakubay.utils.beans.SentenceBean;
import com.brotherjing.danmakubay.utils.beans.WordBean;
import com.brotherjing.danmakubay.utils.providers.ShanbayProvider;
import com.brotherjing.simpledanmakuview.DanmakuView;
import com.greendao.dao.Word;

import java.nio.channels.spi.SelectorProvider;
import java.util.List;

public class FloatToolService extends Service {

    View floatLayout;
    LinearLayout mainLayout;
    ImageView ivExpand;
    TextView tvDesc,tvAdd;
    EditText et;

    WindowManager.LayoutParams layoutParams;
    WindowManager windowManager;

    ShanbayProvider provider;
    WordDBManager wordDBManager;

    WordBean searchResult;
    List<SentenceBean> sentenceBeanList;

    boolean isAdded;

    public FloatToolService() {
        isAdded = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        provider = new ShanbayProvider();
        wordDBManager = new WordDBManager(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(isAdded)
            return super.onStartCommand(intent, flags, startId);
        initView();
        initLayout();
        initListener();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void initView(){
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        floatLayout = inflater.inflate(R.layout.float_tool_layout,null);
        ivExpand = (ImageView)floatLayout.findViewById(R.id.iv_expand);
        mainLayout = (LinearLayout)floatLayout.findViewById(R.id.ll_main);
        tvDesc = (TextView)floatLayout.findViewById(R.id.tv_word_desc);
        et = (EditText)floatLayout.findViewById(R.id.et_find_word);
        tvAdd = (TextView)floatLayout.findViewById(R.id.btn_add_word_float);
    }

    private void initLayout(){

        layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        layoutParams.format = PixelFormat.RGBA_8888;

        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //layoutParams.gravity = Gravity.CENTER_VERTICAL;
        layoutParams.x=0;
        layoutParams.y=0;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        windowManager.addView(floatLayout, layoutParams);
        floatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.AT_MOST), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.AT_MOST));
        isAdded = true;
    }

    private void initListener(){

        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_BACK) {
                    if(mainLayout.getVisibility()==View.VISIBLE){
                        mainLayout.setVisibility(View.GONE);
                        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                        windowManager.updateViewLayout(floatLayout,layoutParams);
                    }
                }
                return false;
            }
        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s)){
                    tvDesc.setVisibility(View.GONE);
                    tvAdd.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_NULL) {
                    if (TextUtils.isEmpty(et.getText())) return true;
                    tvDesc.setVisibility(View.VISIBLE);
                    tvAdd.setVisibility(View.VISIBLE);
                    new SearchWordTask().execute(et.getText().toString());
                }
                return true;
            }
        });

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchResult==null)return;
                new AddWordTask().execute();
            }
        });

        /*et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setFocusable(true);
                et.setFocusableInTouchMode(true);
                et.requestFocusFromTouch();
                et.onWindowFocusChanged(true);
                et.requestFocus();
                InputMethodManager imm = (InputMethodManager)et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
            }
        });*/

        ivExpand.setOnTouchListener(new View.OnTouchListener() {
            int startX, startY;
            int paramx, paramy;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        paramx = layoutParams.x;
                        paramy = layoutParams.y;
                        //Log.i("yj", "down,x=" + startX + " y=" + startY);
                        //background.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - startX;
                        int dy = (int) event.getRawY() - startY;
                        layoutParams.x = paramx + dx;
                        layoutParams.y = paramy + dy;
                        windowManager.updateViewLayout(floatLayout, layoutParams);
                        //Log.i("yj", "move,dx=" + dx + " dy=" + dy);
                        break;
                    case MotionEvent.ACTION_UP:
                        //background.setVisibility(View.GONE);
                        int mx = (int) event.getRawX() - startX;
                        int my = (int) event.getRawY() - startY;
                        if ((mx < 10 && mx > -10) && (my < 10 && my > -10)) {
                            if (mainLayout.getVisibility() == View.GONE) {
                                mainLayout.setVisibility(View.VISIBLE);
                                layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
                            }
                            else {
                                mainLayout.setVisibility(View.GONE);
                                layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                            }
                            windowManager.updateViewLayout(floatLayout,layoutParams);
                        }
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            windowManager.removeView(floatLayout);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private class AddWordTask extends AsyncTask<Void,Void,Result>{
        @Override
        protected Result doInBackground(Void... params) {
            if(provider.addNewWord(searchResult.getId())){
                Word word = provider.from(searchResult);
                if(wordDBManager.addWord(word)){
                    wordDBManager.addSentences(sentenceBeanList,word);
                    return new Result(true,"");
                }
            }
            return new Result(false,"");
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            if(result.isSuccess()){
                Toast.makeText(FloatToolService.this,R.string.add_success,Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(FloatToolService.this,R.string.add_fail,Toast.LENGTH_SHORT).show();
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
                tvDesc.setText(wordBean.getPronunciation() + "\n" + wordBean.getDefinition());
                tvAdd.setVisibility(View.VISIBLE);
                searchResult = wordBean;
            }
            else Toast.makeText(FloatToolService.this,R.string.get_fail,Toast.LENGTH_SHORT).show();
        }
    }
}
