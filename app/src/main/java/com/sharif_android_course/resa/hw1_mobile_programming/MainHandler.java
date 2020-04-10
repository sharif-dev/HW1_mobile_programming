package com.sharif_android_course.resa.hw1_mobile_programming;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.sharif_android_course.resa.hw1_mobile_programming.DataMessage.MessageInfo;
import com.sharif_android_course.resa.hw1_mobile_programming.models.City;
import com.sharif_android_course.resa.hw1_mobile_programming.models.CitySearchResult;
import com.sharif_android_course.resa.hw1_mobile_programming.models.WeatherSearchResult;

public class MainHandler extends Handler {
    private MainActivity mainAct;
    private WeatherActivity weatherActivity;

    MainHandler(@NonNull Looper looper) {
        super(looper);
        refreshRefrences();
    }

    private void refreshRefrences() {
        this.mainAct = SharedObjects.getInstance().mainActivity;
        this.weatherActivity = SharedObjects.getInstance().weatherActivity;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        if (!(msg.obj instanceof DataMessage))
            return;
        DataMessage dMsg = (DataMessage) msg.obj;
        refreshRefrences();

        if (dMsg.info == MessageInfo.CITY_TAKS_COMPLETE) {
            CitySearchResult searchResult = (CitySearchResult) dMsg.data;
            mainAct.broadcastSearchResult(searchResult);


        } else if (dMsg.info == MessageInfo.WEATHER_TASK_COMPLETE) {
            WeatherSearchResult searchResult = (WeatherSearchResult) dMsg.data;
            weatherActivity.broadcastSearchResult(searchResult);


        } else if (dMsg.info == MessageInfo.START_SEARCH) {
            mainAct.startCitySearch();


        } else if (dMsg.info == MessageInfo.CITY_CLICKED) {
            mainAct.goNextForm((City) dMsg.data);


        } else if (dMsg.info == MessageInfo.WEATHER_WITH_IMAGE) {
            weatherActivity.imageReceived((WeatherSearchResult) dMsg.data);


        } else if (dMsg.info == MessageInfo.ERROR_CITY) {
            mainAct.showErrorToUser((String) dMsg.data);
        } else if (dMsg.info == MessageInfo.ERROR_WEATHER) {
            weatherActivity.showErrorToUser((String) dMsg.data);
        }
    }
}
