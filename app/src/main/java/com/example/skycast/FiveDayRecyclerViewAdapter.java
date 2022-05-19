package com.example.skycast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FiveDayRecyclerViewAdapter extends RecyclerView.Adapter<FiveDayRecyclerViewAdapter.WeatherListItemViewHolder> {

    ArrayList<WeatherListItem> days;
    final String TAG = "demoss";

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

        holder.dateTimeText.setText("Test");
        Picasso.get().load("https://openweathermap.org/img/wn/" + day.weather.get(0).icon + "@2x.png").into(holder.weatherImage);
        holder.tempText.setText(String.valueOf(day.main.temp) + "\u00B0");

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
