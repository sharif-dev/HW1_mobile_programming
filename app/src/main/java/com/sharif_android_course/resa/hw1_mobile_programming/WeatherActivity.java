package com.sharif_android_course.resa.hw1_mobile_programming;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.sharif_android_course.resa.hw1_mobile_programming.models.City;

public class WeatherActivity extends AppCompatActivity {

    private City city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Intent myIntent = getIntent();
        String cityinfo = myIntent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        city = (new Gson()).fromJson(cityinfo, City.class);
        setTitle(city.name);
    }
}
