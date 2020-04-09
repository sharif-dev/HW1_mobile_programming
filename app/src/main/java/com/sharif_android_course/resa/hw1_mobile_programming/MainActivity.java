package com.sharif_android_course.resa.hw1_mobile_programming;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    protected static final String TAG = "hw1_main_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressBar prg = findViewById(R.id.prg2);
        Button btn = findViewById(R.id.button);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://api.weatherapi.com/v1/forecast.json?q=35.0231,48.9697&key=6d7607dbb2a84331acc133348200904&days=7";

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Log.i(TAG, response.toString());
                },
                error -> Log.i(TAG, ("That didn't work!") + error.getMessage()));

        queue.add(stringRequest);
    }

    public static void printThreadInfo(String text) {
        Log.i(TAG, text + " \t\tExecuting in --> " + " tid : " + android.os.Process.myTid()
        );
    }
}
