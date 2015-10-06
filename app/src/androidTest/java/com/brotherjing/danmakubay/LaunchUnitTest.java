package com.brotherjing.danmakubay;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.TextView;

import com.brotherjing.danmakubay.activities.ChooseLoginType;
import com.brotherjing.danmakubay.activities.LaunchActivity;

/**
 * Created by Brotherjing on 2015/10/6.
 */
public class LaunchUnitTest extends ActivityUnitTestCase<ChooseLoginType> {

    Intent intent;

    public LaunchUnitTest() {
        super(ChooseLoginType.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        intent = new Intent(getInstrumentation().getTargetContext(),ChooseLoginType.class);
        startActivity(intent,null,null);
        TextView textView = (TextView)getActivity().findViewById(R.id.btn_login_noaccount);
        textView.performClick();
        Intent intent1 = getStartedActivityIntent();
        assertNotNull("intent not null",intent);
    }
}
