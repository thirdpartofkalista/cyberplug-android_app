package com.criss.cyberplug.types.thread_communication;

public enum MessageType {
    NOT_PROVIDED(-1),
    LIST_RELOAD(0),
    LIST_UPDATE_UI(1),
    DEVICE_NEW(2),
    DEVICE_UPDATE_STATUS(3),
    DEVICE_SETTINGS(4);

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
            case NOT_PROVIDED:
                return "NOT_PROVIDED";
            case LIST_RELOAD:
                return "LIST_RELOAD";
            case LIST_UPDATE_UI:
                return "LIST_UPDATE_UI";
            case DEVICE_NEW:
                return "DEVICE_NEW";
            case DEVICE_UPDATE_STATUS:
                return "DEVICE_UPDATE_STATUS";
            default:
                return super.toString();
        }
    }
}
