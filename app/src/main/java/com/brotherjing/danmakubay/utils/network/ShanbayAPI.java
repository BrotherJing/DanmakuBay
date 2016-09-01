package com.brotherjing.danmakubay.utils.network;

import com.brotherjing.danmakubay.utils.beans.SentenceResponse;
import com.brotherjing.danmakubay.utils.beans.ShanbayResponse;
import com.brotherjing.danmakubay.utils.beans.UserInfo;
import com.brotherjing.danmakubay.utils.beans.WordResponse;
import com.squareup.okhttp.ResponseBody;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit.http.Url;
import rx.Observable;

/**
 * Created by jingyanga on 2016/9/1.
 */
public interface ShanbayAPI {

    String BASE_URL = "https://api.shanbay.com";

    String APP_KEY = "cf50c85931279816bae4";
    String APP_SECRET = "1efd66a078ca59eb8f166360f1521fcdb934b6de";

    String URL_AUTH = "https://api.shanbay.com/oauth2/authorize/?client_id=%s&response_type=token&redirect_uri=%s";
    String URL_CALLBACK = "https://api.shanbay.com/oauth2/auth/success/";

    String URL_USER_INFO = "/account/";
    String URL_SEARCH_WORD = "/bdc/search/";
    String URL_ADD_WORD = "/bdc/learning/";
    String URL_GET_SENTENCE = "/bdc/example/?type=sys";

    String SUFFIX_ACCESS_TOKEN = "access_token=";
    String SUFFIX_DENIED = "access_denied";

    @GET(URL_SEARCH_WORD)
    Observable<WordResponse> getWord(@Query("word") String word);

    @GET(URL_USER_INFO)
    Observable<UserInfo> getUserInfo();

    @GET(URL_GET_SENTENCE)
    Observable<SentenceResponse> getSentences(@Query("vocabulary_id") long wordId);

    @FormUrlEncoded
    @POST(URL_ADD_WORD)
    Observable<ShanbayResponse> addNewWord(@Field("id")long id);

    @GET
    Observable<ResponseBody> downloadSound(@Url String audio);

}
