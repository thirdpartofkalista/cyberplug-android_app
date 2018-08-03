package com.criss.cyberplug.types;

public class Device {

    private int id;
    private String name;
    private boolean status;
    private boolean isOnline;

    public Device(int id, String name, boolean status, boolean isOnline) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.isOnline = isOnline;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean getStatus() {
        return status;
    }

    public boolean isOnline() {
        return isOnline;
    }
}
