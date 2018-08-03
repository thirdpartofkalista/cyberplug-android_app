package com.criss.cyberplug.types;

public class Group {

    private int id;
    private String name;
    private boolean status;

    public Group(int id, String name, boolean status) {
        this.id = id;
        this.name = name;
        this.status = status;
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
}
