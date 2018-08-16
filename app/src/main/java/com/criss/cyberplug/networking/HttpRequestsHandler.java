package com.criss.cyberplug.networking;

import android.util.Log;

import com.criss.cyberplug.constants.EndPoints;
import com.criss.cyberplug.types.networking.CredsPair;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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

    private HttpURLConnection getConnection(URL url, String method, String[] requestProperties, boolean doInput, boolean doOutput, String token) throws IOException {

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        Log.i(TAG, "HttpUrlConnection - opened connection.");

        conn.setRequestMethod(method);
        Log.i(TAG, "HttpUrlConnection - set method: " + method);

        for (int i = 0; i < requestProperties.length; i += 2) {
            conn.setRequestProperty(requestProperties[i], requestProperties[i+1]);
            Log.i(TAG, "HttpUrlConnection - set request property: " + requestProperties[i] + ", " + requestProperties[i+1]);
        }

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


////    Method that sends a json
//    public boolean send(final String json, final EndPoints.EndPoint endPoint, final String token) {
//
//        this.networkingThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                HttpURLConnection conn = null;
//                Log.i(TAG, "HttpHandler Thread - started.");
//
//                Log.i(TAG, endPoint.method + " " + endPoint.url);
//                try {
//
////                    if (endPoint.method == "GET") {
////                        conn = getConnection(endPoint.url, endPoint.method, /*new String[]{"Content-type", "application/json"},*/ true, false, token);
////                    }
////                    else {
////                        conn = getConnection(endPoint.url, endPoint.method, /*new String[]{"Content-type", "application/json"},*/ true, true, token);
////                    }
//
//                    if (endPoint.method == "GET" && endPoint == EndPoints.userGet) {
//                        Gson gson = new Gson();
//                        CredsPair creds = gson.fromJson(json, CredsPair.class);
//                        StringBuilder stringBuilder = new StringBuilder(endPoint.url.toString());
//                        stringBuilder.append("?email=")
//                                .append(URLEncoder.encode(creds.email, "UTF-8"))
//                                .append("&password=")
//                                .append(URLEncoder.encode(creds.password, "UTF-8"));
//                        endPoint.url = new URL(stringBuilder.toString());
//
//                        conn = getConnection(endPoint.url, endPoint.method, true, false, token);
//                    }
//                    else {
//                        conn = getConnection(endPoint.url, endPoint.method, /*new String[]{"Content-type", "application/json"},*/ true, true, token);
//                    }
//
//
//
//                    Log.i(TAG, "HttpHandler Thread - retrieved connection.");
//                    conn.connect();
//                    Log.i(TAG, "HttpHandler Thread - connected.");
//
//                    writeStream(conn, json);
//                    Log.i(TAG, "HttpHandler Thread - writeStream() succesful.");
//
//                    response.responseCode = conn.getResponseCode();
//                    Log.i(TAG, "HttpHandler Thread - retrieved response code: " + response.responseCode);
//                    response.responseMessage = conn.getResponseMessage();
//                    Log.i(TAG, "HttpHandler Thread - retrieved response message: " + response.responseMessage);
//                    response.responseData = readStream(conn);
//                    Log.i(TAG, "HttpHandler Thread - readStream() succesful.");
//                    Log.i(TAG, "HttpHandler Thread - retrieved response data.");
//
//                } catch (Exception e) {
//                    Log.e(TAG, e.getMessage());
//                    response.exceptions.add(e);
//                } finally {
//                    try {
//                        conn.disconnect();
//                        Log.i(TAG, "HttpHandler Thread - disconnected.");
//                    } catch (Exception e) {
//                        Log.e(TAG, e.getMessage());
//                        response.exceptions.add(e);
//                    }
//                }
//
//            }
//        });
//        this.networkingThread.start();
//        Log.i(TAG, "HttpHandler Thread - starting.");
//        return true;
//    }
//
////    Method that sends a request without a payload
//    public boolean send(final EndPoints.EndPoint endPoint, final String token) {
//
//        this.networkingThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                HttpURLConnection conn = null;
//                Log.i(TAG, "HttpHandler Thread - started.");
//
//                try {
//
//                    conn = getConnection(endPoint.url, endPoint.method, /*new String[]{"Content-type", "application/json"},*/ true, false, token);
//                    Log.i(TAG, "HttpHandler Thread - retrieved connection.");
//                    conn.connect();
//                    Log.i(TAG, "HttpHandler Thread - connected.");
//
//                    response.responseCode = conn.getResponseCode();
//                    Log.i(TAG, "HttpHandler Thread - retrieved response code: " + response.responseCode);
//                    response.responseMessage = conn.getResponseMessage();
//                    Log.i(TAG, "HttpHandler Thread - retrieved response message: " + response.responseMessage);
//                    response.responseData = readStream(conn);
//                    Log.i(TAG, "HttpHandler Thread - readStream() succesful.");
//                    Log.i(TAG, "HttpHandler Thread - retrieved response data.");
//
//                } catch (Exception e) {
//                    Log.e(TAG, e.getMessage());
//                    response.exceptions.add(e);
//                } finally {
//                    try {
//                        conn.disconnect();
//                        Log.i(TAG, "HttpHandler Thread - disconnected.");
//                    } catch (Exception e) {
//                        Log.e(TAG, e.getMessage());
//                        response.exceptions.add(e);
//                    }
//                }
//
//            }
//        });
//        this.networkingThread.start();
//        Log.i(TAG, "HttpHandler Thread - starting.");
//        return true;
//    }

    public void sendGet(final EndPoints.EndPoint endPoint, final String token, final String[] args) {

        this.networkingThread = new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection conn = null;

                try {
                    StringBuilder finalUrl = new StringBuilder(endPoint.url.toString());
                    if (args.length > 1) {
                        finalUrl.append("?" + URLEncoder.encode(args[0], "UTF-8") + "=")
                                .append(URLEncoder.encode(args[1], "UTF-8"));
                    }
                    if (args.length > 3) {
                        for (int i = 2; i < args.length; i += 2) {
                            finalUrl.append("&" + URLEncoder.encode(args[i], "UTF-8") + "=")
                                    .append(URLEncoder.encode(args[i+1], "UTF-8"));
                        }
                    }

                    conn = getConnection(new URL(finalUrl.toString()), endPoint.method, new String[] {"Authorization", "Bearer " + token}, true, false, token);
                    Log.i(TAG, "HttpHandler Thread - retrieved connection: " + endPoint.url.toString());

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
                    e.printStackTrace();
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
    }

    public void sendPost(final EndPoints.EndPoint endPoint, final String json, final String token) {
        this.networkingThread = new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection conn = null;

                try {

                    conn = getConnection(endPoint.url, endPoint.method, new String[] {"Content-Type", "application/json", "Authorization", "Bearer" + token}, true, true, token);
                    Log.i(TAG, "HttpHandler Thread - retrieved connection: " + endPoint.url.toString());

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
    }

    public void sendPut(final EndPoints.EndPoint endPoint, final String token, final String[] args) {

        this.networkingThread = new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection conn = null;
                try {
                    StringBuilder finalUrl = new StringBuilder(endPoint.url.toString());
                    if (args.length > 1) {
                        finalUrl.append("?" + URLEncoder.encode(args[0], "UTF-8") + "=")
                                .append(URLEncoder.encode(args[1], "UTF-8"));
                    }
                    if (args.length > 3) {
                        for (int i = 2; i < args.length; i += 2) {
                            finalUrl.append("&" + URLEncoder.encode(args[i], "UTF-8") + "=")
                                    .append(URLEncoder.encode(args[i+1], "UTF-8"));
                        }
                    }
                    Log.i("FINAL URL", finalUrl.toString());

                    conn = getConnection(new URL(finalUrl.toString()), endPoint.method, new String[] {"Authorization", "Bearer " + token}, true, false, token);
                    Log.i(TAG, "HttpHandler Thread - retrieved connection: " + endPoint.url.toString());

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
                    e.printStackTrace();
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
