package com.example.skycast;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // API key: 833110e347976d143c39463d4742e9fc
    // Default zip code: 28223
    // URL:  https://api.openweathermap.org/data/2.5/weather?zip=28223,us&appid=833110e347976d143c39463d4742e9fc&units=imperial

    IHome mListener;
    final String TAG = "demoss";
    OkHttpClient client = new OkHttpClient();
    Handler mHandler;
    ImageView currWeatherImage;
    TextView currTempText;
    TextView weatherDescText;
    TextView cityNameText;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        getActivity().setTitle("Skycast");

        // Initializing class fields
        currWeatherImage = view.findViewById(R.id.currWeatherImage);
        currTempText = view.findViewById(R.id.currTempText);
        weatherDescText = view.findViewById(R.id.weatherDescText);
        cityNameText = view.findViewById(R.id.cityNameText);


        // Method to get the HTTP response containing the weather data
        getCurrentWeather();




        // Thread handler to change UI components according to HTTPS Response
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {

                // Create alert dialog if the device has no internet connection
                if (msg.getData().getInt("API_RESPONSE_STATUS") == 1) {
                    String failedMsgString = "Please make sure you are connected to wifi or cellular data";
                    createAlert("No Internet Connection", failedMsgString);

                    return false;
                }

                CurrentWeatherResponse currWeather = (CurrentWeatherResponse) msg.getData().getSerializable("CURRENT_WEATHER");

                // Generate weather icon. Use https in URL
                Picasso.get().load("https://openweathermap.org/img/wn/" + currWeather.weather.get(0).icon + "@4x.png").into(currWeatherImage);

                currTempText.setText(String.valueOf(currWeather.main.temp + "\u00B0"));
                cityNameText.setText(currWeather.name);
                weatherDescText.setText(currWeather.weather.get(0).description);


                return false;
            }
        });


        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        /*
            Will make the API call each time the user goes back to the app
            when it is running in the background.
         */
        getCurrentWeather();
    }

    public void getCurrentWeather() {

        // Create request object
        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?zip=28223,us&appid=833110e347976d143c39463d4742e9fc&units=imperial")
                .build();

        // Initiate API call
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // API call has failed
                Message failMsg = new Message();
                Bundle bundle = new Bundle();

                bundle.putInt("API_RESPONSE_STATUS", 1);

                failMsg.setData(bundle);
                mHandler.sendMessage(failMsg);

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // API call is successful
                // Parse JSON data
                Gson gson = new Gson();
                CurrentWeatherResponse currWeather = gson.fromJson(response.body().charStream(), CurrentWeatherResponse.class);

                // Create message to send to the main thread
                Message weatherMsg = new Message();
                Bundle bundle = new Bundle();

                bundle.putSerializable("CURRENT_WEATHER", currWeather);
                bundle.putInt("API_RESPONSE_STATUS", 0);

                weatherMsg.setData(bundle);
                mHandler.sendMessage(weatherMsg);

            }
        });


    }


    public void createAlert(String title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IHome) {

        } else {
            throw new RuntimeException("Activity must implememnt IHome");
        }
    }

    public interface IHome {

    }


}