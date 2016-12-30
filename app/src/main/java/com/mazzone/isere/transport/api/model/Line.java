package com.mazzone.isere.transport.api.model;

import com.google.gson.annotations.SerializedName;

public class Line {

    @SerializedName("Id")
    private int id;

    @SerializedName("Number")
    private String number;

    @SerializedName("Name")
    private String name;

    @SerializedName("OperatorId")
    private int operatorId;

    public Line() {
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public int getOperatorId() {
        return operatorId;
    }
}
