package com.sharif_android_course.resa.hw1_mobile_programming;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //test comment
        Log.i("test","salam");

        setContentView(R.layout.activity_main);
    }
}
