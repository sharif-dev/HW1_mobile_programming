package com.sharif_android_course.resa.hw1_mobile_programming.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class City {
    @SerializedName("text")
    public String name;

    @SerializedName("place_name")
    public String fullName;
    public transient boolean emptyCity = false;
    @SerializedName("center")
    private List<Float> pointAtMap = null;

    public String getLatitude() {
        if (pointAtMap == null)
            return "0";
        return String.valueOf(pointAtMap.get(1));
    }

    public String getLongitude() {
        if (pointAtMap == null)
            return "0";
        return String.valueOf(pointAtMap.get(0));
    }

}
