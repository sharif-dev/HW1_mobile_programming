package com.sharif_android_course.resa.hw1_mobile_programming.models;

import com.google.gson.annotations.SerializedName;

public class WeatherSearchResult {
    @SerializedName("current")
    public WeatherCurrent weatherCurrent;

    @SerializedName("forecast")
    public WeatherForecast weatherForecast;
}
