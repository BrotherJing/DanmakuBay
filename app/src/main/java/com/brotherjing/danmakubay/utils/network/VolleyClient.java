package com.brotherjing.danmakubay.utils.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Brotherjing on 2015/10/7.
 */
public class VolleyClient {

    static private VolleyClient client = null;
    private static Context con = null;
    private RequestQueue requestQueue = null;
    private ImageLoader imageLoader = null;

    private VolleyClient(Context context){
        con = context.getApplicationContext();
        requestQueue = getRequestQueue();
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            final LruCache<String,Bitmap> cache = new LruCache<>(20);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url,bitmap);
            }
        });
        requestQueue.start();
    }

    public static synchronized VolleyClient getInstance(Context context){
        if(client==null){
            client = new VolleyClient(context);
        }
        return client;
    }

    private RequestQueue getRequestQueue(){
        Cache cache = new DiskBasedCache(con.getCacheDir(),1024*1024);
        Network network = new BasicNetwork(new HurlStack());
        return new RequestQueue(cache,network);
    }

    public <T> void addRequest(Request<T> req){
        requestQueue.add(req);
    }

    public ImageLoader getImageLoader(){
        return imageLoader;
    }
}
