package com.sharif_android_course.resa.hw1_mobile_programming.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sharif_android_course.resa.hw1_mobile_programming.R;
import com.sharif_android_course.resa.hw1_mobile_programming.models.DayState;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private static final String CENTI_GRADE = " °C";
    private static final String DECODE_DAY = "E";
    private List<DayState> weathers;

    public WeatherAdapter(List<DayState> weathers) {
        this.weathers = weathers;
    }

    public static String getDegreeString(float degree) {
        return degree + CENTI_GRADE;
    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.forcast_layout, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position) {
        DayState city = weathers.get(position);
        holder.conditionText.setText(city.dayInformation.condition.conditionState);
        holder.minTemp.setText(getDegreeString(city.dayInformation.minTemp));
        holder.maxTemp.setText(getDegreeString(city.dayInformation.maxTemp));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(city.date.split("-")[0]));
        cal.set(Calendar.MONTH, Integer.parseInt(city.date.split("-")[1]) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(city.date.split("-")[2]));
        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat(DECODE_DAY, Locale.US);
        weekDay = dayFormat.format(cal.getTime());
        holder.dayOfWeek.setText(weekDay.toUpperCase());
        holder.conditionImage.setImageBitmap(city.dayInformation.condition.bitmap);
    }

    @Override
    public int getItemCount() {
        return weathers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayOfWeek;
        TextView maxTemp;
        TextView minTemp;
        ImageView conditionImage;
        TextView conditionText;

        ViewHolder(View itemView) {
            super(itemView);
            dayOfWeek = itemView.findViewById(R.id.dayOfWeek);
            maxTemp = itemView.findViewById(R.id.maxTempText);
            minTemp = itemView.findViewById(R.id.minTempText);
            conditionImage = itemView.findViewById(R.id.conditionImage);
            conditionText = itemView.findViewById(R.id.conditionText);
        }

    }
}