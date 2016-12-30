package com.mazzone.isere.transport.api.model;

import com.google.gson.annotations.SerializedName;

public class StopPoint {

    @SerializedName("Id")
    private String id;

    @SerializedName("Name")
    private String name;

    @SerializedName("Latitude")
    private double latitude;

    @SerializedName("Longitude")
    private double longitude;

    @SerializedName("Locality")
    private Locality locality;

    @SerializedName("LogicalStop")
    private LogicalStop logicalStop;

    @SerializedName("Operator")
    private Operator operator;

    public StopPoint() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Locality getLocality() {
        return locality;
    }

    public LogicalStop getLogicalStop() {
        return logicalStop;
    }

    public Operator getOperator() {
        return operator;
    }
}
