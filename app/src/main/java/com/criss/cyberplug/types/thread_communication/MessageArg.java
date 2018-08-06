package com.criss.cyberplug.types.thread_communication;

public enum MessageArg {
    SUCCES(0),
    FAIL(1),
    NO_RESPONSE(2);

    private final int value;

    MessageArg(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        switch(this) {
            case SUCCES:
                return "SUCCES";
            case FAIL:
                return "FAIL";
            case NO_RESPONSE:
                return "NO_RESPONSE";
            default:
                return super.toString();
        }
    }
}
