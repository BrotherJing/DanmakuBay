package com.brotherjing.danmakubay.utils.network;

import android.os.Environment;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.greendao.dao.Word;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Brotherjing on 2015/10/7.
 */
public class SoundDownloadRequest extends Request {

    Response.Listener<String> listener;

    public SoundDownloadRequest(Word word,Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, word.getAudio(), errorListener);
        this.listener = listener;
    }

    @Override
    protected void deliverResponse(Object response) {
        listener.onResponse(response.toString());
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        String dir_str = Environment.getExternalStorageDirectory().getAbsolutePath()+"/sounds";
        String file_str = System.currentTimeMillis() + ".mp3";
        try {
            File dir = new File(dir_str);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir_str, file_str);
            file.createNewFile();

            OutputStream os = new FileOutputStream(file);
            os.write(response.data);
            os.close();
        }catch (Exception ex){
            ex.printStackTrace();
            return Response.error(new ParseError(ex));
        }
        return Response.success(file_str,HttpHeaderParser.parseCacheHeaders(response));
    }

}
