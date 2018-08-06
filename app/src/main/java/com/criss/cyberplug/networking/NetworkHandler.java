package com.criss.cyberplug.networking;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.criss.cyberplug.constants.Authentication;
import com.criss.cyberplug.types.thread_communication.MessageArg;
import com.criss.cyberplug.types.thread_communication.MessageType;
import com.criss.cyberplug.constants.Urls;
import com.criss.cyberplug.types.list.Device;
import com.criss.cyberplug.networking.HttpRequestsHandler.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class NetworkHandler {

    private static final String TAG = "NetworkHandler";

    private HttpRequestsHandler httpHandler;

    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    private Handler uiHandler;

    private final Authentication authentication;


    private class NetworkingWorker extends Thread {

        private URL url;

        private String token;

        private String method;


        private Payload payload;

        private ArrayList<Runnable> tasks;

        private Runnable httpOkHandler;

        private Runnable httpNotOkHandler;

        private ArrayList<Exception> exceptions;

        private Handler handler;

        private MessageType handlerMessageType;

        private Response response;


        public NetworkingWorker(URL url, String method, String token) {

            super();

            this.url = url;
            this.method = method;
            this.token = token;

            this.tasks = new ArrayList<Runnable>();
            this.exceptions = new ArrayList<Exception>();

        }

        Response getResponse() {
            return response;
        }


        public NetworkingWorker setPayload(Payload payload) {
            this.payload = payload;
            return this;
        }

        public NetworkingWorker setPayload(Payload.Type type, String token) {
            this.payload = new Payload(type, token);
            return this;
        }

        public NetworkingWorker setPayload(Payload.Type type, String token, Object data) {
            this.payload = new Payload(type, token, data);
            return this;
        }

        public NetworkingWorker addTask(Runnable task) {
            this.tasks.add(task);
            return this;
        }

        public NetworkingWorker setHttpOkHandler(Runnable handler) {
            this.httpOkHandler = handler;
            return this;
        }

        public NetworkingWorker setHttpNotOkHandler(Runnable handler) {
            this.httpNotOkHandler = handler;
            return this;
        }

        public NetworkingWorker setHandler(Handler handler) {
            this.handler = handler;
            return this;
        }

        public NetworkingWorker setHandlerMessageType(MessageType type) {
            this.handlerMessageType = type;
            return this;
        }

        public NetworkingWorker setMessageObjectType(Type t){
            return this;
        }


        @Override
        public void run() {
            super.run();

            response = null;

            Message msg;


            try {
                httpHandler.send(gson.toJson(payload), this.url, this.method);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                exceptions.add(e);
            }

            try {
                httpHandler.join();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                exceptions.add(e);
            }

            try{
                response = httpHandler.getResponse();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                exceptions.add(e);
            }

            if (response != null) {

                if (response.responseCode != HttpsURLConnection.HTTP_OK) {
                    if (httpNotOkHandler != null) {
                        Thread tempWorker = new Thread(httpNotOkHandler);
                        tempWorker.start();
                        try {
                            tempWorker.join();
                        } catch (InterruptedException e) {
                            Log.e(TAG, e.getMessage());
                            exceptions.add(e);
                        }
                    }
                    else {
                        msg = new Message();
                        if (handlerMessageType != null)
                            msg.what = handlerMessageType.getValue();
                        msg.arg1 = MessageArg.FAIL.getValue();
                        handler.sendMessage(msg);
                    }
                }
                else {
                    if (httpOkHandler != null) {
                        Thread tempWorker = new Thread(httpNotOkHandler);
                        tempWorker.start();
                        try {
                            tempWorker.join();
                        } catch (InterruptedException e) {
                            Log.e(TAG, e.getMessage());
                            exceptions.add(e);
                        }
                    }
                    else {
                        msg = new Message();
                        if (handlerMessageType != null)
                            msg.what = handlerMessageType.getValue();
                        msg.arg1 = MessageArg.SUCCES.getValue();
                        handler.sendMessage(msg);
                    }
                }

            }
            else {
                msg = new Message();
                if (handlerMessageType != null)
                    msg.what = handlerMessageType.getValue();
                msg.arg1 = MessageArg.NO_RESPONSE.getValue();
                handler.sendMessage(msg);
            }
        }
    }


    public NetworkHandler(Handler handler, Authentication authentication) {
        this.httpHandler = new HttpRequestsHandler();
        this.uiHandler = handler;
        this.authentication = authentication;
        Log.i(TAG, "NetworkHandler - instance created.");
    }


    public boolean updateDeviceStatus(Device device) {

        Log.i(TAG, "NetworkHandler - updateDeviceStatus().");

        Payload payload = new Payload(Payload.Type.DEVICE_STATUS_UPDATE, "token", device);
        Log.i(TAG, "NetworkHandler - created payload.");

        httpHandler.send(gson.toJson(payload), Urls.serverDeviceUrl, "POST");
        Log.i(TAG, "NetworkHandler - forwarded the task to HttpHandler.");

        while (httpHandler.isOngoing()) {

        }
        Log.i(TAG, "NetworkHandler - HttpHandler finished the task.");

        Response response = httpHandler.getResponse();
        Log.i(TAG, "NetworkHandler - retrieved response.");

        return response.responseCode == HttpURLConnection.HTTP_OK;

    }

    public void addDevice(final Device device) {

        Log.i(TAG, "NetworkHandler - addDevice().");

        Thread worker = new Thread(new Runnable() {

            @Override
            public void run() {

                Payload payload = new Payload(Payload.Type.DEVICE_NEW, "token", device);
                Log.i(TAG, "NetworkHandler - worker thread - payload created.");

                httpHandler.send(gson.toJson(payload), Urls.serverDeviceUrl, "POST");
                Log.i(TAG, "NetworkHandler - worker thread - forwarded task to HttpHandler.");

                while (httpHandler.isOngoing()) {

                }
                Log.i(TAG, "NetworkHandler - worker thread - HttpHandler finished the task.");

                Response response = httpHandler.getResponse();
                Log.i(TAG, "NetworkHandler - worker thread - retrieved the response.");

                if (response.responseCode != HttpURLConnection.HTTP_OK) {
                    Log.i(TAG, "NetworkHandler - worker thread - response code != HTTP_OK.");
                    Message msg = new Message();
                    msg.what = MessageType.DEVICE_NEW.getValue();
                    msg.arg1 = 0;
                    uiHandler.sendMessage(msg);
                    Log.i(TAG, "NetworkHandler - worker thread - sent message to uiHandler.");
                    return;
                }

                Message msg = new Message();
                msg.what = MessageType.DEVICE_NEW.getValue();
                msg.arg1 = 1;
                uiHandler.sendMessage(msg);
                Log.i(TAG, "NetworkHandler - worker thread - sent message to uiHandler.");
            }
        });
        worker.start();
        Log.i(TAG, "NetworkHandler - worker thread - worker thread started.");
    }

    public void getDeviceList() {

        Log.i(TAG, "NetworkHandler - getDeviceList().");

        Thread worker = new Thread(new Runnable() {

            @Override
            public void run() {

                Payload payload = new Payload(Payload.Type.DEVICE_LIST_REQUEST, "token");
                Log.i(TAG, "NetworkHandler - worker thread - payload created.");

                httpHandler.send(gson.toJson(payload), Urls.serverDeviceUrl, "POST");
                Log.i(TAG, "NetworkHandler - worker thread - forwarded task to HttpHandler.");

                while (httpHandler.isOngoing()) {

                }
                Log.i(TAG, "NetworkHandler - worker thread - HttpHandler finished the task.");

                Response response = httpHandler.getResponse();
                Log.i(TAG, "NetworkHandler - worker thread - retrieved the response.");

                if (response.responseCode != HttpURLConnection.HTTP_OK) {
                    Log.i(TAG, "NetworkHandler - worker thread - response code != HTTP_OK.");
                    Message msg = new Message();
                    msg.what = MessageType.LIST_RELOAD.getValue();
                    msg.arg1 = 0;
                    uiHandler.sendMessage(msg);
                    Log.i(TAG, "NetworkHandler - worker thread - sent message to uiHandler.");
                    return;
                }

                Device[] list;
                try {
                    list = gson.fromJson(response.responseData, Device[].class);
                    Log.i(TAG, "NetworkHandler - worker thread - converted received JSON to Device[].");
                    Message msg = new Message();
                    msg.what = MessageType.LIST_RELOAD.getValue();
                    msg.arg1 = 1;
                    msg.obj = list;
                    uiHandler.sendMessage(msg);
                    Log.i(TAG, "NetworkHandler - worker thread - sent message to uiHandler.");

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());

                }
            }
        });
        worker.start();
        Log.i(TAG, "NetworkHandler - worker thread - worker thread started.");
    }

    public void uploadDeviceList(final ArrayList<Device> list) {

        Log.i(TAG, "NetworkHandler - worker thread - uploadDeviceList()");

        Thread worker = new Thread(new Runnable() {

            @Override
            public void run() {

                Payload payload = new Payload(Payload.Type.DEVICE_NEW, "token", list);
                Log.i(TAG, "NetworkHandler - worker thread - payload created.");

                httpHandler.send(gson.toJson(payload), Urls.serverDeviceUrl, "POST");
                Log.i(TAG, "NetworkHandler - worker thread - forwarded task to httpHandler.");

                while (httpHandler.isOngoing()) {

                }

                Log.i(TAG, "NetworkHandler - worker thread - HttpHandler finished task.");

                Response response = httpHandler.getResponse();
                Log.i(TAG, "NetworkHandler - worker thread - retrieved the response.");

                if (response.responseCode != HttpURLConnection.HTTP_OK) {
                    Log.i(TAG, "NetworkHandler - worker thread - response code != HTTP_OK.");
                    Message msg = new Message();
                    msg.what = 50;
                    msg.arg1 = -1;
                    uiHandler.sendMessage(msg);
                    Log.i(TAG, "NetworkHandler - worker thread - sent message to uiHandler.");
                    return;
                }

                Message msg = new Message();
                msg.what = 50;
                msg.arg1 = 1;
                uiHandler.sendMessage(msg);
                Log.i(TAG, "NetworkHandler - worker thread - sent message to uiHandler.");
            }
        });
        worker.start();
        Log.i(TAG, "NetworkHandler - worker thread - worker thread started.");
    }
}
