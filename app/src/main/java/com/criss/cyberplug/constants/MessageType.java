package com.criss.cyberplug.constants;

public enum MessageType {
    LIST_RELOAD(0),
    LIST_UPDATE_UI(1),
    DEVICE_NEW(2);

    private final int value;

    MessageType(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        switch(this) {
            case LIST_RELOAD:
                return "LIST_RELOAD";
            case LIST_UPDATE_UI:
                return "LIST_UPDATE_UI";
            default:
                return super.toString();
        }
    }
}
