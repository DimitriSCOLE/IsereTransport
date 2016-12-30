package com.mazzone.isere.transport.api;


import com.mazzone.isere.transport.api.model.ApiResponse;
import com.mazzone.isere.transport.api.model.StopHoursResponse;
import com.mazzone.isere.transport.api.model.StopPoint;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

public interface TransInfoService {

    @GET("map/v2/GetLineStopsByBoundingBox/json")
    Observable<ApiResponse<List<StopPoint>>> lineStopsByBoundingBox(
            @Query("LatitudeMin") double lowerLeftLatitude,
            @Query("LongitudeMin") double lowerLeftLongitude,
            @Query("LatitudeMax") double upperRightLatitude,
            @Query("LongitudeMax") double upperRightLongitude,
            @Query("user_key") String userKey
    );

    @GET("transport/v3/timetable/GetStopHours/json")
    @Headers("Cache-Control: public, max-age=3600")
    Observable<ApiResponse<StopHoursResponse>> stopHoursByLogicalStopId(
            @Query("LogicalStopIds") String logicalStopId,
            @Query("HourType") String hourType,
            @Query("TimeTableType") String timeTableType,
            @Query("user_key") String userKey
    );

    @GET("transport/v3/stop/SearchStops/json")
    Observable<ApiResponse<List<StopPoint>>> lineStopsByKeyword(
            @Query("Keywords") String keyword,
            @Query("MaxItems") int maxResult,
            @Query("user_key") String userKey
    );

}
