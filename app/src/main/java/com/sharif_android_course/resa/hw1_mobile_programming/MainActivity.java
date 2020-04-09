package com.sharif_android_course.resa.hw1_mobile_programming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;

import com.sharif_android_course.resa.hw1_mobile_programming.models.City;
import com.sharif_android_course.resa.hw1_mobile_programming.models.CitySearchResult;
import com.sharif_android_course.resa.hw1_mobile_programming.models.WeatherSearchResult;
import com.sharif_android_course.resa.hw1_mobile_programming.workers.RequestManager;
import com.sharif_android_course.resa.hw1_mobile_programming.workers.ThreadManager;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "hw1_MainActivity";

    private Handler cityHandler;
    private ThreadManager threadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                MainActivity.this.handleReceivedMessage(msg);
            }
        };

        threadManager = new ThreadManager(getCityHandler(), this);


        printThreadInfo("main");

        ProgressBar prg = findViewById(R.id.prg2);
        Button btn = findViewById(R.id.button);

        btn.setOnClickListener((view) -> {
            Log.i(TAG, "button");
            threadManager.ExecuteCityRequest("tehran", getString(R.string.city_token));
        });

    }

    public void handleReceivedMessage(Message msg) {
        Log.i(TAG, "msg received");
        if (msg.what == 22) {
            if (msg.obj instanceof CitySearchResult) {
                for (City c : ((CitySearchResult) msg.obj).cities) {
                    Log.i(TAG, c.name + "--" + c.fullName);
                }
            } else if (msg.obj instanceof String) {
                if (((String) msg.obj).startsWith("Error")) {
                    this.showErrorToUser((String) msg.obj);
                }
            }
        } else if (msg.what == 23) {
            if (msg.obj instanceof WeatherSearchResult) {
                // TODO : receive weather informations
            } else if (msg.obj instanceof String) {
                if (((String) msg.obj).startsWith("Error")) {
                    this.showErrorToUser((String) msg.obj);
                }
            }
        }
    }

    public void showErrorToUser(String text) {
        // TODO : handle error text
        Log.i(TAG, text);
    }

    public Handler getCityHandler() {
        return cityHandler;
    }

    public static void printThreadInfo(String text) {
        Log.i(TAG, text + " \t\tExecuting in --> " + " tid : " + android.os.Process.myTid()
        );
    }


}
