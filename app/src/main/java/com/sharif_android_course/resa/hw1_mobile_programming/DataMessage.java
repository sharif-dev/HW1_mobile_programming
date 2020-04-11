package com.sharif_android_course.resa.hw1_mobile_programming;

import android.os.Message;


public class DataMessage {
    MessageInfo info;
    Object data;

    public static Message makeDataMessage(MessageInfo info, Object data) {
        DataMessage dataMessage = new DataMessage();
        dataMessage.data = data;
        dataMessage.info = info;
        Message msg = new Message();
        msg.obj = dataMessage;
        return msg;
    }

    public enum MessageInfo {
        CITY_TAKS_COMPLETE,
        WEATHER_TASK_COMPLETE,
        CITY_CLICKED,
        WEATHER_WITH_IMAGE,
        ERROR_CITY,
        ERROR_WEATHER
    }
}
