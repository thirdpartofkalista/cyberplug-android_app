package com.criss.cyberplug.types.list;

import com.google.gson.annotations.Expose;

public class Group {

    @Expose private int id;
    @Expose(serialize = false) private String name;
    @Expose private boolean status;

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
