package com.sharif_android_course.resa.hw1_mobile_programming.workers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.sharif_android_course.resa.hw1_mobile_programming.DataMessage;
import com.sharif_android_course.resa.hw1_mobile_programming.R;
import com.sharif_android_course.resa.hw1_mobile_programming.models.DayState;
import com.sharif_android_course.resa.hw1_mobile_programming.models.WeatherCondition;
import com.sharif_android_course.resa.hw1_mobile_programming.models.WeatherSearchResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ThreadManager {
    private static final String IMAGE_PROTOCOL = "https:";
    private final RequestManager requestManager;
    private final Handler handler;
    private Context context;

    public ThreadManager(Handler handler, Context context) {
        this.context = context;
        this.requestManager = new RequestManager(context, handler);
        this.handler = handler;
    }

    public void ExecuteCityRequest(String searchText, String apiToken) {
        Thread thread = new Thread(() -> {
            synchronized (this.requestManager) {
                requestManager.SendCityRequest(searchText, apiToken);
            }
        });
        thread.start();
    }

    public void ExecuteWeatherRequest(String latitude, String longitude, String cityName, String apiToken, int dayNumber) {
        Thread thread = new Thread(() -> {
            synchronized (this.requestManager) {
                requestManager.SendWeatherRequest(latitude, longitude, cityName, apiToken, dayNumber);
            }
        });
        thread.start();
    }

    public void prepareWeatherWithImages(WeatherSearchResult searchResult) {
        List<WeatherCondition> conditions = new ArrayList<>();
        conditions.add(searchResult.weatherCurrent.condition);
        for (DayState c : searchResult.weatherForecast.forecastDay) {
            conditions.add(c.dayInformation.condition);
        }
        Thread thread = new Thread(() -> {
            File iconDir = new File(context.getCacheDir(), context.getString(R.string.iconpath));
            if (!iconDir.exists()) {
                boolean ignore = iconDir.mkdir();
            }
            synchronized (this.requestManager) {
                for (int i = 0; i < conditions.size(); i++) {
                    String iconPath = conditions.get(i).icon;
                    if (iconPath.startsWith("//")) {
                        iconPath = IMAGE_PROTOCOL + iconPath;
                        conditions.get(i).icon = iconPath;
                    }
                    Bitmap img;
                    if (iconPath.startsWith(IMAGE_PROTOCOL)) {
                        File file = new File(context.getCacheDir(), context.getString(R.string.iconpath) + iconPath.substring(iconPath.lastIndexOf("/")));
                        if (file.exists()) {
                            img = BitmapFactory.decodeFile(file.getPath());
                        } else {
                            img = requestManager.DownloadImageRequest(iconPath);
                            if (img != null) {
                                try {
                                    FileOutputStream output = new FileOutputStream(file);
                                    img.compress(Bitmap.CompressFormat.PNG, 85, output);
                                    output.flush();
                                    output.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        img = BitmapFactory.decodeFile(conditions.get(i).icon);
                    }
                    if (img != null) {
                        conditions.get(i).bitmap = img;
                    }
                }
                handler.sendMessage(DataMessage.makeDataMessage(DataMessage.MessageInfo.WEATHER_WITH_IMAGE, searchResult));
            }
        });
        thread.start();
    }

    public void cacheWeatherInfos(WeatherSearchResult data) {
        Thread thread = new Thread(() -> {
            Gson gson = new Gson();
            String dataSerialized = gson.toJson(data);
            try {
                FileOutputStream output = new FileOutputStream(new File(context.getCacheDir(), context.getString(R.string.cache_file_path)), false);
                output.write(dataSerialized.getBytes());
                output.flush();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void readCachedInfo() {
        Thread thread = new Thread(() -> {
            Gson gson = new Gson();
            String err;
            try {
                File file = new File(context.getCacheDir(), context.getString(R.string.cache_file_path));
                if (!file.exists()) {
                    err = context.getString(R.string.cache_empty_error);
                    handler.sendMessage(DataMessage.makeDataMessage(DataMessage.MessageInfo.ERROR_WEATHER, err));
                } else {
                    List<String> data;
                    data = Files.readLines(file, Charset.defaultCharset());
                    String output = TextUtils.join("\n", data);
                    WeatherSearchResult result = gson.fromJson(output, WeatherSearchResult.class);
                    this.prepareWeatherWithImages(result);
                }
            } catch (IOException e) {
                err = e.toString();
                handler.sendMessage(DataMessage.makeDataMessage(DataMessage.MessageInfo.ERROR_WEATHER, err));
                e.printStackTrace();
            }
        });
        thread.start();
    }
}
