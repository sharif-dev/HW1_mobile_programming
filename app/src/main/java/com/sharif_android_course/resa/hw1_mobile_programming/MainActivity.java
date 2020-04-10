package com.sharif_android_course.resa.hw1_mobile_programming;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.sharif_android_course.resa.hw1_mobile_programming.adapters.CitiesAdapter;
import com.sharif_android_course.resa.hw1_mobile_programming.models.City;
import com.sharif_android_course.resa.hw1_mobile_programming.models.CitySearchResult;
import com.sharif_android_course.resa.hw1_mobile_programming.workers.ThreadManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "hw1_ActivityMain";
    public static final String EXTRA_MESSAGE = "MESSAGE_HW1_KEY";

    public ThreadManager threadManager;
    public List<City> cityList;
    public CitiesAdapter citiesAdapter;

    private Handler cityHandler;
    private ScheduledExecutorService searchExecutor = null;
    private ScheduledFuture scheduledSeacrh = null;
    private TextInputLayout citySearch;
    private ProgressBar prg;
    private RecyclerView rvCities;

    private ImageView imageView;


    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedObjects.getInstance().mainActivity = this;

        cityHandler = new MainHandler(Looper.getMainLooper());

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
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.this.scheduleSearching(s.toString(), MainActivity.this.getResources().getInteger(R.integer.search_time_interval));
                if (s.toString().equals("")) {
                    setLoading(false);
                } else {
                    setLoading(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imageView = findViewById(R.id.imageView3);

        threadManager.ExecuteImageRequest();

    }

    public void imageReceived(String data) {
        Log.i(TAG, data);
        byte[] decodedString = Base64.decode(data, Base64.URL_SAFE);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(decodedByte);
    }

    public void goNextForm(City city) {
        Intent intent = new Intent(this, WeatherActivity.class);
        Gson gson = new Gson();
        intent.putExtra(EXTRA_MESSAGE, gson.toJson(city));
        startActivity(intent);
    }

    public void scheduleSearching(String cityName, Integer interval) {
        if (searchExecutor == null)
            searchExecutor = Executors.newSingleThreadScheduledExecutor();

        if (this.scheduledSeacrh != null) {
            if (!this.scheduledSeacrh.isDone())
                this.scheduledSeacrh.cancel(false);
        }

        Runnable task = () -> {
            ///MainActivity.this.startCitySearch();
            getCityHandler().sendMessage(DataMessage.makeDataMessage(DataMessage.MessageInfo.START_SEARCH, null));
        };
        this.scheduledSeacrh = searchExecutor.schedule(task, interval, TimeUnit.MILLISECONDS);
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


    public void broadcastSearchResult(CitySearchResult searchResult) {
        cityList.clear();
        citiesAdapter.notifyDataSetChanged();
        for (City c : searchResult.cities) {
            cityList.add(c);
            citiesAdapter.notifyItemInserted(cityList.size() - 1);
        }
        if (searchResult.cities.size() == 0) {
            City city = new City();
            city.emptyCity = true;
            cityList.add(city);
            citiesAdapter.notifyItemInserted(cityList.size() - 1);
        }
        setLoading(false);
    }

    public void startCitySearch() {
        String city = getSearchTextStr();
        if (!city.equals("")) {
            threadManager.ExecuteCityRequest(city, getString(R.string.city_token));
        }
    }


}
