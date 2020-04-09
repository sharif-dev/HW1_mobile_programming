package com.sharif_android_course.resa.hw1_mobile_programming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputLayout;
import com.sharif_android_course.resa.hw1_mobile_programming.controllers.CitiesAdapter;
import com.sharif_android_course.resa.hw1_mobile_programming.models.City;
import com.sharif_android_course.resa.hw1_mobile_programming.models.CitySearchResult;
import com.sharif_android_course.resa.hw1_mobile_programming.models.WeatherSearchResult;
import com.sharif_android_course.resa.hw1_mobile_programming.workers.RequestManager;
import com.sharif_android_course.resa.hw1_mobile_programming.workers.ThreadManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "hw1_MainActivity";

    private Handler cityHandler;
    public ThreadManager threadManager;
    List<City> cityList;
    CitiesAdapter citiesAdapter;
    private ScheduledExecutorService searchExecutor = null;
    private ScheduledFuture scheduledSeacrh = null;
    private TextInputLayout citySearch;
    private ProgressBar prg;
    private RecyclerView rvCities;

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityHandler = new MainActivityHandler(Looper.getMainLooper(), this);


        threadManager = new ThreadManager(getCityHandler(), this);


        printThreadInfo("main");

        rvCities = findViewById(R.id.rvCities);
        cityList = new ArrayList<>();
        citiesAdapter = new CitiesAdapter(cityList, getCityHandler());
        rvCities.setAdapter(citiesAdapter);
        rvCities.setLayoutManager(new LinearLayoutManager(this));

        prg = findViewById(R.id.loading);
        setLoading(false);

        citySearch = findViewById(R.id.citySearchLayout);
        Objects.requireNonNull(citySearch.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.this.scheduleSearching(s.toString());
                if (s.toString().equals("")) {
                    setLoading(false);
                } else {
                    setLoading(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void goNextForm(City city) {
        Log.i(TAG, city.fullName);
    }

    public void scheduleSearching(String cityName) {
        if (searchExecutor == null)
            searchExecutor = Executors.newSingleThreadScheduledExecutor();

        if (this.scheduledSeacrh != null) {
            if (!this.scheduledSeacrh.isDone())
                this.scheduledSeacrh.cancel(false);
        }

        Runnable task = () -> {
            getCityHandler().sendMessage(DataMessage.makeDataMessage(DataMessage.MessageInfo.START_SEARCH, null));
        };

        this.scheduledSeacrh = searchExecutor.schedule(task, 200, TimeUnit.MILLISECONDS);
    }

    public void showErrorToUser(String text) {
        // TODO : handle error text
        Log.i(TAG, text);
    }

    public Handler getCityHandler() {
        return cityHandler;
    }

    public String getSearchTextStr() {
        return Objects.requireNonNull(citySearch.getEditText()).getText().toString();
    }

    void setLoading(boolean loading) {
        if (loading) {
            prg.setVisibility(View.VISIBLE);
            rvCities.setVisibility(View.INVISIBLE);
        } else {
            prg.setVisibility(View.INVISIBLE);
            rvCities.setVisibility(View.VISIBLE);
        }
    }

    public static void printThreadInfo(String text) {
        Log.i(TAG, text + " \t\tExecuting in --> " + " tid : " + android.os.Process.myTid()
        );
    }


}
