package com.criss.cyberplug.networking;

import android.content.Context;
import android.widget.Toast;

import com.criss.cyberplug.types.Device;
import com.criss.cyberplug.types.Group;
import com.criss.cyberplug.types.Operation;
import com.criss.cyberplug.types.OperationTypes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class NetworkHandler {

    private Context context;

    private HttpRequestsHandler httpHandler;

    private Queue<Operation> buffer;

    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

//    Thread that goes through operations and handles them
    private Thread networkingThread = new Thread(new Runnable() {

        @Override
        public void run() {

//            Toast.makeText(context, "thread started", Toast.LENGTH_SHORT).show();

            while (true) {

//                if (haveTasks()) {
//
//                    Toast.makeText(context, "HAVE TASK", Toast.LENGTH_SHORT).show();
//
//                    Operation o = buffer.peek();
//
//                    switch (o.getType()) {
//
//                        case UPDATE_DEVICE_STATUS:
//                            String json = gson.toJson(o.getObj());
//
//                            httpHandler.sendPost(json);
//
//                            while (httpHandler.isOngoing()) {
//                                try {
//                                    this.wait(1);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            Toast.makeText(context, "operation succesful", Toast.LENGTH_SHORT).show();
//
//                            break;
//                    }
//                }
//
//                else {
//                    try {
//                        this.wait(10);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        }
    });


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

    public ArrayList getDeviceList(String json) {
        return gson.fromJson(json, ArrayList.class);
    }

    public ArrayList getGroupList(String json) {
        return gson.fromJson(json, ArrayList.class);
    }


    public NetworkHandler(Context context) {
        this.httpHandler = new HttpRequestsHandler(context);
        this.buffer = new LinkedList<>();
        this.networkingThread.start();
    }


    public void addOperation(OperationTypes type, Object obj) {
        buffer.add(new Operation(type, obj));
    }

    public void addOperation(OperationTypes type, String json) {
        buffer.add(new Operation(type, json));
    }

    public String getOperationJson() {
        return buffer.peek().getJson();
    }

    public Object getOperationObject() {
        return buffer.peek().getObj();
    }

    public void moveForward() {
        if (!buffer.isEmpty())
            buffer.remove();
    }

    public boolean haveTasks() {
        return !buffer.isEmpty();
    }

}
