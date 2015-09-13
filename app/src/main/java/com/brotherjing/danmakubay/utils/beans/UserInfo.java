package com.brotherjing.danmakubay.utils.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Brotherjing on 2015/8/15.
 */
public class UserInfo {
    /**
     * {
     "username": "username",
     "nickname": "nickname",
     "id": 269875,
     "avatar": "http://qstatic.shanbay.com/thumbnail/media_store/8cb0e9275e8b765ad96817d38e6a0633.jpg"
     }
     */

    @SerializedName("username")
    String username;
    @SerializedName("nickname")
    String nickname;
    @SerializedName("id")
    long id;
    @SerializedName("avatar")
    String avatar;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
