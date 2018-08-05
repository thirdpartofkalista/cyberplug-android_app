package com.criss.cyberplug.networking;

import android.util.Log;

import com.criss.cyberplug.constants.Urls;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


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

        public boolean hadError = false;

//        private boolean finished = false;

        public Response() {

        }

//        public void markAsFinished() {
//            this.finished = true;
//        }
//
        public void reset() {
//            this.finished = false;
            this.hadError = false;
        }
//
//        public boolean isFinished() {
//            return finished;
//        }
    }

    private volatile Response response;

    public HttpRequestsHandler() {
        this.response = new Response();
    }

    private HttpURLConnection getConnection(URL url, String method, String[] requestProperty, boolean doInput, boolean doOutput) throws IOException {

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod(method);

        conn.setRequestProperty(requestProperty[0], requestProperty[1]);

        conn.setDoInput(doInput);

        conn.setDoOutput(doOutput);

        return conn;
    }

    private void writeStream(HttpURLConnection conn, String json) throws IOException {

        DataOutputStream writer = new DataOutputStream(conn.getOutputStream());

        Log.i(TAG, "writer init");

        writer.writeBytes(json);
        Log.i(TAG, "wrote:" + json);

        writer.flush();
        Log.i(TAG, "flush");

        writer.close();
        Log.i(TAG, "writer close");
    }

    private String readStream(HttpURLConnection conn) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line;

        StringBuilder result = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        reader.close();

        return result.toString();
    }

//    Main methods

//    Method that sends a json as a GET request
    public Response sendGet(final String json) {

        response.reset();

        this.networkingThread = new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection conn = null;

                try {

                    conn = getConnection(Urls.serverDeviceUrl, "GET", new String[]{"Content-type", "application/json"}, true, true);
                    conn.connect();

                    writeStream(conn, json);

                    response.responseCode = conn.getResponseCode();
                    response.responseMessage = conn.getResponseMessage();
                    response.responseData = readStream(conn);

                } catch (Exception e) {
                    e.printStackTrace();
//                    response.hadError = true;
                } finally {
                    try {
                        conn.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                        response.hadError = true;
                    } finally {
//                        response.markAsFinished();
                    }
                }

            }
        });
        this.networkingThread.start();
        return response;
    }

//    Method that sends a json as a POST request
    public boolean sendPost(final String json) {

//        response.reset();

        this.networkingThread = new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection conn = null;

                Log.i(TAG, "init conn");

                try {

                    conn = getConnection(Urls.serverDeviceUrl, "POST", new String[]{"Content-type", "application/json"}, true, true);
                    Log.i(TAG, "getConnection() succesful");
                    conn.connect();
                    Log.i(TAG, "connect() succesful");

                    writeStream(conn, json);
                    Log.i(TAG, "writeStream() succesful");

                    response.responseCode = conn.getResponseCode();
                    response.responseMessage = conn.getResponseMessage();
                    response.responseData = readStream(conn);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                    response.hadError = true;
                } finally {
                    try {
                        conn.disconnect();
                        Log.i(TAG, "disconnect() succesful");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, e.getMessage());
                        response.hadError = true;
                    } finally {
//                        response.markAsFinished();
                    }
                }

            }
        });
        this.networkingThread.start();
        Log.i(TAG, "Thread started");
        return true;
    }

//    Method that checks if the networkingThread is still running
//    If the operation is ongoing, it returns true
//    Otherwise, returns false
    public boolean isOngoing() {
        return networkingThread.isAlive();
    }

    public Response getResponse() {
        return response;
    }

}
