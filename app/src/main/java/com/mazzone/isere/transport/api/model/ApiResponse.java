package com.mazzone.isere.transport.api.model;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {

    @SerializedName("Data")
    private T mData;

    @SerializedName("Message")
    private String message;

    @SerializedName("StatusCode")
    private int statusCode;

    public ApiResponse() {
    }

    public T getData() {
        return mData;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
