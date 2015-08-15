package com.brotherjing.danmakubay.utils.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Brotherjing on 2015/8/15.
 */
public class Word {
    @SerializedName("definition")
    String definition;
    @SerializedName("learning_id")
    long learning_id;
    @SerializedName("content")
    String content;
    @SerializedName("pronunciation")
    String pronunciation;
    @SerializedName("id")
    long id;

    public Word() {
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public long getLearning_id() {
        return learning_id;
    }

    public void setLearning_id(long learning_id) {
        this.learning_id = learning_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }
}
