package com.brotherjing.danmakubay.activities;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.brotherjing.danmakubay.R;
import com.brotherjing.simpledanmakuview.Danmaku;
import com.brotherjing.simpledanmakuview.DanmakuView;

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

    private String[] wordlist = {"tongue","I walked over to the mirror and stuck my tongue out.",
    "margin","They could end up with a 50-point winning margin.",
    "legend","The castle is steeped in history and legend.",
    "distribute","Students shouted slogans and distributed leaflets."};
    private final static int WORD_COUNT = 8;
    private int curr_word=0;

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);

        initView();
        initLayout();
        initListener();

        //only show floating window in HOME activity
        packageNames = getNames();
        handler.sendEmptyMessageDelayed(HANDLE_CHECK_ACTIVITY, 1000);
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
    private final static  int HANDLE_CHECK_ACTIVITY = 1;
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
                if (service.isHome()) {
                    if (!service.isAdded) {
                        service.windowManager.addView(service.danmakuLayout, service.layoutParams);
                        service.isAdded = true;
                    }
                    service.danmakuView.addDanmaku(new Danmaku(service.wordlist[service.curr_word], false));
                    service.curr_word=(++service.curr_word)%WORD_COUNT;
                } else {
                    if (service.isAdded) {
                        service.windowManager.removeView(service.danmakuLayout);
                        service.isAdded = false;
                    }
                }
                break;
            }
            sendEmptyMessageDelayed(HANDLE_CHECK_ACTIVITY, 500);
        }
    }
    private final MyHandler handler = new MyHandler(this);

    private void initView(){
        LayoutInflater inflater = LayoutInflater.from(getApplication());

        //the floating window
        /*danmakuLayout = new LinearLayout(this,null,0);
        danmakuLayout.setOrientation(LinearLayout.VERTICAL);
        danmakuView = new DanmakuView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        danmakuLayout.addView(danmakuView,lp);

        ivRemove = new ImageView(this);
        ivRemove.setImageResource(android.R.drawable.btn_minus);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        danmakuLayout.addView(ivRemove,lp1);*/

        //background = new View(this);
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
                        background.setVisibility(View.VISIBLE);
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
                        background.setVisibility(View.GONE);
                        int mx = (int)event.getRawX()-startX;
                        int my = (int)event.getRawY()-startY;
                        if((mx<10&&mx>-10)&&(my<10&&my>-10)){
                            if(ivRemove.getVisibility()== View.INVISIBLE)
                                ivRemove.setVisibility(View.VISIBLE);
                            else
                                ivRemove.setVisibility(View.INVISIBLE);
                        }
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
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

