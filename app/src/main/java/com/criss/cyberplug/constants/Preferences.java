package com.criss.cyberplug.constants;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private Context context;

    private SharedPreferences preferences;

    private SharedPreferences.Editor editor;

    private boolean isLoggedIn;

    private String email;

    private String token;

    public Preferences(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);

        this.editor = preferences.edit();

        email = preferences.getString("email", "");
        isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        this.token = preferences.getString("token", "");
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        editor.putString("email", email);
        editor.apply();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        editor.putString("token", token);
        editor.apply();
    }
}
