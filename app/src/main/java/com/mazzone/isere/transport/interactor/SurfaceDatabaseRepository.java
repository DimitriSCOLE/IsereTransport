package com.mazzone.isere.transport.interactor;

import com.mazzone.isere.transport.ui.model.DaoSession;
import com.mazzone.isere.transport.ui.model.JoinSurfaceToMapItem;
import com.mazzone.isere.transport.ui.model.MapItem;
import com.mazzone.isere.transport.ui.model.SurfaceItem;
import com.mazzone.isere.transport.ui.model.SurfaceItemDao;

import java.util.List;


public class SurfaceDatabaseRepository implements Repo<SurfaceItem> {

    private DaoSession mDaoSession;

    public SurfaceDatabaseRepository(DaoSession daoSession) {
        mDaoSession = daoSession;
    }

    @Override
    public long count() {
        return mDaoSession.getSurfaceItemDao().count();
    }

    @Override
    public void insert(SurfaceItem surface) {
        //insert surface
        mDaoSession.getSurfaceItemDao().insertOrReplaceInTx(surface);
        for (MapItem mapItem : surface.getMapItems()) {
            //insert map item
            mDaoSession.getMapItemDao().insertOrReplaceInTx(mapItem);
            //Create and insert join
            JoinSurfaceToMapItem join = new JoinSurfaceToMapItem();
            join.setSurfaceId(surface.getId());
            join.setMapItemId(mapItem.getId());
            mDaoSession.getJoinSurfaceToMapItemDao().insertOrReplaceInTx(join);
        }

    }

    @Override
    public boolean exist(SurfaceItem surfaceItem) {
        return mDaoSession.getSurfaceItemDao().queryBuilder()
                .where(SurfaceItemDao.Properties.NorthEastLatitude.eq(surfaceItem.getNorthEastLatitude()),
                        SurfaceItemDao.Properties.NorthEastLongitude.eq(surfaceItem.getNorthEastLongitude()),
                        SurfaceItemDao.Properties.SouthWestLatitude.eq(surfaceItem.getSouthWestLatitude()),
                        SurfaceItemDao.Properties.SouthWestLongitude.eq(surfaceItem.getSouthWestLongitude()))
                .build().unique() != null;
    }

    @Override
    public List<SurfaceItem> getAll() {
        return mDaoSession.getSurfaceItemDao().loadAll();
    }


}
