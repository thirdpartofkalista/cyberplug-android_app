package com.criss.cyberplug.networking;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.criss.cyberplug.constants.Authentication;
import com.criss.cyberplug.constants.Urls;
import com.criss.cyberplug.networking.HttpRequestsHandler.Response;
import com.criss.cyberplug.types.list.Device;
import com.criss.cyberplug.types.thread_communication.MessageArg;
import com.criss.cyberplug.types.thread_communication.MessageType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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


    public class MessagePayload {

        public ArrayList<Exception> exceptions;

        public Object data;

        public MessagePayload() {
            this.exceptions = new ArrayList<Exception>();
            this.data = null;
        }

    }


    private class NetworkingWorker extends Thread {

        private static final String TAG = "Networking worker";

        private URL url;

        private String method;


        private Payload payload;

        private ArrayList<Runnable> tasks;

        private Runnable httpOkHandler;

        private Runnable httpNotOkHandler;

        private ArrayList<Exception> exceptions;

        private Handler handler;

        private MessageType handlerMessageType;

        private Response response;

        private boolean shouldRetrieveDataAsObject;

        private Class messageObjectClass;


        public NetworkingWorker(URL url, String method) {

            super();

            this.url = url;
            this.method = method;

            this.payload = null;
            this.tasks = new ArrayList<Runnable>();
            this.httpOkHandler = null;
            this.httpNotOkHandler = null;
            this.exceptions = new ArrayList<Exception>();
            this.handler = null;
            this.handlerMessageType = MessageType.NOT_PROVIDED;
            this.response = null;
            this.shouldRetrieveDataAsObject = false;
            this.messageObjectClass = null;

        }

        public void logInfo(String text) {
            Log.i(TAG, "(Info)Thread " + super.getId() + " - " + super.getName() + " --> " + text);
        }

        public void logError(String text) {
            Log.e(TAG, "(Error)Thread " + super.getId() + " - " + super.getName() + " --> " + text);
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

        public NetworkingWorker retrieveDataAsObject(Class c){
            this.shouldRetrieveDataAsObject = true;
            this.messageObjectClass = c;
            return this;
        }


        @Override
        public void run() {
            super.run();

            logInfo("started.");

            Message msg = new Message();

            MessagePayload msgPayload = new MessagePayload();

            try {
                httpHandler.send(gson.toJson(payload), this.url, this.method);
                logInfo("passed request to httpHandler.");
            } catch (Exception e) {
                e.printStackTrace();
                logError(e.getMessage());
                exceptions.add(e);
            }

            try {
                logInfo("joining httpHandler thread, waiting.");
                httpHandler.join();
                logInfo("httpHandler thread finished.");
            } catch (Exception e) {
                e.printStackTrace();
                logError(e.getMessage());
                exceptions.add(e);
            }

            try{
                response = httpHandler.getResponse();
                logInfo("retrieved the response.");
            } catch (Exception e) {
                e.printStackTrace();
                logError(e.getMessage());
                exceptions.add(e);
            }

            if (response != null) {
                logInfo("response is not null.");
                if (response.responseCode != HttpsURLConnection.HTTP_OK) {
                    logInfo("response code is not HTTP_OK.");
                    if (httpNotOkHandler != null) {
                        logInfo("using the provided httpNotOkHandler.");
                        Thread tempWorker = new Thread(httpNotOkHandler);
                        tempWorker.start();
                        try {
                            logInfo("joining httpNotOkHandler, waiting.");
                            tempWorker.join();
                            logInfo("httpNotOkHandler finished.");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            logError(e.getMessage());
                            exceptions.add(e);
                        }
                    }
                    else {
                        logInfo("handling http not ok.");
                        if (handler != null) {
                            logInfo("handler provided, building and sending the message.");
                            if (handlerMessageType != MessageType.NOT_PROVIDED) {
                                logInfo("message type is: " + handlerMessageType.toString() + ".");
                            }
                            else {
                                logInfo("message type not provided.");
                            }
                            msg.what = handlerMessageType.getValue();
                            if (shouldRetrieveDataAsObject) {
                                logInfo("worker should retrieve data as object.");
                                try {
                                    msgPayload.data = gson.fromJson(response.responseData, messageObjectClass);
                                    logInfo("converted data to given object class.");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    logError(e.getMessage());
                                    exceptions.add(e);
                                }
                            }
                            msg.arg1 = MessageArg.FAIL.getValue();
                            msgPayload.exceptions = exceptions;
                            msg.obj = msgPayload;
                            handler.sendMessage(msg);
                            logInfo("sent message to handler.");
                        }
                        else {
                            logInfo("handler not provided.");
                        }
                    }
                }
                else {
                    logInfo("response code is HTTP_OK.");
                    if (httpOkHandler != null) {
                        logInfo("using the provided httpOkHandler.");
                        Thread tempWorker = new Thread(httpNotOkHandler);
                        tempWorker.start();
                        try {
                            logInfo("joining httpNotOkHandler, waiting.");
                            tempWorker.join();
                            logInfo("httpNotOkHandler finished.");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            logError(e.getMessage());
                            exceptions.add(e);
                        }
                    }
                    else {
                        logInfo("handling http ok.");
                        if (handler != null) {
                            logInfo("handler provided, building and sending the message.");
                            if (handlerMessageType != MessageType.NOT_PROVIDED) {
                                logInfo("message type is: " + handlerMessageType.toString() + ".");
                                msg.what = handlerMessageType.getValue();
                            }
                            if (shouldRetrieveDataAsObject) {
                                logInfo("worker should retrieve data as object.");
                                try {
                                    msgPayload.data = gson.fromJson(response.responseData, messageObjectClass);
                                    logInfo("converted data to given object class.");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    logError(e.getMessage());
                                    exceptions.add(e);
                                }
                            }
                            msg.arg1 = MessageArg.SUCCES.getValue();
                            msgPayload.exceptions = exceptions;
                            msg.obj = msgPayload;
                            handler.sendMessage(msg);
                            logInfo("sent message to handler.");
                        }
                        else {
                            logInfo("handler not provided.");
                        }
                    }
                }

            }
            else {
                logInfo("no response.");
                if (handler != null) {
                    if (handlerMessageType != null) {
                        logInfo("message type is: " + handlerMessageType.toString() + ".");
                        msg.what = handlerMessageType.getValue();
                    }
                    msg.arg1 = MessageArg.NO_RESPONSE.getValue();
                    msgPayload.exceptions = exceptions;
                    msg.obj = msgPayload;
                    handler.sendMessage(msg);
                    logInfo("sent message to handler.");
                }
                else {
                    logInfo("handler not provided.");
                }
            }
            logInfo("finished.");
        }
    }


    public NetworkHandler(Handler handler, Authentication authentication) {
        this.httpHandler = new HttpRequestsHandler();
        this.uiHandler = handler;
        this.authentication = authentication;
        Log.i(TAG, "NetworkHandler - instance created.");
    }


    public void updateDeviceStatus(Device device) {

        NetworkingWorker worker = new NetworkingWorker(Urls.serverDeviceUrl, "POST");
        worker.setPayload(Payload.Type.DEVICE_STATUS_UPDATE, authentication.token, device)
                .setHandler(uiHandler)
                .setHandlerMessageType(MessageType.DEVICE_UPDATE_STATUS)
                .start();
    }

    public void addDevice(final Device device) {

        NetworkingWorker worker = new NetworkingWorker(Urls.serverDeviceUrl, "POST");
        worker.setPayload(Payload.Type.DEVICE_NEW, authentication.token, device)
                .setHandlerMessageType(MessageType.DEVICE_NEW)
                .setHandler(uiHandler)
                .start();
    }

    public void getDeviceList() {

        NetworkingWorker worker = new NetworkingWorker(Urls.serverDeviceUrl, "POST");
        worker.setPayload(Payload.Type.DEVICE_LIST_REQUEST, authentication.token)
                .retrieveDataAsObject(Device[].class)
                .setHandlerMessageType(MessageType.LIST_RELOAD)
                .setHandler(uiHandler)
                .start();
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
