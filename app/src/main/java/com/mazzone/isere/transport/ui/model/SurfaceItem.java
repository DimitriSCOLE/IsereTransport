package com.mazzone.isere.transport.ui.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(active = true)
public class SurfaceItem {

    @Id(autoincrement = true) private Long id;
    @NotNull private double northEastLatitude;
    @NotNull private double northEastLongitude;
    @NotNull private double southWestLatitude;
    @NotNull private double southWestLongitude;
    @ToMany @JoinEntity(
            entity = JoinSurfaceToMapItem.class,
            sourceProperty = "surfaceId",
            targetProperty = "mapItemId")
    private List<MapItem> mapItems;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1751792118)
    private transient SurfaceItemDao myDao;
    @Generated(hash = 1549824681)
    public SurfaceItem(Long id, double northEastLatitude,
            double northEastLongitude, double southWestLatitude,
            double southWestLongitude) {
        this.id = id;
        this.northEastLatitude = northEastLatitude;
        this.northEastLongitude = northEastLongitude;
        this.southWestLatitude = southWestLatitude;
        this.southWestLongitude = southWestLongitude;
    }
    @Generated(hash = 677474299)
    public SurfaceItem() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public double getNorthEastLatitude() {
        return this.northEastLatitude;
    }
    public void setNorthEastLatitude(double northEastLatitude) {
        this.northEastLatitude = northEastLatitude;
    }
    public double getNorthEastLongitude() {
        return this.northEastLongitude;
    }
    public void setNorthEastLongitude(double northEastLongitude) {
        this.northEastLongitude = northEastLongitude;
    }
    public double getSouthWestLatitude() {
        return this.southWestLatitude;
    }
    public void setSouthWestLatitude(double southWestLatitude) {
        this.southWestLatitude = southWestLatitude;
    }
    public double getSouthWestLongitude() {
        return this.southWestLongitude;
    }
    public void setSouthWestLongitude(double southWestLongitude) {
        this.southWestLongitude = southWestLongitude;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 843900743)
    public List<MapItem> getMapItems() {
        if (mapItems == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapItemDao targetDao = daoSession.getMapItemDao();
            List<MapItem> mapItemsNew = targetDao._querySurfaceItem_MapItems(id);
            synchronized (this) {
                if(mapItems == null) {
                    mapItems = mapItemsNew;
                }
            }
        }
        return mapItems;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 991005104)
    public synchronized void resetMapItems() {
        mapItems = null;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    public void setMapItems(List<MapItem> mapItems) {
        this.mapItems = mapItems;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 393120317)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSurfaceItemDao() : null;
    }
}