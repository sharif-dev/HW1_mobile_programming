package com.sharif_android_course.resa.hw1_mobile_programming;

public class SharedObjects {
    private static SharedObjects instance = null;
    public MainActivity mainActivity;
    public WeatherActivity weatherActivity;
    public String lastCitySearch = "";

    private SharedObjects() {

    }

    public static SharedObjects getInstance() {
        if (instance == null)
            instance = new SharedObjects();
        return instance;
    }
}
