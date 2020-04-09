package com.sharif_android_course.resa.hw1_mobile_programming.controllers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sharif_android_course.resa.hw1_mobile_programming.R;
import com.sharif_android_course.resa.hw1_mobile_programming.models.City;

import java.util.List;

public class CitiesAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<City> cities;

    public CitiesAdapter(List<City> cities) {
        this.cities = cities;
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
        String text = city.name.toUpperCase() + "\n\n" + city.fullName;
        button.setText(text);
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

}

class ViewHolder extends RecyclerView.ViewHolder {
    private Button cityBtn;

    public ViewHolder(View itemView) {
        super(itemView);
        cityBtn = itemView.findViewById(R.id.city_button);
    }

    public Button getCityBtn() {
        return cityBtn;
    }
}