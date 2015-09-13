package com.brotherjing.danmakubay.utils.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

/**
 * Created by Brotherjing on 2015/9/6.
 */
public class DragClickLayout extends LinearLayout {

    private int touchSlop;
    private float downY,downX;
    private float movedY,movedX;

    public DragClickLayout(Context context) {
        this(context,null);
    }

    public DragClickLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DragClickLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //Log.i("yj", "on intercept");
        int action = ev.getAction();
        if(action==MotionEvent.ACTION_DOWN){
            //Log.i("yj","refresh, on intercept down");
            downY = ev.getRawY();
            downX = ev.getRawX();
            if(onDragListener!=null)onDragListener.onDragStarted();
            return false;
        }else if(action==MotionEvent.ACTION_MOVE){
            //Log.i("yj","refresh, on intercept move");
            movedX = ev.getRawX()-downX;
            movedY = ev.getRawY()-downY;
            if(Math.abs(movedY)<touchSlop&&Math.abs(movedX)<touchSlop)return false;
            if(onDragListener!=null)onDragListener.onDrag(movedX,movedY);
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.i("yj","on touch event");
        int action = event.getAction();
        if(action==MotionEvent.ACTION_MOVE){
            //Log.i("yj","refresh, on touch move");
            movedY = event.getRawY()-downY;
            movedX = event.getRawX()-downX;
            if(onDragListener!=null)onDragListener.onDrag(movedX,movedY);
            return true;
        }else if(action==MotionEvent.ACTION_UP){
            //Log.i("yj","on touch up");
        }
        return true;
    }

    public interface OnDragListener{
        void onDragStarted();
        void onDrag(float movedX,float movedY);
    }

    private OnDragListener onDragListener;
    public void setOnDragListener(OnDragListener listener){this.onDragListener=listener;}
}
