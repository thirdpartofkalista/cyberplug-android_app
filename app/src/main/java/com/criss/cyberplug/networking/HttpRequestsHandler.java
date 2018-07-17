package com.criss.cyberplug.networking;

import android.content.Context;

import com.criss.cyberplug.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


//  Class that handles sending and receiving http requests
public class HttpRequestsHandler {

//    The URL of the remote server
    private URL serverURL;

//    The class constructor requires the activity's context in order to retrieve
//    the URL from a string resource
    public HttpRequestsHandler(Context context) {
        try {
            this.serverURL = new URL(context.getString(R.string.server_url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private HttpURLConnection getConnection() throws IOException {
        HttpURLConnection conn = (HttpURLConnection) serverURL.openConnection();
        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/json");
//        conn.setRequestProperty("Accept","application/json");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        return conn;
    }

//    Main methods
    public void sendGet(Object json) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        thread.start();
    }

    public void sendPost(Object json) {

    }
}
