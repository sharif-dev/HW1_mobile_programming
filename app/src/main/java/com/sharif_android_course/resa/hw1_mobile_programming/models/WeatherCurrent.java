package com.sharif_android_course.resa.hw1_mobile_programming.models;

import com.google.gson.annotations.SerializedName;

public class WeatherCurrent {
    @SerializedName("temp_c")
    public Float temperature;

    @SerializedName("is_day")
    private int isDay;

    @SerializedName("condition")
    public WeatherCondition condition;

    public boolean getIsDay(){
        return isDay == 1;
    }
}
