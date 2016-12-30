package com.mazzone.isere.transport.interactor;


import com.mazzone.isere.transport.api.TransInfoService;
import com.mazzone.isere.transport.api.model.ApiResponse;
import com.mazzone.isere.transport.api.model.Hour;
import com.mazzone.isere.transport.api.model.Line;
import com.mazzone.isere.transport.api.model.StopHoursResponse;
import com.mazzone.isere.transport.api.model.StopPoint;
import com.mazzone.isere.transport.api.model.VehicleJourney;
import com.mazzone.isere.transport.ui.model.DaoSession;
import com.mazzone.isere.transport.ui.model.HourItem;
import com.mazzone.isere.transport.ui.model.MapItem;
import com.mazzone.isere.transport.ui.model.SurfaceItem;
import com.mazzone.isere.transport.util.SurfaceManager;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

import static com.mazzone.isere.transport.util.DateUtil.minutesToDateTime;
import static com.mazzone.isere.transport.util.DateUtil.nowWithoutSecondMinusOneMinute;

public class TransInfoInteractorImpl implements TransInfoInteractor {

    private static final String USER_KEY = "55373d4599c8477eeaca9c48c84b12ec";
    private static final String HOUR_TYPE = "Departure";
    private static final String TIME_TABLE_TYPE = "TheoricalTime";
    private static final String API_TIMEZONE_ID = "Europe/Paris";

    public static final float MIN_ZOOM = 15f;
    private static final int NUMBER_HOURS = 25;
    private static final int MAX_SEARCH_RESULT = 20;

    private TransInfoService mTransInfoService;
    private DaoSession mDaoSession;

    @Inject
    public TransInfoInteractorImpl(Retrofit retrofit, DaoSession daoSession) {
        mTransInfoService = retrofit.create(TransInfoService.class);
        mDaoSession = daoSession;
    }

    @Override
    public Observable<List<MapItem>> getMapItemInBounds(final double northEastLatitude, final double northEastLongitude, final double southWestLatitude, final double southWestLongitude, float zoom) {
        //Call only if zoom is acceptable
        if (zoom < MIN_ZOOM)
            return Observable.just(Collections.<MapItem>emptyList()); // TODO: 29/10/2016 replace by an exception

        //Get list of available surfaces
        Repo<SurfaceItem> surfaceItemRepo = new SurfaceDatabaseRepository(mDaoSession);

        //Check if covered
        final SurfaceManager manager = new SurfaceManager(surfaceItemRepo);
        boolean entirelyCovered = manager.areBoundsCovered(
                northEastLatitude, northEastLongitude,
                southWestLatitude, southWestLongitude);
        // retrieve points if all covered
        if (entirelyCovered)
            return manager.intersectMapItem(
                    northEastLatitude, northEastLongitude,
                    southWestLatitude, southWestLongitude)
                    .toList();

        // or
        return mTransInfoService.lineStopsByBoundingBox(
                southWestLatitude, southWestLongitude,
                northEastLatitude, northEastLongitude,
                USER_KEY)
                .flatMap(new Func1<ApiResponse<List<StopPoint>>, Observable<StopPoint>>() {
                    @Override
                    public Observable<StopPoint> call(ApiResponse<List<StopPoint>> listApiResponse) {
                        if (listApiResponse != null && listApiResponse.getData() != null)
                            return Observable.from(listApiResponse.getData());
                        return Observable.empty();
                    }
                })
                .map(new Func1<StopPoint, MapItem>() {
                    @Override
                    public MapItem call(StopPoint stopPoint) {
                        return new MapItem(
                                stopPoint.getId(),
                                stopPoint.getLogicalStop().getId(),
                                stopPoint.getOperator().getId(),
                                stopPoint.getOperator().getName(),
                                stopPoint.getName(),
                                stopPoint.getLocality().getName(),
                                stopPoint.getLatitude(),
                                stopPoint.getLongitude()
                        );
                    }
                })
                .toList()
                .doOnNext(new Action1<List<MapItem>>() {
                              @Override
                              public void call(List<MapItem> mapItems) {
                                  SurfaceDatabaseRepository surfaceDatabaseRepository = new SurfaceDatabaseRepository(mDaoSession); //TODO inject dependency
                                  //create
                                  SurfaceItem surface = new SurfaceItem();
                                  surface.setNorthEastLatitude(northEastLatitude);
                                  surface.setNorthEastLongitude(northEastLongitude);
                                  surface.setSouthWestLatitude(southWestLatitude);
                                  surface.setSouthWestLongitude(southWestLongitude);
                                  surface.setMapItems(mapItems);
                                  //insert if new
                                  if (!surfaceDatabaseRepository.exist(surface))
                                      surfaceDatabaseRepository.insert(surface);
                              }
                          }

                );
    }

