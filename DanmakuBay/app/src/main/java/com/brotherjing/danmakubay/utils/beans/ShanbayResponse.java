package com.brotherjing.danmakubay.utils.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Brotherjing on 2015/8/15.
 */
public class ShanbayResponse {

    @SerializedName("status_code")
    int status_code;
    @SerializedName("msg")
    String msg;

    public ShanbayResponse() {
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
