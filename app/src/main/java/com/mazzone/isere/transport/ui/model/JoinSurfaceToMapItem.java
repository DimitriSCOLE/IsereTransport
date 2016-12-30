package com.mazzone.isere.transport.ui.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinSurfaceToMapItem {
    @Id(autoincrement = true) private Long id;
    private Long surfaceId;
    private String mapItemId;
    @Generated(hash = 2051019819)
    public JoinSurfaceToMapItem(Long id, Long surfaceId, String mapItemId) {
        this.id = id;
        this.surfaceId = surfaceId;
        this.mapItemId = mapItemId;
    }
    @Generated(hash = 1044780322)
    public JoinSurfaceToMapItem() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getSurfaceId() {
        return this.surfaceId;
    }
    public void setSurfaceId(Long surfaceId) {
        this.surfaceId = surfaceId;
    }
    public String getMapItemId() {
        return this.mapItemId;
    }
    public void setMapItemId(String mapItemId) {
        this.mapItemId = mapItemId;
    }
}
