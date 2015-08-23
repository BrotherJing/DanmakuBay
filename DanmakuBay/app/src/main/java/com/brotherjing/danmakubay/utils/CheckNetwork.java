package com.brotherjing.danmakubay.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Brotherjing on 2015/8/23.
 */
public class CheckNetwork {

    private Context context;

    public CheckNetwork(Context ct) {
        context = ct;
    }

    public int getConnectionType() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)==null){
            return 2;
        }
        NetworkInfo.State mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

        if (wifi.toString().equals("CONNECTED")) {
            return 2;
        } else if (mobile.toString().equals("CONNECTED") && wifi.toString().equals("DISCONNECTED")) {
            return 1;
        } else {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()){
                return 2;
            }else {
                return 0;
            }

        }
    }

}