    @Override
    public Observable<List<HourItem>> getHourItemByLogicalId(String logicalStopId) {
        return mTransInfoService.stopHoursByLogicalStopId(logicalStopId,
                HOUR_TYPE, TIME_TABLE_TYPE, USER_KEY)
                .flatMap(new Func1<ApiResponse<StopHoursResponse>, Observable<HourItem>>() {
                    @Override
                    public Observable<HourItem> call(ApiResponse<StopHoursResponse> stopHoursResponseApiResponse) {
                        //Handle good api result with wrong info
                        if (stopHoursResponseApiResponse == null || stopHoursResponseApiResponse.getData() == null)
                            return Observable.empty();
                        //Else actually do the job
                        return Observable.combineLatest(
                                Observable.just(stopHoursResponseApiResponse.getData()),
                                Observable.from(stopHoursResponseApiResponse.getData().getHours()),
                                new Func2<StopHoursResponse, Hour, HourItem>() {
                                    @Override
                                    public HourItem call(StopHoursResponse stopHoursResponse, Hour hour) {
                                        // Time Info
                                        DateTime departure = minutesToDateTime(hour.getTheoreticalDepartureTime(), API_TIMEZONE_ID);
                                        // Line Info
                                        String lineNumber = null;
                                        String lineName = null;
                                        int operatorId = 0;
                                        for (Line line : stopHoursResponse.getLines()) {
                                            if (line.getId() == hour.getLineId()) {
                                                lineNumber = line.getNumber();
                                                lineName = line.getName();
                                                operatorId = line.getOperatorId();
                                                break;
                                            }
                                        }
                                        // Vehicle Info
                                        String journeyDestination = null;
                                        int journeyDirection = 0;
                                        for (VehicleJourney vehicleJourney : stopHoursResponse.getVehicleJourneys()) {
                                            if (vehicleJourney.getId() == hour.getVehicleJourneyId()) {
                                                journeyDestination = vehicleJourney.getDestination();
                                                journeyDirection = vehicleJourney.getDirection();
                                                break;
                                            }
                                        }

                                        return new HourItem(
                                                lineNumber,
                                                lineName,
                                                operatorId,
                                                journeyDestination,
                                                journeyDirection,
                                                departure);
                                    }
                                })
                                .onBackpressureBuffer(); //Enable buffer
                    }
                })
                .filter(new Func1<HourItem, Boolean>() {
                    @Override
                    public Boolean call(HourItem hourItem) { //After now only
                        return hourItem.getTheoreticalDepartureDateTime().isAfter(nowWithoutSecondMinusOneMinute());
                    }
                })
                .toSortedList(new Func2<HourItem, HourItem, Integer>() {
                    @Override
                    public Integer call(HourItem hourItem, HourItem hourItem2) {
                        return hourItem.getTheoreticalDepartureDateTime().compareTo(hourItem2.getTheoreticalDepartureDateTime());
                    }
                })
                .flatMap(new Func1<List<HourItem>, Observable<HourItem>>() {
                    @Override
                    public Observable<HourItem> call(List<HourItem> hourItems) {
                        return Observable.from(hourItems);
                    }
                })
                .take(NUMBER_HOURS) //Limit number of result
                .toList();

    }

    @Override
    public Observable<List<StopPoint>> getResultForQuery(String query) {
        return mTransInfoService.lineStopsByKeyword(query, MAX_SEARCH_RESULT, USER_KEY)
                .flatMap(new Func1<ApiResponse<List<StopPoint>>, Observable<StopPoint>>() {
                    @Override
                    public Observable<StopPoint> call(ApiResponse<List<StopPoint>> listApiResponse) {
                        return Observable.from(listApiResponse.getData());
                    }
                })
                .toList();
    }
}
