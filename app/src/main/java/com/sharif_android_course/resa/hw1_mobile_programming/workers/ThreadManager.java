package com.sharif_android_course.resa.hw1_mobile_programming.workers;

import android.content.Context;
import android.os.Handler;

public class ThreadManager {
    private final RequestManager requestManager;

    public ThreadManager(Handler handler, Context context) {
        this.requestManager = new RequestManager(context, handler);
    }

    public void ExecuteCityRequest(String searchText, String apiToken) {
        Thread thread = new Thread(() -> {
            synchronized (this.requestManager) {
                requestManager.SendCityRequest(searchText, apiToken);
            }
        });
        thread.start();
    }

    public void ExecuteWeatherRequest(String latitude, String longitude, String apiToken, int dayNumber) {
        Thread thread = new Thread(() -> {
            synchronized (this.requestManager) {
                requestManager.SendWeatherRequest(latitude, longitude, apiToken, dayNumber);
            }
        });
        thread.start();
    }
}
