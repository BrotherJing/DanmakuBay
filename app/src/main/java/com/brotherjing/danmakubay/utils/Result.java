package com.brotherjing.danmakubay.utils;

/**
 * Created by Brotherjing on 2015/8/15.
 */
public class Result {

    boolean success;
    String msg;

    public Result(boolean success,String msg){
        this.msg = msg;
        this.success = success;
    }

    public boolean isSuccess(){return success;}

    public String getMsg(){return msg;}

}
