package com.criss.cyberplug.networking;

import android.content.Context;

import com.criss.cyberplug.R;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


//  Class that handles sending and receiving http requests
public class HttpRequestsHandler {

//    The URL of the remote server
    private URL serverURL;

//    The thread that will be used for networking
    private Thread networkingThread;

//    A string that contains the response message from the last operation
    private String responseMessage;

//    An int that contains the response code from the last operation
    private int responseCode;

//    The class constructor requires the activity's context in order to retrieve
//    the URL from a string resource
    public HttpRequestsHandler(Context context) {
        try {
            this.serverURL = new URL(context.getString(R.string.server_url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

//    Method that returns an HTTP URL Connection
//    Passing 0 returns a POST connection
//    Passing 1 returns a GET connection
    private HttpURLConnection getConnection(boolean x) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) serverURL.openConnection();
        if (x)
            conn.setRequestMethod("POST");
        else
            conn.setRequestMethod("GET");
//        conn.setRequestProperty("Content-Type", "application/json");
//        conn.setRequestProperty("Accept","application/json");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        return conn;
    }

//    Main methods

//    Method that sends a json as a GET request
    public void sendGet(final String json) {
        this.networkingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = getConnection(false);
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(json);
                    os.flush();
                    os.close();

                    responseCode = conn.getResponseCode();
                    responseMessage = conn.getResponseMessage();
                    
                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.networkingThread.start();
    }

//    Method that sends a json as a POST request
    public void sendPost(final String json) {
        this.networkingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = getConnection(true);
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(json);
                    os.flush();
                    os.close();

                    responseCode = conn.getResponseCode();
                    responseMessage = conn.getResponseMessage();

                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.networkingThread.start();
    }

//    Method that checks if the networkingThread is still running
//    If the operation is ongoing, it returns true
//    Otherwise, returns false
    public boolean isOngoing() {
        return networkingThread.isAlive();
    }

//    Method that returns the latest response from the server
//    If the operation is ongoing, returns -1
    public String getResponseMessage() {
        if (isOngoing())
            return "-1";
        return responseMessage;
    }

    public int getResponseCode()
    {
        if (isOngoing())
            return -1;
        return responseCode;
    }
}
