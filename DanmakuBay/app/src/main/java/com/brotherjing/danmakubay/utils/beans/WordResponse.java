package com.brotherjing.danmakubay.utils.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Brotherjing on 2015/8/15.
 */
public class WordResponse extends ShanbayResponse {

    @SerializedName("data")
    WordBean wordBean;

    public WordBean getWordBean() {
        return wordBean;
    }

    public void setWordBean(WordBean wordBean) {
        this.wordBean = wordBean;
    }
}
