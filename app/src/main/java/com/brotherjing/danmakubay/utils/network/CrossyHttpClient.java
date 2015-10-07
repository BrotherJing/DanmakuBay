package com.brotherjing.danmakubay.utils.network;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 封装http操作, 全静态, 调用时只需要CrossyHttpClient.get这样就可以了
 * 目前只提供了get, post还有cookie相关方法
 */
public class CrossyHttpClient {
    /**
     * 有些网站要伪装成浏览器才能访问, 为了方便, 所有的访问都设置了
     */
    final private static String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)";
    final private static String CONNECTION = "Keep-Alive";
    final private static String TAG = "atomu";
    final private static int TIME_OUT = 10*1000;

    private static Gson gson;
    private static HttpClient httpClient;

    /**
     * 使用之前必须初始化
     * TODO: 请求头添加 device version 等
     */
    public static void init() {
        if (httpClient == null)
            httpClient = new DefaultHttpClient();

        gson = new Gson();
//        cookieManager = new CookieManager();
//        okHttpClient.setCookieHandler(cookieManager);
    }

    /**
     * 只是为了少一步gson转化而已, 用法和gson一样
     *
     * @param fromJson JSON字符串
     * @param type     待转化的bean的classType
     * @param <T>      class模板
     * @return JSON对应的bean
     */
    public static <T> T fromJson(String fromJson, Type type) {
        if (fromJson == null || fromJson.isEmpty())
            fromJson = "{}";

        return gson.fromJson(fromJson, type);
    }

    /**
     * get方法
     *
     * @param url     链接
     * @param session 因为交汇涉及多个网站的会话, 因此session可以直接传进来, 如果不需要, 置为null或者""
     * @return
     */
    public static HttpResponse getResponse(String url, String session) {
//        Request request = new Request.Builder()
//                .header("User-Agent", USER_AGENT)
//                .url(url)
//                .build();
//
        httpClient = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        get.setHeader("User-Agent", USER_AGENT);
        get.setHeader("Connection", CONNECTION);

        if (session != null && !session.isEmpty()) {
//            request.newBuilder().header("Cookie", session).build();
            get.setHeader("Cookie", session);
        }

        //默认不允许重定向
        HttpParams httpParams = new BasicHttpParams();
        httpParams.setParameter("http.protocol.handle-redirects",false);
        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        HttpConnectionParams.setSoTimeout(httpParams, TIME_OUT);
        get.setParams(httpParams);

        Header[] headers = get.getAllHeaders();
        /*for(Header h:headers){
            Log.i("yj",h.getName()+"="+h.getValue());
        }*/

        try {
//            Response response = okHttpClient.newCall(request).execute();
//            Log.d(TAG, "response is " + response.toString());
//            if (response.isSuccessful()) {
//                return response.body().string();
//            }
            HttpResponse httpResponse = httpClient.execute(get);
            return httpResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String get(String url, String session, String charSet){
        HttpResponse httpResponse = getResponse(url, session);
        if(httpResponse == null){
            return "";
        }
        try {
            return EntityUtils.toString(httpResponse.getEntity(), charSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * get方法, 默认utf-8编码
     *
     * @param url     链接
     * @param session 因为交汇涉及多个网站的会话, 因此session可以直接传进来, 如果不需要, 置为null或者""
     * @return 如果成功返回entity, 否则返回""
     */
    public static String get(String url, String session) {
        return get(url, session, "UTF-8");
    }

    /**
     * 获取流, 这里主要是图片
     *
     * @param url     链接
     * @param session 因为交汇涉及多个网站的会话, 因此session可以直接传进来, 如果不需要, 置为null或者""
     * @return 返回InputStream
     */
    public static InputStream getStream(String url, String session) {
//        Request request = new Request.Builder()
//                .header("User-Agent", USER_AGENT)
//                .url(url)
//                .build();
        httpClient = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        get.setHeader("User-Agent", USER_AGENT);
        if (session != null && !session.isEmpty()) {
//            request.newBuilder().header("Cookie", session).build();
            get.setHeader("Cookie", session);
        }


        try {
//            Response response = okHttpClient.newCall(request).execute();
//            Log.d(TAG, "response is " + response.toString());
//            if (response.isSuccessful()) {
//                return response.body().byteStream();
//            }
            HttpResponse httpResponse = httpClient.execute(get);
            if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == 200) {
                return new ByteArrayInputStream(EntityUtils.toByteArray(httpResponse.getEntity()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream("".getBytes());
    }

    /**
     * 只是为了少一步gson而已=_=, 反正接口都给了, 用哪个无所谓...
     *
     * @param url     链接
     * @param session 因为交汇涉及多个网站的会话, 因此session可以直接传进来, 如果不需要, 置为null或者""
     * @param type    待转为的bean的classType
     * @param <T>     class模板
     * @param charSet 指定字符集, 取值为 "UTF-8"
     * @return 返回response对应的bean
     */
    public static <T> T getBean(String url, String session, Type type, String charSet) {
        String response = get(url, session, charSet);
        //Log.d("atomu", "bean is " + response);
        return fromJson(response, type);
    }

    /**
     * 只是为了少一步gson而已=_=, 反正接口都给了, 用哪个无所谓...默认编码为utf-8
     *
     * @param url     链接
     * @param session 因为交汇涉及多个网站的会话, 因此session可以直接传进来, 如果不需要, 置为null或者""
     * @param type    待转为的bean的classType
     * @param <T>     class模板
     * @return 返回response对应的bean
     */
    public static <T> T getBean(String url, String session, Type type) {
        return getBean(url, session, type, "UTF-8");
    }

    /**
     * post 方法, 参数参照get方法
     *
     * @param url     链接
     * @param pairs   待post的表单
     * @param session 如果不需要设置置为null或者""
     * @return 如果成功返回entity, 否则返回""
     */
    public static HttpResponse postResponse(String url, List<NameValuePair> pairs, String session) {
//        FormEncodingBuilder builder = new FormEncodingBuilder();
//        for (NameValuePair pair : pairs) {
//            builder.add(pair.getName(), pair.getValue());
//        }
//        RequestBody formBody = builder.build();
//
//        Request request = new Request.Builder()
//                .header("User-Agent", USER_AGENT)
//                .url(url)
//                .post(formBody)
//                .build();
        httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        try {
            post.setHeader("User-Agent", USER_AGENT);
            post.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        if (session != null && !session.isEmpty()) {
//            request.newBuilder().header("Cookie", session).build();
            post.setHeader("Cookie", session);
        }
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Connection", CONNECTION);

        //默认不允许重定向
        HttpParams httpParams = new BasicHttpParams();
        httpParams.setParameter("http.protocol.handle-redirects",false);
        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        HttpConnectionParams.setSoTimeout(httpParams, TIME_OUT);
        post.setParams(httpParams);

        try {
//            Response response = okHttpClient.newCall(request).execute();
//            if (response.isSuccessful()) {
//                return response.body().string();
//            }
            HttpResponse httpResponse = httpClient.execute(post);
            return httpResponse;
        } catch (Exception e) {
            //Log.i("yj","post fail?");
            e.printStackTrace();
        }

        return null;
    }

    public static String post(String url, List<NameValuePair> pairs, String session){
        HttpResponse httpResponse = postResponse(url, pairs, session);
        if(httpResponse == null){
            return "";
        }
        try {
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * post方法直接转化bean
     *
     * @param url     链接
     * @param pairs   待post的表单
     * @param session 如果不需要设置session则置为null或者""
     * @param type    将要转化为Bean的classType
     * @param <T>     classType模板
     * @return 返回response对应的bean
     */
    public static <T> T postBean(String url, List<NameValuePair> pairs, String session, Type type) {
        String response = post(url, pairs, session);
        return fromJson(response, type);
    }

    /**
     * 获取当前url下的cookie?
     *
     * @return TODO: 返回 (待测试: 当前url下的) cookie list
     */
    public static List<Cookie> getCookies() {
//        return cookieManager.getCookieStore().getCookies();
        return ((AbstractHttpClient) httpClient).getCookieStore().getCookies();
    }

    /**
     * 清除cookies
     */
    public static void clearCookies() {
//        cookieManager.getCookieStore().removeAll();
        ((AbstractHttpClient) httpClient).getCookieStore().clear();
    }

}