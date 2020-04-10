package com.sharif_android_course.resa.hw1_mobile_programming.controllers;

import android.app.ActivityManager;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sharif_android_course.resa.hw1_mobile_programming.DataMessage;
import com.sharif_android_course.resa.hw1_mobile_programming.MainActivity;
import com.sharif_android_course.resa.hw1_mobile_programming.R;
import com.sharif_android_course.resa.hw1_mobile_programming.models.City;

import java.util.List;
import android.os.Handler;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.ViewHolder> {

    private List<City> cities;
    private Handler handler;

    public CitiesAdapter(List<City> cities, Handler handler) {
        this.cities = cities;
        this.handler = handler;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private Button cityBtn;

        ViewHolder(View itemView) {
            super(itemView);
            cityBtn = itemView.findViewById(R.id.city_button);
            cityBtn.setOnClickListener((view) -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    City city = cities.get(pos);
                    if (!city.emptyCity)
                        handler.sendMessage(DataMessage.makeDataMessage(DataMessage.MessageInfo.CITY_CLICKED, city));
                }
            });
        }

        Button getCityBtn() {
            return cityBtn;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.city_button_layout, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        City city = cities.get(position);
        Button button = holder.getCityBtn();
        if (city.emptyCity){
            button.setText(R.string.no_result);
            return;
        }
        String text = city.name.toUpperCase() + "\n\n" + city.fullName;
        button.setText(text);
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

}

