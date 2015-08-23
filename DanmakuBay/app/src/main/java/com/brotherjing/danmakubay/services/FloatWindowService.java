package com.brotherjing.danmakubay.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.brotherjing.danmakubay.R;
import com.brotherjing.danmakubay.api.API_SPF;
import com.brotherjing.danmakubay.utils.DataUtil;
import com.brotherjing.danmakubay.utils.WordDBManager;
import com.brotherjing.simpledanmakuview.Danmaku;
import com.brotherjing.simpledanmakuview.DanmakuView;
import com.greendao.dao.Sentence;
import com.greendao.dao.Word;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class FloatWindowService extends Service {

    private WindowManager.LayoutParams layoutParams;
    private WindowManager windowManager;

    //danmaku layout and view
    private LinearLayout danmakuLayout;
    private DanmakuView danmakuView;
    private View background;
    private ImageView ivRemove;

    //if the floating window is added
    private boolean isAdded;

    private ActivityManager activityManager;
    private List<String> packageNames;

    private WordDBManager wordDBManager;

    private List<Word> wordlist;
    private List<Sentence> sentenceList;
    private int word_cnt,sentence_cnt,total_cnt, current;

    private int speed;
    private boolean show_bg,all_app;

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(isAdded)return super.onStartCommand(intent, flags, startId);

        initView();
        initData();
        initLayout();
        initListener();

        //only show floating window in HOME activity
        packageNames = getNames();
        handler.sendEmptyMessageDelayed(HANDLE_CHECK_ACTIVITY, 1000);
        return super.onStartCommand(intent, flags, startId);
    }

    private void initData(){
        wordDBManager = new WordDBManager(this);
        wordlist = wordDBManager.getList();
        sentenceList = new ArrayList<>();
        for(Word word:wordlist){
            sentenceList.addAll(word.getSentenceList());
        }
        word_cnt = wordlist.size();
        sentence_cnt = sentenceList.size();
        total_cnt = word_cnt+sentence_cnt;
        current = 0;

        speed = DataUtil.getInt(API_SPF.SPF_SETTING, API_SPF.ITEM_DANMAKU_SPEED, 50);
        show_bg = DataUtil.getBoolean(API_SPF.SPF_SETTING, API_SPF.ITEM_SHOW_BG, true);
        all_app = DataUtil.getBoolean(API_SPF.SPF_SETTING, API_SPF.ITEM_DISPLAY_AREA, false);

        danmakuView.setMSPF(20 + (speed - 50) / 10);
        if(!show_bg)background.setVisibility(View.GONE);
    }

    /**
     * get a list of home activity
     */
    private List<String> getNames(){
        List<String> names = new ArrayList<String>();
        PackageManager manager = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> infos = manager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for(ResolveInfo i : infos){
            names.add(i.activityInfo.packageName);
        }
        return names;
    }

    /**
     * check if the current activity is home activity
     */
    private boolean isHome(){
        if(activityManager==null){
            activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        }
        List<ActivityManager.RunningTaskInfo> infos = activityManager.getRunningTasks(1);
        return packageNames.contains(infos.get(0).topActivity.getPackageName());
    }

    /**
     * check if the current activity is home. if not, hide the floating window.
     */
    private final static int HANDLE_CHECK_ACTIVITY = 1;
    private final static int HANDLE_FINISH = 2;
    private final static class MyHandler extends Handler{

        private WeakReference<FloatWindowService> reference;

        public MyHandler(FloatWindowService service){
            reference = new WeakReference<FloatWindowService>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            if(reference.get()==null)return;
            FloatWindowService service = reference.get();
            switch (msg.what) {
                case HANDLE_CHECK_ACTIVITY:
                if (service.all_app||service.isHome()) {
                    if (!service.isAdded) {
                        service.windowManager.addView(service.danmakuLayout, service.layoutParams);
                        service.isAdded = true;
                    }
                    if(service.current>=service.word_cnt){
                        service.danmakuView.addDanmaku(new Danmaku(service.sentenceList.get(service.current-service.word_cnt).getSentence(), Color.WHITE,false, Danmaku.DanmakuType.SHIFTING, Danmaku.DanmakuSpeed.SLOW));
                    }
                    else {
                        service.danmakuView.addDanmaku(new Danmaku(service.wordlist.get(service.current).getWord()));
                    }
                    service.current = (service.current+1)%service.total_cnt;
                } else {
                    if (service.isAdded) {
                        service.windowManager.removeView(service.danmakuLayout);
                        service.isAdded = false;
                    }
                }
                sendEmptyMessageDelayed(HANDLE_CHECK_ACTIVITY, 500);
                break;
                case HANDLE_FINISH:
                    break;
            }
        }
    }
    private final MyHandler handler = new MyHandler(this);

    private void initView(){
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        danmakuLayout = (LinearLayout)inflater.inflate(R.layout.float_window_danmaku, null);
        danmakuView = (DanmakuView)danmakuLayout.findViewById(R.id.danmakuFloating);
        danmakuView.setMode(DanmakuView.MODE_NO_OVERDRAW);
        ivRemove = (ImageView)danmakuLayout.findViewById(R.id.ivRemoveFloatWindow);
        background = danmakuLayout.findViewById(R.id.background);
    }

    private void initLayout(){

        layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        layoutParams.x=0;
        layoutParams.y=0;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        windowManager.addView(danmakuLayout, layoutParams);
        danmakuLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        isAdded = true;
    }

    private void initListener(){

        danmakuLayout.setOnTouchListener(new View.OnTouchListener() {
            int startX,startY;
            int paramx,paramy;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX = (int)event.getRawX();
                        startY = (int)event.getRawY();
                        paramx = layoutParams.x;
                        paramy = layoutParams.y;
                        //Log.i("yj", "down,x=" + startX + " y=" + startY);
                        //background.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int)event.getRawX()-startX;
                        int dy = (int)event.getRawY()-startY;
                        layoutParams.x = paramx+dx;
                        layoutParams.y = paramy+dy;
                        windowManager.updateViewLayout(danmakuLayout,layoutParams);
                        //Log.i("yj", "move,dx=" + dx + " dy=" + dy);
                        break;
                    case MotionEvent.ACTION_UP:
                        //background.setVisibility(View.GONE);
                        int mx = (int)event.getRawX()-startX;
                        int my = (int)event.getRawY()-startY;
                        /*if((mx<10&&mx>-10)&&(my<10&&my>-10)){
                            if(ivRemove.getVisibility()== View.INVISIBLE)
                                ivRemove.setVisibility(View.VISIBLE);
                            else
                                ivRemove.setVisibility(View.INVISIBLE);
                        }*/
                        break;
                    default:break;
                }
                return true;
            }
        });

        ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ivRemove.getVisibility()== View.VISIBLE) {
                    handler.removeMessages(HANDLE_CHECK_ACTIVITY);
                    windowManager.removeView(danmakuLayout);
                    FloatWindowService.this.stopSelf();
                }
            }
        });

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            windowManager.removeView(danmakuLayout);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        handler.removeMessages(HANDLE_CHECK_ACTIVITY);
        handler.sendEmptyMessage(HANDLE_FINISH);
    }
}

