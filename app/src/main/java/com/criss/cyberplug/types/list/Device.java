package com.criss.cyberplug.types.list;

import com.google.gson.annotations.Expose;

public class Device {

    @Expose private int id;
    @Expose private String name;
    @Expose private boolean status;
    @Expose(serialize = false) private boolean isOnline;

    public Device(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isOnline() {
        return isOnline;
    }
}
