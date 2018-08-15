package com.criss.cyberplug.networking;

import android.util.Log;

import com.criss.cyberplug.constants.EndPoints;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


//  Class that handles sending and receiving http requests
public class HttpRequestsHandler {

    private static final String TAG = "HttpRequestHandler";

//    The thread that will be used for networking
    private Thread networkingThread;

//    Class that holds the response from the last request
    public class Response {

        public int responseCode;

        public String responseMessage;

        public String responseData;

        public ArrayList<Exception> exceptions;

        public Response() {
            this.exceptions = new ArrayList<Exception>();
        }

    }

    private volatile Response response;

    public HttpRequestsHandler() {
        this.response = new Response();
        Log.i(TAG, "HttpRequestHandler - instance created.");
    }

    private HttpURLConnection getConnection(URL url, String method, /*String[] requestProperty,*/ boolean doInput, boolean doOutput, String token) throws IOException {

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        Log.i(TAG, "HttpUrlConnection - opened connection.");

        conn.setRequestMethod(method);
        Log.i(TAG, "HttpUrlConnection - set method: " + method);

        if (doOutput) {
            conn.setRequestProperty("Content-type", "application/json");
        }
        conn.setRequestProperty("Authentication", "Bearer " + token);
//        Log.i(TAG, "HttpUrlConnection - set request property: " + requestProperty[0] + ", " + requestProperty[1]);

        conn.setDoInput(doInput);
        Log.i(TAG, "HttpUrlConnection - set do input: " + doInput);

        conn.setDoOutput(doOutput);
        Log.i(TAG, "HttpUrlConnection - set do output: " + doOutput);

        return conn;
    }

    private void writeStream(HttpURLConnection conn, String json) throws IOException {

        DataOutputStream writer = new DataOutputStream(conn.getOutputStream());
        Log.i(TAG, "Writer - initialized.");

        writer.writeBytes(json);
        Log.i(TAG, "Writer - wrote: " + json);

        writer.flush();
        Log.i(TAG, "Writer - flush");

        writer.close();
        Log.i(TAG, "Writer - closed.");
    }

    private String readStream(HttpURLConnection conn) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        Log.i(TAG, "Reader - initialized.");

        String line;

        StringBuilder result = new StringBuilder();
        Log.i(TAG, "String builder - initialized.");

        while ((line = reader.readLine()) != null) {
            result.append(line);
            Log.i(TAG, "Line - read: " + line);
        }
        Log.i(TAG, "Reader - read: " + result.toString());

        reader.close();
        Log.i(TAG, "Reader - closed.");

        return result.toString();
    }


//    Method that sends a json
    public boolean send(final String json, final EndPoints.EndPoint endPoint, final String token) {

        this.networkingThread = new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection conn = null;
                Log.i(TAG, "HttpHandler Thread - started.");

                Log.i(TAG, endPoint.method + " " + endPoint.url);
                try {

                    conn = getConnection(endPoint.url, endPoint.method, /*new String[]{"Content-type", "application/json"},*/ true, true, token);
                    Log.i(TAG, "HttpHandler Thread - retrieved connection.");
                    conn.connect();
                    Log.i(TAG, "HttpHandler Thread - connected.");

                    writeStream(conn, json);
                    Log.i(TAG, "HttpHandler Thread - writeStream() succesful.");

                    response.responseCode = conn.getResponseCode();
                    Log.i(TAG, "HttpHandler Thread - retrieved response code: " + response.responseCode);
                    response.responseMessage = conn.getResponseMessage();
                    Log.i(TAG, "HttpHandler Thread - retrieved response message: " + response.responseMessage);
                    response.responseData = readStream(conn);
                    Log.i(TAG, "HttpHandler Thread - readStream() succesful.");
                    Log.i(TAG, "HttpHandler Thread - retrieved response data.");

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    response.exceptions.add(e);
                } finally {
                    try {
                        conn.disconnect();
                        Log.i(TAG, "HttpHandler Thread - disconnected.");
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        response.exceptions.add(e);
                    }
                }

            }
        });
        this.networkingThread.start();
        Log.i(TAG, "HttpHandler Thread - starting.");
        return true;
    }

//    Method that sends a request without a payload
    public boolean send(final EndPoints.EndPoint endPoint, final String token) {

        this.networkingThread = new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection conn = null;
                Log.i(TAG, "HttpHandler Thread - started.");

                try {

                    conn = getConnection(endPoint.url, endPoint.method, /*new String[]{"Content-type", "application/json"},*/ true, true, token);
                    Log.i(TAG, "HttpHandler Thread - retrieved connection.");
                    conn.connect();
                    Log.i(TAG, "HttpHandler Thread - connected.");

                    response.responseCode = conn.getResponseCode();
                    Log.i(TAG, "HttpHandler Thread - retrieved response code: " + response.responseCode);
                    response.responseMessage = conn.getResponseMessage();
                    Log.i(TAG, "HttpHandler Thread - retrieved response message: " + response.responseMessage);
                    response.responseData = readStream(conn);
                    Log.i(TAG, "HttpHandler Thread - readStream() succesful.");
                    Log.i(TAG, "HttpHandler Thread - retrieved response data.");

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    response.exceptions.add(e);
                } finally {
                    try {
                        conn.disconnect();
                        Log.i(TAG, "HttpHandler Thread - disconnected.");
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        response.exceptions.add(e);
                    }
                }

            }
        });
        this.networkingThread.start();
        Log.i(TAG, "HttpHandler Thread - starting.");
        return true;
    }

    public boolean isOngoing() {
        return networkingThread.isAlive();
    }

    public Response getResponse() {
        return response;
    }

    public void join() throws InterruptedException {
        networkingThread.join();
    }

}
