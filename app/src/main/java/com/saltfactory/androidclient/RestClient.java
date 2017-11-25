package com.saltfactory.androidclient;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by eurybus on 25.11.2017.
 */

public class RestClient {
    private Context context;
    private AsyncHttpClient client;
    private RequestParams params;
    private String url = "http://salt.eu-gb.mybluemix.net/api/authorization";
    final static String TAG = "RestClient";
    private boolean auth = false;


    public RestClient(Context context) {
        this.context = context;

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("status", "1");
    }

    public boolean authUser(){
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(context, "HTTP FAIL: " + statusCode + " " + responseString, Toast.LENGTH_LONG).show();
                Log.d(TAG, "HTTP " + statusCode + " " + responseString);
                auth = false;
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Toast.makeText(context, "HTTP WIN: " + statusCode + " " + responseString, Toast.LENGTH_LONG).show();
                Log.d(TAG, "HTTP " + statusCode + " " + responseString);
                auth = true;
            }
        });
        return auth;
    }
}
