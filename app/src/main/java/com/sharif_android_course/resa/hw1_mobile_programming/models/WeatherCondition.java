package com.sharif_android_course.resa.hw1_mobile_programming.models;

import com.google.gson.annotations.SerializedName;

public class WeatherCondition {
    @SerializedName("text")
    public String conditionState;

    @SerializedName("icon")
    public String icon;
}
