package com.brotherjing.danmakubay.utils.providers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.brotherjing.danmakubay.api.API_SPF;
import com.brotherjing.danmakubay.api.API_URL;
import com.brotherjing.danmakubay.utils.network.CrossyHttpClient;
import com.brotherjing.danmakubay.utils.DataUtil;
import com.brotherjing.danmakubay.utils.network.GsonRequest;
import com.brotherjing.danmakubay.utils.network.VolleyClient;
import com.brotherjing.danmakubay.utils.beans.SentenceBean;
import com.brotherjing.danmakubay.utils.beans.SentenceResponse;
import com.brotherjing.danmakubay.utils.beans.ShanbayResponse;
import com.brotherjing.danmakubay.utils.beans.UserInfo;
import com.brotherjing.danmakubay.utils.beans.WordBean;
import com.brotherjing.danmakubay.utils.beans.WordResponse;
import com.google.gson.Gson;
import com.greendao.dao.Word;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brotherjing on 2015/8/15.
 */
public class ShanbayProvider {

    public WordBean getWord(String word){
        //+"&"+API_URL.SUFFIX_ACCESS_TOKEN+DataUtil.getString(API_SPF.SPF_TOKEN,API_SPF.ITEM_ACCESS_TOKEN)
        WordResponse response = CrossyHttpClient.getBean(API_URL.URL_SEARCH_WORD+word,
                DataUtil.getString(API_SPF.SPF_TOKEN,API_SPF.ITEM_COOKIES),WordResponse.class);
        if(response.getStatus_code()!=0){
            return null;
        }
        return response.getWordBean();
    }

    public void getWord(Context context,String word,Response.Listener<WordResponse> listener,Response.ErrorListener errorListener){
        GsonRequest<WordResponse> request = new GsonRequest<WordResponse>(Request.Method.GET,API_URL.URL_SEARCH_WORD+word,WordResponse.class,listener,errorListener);
        request.putHeader("Cookie", DataUtil.getString(API_SPF.SPF_TOKEN, API_SPF.ITEM_COOKIES));
        VolleyClient.getInstance(context).addRequest(request);
    }

    public UserInfo getUserInfo(){
        return CrossyHttpClient.getBean(API_URL.URL_USER_INFO + "?" +
                        API_URL.SUFFIX_ACCESS_TOKEN + DataUtil.getString(API_SPF.SPF_TOKEN, API_SPF.ITEM_ACCESS_TOKEN),
                DataUtil.getString(API_SPF.SPF_TOKEN, API_SPF.ITEM_COOKIES), UserInfo.class);
    }

    public void getUserInfo(Context context,Response.Listener<UserInfo> listener,Response.ErrorListener errorListener){
        GsonRequest<UserInfo> request = new GsonRequest<>(Request.Method.GET,API_URL.URL_USER_INFO + "?" +
                API_URL.SUFFIX_ACCESS_TOKEN + DataUtil.getString(API_SPF.SPF_TOKEN, API_SPF.ITEM_ACCESS_TOKEN),
                UserInfo.class,listener,errorListener);
        request.putHeader("Cookie",DataUtil.getString(API_SPF.SPF_TOKEN, API_SPF.ITEM_COOKIES));
        VolleyClient.getInstance(context).addRequest(request);
    }

    public List<SentenceBean> getSentences(long word_id){
        SentenceResponse response = CrossyHttpClient.getBean(API_URL.URL_GET_SENTENCE + word_id + "&" + API_URL.SUFFIX_ACCESS_TOKEN +
                        DataUtil.getString(API_SPF.SPF_TOKEN, API_SPF.ITEM_ACCESS_TOKEN), DataUtil.getString(API_SPF.SPF_TOKEN, API_SPF.ITEM_COOKIES),
                SentenceResponse.class);
        if(response.getStatus_code()!=0)return null;
        return response.getList();
    }

    public void getSentences(Context context,long word_id,Response.Listener<SentenceResponse> listener,Response.ErrorListener errorListener){
        GsonRequest<SentenceResponse> request = new GsonRequest<SentenceResponse>(Request.Method.GET,API_URL.URL_GET_SENTENCE + word_id + "&" + API_URL.SUFFIX_ACCESS_TOKEN +
                DataUtil.getString(API_SPF.SPF_TOKEN, API_SPF.ITEM_ACCESS_TOKEN),SentenceResponse.class,listener,errorListener);
        request.putHeader("Cookie",DataUtil.getString(API_SPF.SPF_TOKEN, API_SPF.ITEM_COOKIES));
        VolleyClient.getInstance(context).addRequest(request);
    }

    public boolean addNewWord(long id){
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("id", id + ""));
        String result = CrossyHttpClient.post(API_URL.URL_ADD_WORD+"?"+API_URL.SUFFIX_ACCESS_TOKEN+
                DataUtil.getString(API_SPF.SPF_TOKEN,API_SPF.ITEM_ACCESS_TOKEN)
                ,list,DataUtil.getString(API_SPF.SPF_TOKEN,API_SPF.ITEM_COOKIES));
        if(result.equals(""))return false;
        ShanbayResponse response = new Gson().fromJson(result,ShanbayResponse.class);
        if(response.getStatus_code()!=0)return false;
        return true;
    }

    public void addNewWord(Context context,long id,Response.Listener<ShanbayResponse> listener,Response.ErrorListener errorListener){
        GsonRequest<ShanbayResponse> request = new GsonRequest<>(Request.Method.POST,API_URL.URL_ADD_WORD+"?"+API_URL.SUFFIX_ACCESS_TOKEN+
                DataUtil.getString(API_SPF.SPF_TOKEN,API_SPF.ITEM_ACCESS_TOKEN),ShanbayResponse.class,listener,errorListener);
        request.putHeader("Cookie",DataUtil.getString(API_SPF.SPF_TOKEN,API_SPF.ITEM_COOKIES));
        request.putParam("id", id + "");
        VolleyClient.getInstance(context).addRequest(request);
    }

    public Word from(WordBean wordBean){
        Word word = new Word();
        word.setWord(wordBean.getContent());
        word.setShanbay_id(wordBean.getId());
        word.setDefinition(wordBean.getDefinition());
        word.setPronounce(wordBean.getPronunciation());
        word.setAudio(wordBean.getAudioUrl());
        return word;
    }

}
