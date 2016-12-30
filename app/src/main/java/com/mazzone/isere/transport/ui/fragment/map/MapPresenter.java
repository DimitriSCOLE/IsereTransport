package com.mazzone.isere.transport.ui.fragment.map;

public interface MapPresenter {
    void boundsUpdate(double northEastLatitude, double northEastLongitude,
                      double southWestLatitude, double southWestLongitude,
                      float zoom);

    void selectMapItem(String id);

    void selectMap();

    void bindCallback(MapFragment.OnStopSelectedListener listener);

    void unbindCallback();
}
