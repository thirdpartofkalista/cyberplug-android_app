package com.criss.cyberplug.types.intents;

public enum RequestCode {
    ADD_NEW_DEVICE(0),
    DEVICE_SETTINGS(1),
    APP_SETTINGS(2),
    ADD_NEW_GROUP(3),
    GROUP_SETTINGS(4),
    CONFIGURE_DEVICE(5),
    CREATE_ACCOUNT(6),
    LOG_IN(7),
    SCAN_QR(8);

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
