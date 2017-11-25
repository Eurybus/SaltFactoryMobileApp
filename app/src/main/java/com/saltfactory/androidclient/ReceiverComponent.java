package com.saltfactory.androidclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import io.proximi.proximiiolibrary.ProximiioAPI;

/**
 * Created by eurybus on 25.11.2017.
 */

public class ReceiverComponent extends BroadcastReceiver {
    private static final String TAG="BackgroundReceiver";

    public void startProxim(Context context){
        Log.d(TAG, "Manually started Proximi.io listener");
        ProximiioAPI proximiioAPI = new ProximiioAPI("BackgroundReceiver", context);
        proximiioAPI.setLogin("h4211@student.jamk.fi", "Omena11");
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case ProximiioAPI.ACTION_POSITION:
                double lat = intent.getDoubleExtra(ProximiioAPI.EXTRA_LAT, 0);
                double lon = intent.getDoubleExtra(ProximiioAPI.EXTRA_LON, 0);
                Log.d(TAG, "Position! (" + lat + ", " + lon + ")");
                break;
            case Intent.ACTION_BOOT_COMPLETED:
                Log.d(TAG, "Phone booted!");
                ProximiioAPI proximiioAPI = new ProximiioAPI("BackgroundReceiver", context);
                proximiioAPI.setLogin("h4211@student.jamk.fi", "Omena11");
//                proximiioAPI.destroy();
                break;
        }
    }
}

