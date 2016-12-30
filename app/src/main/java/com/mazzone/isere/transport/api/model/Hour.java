package com.mazzone.isere.transport.api.model;

import com.google.gson.annotations.SerializedName;

public class Hour {

    @SerializedName("LineId")
    private int lineId;

    @SerializedName("VehicleJourneyId")
    private int vehicleJourneyId;

    @SerializedName("TheoricDepartureTime")
    private int theoreticalDepartureTime;

    public Hour() {
    }

    public int getLineId() {
        return lineId;
    }

    public int getVehicleJourneyId() {
        return vehicleJourneyId;
    }

    public int getTheoreticalDepartureTime() {
        return theoreticalDepartureTime;
    }
}
