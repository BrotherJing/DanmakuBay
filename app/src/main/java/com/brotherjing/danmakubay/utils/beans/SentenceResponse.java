package com.brotherjing.danmakubay.utils.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Brotherjing on 2015/8/23.
 */
public class SentenceResponse extends ShanbayResponse {

    @SerializedName("data")
    List<SentenceBean> list;

    public List<SentenceBean> getList() {
        return list;
    }

    public void setList(List<SentenceBean> list) {
        this.list = list;
    }
}
