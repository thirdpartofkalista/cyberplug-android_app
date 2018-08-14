package com.criss.cyberplug.constants;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private Context context;

    private SharedPreferences preferences;

    private SharedPreferences.Editor editor;

    private boolean isLoggedIn;

    private String userName;

    private String token;

    public Preferences(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);

        this.editor = preferences.edit();

        userName = preferences.getString("userName", "");
        isLoggedIn = preferences.getBoolean("isLoggedIn", false);
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        editor.putString("userName", userName);
        editor.apply();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
