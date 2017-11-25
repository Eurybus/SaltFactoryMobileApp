package com.saltfactory.androidclient;

import android.Manifest;
import android.content.Intent;
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
    public ReceiverComponent rx;
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

    @Override
    protected void onStart(){
        super.onStart();
        checkPermissions();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // accepted, start getting location information
//                    proximiioAPI.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    Toast.makeText(this, "Location access granted", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Location access granted");
                } else {
                    // denied
                    Toast.makeText(this, "Location access denied by the user!", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_BLUETOOTH:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    proximiioAPI.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    private boolean hasPermsissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            return false;
        }
    }
    public void startButtonClicked(View view){
        if(!proximInit && hasPermsissions()){
            rx = new ReceiverComponent();
            rx.startProxim(this);
            proximInit = true;
        }

    }

    public void stopButtonClicked(View view){
        rx.stopProxim();
    }

    public void loginButtonClicked(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);
    }
}
