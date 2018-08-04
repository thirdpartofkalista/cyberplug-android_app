package com.criss.cyberplug.types;

public class Operation {

    private OperationTypes type;

    private String json;

    private Object obj;

    private boolean finished = false;


    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isFinished() {
        return finished;
    }

    public OperationTypes getType() {
         return type;
    }

    public Operation(OperationTypes type, String json) {
        this.type = type;
        this.json = json;
    }

    public Operation(OperationTypes type, Object obj) {
        this.type = type;
        this.obj = obj;
    }
}
