package com.criss.cyberplug.types.networking;

public enum RequestCode {
    ADD_NEW_DEVICE(0),
    DEVICE_SETTINGS(1),
    APP_SETTINGS(2);

    private final int value;

    RequestCode(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        switch (this) {
            case ADD_NEW_DEVICE:
                return "ADD_NEW_DEVICE";
            case DEVICE_SETTINGS:
                return "DEVICE_SETTINGS";
            case APP_SETTINGS:
                return "APP_SETTINGS";
            default:
                return super.toString();
        }
    }
}
