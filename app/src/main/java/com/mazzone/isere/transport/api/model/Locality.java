package com.mazzone.isere.transport.api.model;

import com.google.gson.annotations.SerializedName;

public class Locality {

    @SerializedName("Name")
    private String name;

    public Locality() {
    }

    public String getName() {
        return name;
    }
}
