package com.mazzone.isere.transport.util;

import android.graphics.RectF;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mazzone.isere.transport.interactor.Repo;
import com.mazzone.isere.transport.ui.model.MapItem;
import com.mazzone.isere.transport.ui.model.SurfaceItem;

import rx.Observable;
import rx.functions.Func1;

public class SurfaceManager {

    private static final float RECT_SIDE_PRECISION = 0.0001f;

    private Repo<SurfaceItem> mSurfaceItemRepo;

    public SurfaceManager(Repo<SurfaceItem> surfaceItemRepo) {
        mSurfaceItemRepo = surfaceItemRepo;
    }

    public boolean areBoundsCovered(double northEastLatitude, double northEastLongitude, double southWestLatitude, double southWestLongitude) {
        RectF target = createValidRectangleFromLatLng(
                northEastLatitude,
                northEastLongitude,
                southWestLatitude,
                southWestLongitude);

        return Solve(target);
    }

    private boolean Solve(RectF rect) {
        // if there is a rectangle containing R
        for (SurfaceItem surfaceItem : mSurfaceItemRepo.getAll()) {
            if (createValidRectangleFromSurface(surfaceItem).contains(rect))
                return true;
        }

        if (rect.width() < RECT_SIDE_PRECISION && rect.height() < RECT_SIDE_PRECISION)
            return false;

        RectF UpperLeft = new RectF(rect.left, rect.top, rect.centerX(), rect.centerY());
        RectF UpperRight = new RectF(rect.centerX(), rect.top, rect.right, rect.centerY());
        RectF LowerLeft = new RectF(rect.left, rect.centerY(), rect.centerX(), rect.bottom);
        RectF LowerRight = new RectF(rect.centerX(), rect.centerY(), rect.right, rect.bottom);

        return Solve(UpperLeft) && Solve(UpperRight) && Solve(LowerLeft) && Solve(LowerRight);
    }

    /**
     * The contains method do not work with inverted rectangles, fix it by creating valid rect
     */
    private static RectF createValidRectangleFromLatLng(double northEastLatitude, double northEastLongitude, double southWestLatitude, double southWestLongitude) {
        return new RectF(
                (float) Math.min(northEastLongitude, southWestLongitude),
                (float) Math.min(northEastLatitude, southWestLatitude),
                (float) Math.max(northEastLongitude, southWestLongitude),
                (float) Math.max(northEastLatitude, southWestLatitude));
    }

    private RectF createValidRectangleFromSurface(SurfaceItem surfaceItem) {
        return createValidRectangleFromLatLng(
                surfaceItem.getNorthEastLatitude(),
                surfaceItem.getNorthEastLongitude(),
                surfaceItem.getSouthWestLatitude(),
                surfaceItem.getSouthWestLongitude());
    }

    /**
     * Provide an Observable of map item include in the provided bounds
     *
     * @param northEastLatitude  north east latitude point of bounds
     * @param northEastLongitude north east longitude point of bounds
     * @param southWestLatitude  south west latitude point of bounds
     * @param southWestLongitude south west longitude point of bounds
     * @return Observable of map Item
     */
    public Observable<MapItem> intersectMapItem(final double northEastLatitude, final double northEastLongitude, final double southWestLatitude, final double southWestLongitude) {
        return intersectSurfaceItem(northEastLatitude, northEastLongitude, southWestLatitude, southWestLongitude)
                .flatMap(new Func1<SurfaceItem, Observable<MapItem>>() {
                    @Override
                    public Observable<MapItem> call(SurfaceItem surfaceItem) {
                        return Observable.from(surfaceItem.getMapItems());
                    }
                })
                .distinct()
                .filter(new Func1<MapItem, Boolean>() {
                    @Override
                    public Boolean call(MapItem mapItem) {
                        return new LatLngBounds(
                                new LatLng(southWestLatitude, southWestLongitude), new LatLng(northEastLatitude, northEastLongitude))
                                .contains(new LatLng(mapItem.getLatitude(), mapItem.getLongitude()));
                    }
                });
    }

    private Observable<SurfaceItem> intersectSurfaceItem(double northEastLatitude, double northEastLongitude, double southWestLatitude, double southWestLongitude) {
        final RectF target = createValidRectangleFromLatLng(
                northEastLatitude,
                northEastLongitude,
                southWestLatitude,
                southWestLongitude);

        return Observable.from(mSurfaceItemRepo.getAll())
                .filter(new Func1<SurfaceItem, Boolean>() {
                    @Override
                    public Boolean call(SurfaceItem surfaceItem) {
                        return createValidRectangleFromSurface(surfaceItem).intersect(target);
                    }
                });
    }
}
