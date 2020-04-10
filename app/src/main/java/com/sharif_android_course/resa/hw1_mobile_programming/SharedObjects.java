package com.sharif_android_course.resa.hw1_mobile_programming;

public class SharedObjects {
    public MainActivity mainActivity;
    private SharedObjects() {

    }
    private static SharedObjects instance = null;
    public static SharedObjects getInstance(){
        if (instance == null)
            instance = new SharedObjects();
        return instance;
    }
}
