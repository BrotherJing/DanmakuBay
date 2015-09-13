package com.brotherjing.danmakubay;

/**
 * Created by Brotherjing on 2015/9/13.
 */
public final class GlobalEnv {

    static boolean login;

    public static void init(){
        login = false;
    }

    public static boolean isLogin() {
        return login;
    }

    public static void setLogin(boolean login) {
        GlobalEnv.login = login;
    }
}
