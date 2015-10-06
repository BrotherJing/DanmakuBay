package com.brotherjing.danmakubay;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.brotherjing.danmakubay.activities.MainActivity;

/**
 * Created by Brotherjing on 2015/10/6.
 */
public class ClickTest extends ActivityInstrumentationTestCase2<MainActivity> {

    MainActivity activity;
    TextView tvOpen;

    public ClickTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        activity = getActivity();
        tvOpen = (TextView)activity.findViewById(R.id.tv_open_window);

    }

    @MediumTest
    public void testCheck()throws Exception{
        final View decorView = activity.getWindow().getDecorView();

        ViewAsserts.assertOnScreen(decorView, tvOpen);

        final ViewGroup.LayoutParams layoutParams =
                tvOpen.getLayoutParams();
        assertNotNull(layoutParams);
        assertEquals(layoutParams.width, WindowManager.LayoutParams.MATCH_PARENT);
        assertEquals(layoutParams.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @MediumTest
    public void testClick()throws Exception{
        String expectedInfoText = activity.getString(R.string.close_window);
        TouchUtils.clickView(this, tvOpen);
        assertEquals(expectedInfoText, tvOpen.getText().toString());
    }
}
