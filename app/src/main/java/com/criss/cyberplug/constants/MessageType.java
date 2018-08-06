package com.criss.cyberplug.constants;

public enum MessageType {
    RELOAD_LIST(0),
    UPDATE_LIST_UI(1);

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
            case RELOAD_LIST:
                return "RELOAD_LIST";
            case UPDATE_LIST_UI:
                return "UPDATE_LIST_UI";
            default:
                return super.toString();
        }
    }
}
