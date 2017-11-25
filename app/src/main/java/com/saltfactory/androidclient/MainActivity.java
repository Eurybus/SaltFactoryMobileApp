package com.saltfactory.androidclient;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;

import io.proximi.proximiiolibrary.ProximiioAPI;
import io.proximi.proximiiolibrary.ProximiioGeofence;
import io.proximi.proximiiolibrary.ProximiioListener;

import static android.R.attr.targetSdkVersion;

public class MainActivity extends AppCompatActivity {
    private boolean proximInit = false;
    private ProximiioAPI proximiioAPI;
    private static final String TAG = "ProximiioDemo";
    public static final String AUTH = "AUTH_KEY_HERE";
    private final int REQUEST_LOCATION = 1;
    private final int REQUEST_BLUETOOTH = 2;
    private final int REQUEST_BLUETOOTH_ADMIN = 3;

    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static String[] PERMISSIONS_BLUETOOTH = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void checkPermissions() {
        // Verify that all required contact permissions have been granted.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Location permissions have not been granted.
            Log.i(TAG, "Location permissions has NOT been granted. Requesting permissions.");
            requestLocationPermissions();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN)
                != PackageManager.PERMISSION_GRANTED) {
            // Location permissions have not been granted.
            Log.i(TAG, "Location permissions has NOT been granted. Requesting permissions.");
            requestBluetoothPermissions();
        }
    }
//    @Override
//    protected void onStart(){
//        int hasLocationPermission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // accepted, start getting location information
                    proximiioAPI.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    Toast.makeText(this, "Location access granted", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Location access granted");
                } else {
                    // denied
                    Toast.makeText(this, "Location access denied by the user!", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_BLUETOOTH:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    proximiioAPI.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    Toast.makeText(this, "Bluetooth access granted", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Bluetooth access granted");
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void requestLocationPermissions() {
        // Contact permissions have not been granted yet. Request them directly.
        ActivityCompat.requestPermissions(this, PERMISSIONS_LOCATION, REQUEST_LOCATION);
    }
    private void requestBluetoothPermissions(){
        ActivityCompat.requestPermissions(this, PERMISSIONS_BLUETOOTH, REQUEST_BLUETOOTH);
    }


    public void startButtonClicked(View view){
        if(!proximInit){
            // Create our Proximi.io listener
            proximiioAPI = new ProximiioAPI(TAG, this);
            proximiioAPI.setListener(new ProximiioListener() {
                @Override
                public void geofenceEnter(ProximiioGeofence geofence) {
                    Log.d(TAG, "Geofence enter: " + geofence.getName());
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
            proximiioAPI.setLogin("h4211@student.jamk.fi", "omena11");
            proximiioAPI.setActivity(this);
            proximInit = true;
        }

        checkPermissions();
    }

}
