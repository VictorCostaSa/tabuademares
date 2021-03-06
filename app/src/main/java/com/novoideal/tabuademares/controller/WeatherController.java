package com.novoideal.tabuademares.controller;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.novoideal.tabuademares.R;
import com.novoideal.tabuademares.model.LocationParam;
import com.novoideal.tabuademares.model.Weather;
import com.novoideal.tabuademares.service.WeatherService;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by Helio on 14/08/2017.
 */

public class WeatherController {

    private String baseUrl = "https://api.weather.com/v2/turbo/vt1dailyforecast?apiKey=d522aa97197fd864d36b418f39ebb323&format=json&language=pt-BR&units=m";
    private String url = "https://api.weather.com/v2/turbo/vt1dailyforecast?apiKey=d522aa97197fd864d36b418f39ebb323&format=json&language=pt-BR&units=m&geocode=38.89%2C-77.03";
    private LocationParam city;
    public View rootView;

    public WeatherController(View view, LocationParam city) {
        this.rootView = view;
        this.city = city;
    }

    public void request() {
        this.city = city;
        url = baseUrl + "&geocode=" + city.getLatitude() + "%2C" + city.getLongetude();

        List<Weather> result = new WeatherService(this).geCondition(city);

        if (!result.isEmpty()) {
            populateView(result);
        }
    }

    public void populateView(List<Weather> result) {
        DateTime cityDate = new DateTime(city.getDate());

        for (Weather weather : result) {
            NumberFormat nf = new DecimalFormat("#.##");
            DateTime exDate = new DateTime(weather.getDate());
            if (exDate.getDayOfMonth() == cityDate.getDayOfMonth()) {
                if (weather.getType().equals("day")) {
                    ((TextView) rootView.findViewById(R.id.weather_narrative)).setText(getContext().getString(R.string.weather_narrative, weather.getNarrative()));
                    return;
                }
            }
        }

    }

    public LocationParam getCity() {
        return city;
    }


    public String getURL() {
        return url;
    }

    public Context getContext() {
        return rootView.getContext();
    }

    public void update() {
        new WeatherService(this).cleanCondiction(city);
    }
}