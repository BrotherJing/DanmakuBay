package com.brotherjing.danmakubay.api;

/**
 * Created by Brotherjing on 2015/8/15.
 */
public interface API_URL {

    String APP_KEY = "cf50c85931279816bae4";
    String APP_SECRET = "1efd66a078ca59eb8f166360f1521fcdb934b6de";

    String URL_AUTH = "https://api.shanbay.com/oauth2/authorize/?client_id=%s&response_type=token&redirect_uri=%s";
    String URL_CALLBACK = "https://api.shanbay.com/oauth2/auth/success/";

    String URL_USER_INFO = "https://api.shanbay.com/account/";
    String URL_SEARCH_WORD = "https://api.shanbay.com/bdc/search/?word=";
    String URL_ADD_WORD = "https://api.shanbay.com/bdc/learning/";
    String URL_GET_SENTENCE = "https://api.shanbay.com/bdc/example/?type=sys&vocabulary_id=";

    String SUFFIX_ACCESS_TOKEN = "access_token=";
    String SUFFIX_DENIED = "access_denied";
}
