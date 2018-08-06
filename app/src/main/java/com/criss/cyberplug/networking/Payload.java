package com.criss.cyberplug.networking;

import com.google.gson.annotations.Expose;

public class Payload {

    @Expose Object data;

    @Expose public String type;

    @Expose public String token;


    public Payload(Type type, String token, Object data) {
        this.type = type.toString();
        this.token = token;
        this.data = data;
    }

    public Payload(Type type, String token) {
        this.type = type.toString();
        this.token = token;
        this.data = null;
    }

    public enum Type {
//        Devices
        DEVICE_LIST_REQUEST,
        DEVICE_STATUS_UPDATE,
        DEVICE_DATA_UPDATE,
        DEVICE_NEW,
//        Groups
        GROUP_LIST_REQUEST,
        GROUP_STATUS_UPDATE,
        GROUP_DATA_UPDATE,
        GROUP_NEW;

        @Override
        public String toString() {
            return super.toString();
        }
    }

}
