package com.brotherjing.danmakubay.utils.providers;

import com.brotherjing.danmakubay.api.API_SPF;
import com.brotherjing.danmakubay.api.API_URL;
import com.brotherjing.danmakubay.utils.CrossyHttpClient;
import com.brotherjing.danmakubay.utils.DataUtil;
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
import java.util.List;

/**
 * Created by Brotherjing on 2015/8/15.
 */
public class ShanbayProvider {

    public WordBean getWord(String word){
        WordResponse response = CrossyHttpClient.getBean(API_URL.URL_SEARCH_WORD+word+"&"+
                API_URL.SUFFIX_ACCESS_TOKEN+DataUtil.getString(API_SPF.SPF_TOKEN,API_SPF.ITEM_ACCESS_TOKEN),
                DataUtil.getString(API_SPF.SPF_TOKEN,API_SPF.ITEM_COOKIES),WordResponse.class);
        if(response.getStatus_code()!=0){
            return null;
        }
        return response.getWordBean();
    }

    public UserInfo getUserInfo(){
        return CrossyHttpClient.getBean(API_URL.URL_USER_INFO + "?" +
                        API_URL.SUFFIX_ACCESS_TOKEN + DataUtil.getString(API_SPF.SPF_TOKEN, API_SPF.ITEM_ACCESS_TOKEN),
                DataUtil.getString(API_SPF.SPF_TOKEN, API_SPF.ITEM_COOKIES), UserInfo.class);
    }

    public List<SentenceBean> getSentences(long word_id){
        SentenceResponse response = CrossyHttpClient.getBean(API_URL.URL_GET_SENTENCE + word_id + "&" + API_URL.SUFFIX_ACCESS_TOKEN +
                        DataUtil.getString(API_SPF.SPF_TOKEN, API_SPF.ITEM_ACCESS_TOKEN), DataUtil.getString(API_SPF.SPF_TOKEN, API_SPF.ITEM_COOKIES),
                SentenceResponse.class);
        if(response.getStatus_code()!=0)return null;
        return response.getList();
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

    public Word from(WordBean wordBean){
        Word word = new Word();
        word.setWord(wordBean.getContent());
        word.setShanbay_id(wordBean.getId());
        word.setDefinition(wordBean.getDefinition());
        word.setPronounce(wordBean.getPronunciation());
        return word;
    }

}
