package com.sharif_android_course.resa.hw1_mobile_programming.models;

import com.google.gson.annotations.SerializedName;

public class DayClimateInfos {
    @SerializedName("mintemp_c")
    public Float minTemp;

    @SerializedName("maxtemp_c")
    public Float maxTemp;

    @SerializedName("avgtemp_c")
    public Float avgTemp;

    @SerializedName("condition")
    public WeatherCondition condition;
}
