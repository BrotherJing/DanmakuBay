package com.brotherjing.danmakubay.utils.providers;

import com.brotherjing.danmakubay.api.API_SPF;
import com.brotherjing.danmakubay.api.API_URL;
import com.brotherjing.danmakubay.utils.CrossyHttpClient;
import com.brotherjing.danmakubay.utils.DataUtil;
import com.brotherjing.danmakubay.utils.beans.ShanbayResponse;
import com.brotherjing.danmakubay.utils.beans.UserInfo;
import com.brotherjing.danmakubay.utils.beans.Word;
import com.brotherjing.danmakubay.utils.beans.WordResponse;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brotherjing on 2015/8/15.
 */
public class ShanbayProvider {

    public Word getWord(String word){
        WordResponse response = CrossyHttpClient.getBean(API_URL.URL_SEARCH_WORD+word+"&"+
                API_URL.SUFFIX_ACCESS_TOKEN+DataUtil.getString(API_SPF.SPF_TOKEN,API_SPF.ITEM_ACCESS_TOKEN),
                DataUtil.getString(API_SPF.SPF_TOKEN,API_SPF.ITEM_COOKIES),WordResponse.class);
        if(response.getStatus_code()!=0){
            return null;
        }
        return response.getWord();
    }

    public UserInfo getUserInfo(){
        return CrossyHttpClient.getBean(API_URL.URL_USER_INFO + "?" +
                        API_URL.SUFFIX_ACCESS_TOKEN + DataUtil.getString(API_SPF.SPF_TOKEN, API_SPF.ITEM_ACCESS_TOKEN),
                DataUtil.getString(API_SPF.SPF_TOKEN, API_SPF.ITEM_COOKIES), UserInfo.class);
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

}
