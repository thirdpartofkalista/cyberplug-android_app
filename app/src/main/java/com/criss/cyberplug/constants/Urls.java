package com.criss.cyberplug.constants;

import java.net.MalformedURLException;
import java.net.URL;

public class Urls {

    public static URL deviceApUrl;

    public static URL serverDeviceUrl;

    public static URL serverGroupUrl;

    static {
        try {
            serverDeviceUrl = new URL("http://192.168.0.157:1234/device");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    static {
        try {
            serverGroupUrl = new URL("http://192.168.0.157:1234/group");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    static {
        try {
            deviceApUrl = new URL("http://192.168.1.1");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
