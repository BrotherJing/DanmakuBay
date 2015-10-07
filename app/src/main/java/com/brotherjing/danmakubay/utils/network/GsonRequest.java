package com.brotherjing.danmakubay.utils.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Brotherjing on 2015/10/7.
 */
public class GsonRequest<T> extends Request<T> {

    private final Gson gson = new Gson();
    Class<T> clazz;
    Response.Listener<T> listener;
    Map<String,String> headers;
    Map<String,String> params;

    public GsonRequest(int method,String url,Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.clazz = clazz;
        this.listener = listener;
    }

    public void putHeader(String key,String value){
        if(headers==null){
            headers = new HashMap<>();
        }
        headers.put(key,value);
    }

    public void putParam(String key,String value){
        if(params==null){
            params=new HashMap<>();
        }
        params.put(key,value);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params==null?super.getParams():params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers==null?super.getHeaders():headers;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(gson.fromJson(json,clazz),HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }catch (JsonParseException e){
            return Response.error(new ParseError(e));
        }
    }
}
