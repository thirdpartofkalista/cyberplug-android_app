package com.criss.cyberplug.networking;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.criss.cyberplug.constants.MessageType;
import com.criss.cyberplug.types.Device;
import com.criss.cyberplug.types.Group;
import com.criss.cyberplug.networking.HttpRequestsHandler.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class NetworkHandler {

    private static final String TAG = "NetworkHandler";

    private HttpRequestsHandler httpHandler;

    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    private Handler uiHandler;

//    public String deviceToJson(Device device) {
//        return gson.toJson(device);
//    }
//
//    public String groupToJson(Group group) {
//        return gson.toJson(group);
//    }
//
//    public Device jsonToDevice(String json) {
//        return gson.fromJson(json, Device.class);
//    }
//
//    public Group jsonToGroup(String json) {
//        return gson.fromJson(json, Group.class);
//    }
//
//    public ArrayList jsonToDeviceList(String json) {
//        return gson.fromJson(json, ArrayList.class);
//    }
//
//    public ArrayList jsonToGroupList(String json) {
//        return gson.fromJson(json, ArrayList.class);
//    }


    public NetworkHandler(Handler handler) {
        this.httpHandler = new HttpRequestsHandler();
        this.uiHandler = handler;
        Log.i(TAG, "NetworkHandler - instance created.");
    }


    public boolean updateDeviceStatus(Device device) {

        Log.i(TAG, "NetworkHandler - task updateDeviceStatus started.");

        Payload payload = new Payload(Payload.Type.DEVICE_STATUS_UPDATE, "token", device);
        Log.i(TAG, "NetworkHandler - created payload.");

        httpHandler.sendPost(gson.toJson(payload));
        Log.i(TAG, "NetworkHandler - forwarded the task to HttpHandler.");

        while (httpHandler.isOngoing()) {

        }
        Log.i(TAG, "NetworkHandler - HttpHandler finished the task.");

        Response response = httpHandler.getResponse();
        Log.i(TAG, "NetworkHandler - retrieved response.");

        return response.responseCode == HttpURLConnection.HTTP_OK;

    }

    public boolean addDevice(Device device) {
        // TODO: 06.08.2018 replace uploadDeviceList with this
        Payload payload = new Payload(Payload.Type.DEVICE_NEW, "token", device);

        httpHandler.sendPost(gson.toJson(payload));

        while (httpHandler.isOngoing()) {

        }

        Response response = httpHandler.getResponse();

        return response.responseCode == HttpURLConnection.HTTP_OK;
    }

    public void getDeviceList() {

        Log.i(TAG, "NetworkHandler - getDeviceList.");

        Thread worker = new Thread(new Runnable() {

            @Override
            public void run() {

                Payload payload = new Payload(Payload.Type.DEVICE_LIST_REQUEST, "token");
                Log.i(TAG, "NetworkHandler - worker thread - payload created.");

                httpHandler.sendPost(gson.toJson(payload));
                Log.i(TAG, "NetworkHandler - worker thread - forwarded task to HttpHandler.");

                while (httpHandler.isOngoing()) {

                }
                Log.i(TAG, "NetworkHandler - worker thread - HttpHandler finished the task.");

                Response response = httpHandler.getResponse();
                Log.i(TAG, "NetworkHandler - worker thread - retrieved the response.");

                if (response.responseCode != HttpURLConnection.HTTP_OK) {
                    Log.i(TAG, "NetworkHandler - worker thread - response code != HTTP_OK.");
                    Message msg = new Message();
                    msg.what = MessageType.RELOAD_LIST.getValue();
                    msg.arg1 = -1;
                    uiHandler.sendMessage(msg);
                    Log.i(TAG, "NetworkHandler - worker thread - sent message to uiHandler.");
                    return;
                }

                Device[] list;
                try {
                    list = gson.fromJson(response.responseData, Device[].class);
                    Log.i(TAG, "NetworkHandler - worker thread - converted received JSON to Device[].");
                    Message msg = new Message();
                    msg.what = MessageType.RELOAD_LIST.getValue();
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

        Log.i(TAG, "NetworkHandler - worker thread - uploadDeviceList");

        Thread worker = new Thread(new Runnable() {

            @Override
            public void run() {

                Payload payload = new Payload(Payload.Type.DEVICE_NEW, "token", list);
                Log.i(TAG, "NetworkHandler - worker thread - payload created.");

                httpHandler.sendPost(gson.toJson(payload));
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
