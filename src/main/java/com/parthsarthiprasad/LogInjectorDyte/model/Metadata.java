package com.parthsarthiprasad.LogInjectorDyte.model;

public class Metadata {
    private String parentResourceId;
    public  Metadata(){
    }
    public Metadata(String parentResourceId) {
        this.parentResourceId = parentResourceId;
    }

    public String getParentResourceId() {
        return parentResourceId;
    }

    public void setParentResourceId(String parentResourceId) {
        this.parentResourceId = parentResourceId;
    }
}
