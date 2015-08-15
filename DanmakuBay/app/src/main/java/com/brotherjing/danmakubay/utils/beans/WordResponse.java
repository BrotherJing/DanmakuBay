package com.brotherjing.danmakubay.utils.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Brotherjing on 2015/8/15.
 */
public class WordResponse extends ShanbayResponse {

    @SerializedName("data")
    Word word;

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }
}
