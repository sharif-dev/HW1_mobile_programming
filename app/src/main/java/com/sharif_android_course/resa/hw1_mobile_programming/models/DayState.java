package com.sharif_android_course.resa.hw1_mobile_programming.models;

import com.google.gson.annotations.SerializedName;

public class DayState {
    @SerializedName("date")
    public String date;
    
    @SerializedName("day")
    public DayClimateInfos dayInformation;
}

