package com.sharif_android_course.resa.hw1_mobile_programming;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.sharif_android_course.resa.hw1_mobile_programming.adapters.CitiesAdapter;
import com.sharif_android_course.resa.hw1_mobile_programming.adapters.WeatherAdapter;
import com.sharif_android_course.resa.hw1_mobile_programming.models.City;
import com.sharif_android_course.resa.hw1_mobile_programming.models.DayState;
import com.sharif_android_course.resa.hw1_mobile_programming.models.WeatherSearchResult;
import com.sharif_android_course.resa.hw1_mobile_programming.workers.ThreadManager;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {

    public static final String TAG = "hw1_ActivityWeather";

    private City city;
    private List<DayState> weatherList;
    private WeatherAdapter weatherAdapter;
    private ThreadManager threadManager;
    private ProgressBar prg;
    private RecyclerView rvWeather;

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

        rvWeather = findViewById(R.id.rvWeather);
        weatherList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(weatherList);
        rvWeather.setAdapter(weatherAdapter);
        rvWeather.setLayoutManager(new LinearLayoutManager(this));

        prg = findViewById(R.id.loading2);
        setLoading(true);

    }

    public void broadcastSearchResult(WeatherSearchResult searchResult) {
        weatherList.clear();
        weatherAdapter.notifyDataSetChanged();
        for(DayState state : searchResult.weatherForecast.forecastDay){
            weatherList.add(state);
            weatherAdapter.notifyItemInserted(weatherList.size() - 1);
        }
        setLoading(false);
    }

    void setLoading(boolean loading) {
        if (loading) {
            prg.setVisibility(View.VISIBLE);
            rvWeather.setVisibility(View.INVISIBLE);
        } else {
            prg.setVisibility(View.INVISIBLE);
            rvWeather.setVisibility(View.VISIBLE);
        }
    }
}
