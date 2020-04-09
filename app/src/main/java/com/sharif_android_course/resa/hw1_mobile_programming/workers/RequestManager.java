package com.sharif_android_course.resa.hw1_mobile_programming.workers;

import android.os.Message;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.sharif_android_course.resa.hw1_mobile_programming.DataMessage;
import com.sharif_android_course.resa.hw1_mobile_programming.MainActivity;
import com.sharif_android_course.resa.hw1_mobile_programming.R;
import com.sharif_android_course.resa.hw1_mobile_programming.models.CitySearchResult;
import com.sharif_android_course.resa.hw1_mobile_programming.models.WeatherSearchResult;

import android.os.Handler;

public class RequestManager {

    private final RequestQueue queue;
    private final Handler mainHandler;
    private final android.content.Context context;

    public RequestManager(android.content.Context context, Handler mainHandler) {
        queue = Volley.newRequestQueue(context);
        this.context = context;
        this.mainHandler = mainHandler;
    }

    public void SendCityRequest(String searchText, String apiToken) {
        String url = this.context.getString(R.string.city_url);
        url = String.format(url, searchText, apiToken);
        MainActivity.printThreadInfo("in request manager");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Gson gson = new Gson();
                    MainActivity.printThreadInfo("in response manager");
                    CitySearchResult searchResult = gson.fromJson(response.toString(), CitySearchResult.class);
                    mainHandler.sendMessage(DataMessage.makeDataMessage(DataMessage.MessageInfo.CITY_TAKS_COMPLETE, searchResult));
                },
                error -> {
                    String err = "Error In Request Sending : \n" + error.getMessage();
                    mainHandler.sendMessage(DataMessage.makeDataMessage(DataMessage.MessageInfo.ERROR, err));
                });
        this.queue.add(jsonObjectRequest);
    }

    public void SendWeatherRequest(String latitude, String longitude, String apiToken, int dayNumber) {
        String url = this.context.getString(R.string.weather_url);
        url = String.format(url, latitude, longitude, apiToken, String.valueOf(dayNumber));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Gson gson = new Gson();
                    WeatherSearchResult searchResult = gson.fromJson(response.toString(), WeatherSearchResult.class);
                    mainHandler.sendMessage(DataMessage.makeDataMessage(DataMessage.MessageInfo.WEATHER_TASK_COMPLETE, searchResult));
                },
                error -> {
                    String err = "Error In Request Sending : \n" + error.getMessage();
                    mainHandler.sendMessage(DataMessage.makeDataMessage(DataMessage.MessageInfo.ERROR, err));
                });
        this.queue.add(jsonObjectRequest);
    }

    public RequestQueue getQueue() {
        return queue;
    }
}
