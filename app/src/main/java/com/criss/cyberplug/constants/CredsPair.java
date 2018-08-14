package com.criss.cyberplug.constants;

import com.google.gson.annotations.Expose;

public class CredsPair {

    @Expose public String email;

    @Expose public String password;

    public CredsPair(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
