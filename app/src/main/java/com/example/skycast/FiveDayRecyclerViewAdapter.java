package com.example.skycast;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class FiveDayRecyclerViewAdapter extends RecyclerView.Adapter<FiveDayRecyclerViewAdapter.WeatherListItemViewHolder> {

    ArrayList<WeatherListItem> days;
    Calendar calendar = Calendar.getInstance();;
    String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

    int dayInt = calendar.get(Calendar.DAY_OF_WEEK) - 1;
    final String TAG = "demoss";
    int test;

    public FiveDayRecyclerViewAdapter(ArrayList<WeatherListItem> days) {
        this.days = days;
    }

    @NonNull
    @Override
    public WeatherListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.five_day_list_item, parent, false);
        WeatherListItemViewHolder wvh = new WeatherListItemViewHolder(view);

        return wvh;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherListItemViewHolder holder, int position) {

        WeatherListItem day = days.get(position);

        // Getting the next name of the next consecutive day
        dayInt++;
        if (dayInt > 6) {
            dayInt = 0;
        }
        holder.dateTimeText.setText(daysOfWeek[dayInt]);


        Picasso.get().load("https://openweathermap.org/img/wn/" + day.weather.get(0).icon + "@2x.png").into(holder.weatherImage);
        holder.tempText.setText(String.valueOf((int) day.main.temp) + "\u00B0 F");

        holder.position = position;
        holder.weatherListItem = day;



    }

    @Override
    public int getItemCount() {
        return this.days.size();
    }


    public class WeatherListItemViewHolder extends RecyclerView.ViewHolder {


        TextView dateTimeText;
        ImageView weatherImage;
        TextView tempText;

        int position;
        WeatherListItem weatherListItem;


        public WeatherListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTimeText = itemView.findViewById(R.id.dtFiveDayText);
            weatherImage = itemView.findViewById(R.id.weatherFiveDayImage);
            tempText = itemView.findViewById(R.id.tempFiveDayText);


        }
    }



}
