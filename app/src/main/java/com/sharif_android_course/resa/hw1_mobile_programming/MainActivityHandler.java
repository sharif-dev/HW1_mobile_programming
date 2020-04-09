package com.sharif_android_course.resa.hw1_mobile_programming;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.sharif_android_course.resa.hw1_mobile_programming.models.City;
import com.sharif_android_course.resa.hw1_mobile_programming.models.CitySearchResult;
import com.sharif_android_course.resa.hw1_mobile_programming.DataMessage.MessageInfo;

public class MainActivityHandler extends Handler {
    private MainActivity mainAct;

    MainActivityHandler(@NonNull Looper looper, MainActivity mainActivity) {
        super(looper);
        this.mainAct = mainActivity;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        if (!(msg.obj instanceof DataMessage))
            return;
        DataMessage dMsg = (DataMessage) msg.obj;


        if (dMsg.info == MessageInfo.CITY_TAKS_COMPLETE) {
            CitySearchResult searchResult = (CitySearchResult) dMsg.data;
            mainAct.cityList.clear();
            mainAct.citiesAdapter.notifyDataSetChanged();
            for (City c : searchResult.cities) {
                mainAct.cityList.add(c);
                mainAct.citiesAdapter.notifyItemInserted(mainAct.cityList.size() - 1);
            }
            mainAct.setLoading(false);



        } else if (dMsg.info == MessageInfo.WEATHER_TASK_COMPLETE) {




        } else if (dMsg.info == MessageInfo.START_SEARCH) {
            String city = mainAct.getSearchTextStr();
            if (!city.equals("")) {
                mainAct.threadManager.ExecuteCityRequest(city, mainAct.getString(R.string.city_token));
            }



        } else if (dMsg.info == MessageInfo.CITY_CLICKED) {
            mainAct.goNextForm((City) dMsg.data);



        } else if (dMsg.info == MessageInfo.ERROR) {
            mainAct.showErrorToUser((String) dMsg.data);
        }
    }
}
