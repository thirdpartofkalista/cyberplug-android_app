package com.criss.cyberplug.networking;

import com.google.gson.annotations.Expose;

public class Operation {

    @Expose Object data;

    @Expose public String type;

    @Expose public String token;


    public Operation(Type type, String token, Object data) {
        this.type = type.toString();
        this.token = token;
        this.data = data;
    }

    public enum Type {
        DEVICE_STATUS_UPDATE,
        DEVICE_DATA_UPDATE;

        @Override
        public String toString() {
            return super.toString();
        }
    }

}
