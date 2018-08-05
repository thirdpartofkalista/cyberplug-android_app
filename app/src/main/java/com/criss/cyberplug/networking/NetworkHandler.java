package com.criss.cyberplug.networking;

import android.util.Log;

import com.criss.cyberplug.types.Device;
import com.criss.cyberplug.types.Group;
import com.criss.cyberplug.networking.HttpRequestsHandler.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class NetworkHandler {

    private static final String TAG = "NetworkHandler";

    private HttpRequestsHandler httpHandler;

    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();


    public String deviceToJson(Device device) {
        return gson.toJson(device);
    }

    public String groupToJson(Group group) {
        return gson.toJson(group);
    }

    public Device jsonToDevice(String json) {
        return gson.fromJson(json, Device.class);
    }

    public Group jsonToGroup(String json) {
        return gson.fromJson(json, Group.class);
    }

    public ArrayList jsonToDeviceList(String json) {
        return gson.fromJson(json, ArrayList.class);
    }

    public ArrayList jsonToGroupList(String json) {
        return gson.fromJson(json, ArrayList.class);
    }


    public NetworkHandler() {
        this.httpHandler = new HttpRequestsHandler();
    }


    public boolean updateDeviceStatus(Device device) {

        Response response;

        Operation payload = new Operation(Operation.Type.DEVICE_STATUS_UPDATE, "token", device);

        Log.i(TAG, "before calling send post method");

        httpHandler.sendPost(gson.toJson(payload));

        Log.i(TAG, "after calling send post method");

        while (httpHandler.isOngoing()) {

        }

        response = httpHandler.getResponse();
        Log.i(TAG, "Thread finished");
        Log.i(TAG, "received response code:" + httpHandler.getResponse().responseData);

        if (response.responseCode != HttpURLConnection.HTTP_OK)
            return false;
        return true;

    }

//    public ArrayList getDeviceList(ArrayList deviceList) {
//
//        Operation payload = new Operation(Operation.Type.DEVICE_DATA_UPDATE, "token", deviceList);
//
//        httpHandler.sendPost(gson.toJson(payload));
//
//        return true;
//    }
}
