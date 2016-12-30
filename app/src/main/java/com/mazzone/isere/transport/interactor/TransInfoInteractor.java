package com.mazzone.isere.transport.interactor;


import com.mazzone.isere.transport.api.model.StopPoint;
import com.mazzone.isere.transport.ui.model.HourItem;
import com.mazzone.isere.transport.ui.model.MapItem;

import java.util.List;

import rx.Observable;

public interface TransInfoInteractor {
    Observable<List<MapItem>> getMapItemInBounds(double northEastLatitude,
                                                 double northEastLongitude,
                                                 double southWestLatitude,
                                                 double southWestLongitude,
                                                 float zoom);

    Observable<List<HourItem>> getHourItemByLogicalId(String logicalStopId);

    Observable<List<StopPoint>> getResultForQuery(String query);
}
