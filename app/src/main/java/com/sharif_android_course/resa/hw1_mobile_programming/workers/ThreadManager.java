package com.sharif_android_course.resa.hw1_mobile_programming.workers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import com.sharif_android_course.resa.hw1_mobile_programming.DataMessage;
import com.sharif_android_course.resa.hw1_mobile_programming.models.DayState;
import com.sharif_android_course.resa.hw1_mobile_programming.models.WeatherCondition;
import com.sharif_android_course.resa.hw1_mobile_programming.models.WeatherSearchResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ThreadManager {
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

    public void ExecuteWeatherRequest(String latitude, String longitude, String apiToken, int dayNumber) {
        Thread thread = new Thread(() -> {
            synchronized (this.requestManager) {
                requestManager.SendWeatherRequest(latitude, longitude, apiToken, dayNumber);
            }
        });
        thread.start();
    }

    public void PrepareImages(WeatherSearchResult searchResult) {
        List<WeatherCondition> conditions = new ArrayList<>();
        conditions.add(searchResult.weatherCurrent.condition);
        for (DayState c : searchResult.weatherForecast.forecastDay) {
            conditions.add(c.dayInformation.condition);
        }
        Thread thread = new Thread(() -> {
            File iconDir = new File(context.getCacheDir(), "icons/");
            if (!iconDir.exists()){
                iconDir.mkdir();
            }
            synchronized (this.requestManager) {
                for (int i = 0; i < conditions.size(); i++) {
                    String iconPath = conditions.get(i).icon;
                    if (iconPath.startsWith("//")) {
                        iconPath = "https:" + iconPath;
                        conditions.get(i).icon = iconPath;
                    }
                    Bitmap img;
                    if (iconPath.startsWith("http")) {
                        File file = new File(context.getCacheDir(), "icons/" + iconPath.substring(iconPath.lastIndexOf("/")));
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
                                    conditions.get(i).icon = file.getPath();
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
                handler.sendMessage(DataMessage.makeDataMessage(DataMessage.MessageInfo.IMAGE, searchResult));
            }
        });
        thread.start();
    }
}
