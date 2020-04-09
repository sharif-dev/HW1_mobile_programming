package com.sharif_android_course.resa.hw1_mobile_programming.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherForecast {
    @SerializedName("forecastday")
    public List<DayState> forecastDay;
}