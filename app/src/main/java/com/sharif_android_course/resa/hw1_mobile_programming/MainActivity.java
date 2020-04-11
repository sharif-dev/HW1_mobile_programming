package com.sharif_android_course.resa.hw1_mobile_programming;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.sharif_android_course.resa.hw1_mobile_programming.adapters.CitiesAdapter;
import com.sharif_android_course.resa.hw1_mobile_programming.models.City;
import com.sharif_android_course.resa.hw1_mobile_programming.models.CitySearchResult;
import com.sharif_android_course.resa.hw1_mobile_programming.workers.RequestManager;
import com.sharif_android_course.resa.hw1_mobile_programming.workers.ThreadManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "hw1_ActivityMain";
    public static final String EXTRA_MESSAGE = "INTENT_MESSAGE_HW1_KEY";

    public Handler cityHandler;
    public ThreadManager threadManager;

    public List<City> cityList;
    public CitiesAdapter citiesAdapter;
    private RecyclerView rvCities;

    private ScheduledExecutorService searchExecutor = null;
    private ScheduledFuture scheduledSeacrh = null;
    private TextInputLayout citySearch;
    private ProgressBar prg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedObjects.getInstance().mainActivity = this;

        cityHandler = new MainHandler(Looper.getMainLooper());
        threadManager = new ThreadManager(cityHandler, this);

        printThreadInfo(this.getLocalClassName());

        rvCities = findViewById(R.id.rvCities);
        prg = findViewById(R.id.loading);
        citySearch = findViewById(R.id.citySearchLayout);

        cityList = new ArrayList<>();
        citiesAdapter = new CitiesAdapter(cityList, cityHandler);
        rvCities.setAdapter(citiesAdapter);
        rvCities.setLayoutManager(new LinearLayoutManager(this));

        setLoading(false);

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
        citySearch.getEditText().setText(SharedObjects.getInstance().lastCitySearch);

        checkIntenetConnectivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void checkIntenetConnectivity() {
        if (RequestManager.isUserOffline(this)) {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.no_internet_error), Toast.LENGTH_SHORT);
            toast.show();
            goNextForm(null);
        }
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

        Runnable task = () -> MainActivity.this.startCitySearch(cityName);
        this.scheduledSeacrh = searchExecutor.schedule(task, interval, TimeUnit.MILLISECONDS);
    }

    public void startCitySearch(String city) {
        if (!city.equals("")) {
            threadManager.ExecuteCityRequest(city, getString(R.string.city_token));
        }
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
        SharedObjects.getInstance().lastCitySearch = getSearchTextStr();
    }

    public void showErrorToUser(String text) {
        if (RequestManager.isUserOffline(this)) {
            text = getString(R.string.no_internet_error);
        }
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.city_search_error)
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, null).show();
        setLoading(false);
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

    public String getSearchTextStr() {
        return Objects.requireNonNull(citySearch.getEditText()).getText().toString();
    }

    public static void printThreadInfo(String text) {
        Log.i(TAG, text + " \t\tExecuting in --> " + " tid : " + android.os.Process.myTid());
    }


}
