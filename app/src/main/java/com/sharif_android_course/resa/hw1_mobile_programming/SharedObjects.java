package com.sharif_android_course.resa.hw1_mobile_programming;

class SharedObjects {
    private static SharedObjects instance = null;
    MainActivity mainActivity;
    WeatherActivity weatherActivity;
    String lastCitySearch = "";

    private SharedObjects() {

    }

    static SharedObjects getInstance() {
        if (instance == null)
            instance = new SharedObjects();
        return instance;
    }
}
