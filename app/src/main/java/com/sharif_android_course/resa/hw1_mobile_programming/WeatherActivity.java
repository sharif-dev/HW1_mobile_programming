package com.sharif_android_course.resa.hw1_mobile_programming;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.sharif_android_course.resa.hw1_mobile_programming.models.City;
import com.sharif_android_course.resa.hw1_mobile_programming.models.WeatherSearchResult;
import com.sharif_android_course.resa.hw1_mobile_programming.workers.ThreadManager;

public class WeatherActivity extends AppCompatActivity {

    public static final String TAG = "hw1_ActivityWeather";

    private City city;
    private ThreadManager threadManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Intent myIntent = getIntent();
        String cityinfo = myIntent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        city = (new Gson()).fromJson(cityinfo, City.class);
        setTitle(city.name);


        SharedObjects.getInstance().weatherActivity = this;

        threadManager = SharedObjects.getInstance().mainActivity.threadManager;

        threadManager.ExecuteWeatherRequest(city.getLatitude(), city.getLongitude(), getString(R.string.weather_token), 7);
    }

    public void broadcastSearchResult(WeatherSearchResult searchResult) {
        Log.i(TAG, searchResult.weatherCurrent.temperature.toString());
    }
}
