package com.saltfactory.androidclient;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by eurybus on 25.11.2017.
 */

public class RestClient {
    private Context context;
    private SyncHttpClient client;
    private RequestParams params;
    private String url = "https://salt.eu-gb.mybluemix.net/api/user/login/1";
    final static String TAG = "RestClient";
    private boolean auth = false;


    public RestClient(Context context) {
        this.context = context;

        this.client = new SyncHttpClient();
        RequestParams params = new RequestParams();
    }

    public boolean authUser(){
        try {
            this.client.get(url, getResponseHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return auth;
    }

    public ResponseHandlerInterface getResponseHandler() {
        return new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG, "HTTP " + statusCode + " " + responseString);
                auth = false;
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(TAG, "HTTP " + statusCode + " " + responseString);
                auth = true;
            }

        };
    }
}
