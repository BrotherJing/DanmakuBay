package com.brotherjing.danmakubay.utils;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Environment;

import com.brotherjing.danmakubay.utils.network.ShanbayClient;
import com.greendao.dao.Word;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Brotherjing on 2015/9/6.
 */
public class SoundManager {

    private static SoundPool soundPool;

    public static void prepare(){
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,5);
    }

    public static void release(){
        soundPool = null;
    }

    public static Observable<String> downloadAudio(Word word){
        return ShanbayClient.getInstance().downloadSound(word.getAudio())
                .map(new Func1<ResponseBody, String>() {
                    @Override
                    public String call(ResponseBody responseBody) {
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
                            os.write(responseBody.bytes());
                            os.close();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        return file_str;
                    }
                });
    }

    public static String downloadSound(Word word){

        String url_str = word.getAudio();
        String dir_str = Environment.getExternalStorageDirectory().getAbsolutePath()+"/sounds";
        String file_str = System.currentTimeMillis() + ".mp3";
        try {
            File dir = new File(dir_str);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir_str, file_str);
            file.createNewFile();

            URL url = new URL(url_str);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int len1;
            while((len1=is.read(bytes))!=-1){
                os.write(bytes,0,len1);
                os.flush();
            }
            os.close();
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
        return file_str;
    }

    public static void deleteSound(Word word){
        String dir_str = Environment.getExternalStorageDirectory().getAbsolutePath()+"/sounds";
        String file_str = word.getAudio_local();
        try{
            File file = new File(dir_str,file_str);
            file.delete();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void playSound(Word word){
        String file_str = word.getAudio_local();
        String dir_str = Environment.getExternalStorageDirectory().getAbsolutePath()+"/sounds";
        try {
            File file = new File(dir_str, file_str);
            int id = soundPool.load(file.getAbsolutePath(), 100);
            final int ii = id;
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    if (sampleId == ii)
                        soundPool.play(ii, 1f, 1f, 100, 0, 1f);
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
