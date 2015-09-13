package com.brotherjing.danmakubay.utils.providers;

import com.brotherjing.danmakubay.GlobalEnv;
import com.brotherjing.danmakubay.api.API_SPF;
import com.brotherjing.danmakubay.utils.DataUtil;

/**
 * Created by Brotherjing on 2015/9/13.
 */
public class AccountProvider {

    public static void logout(){
        GlobalEnv.setLogin(false);
        DataUtil.clearSpf(API_SPF.SPF_TOKEN);
    }

}
