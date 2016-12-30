package com.mazzone.isere.transport.ui.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class MapItem {

    @Id private String id;
    private String logicalStopId;
    private int operatorId;
    private String operatorName;
    private String name;
    private String city;
    private double latitude;
    private double longitude;

    @Generated(hash = 1613090306)
    public MapItem(String id, String logicalStopId, int operatorId, String operatorName,
            String name, String city, double latitude, double longitude) {
        this.id = id;
        this.logicalStopId = logicalStopId;
        this.operatorId = operatorId;
        this.operatorName = operatorName;
        this.name = name;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Generated(hash = 6009838)
    public MapItem() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogicalStopId() {
        return this.logicalStopId;
    }

    public void setLogicalStopId(String logicalStopId) {
        this.logicalStopId = logicalStopId;
    }

    public int getOperatorId() {
        return this.operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return this.operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
