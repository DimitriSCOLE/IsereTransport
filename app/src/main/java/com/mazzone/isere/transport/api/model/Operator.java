package com.mazzone.isere.transport.api.model;

import com.google.gson.annotations.SerializedName;

public class Operator {

    @SerializedName("Id")
    private int id;

    @SerializedName("Name")
    private String name;

    public Operator() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
