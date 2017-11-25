package com.saltfactory.androidclient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import io.proximi.proximiiolibrary.ProximiioAPI;
import io.proximi.proximiiolibrary.ProximiioGeofence;
import io.proximi.proximiiolibrary.ProximiioListener;

/**
 * Created by eurybus on 25.11.2017.
 */

public class ReceiverComponent extends BroadcastReceiver {
    private static final String TAG="BackgroundReceiver";
    private ProximiioAPI proximiioAPI;
    private Context appContext;
    public boolean isAuthenticated = false;

    public void startProxim(Context context){
        appContext = context;
        Log.d(TAG, "Manually started Proximi.io listener");
        proximiioAPI = new ProximiioAPI(TAG, context);
        proximiioAPI.setLogin("h4211@student.jamk.fi", "omena11");
        proximiioAPI.setListener(new ProximiioListener() {
            @Override
            public void geofenceEnter(ProximiioGeofence geofence) {
                Log.d(TAG, "Geofence enter: " + geofence.getName());
                if(Objects.equals(geofence.getName(), "Lobby") && !isAuthenticated){
                    askForAuth();
                }
            }

            @Override
            public void geofenceExit(ProximiioGeofence geofence, @Nullable Long dwellTime) {
                Log.d(TAG, "Geofence exit: " + geofence.getName() + ", dwell time: " + String.valueOf(dwellTime));
            }

            @Override
            public void loginFailed(LoginError loginError) {
                Log.e(TAG, "LoginError! (" + loginError.toString() + ")");
            }
        });
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
                proximiioAPI.destroy();
                break;
            case ProximiioAPI.ACTION_OUTPUT:
                JSONObject json = null;
                Log.d(TAG, "ProximiApi Output intent action");
                try {
                    json = new JSONObject(intent.getStringExtra(ProximiioAPI.EXTRA_JSON));
                }
                catch (JSONException e) {
                    Log.d(TAG, "JSON error");
                }

                if (json != null) {
                    String title = null;
                    try {
                        if (!json.isNull("type") && !json.isNull("title")) {
                            if (json.getString("type").equals("push")) {
                                title = json.getString("title");
                            }
                        }
                    }
                    catch (JSONException e) {
                        // Not a push
                    }

                    if (title != null) {
                        Intent intent2 = new Intent(context, MainActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                                .setContentIntent(contentIntent)
                                .setSmallIcon(R.drawable.notification)
                                .setContentTitle(title);

                        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);

                        Notification notification = notificationBuilder.build();

                        notification.flags |= Notification.FLAG_AUTO_CANCEL;
                        notificationManager.notify(1, notification);
                    }
                }
                break;
            case ProximiioAPI.ACTION_GEOFENCE_ENTER:
                Log.d(TAG, "EBIN");
        }
    }

    public void stopProxim() {
        proximiioAPI.destroyService(true);
        proximiioAPI.setListener(null);
        Log.d(TAG, "Stopped");
    }

    private void askForAuth(){
        Log.d(TAG, "Begin authenticating user");
        isAuthenticated = true;
        Intent intent = new Intent(appContext, LoginActivity.class);
        appContext.startActivity(intent);
    }

}

