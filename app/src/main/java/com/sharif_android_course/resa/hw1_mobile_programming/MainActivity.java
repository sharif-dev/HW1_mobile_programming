package com.sharif_android_course.resa.hw1_mobile_programming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;

import com.sharif_android_course.resa.hw1_mobile_programming.controllers.CitiesAdapter;
import com.sharif_android_course.resa.hw1_mobile_programming.models.City;
import com.sharif_android_course.resa.hw1_mobile_programming.models.CitySearchResult;
import com.sharif_android_course.resa.hw1_mobile_programming.models.WeatherSearchResult;
import com.sharif_android_course.resa.hw1_mobile_programming.workers.RequestManager;
import com.sharif_android_course.resa.hw1_mobile_programming.workers.ThreadManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "hw1_MainActivity";

    private Handler cityHandler;
    private ThreadManager threadManager;
    private List<City> cityList;
    private CitiesAdapter citiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                MainActivity.this.handleReceivedMessage(msg);
            }
        };

        threadManager = new ThreadManager(getCityHandler(), this);


        printThreadInfo("main");


        RecyclerView rvCities = findViewById(R.id.rvCities);
        cityList = new ArrayList<>();
        citiesAdapter = new CitiesAdapter(cityList);
        rvCities.setAdapter(citiesAdapter);
        rvCities.setLayoutManager(new LinearLayoutManager(this));



    }

    public void handleReceivedMessage(Message msg) {
        Log.i(TAG, "msg received");
        if (msg.what == R.integer.city_task_complete) {
            if (msg.obj instanceof CitySearchResult) {
                cityList.clear();
                citiesAdapter.notifyDataSetChanged();
                for (City c : ((CitySearchResult) msg.obj).cities) {
                    Log.i(TAG, c.name + "--" + c.fullName);
                    cityList.add(c);
                    citiesAdapter.notifyItemInserted(cityList.size() - 1);
                }
            } else if (msg.obj instanceof String) {
                if (((String) msg.obj).startsWith("Error")) {
                    this.showErrorToUser((String) msg.obj);
                }
            }
        } else if (msg.what == R.integer.weather_task_complete) {
            if (msg.obj instanceof WeatherSearchResult) {
                // TODO : receive weather informations
            } else if (msg.obj instanceof String) {
                if (((String) msg.obj).startsWith("Error")) {
                    this.showErrorToUser((String) msg.obj);
                }
            }
        }
    }

    public void showErrorToUser(String text) {
        // TODO : handle error text
        Log.i(TAG, text);
    }

    public Handler getCityHandler() {
        return cityHandler;
    }

    public static void printThreadInfo(String text) {
        Log.i(TAG, text + " \t\tExecuting in --> " + " tid : " + android.os.Process.myTid()
        );
    }


}
