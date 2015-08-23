package com.brotherjing.danmakubay.services;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brotherjing.danmakubay.R;
import com.brotherjing.simpledanmakuview.DanmakuView;

public class FloatToolService extends Service {

    View floatLayout;
    LinearLayout mainLayout;
    ImageView ivExpand;
    TextView tvDesc,tvAdd;
    EditText et;

    WindowManager.LayoutParams layoutParams;
    WindowManager windowManager;

    boolean isAdded;

    public FloatToolService() {
        isAdded = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
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

        View.OnKeyListener li = new View.OnKeyListener() {
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
        };
        /*
        floatLayout.setOnKeyListener(li);
        mainLayout.setOnKeyListener(li);
        ivExpand.setOnKeyListener(li);
        tvAdd.setOnKeyListener(li);
        tvDesc.setOnKeyListener(li);*/
        et.setOnKeyListener(li);

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
}
