package com.mazzone.isere.transport.api.model;

import com.google.gson.annotations.SerializedName;

public class LogicalStop {

    @SerializedName("Id")
    private String id;

    @SerializedName("Name")
    private String name;

    public LogicalStop() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
