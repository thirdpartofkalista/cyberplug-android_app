package com.criss.cyberplug.constants;

import java.net.MalformedURLException;
import java.net.URL;

public class EndPoints {

//    USER

    public static EndPoint userGet;
    static {
        try {
            userGet = new EndPoint("user", "GET");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static EndPoint userPost;
    static {
        try {
            userPost = new EndPoint("user", "POST");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static EndPoint userPut;
    static {
        try {
            userPut = new EndPoint("user", "PUT");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static EndPoint userDelete;
    static {
        try {
            userDelete = new EndPoint("user", "POST");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

//    DEVICE

    public static EndPoint deviceGet;
    static {
        try {
            deviceGet = new EndPoint("device", "GET");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static EndPoint devicePost;
    static {
        try {
            devicePost = new EndPoint("device", "POST");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static EndPoint devicePut;
    static {
        try {
            devicePut = new EndPoint("device", "PUT");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static EndPoint deviceDelete;
    static {
        try {
            deviceDelete = new EndPoint("device", "POST");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

//    GROUP

    public static class EndPoint {
        public String method;
        public URL url;

        public EndPoint(String endpoint, String method) throws MalformedURLException {
            this.method = method;
            this.url = new URL(Url.serverUrl + endpoint);
        }
    }

}
