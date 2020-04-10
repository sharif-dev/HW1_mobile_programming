package com.sharif_android_course.resa.hw1_mobile_programming;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.sharif_android_course.resa.hw1_mobile_programming.adapters.WeatherAdapter;
import com.sharif_android_course.resa.hw1_mobile_programming.models.City;
import com.sharif_android_course.resa.hw1_mobile_programming.models.DayState;
import com.sharif_android_course.resa.hw1_mobile_programming.models.WeatherSearchResult;
import com.sharif_android_course.resa.hw1_mobile_programming.workers.RequestManager;
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
    private TextView nowTemp;
    private TextView nowConditionText;
    private ImageView nowConditionImage;
    private ViewGroup nowCard;
    private RecyclerView rvWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);


        SharedObjects.getInstance().weatherActivity = this;
        threadManager = SharedObjects.getInstance().mainActivity.threadManager;

        rvWeather = findViewById(R.id.rvWeather);
        weatherList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(weatherList);
        rvWeather.setAdapter(weatherAdapter);
        rvWeather.setLayoutManager(new LinearLayoutManager(this));

        prg = findViewById(R.id.loading2);

        nowConditionText = findViewById(R.id.nowConditionText);
        nowTemp = findViewById(R.id.nowTempText);
        nowConditionImage = findViewById(R.id.nowConditionImage);
        nowCard = findViewById(R.id.nowCard);
        setLoading(true);


        Intent myIntent = getIntent();
        String cityinfo = myIntent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        if (cityinfo != null && !cityinfo.equals("null")) {
            city = (new Gson()).fromJson(cityinfo, City.class);
            threadManager.ExecuteWeatherRequest(city.getLatitude(), city.getLongitude(), city.name, getString(R.string.weather_token), 7);
            setTitle(city.name);
        } else {
            threadManager.readCachedInfo();
        }


    }


    public void imageReceived(WeatherSearchResult data) {
        weatherList.clear();
        weatherAdapter.notifyDataSetChanged();
        for (DayState state : data.weatherForecast.forecastDay) {
            weatherList.add(state);
            weatherAdapter.notifyItemInserted(weatherList.size() - 1);
        }
        nowTemp.setText(WeatherAdapter.getDegreeString(data.weatherCurrent.temperature));
        nowConditionImage.setImageBitmap(data.weatherCurrent.condition.bitmap);
        nowConditionText.setText(data.weatherCurrent.condition.conditionState);
        if (data.cityName != null) {
            setTitle(data.cityName);
        }
        setLoading(false);
    }

    public void broadcastSearchResult(WeatherSearchResult searchResult) {
        threadManager.cacheWeatherInfos(searchResult);
        threadManager.prepareWeatherWithImages(searchResult);
    }

    public void showErrorToUser(String text) {
        if (!RequestManager.isUserHaveInternet(this)) {
            text = "You dont have internet connection!";
        }
        new AlertDialog.Builder(WeatherActivity.this)
                .setTitle("ERROR in Weather Request")
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton("ok", (dialog, which) -> {
                    finish();
                }).show();
    }

    void setLoading(boolean loading) {
        if (loading) {
            prg.setVisibility(View.VISIBLE);
            rvWeather.setVisibility(View.INVISIBLE);
            nowCard.setVisibility(View.INVISIBLE);
        } else {
            prg.setVisibility(View.INVISIBLE);
            rvWeather.setVisibility(View.VISIBLE);
            nowCard.setVisibility(View.VISIBLE);
        }
    }

}
