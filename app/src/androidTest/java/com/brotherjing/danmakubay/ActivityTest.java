package com.brotherjing.danmakubay;

import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityTestCase;
import android.widget.TextView;

import com.brotherjing.danmakubay.activities.LaunchActivity;
import com.brotherjing.danmakubay.activities.MainActivity;

/**
 * Created by Brotherjing on 2015/10/6.
 */
public class ActivityTest extends ActivityInstrumentationTestCase2<LaunchActivity> {

    LaunchActivity activity;
    TextView textView;

    public ActivityTest(Class<LaunchActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        textView = (TextView)activity.findViewById(R.id.tv_open_window);
        assertEquals(1,2);
        testText();
    }

    public void testText()throws Exception{
        String exp = activity.getString(R.string.open_window);
        String text = textView.getText().toString();
        assertEquals(exp, text);
    }
}
