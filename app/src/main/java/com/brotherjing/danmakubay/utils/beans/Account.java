package com.brotherjing.danmakubay.utils.beans;

/**
 * Created by jingyanga on 2016/9/1.
 */
public class Account {

    private String cookie;
    private String accessToken;

    public Account() {
    }

    public Account(String cookie, String accessToken) {
        this.cookie = cookie;
        this.accessToken = accessToken;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (!cookie.equals(account.cookie)) return false;
        return accessToken.equals(account.accessToken);

    }

    @Override
    public int hashCode() {
        int result = cookie.hashCode();
        result = 31 * result + accessToken.hashCode();
        return result;
    }
}
