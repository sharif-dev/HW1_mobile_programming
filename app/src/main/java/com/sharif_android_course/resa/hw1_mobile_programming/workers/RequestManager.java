package com.sharif_android_course.resa.hw1_mobile_programming.workers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.sharif_android_course.resa.hw1_mobile_programming.DataMessage;
import com.sharif_android_course.resa.hw1_mobile_programming.R;
import com.sharif_android_course.resa.hw1_mobile_programming.models.CitySearchResult;
import com.sharif_android_course.resa.hw1_mobile_programming.models.WeatherSearchResult;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class RequestManager {

    private static final String TAG = "RequestManager";
    private final RequestQueue queue;
    private final Handler mainHandler;
    private final android.content.Context context;

    public RequestManager(android.content.Context context, Handler mainHandler) {
        queue = Volley.newRequestQueue(context);
        this.context = context;
        this.mainHandler = mainHandler;
    }

    public static boolean isUserHaveInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void SendCityRequest(String searchText, String apiToken) {
        String url = this.context.getString(R.string.city_url);
        url = String.format(url, searchText, apiToken);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Gson gson = new Gson();
                    CitySearchResult searchResult = gson.fromJson(response.toString(), CitySearchResult.class);
                    mainHandler.sendMessage(DataMessage.makeDataMessage(DataMessage.MessageInfo.CITY_TAKS_COMPLETE, searchResult));
                },
                error -> {
                    String err = "Error In Request Sending : \n" + error.getMessage();
                    mainHandler.sendMessage(DataMessage.makeDataMessage(DataMessage.MessageInfo.ERROR_CITY, err));
                });
        this.queue.add(jsonObjectRequest);
    }

    public void SendWeatherRequest(String latitude, String longitude, String cityName, String apiToken, int dayNumber) {
        String url = this.context.getString(R.string.weather_url);
        url = String.format(url, latitude, longitude, apiToken, String.valueOf(dayNumber));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Gson gson = new Gson();
                    WeatherSearchResult searchResult = gson.fromJson(response.toString(), WeatherSearchResult.class);
                    searchResult.cityName = cityName;
                    mainHandler.sendMessage(DataMessage.makeDataMessage(DataMessage.MessageInfo.WEATHER_TASK_COMPLETE, searchResult));
                },
                error -> {
                    String err = "Error In Request Sending : \n" + error.toString();
                    mainHandler.sendMessage(DataMessage.makeDataMessage(DataMessage.MessageInfo.ERROR_WEATHER, err));
                });
        this.queue.add(jsonObjectRequest);
    }

    public Bitmap DownloadImageRequest(String url) {
        InputStream is = null;
        BufferedInputStream bis = null;
        Bitmap bmp = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is);
            bmp = BitmapFactory.decodeStream(bis);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Bad ad URL : ", e);
        } catch (IOException e) {
            Log.e(TAG, "Could not get remote ad image : ", e);
        } catch (Exception e) {
            Log.e(TAG, "Other Error : ", e);
        } finally {
            try {
                if (is != null)
                    is.close();
                if (bis != null)
                    bis.close();
            } catch (IOException e) {
                Log.w(TAG, "Error closing stream.");
            }
        }
        return bmp;
    }

    public RequestQueue getQueue() {
        return queue;
    }
}
