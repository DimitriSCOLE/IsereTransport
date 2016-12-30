package com.mazzone.isere.transport.api.model;

import com.google.gson.annotations.SerializedName;

public class VehicleJourney {

    @SerializedName("Id")
    private int id;

    @SerializedName("JourneyDestination")
    private String destination;

    @SerializedName("JourneyDirection")
    private int direction;

    public VehicleJourney() {
    }

    public int getId() {
        return id;
    }

    public String getDestination() {
        return destination;
    }

    public int getDirection() {
        return direction;
    }
}
