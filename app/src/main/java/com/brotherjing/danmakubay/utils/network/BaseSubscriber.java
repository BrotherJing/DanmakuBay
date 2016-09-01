package com.brotherjing.danmakubay.utils.network;

import java.net.UnknownHostException;

import retrofit.HttpException;
import rx.Subscriber;

/**
 * Created by jingyanga on 2016/9/1.
 */
public abstract class BaseSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            //ToastUtils.showShort("error code " + ((HttpException) e).code());
        }else if(e instanceof UnknownHostException){
            //ToastUtils.showShort(R.string.error_unknown_host);
        }else{
            //ToastUtils.showShort(R.string.error_not_connected);
        }
        e.printStackTrace();
    }
}
