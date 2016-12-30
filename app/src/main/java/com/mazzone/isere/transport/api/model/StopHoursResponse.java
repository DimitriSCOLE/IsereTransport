package com.mazzone.isere.transport.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StopHoursResponse {

    @SerializedName("Hours")
    private List<Hour> hours;

    @SerializedName("Lines")
    private List<Line> lines;

    @SerializedName("VehicleJourneys")
    private List<VehicleJourney> vehicleJourneys;

    public StopHoursResponse() {
    }

    public List<Hour> getHours() {
        return hours;
    }

    public List<Line> getLines() {
        return lines;
    }

    public List<VehicleJourney> getVehicleJourneys() {
        return vehicleJourneys;
    }
}
