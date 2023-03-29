package com.example.weatherapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class forecastData {
    private ArrayList<forecastItem> mForecastItems = new ArrayList<>();
    final String AppId = "f0aeccd9b7e2a1a9af2c2d6be83cbbde";
    final String Weather_URL = "https://api.openweathermap.org/data/2.5/weather";
    final String FORECAST_WEATHER_URL = "https://pro.openweathermap.org/data/2.5/forecast/hourly?";
    public static forecastData fromJson(JSONObject jsonObject) {
        forecastData forecastD = new forecastData();

        try {
            JSONArray items = jsonObject.getJSONArray("list");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                forecastItem forecastI = new forecastItem();
                forecastI.setDate(item.getLong("dt"));
                forecastI.setTemperature(item.getJSONObject("main").getDouble("temp"));
                forecastI.setWeatherId(item.getJSONArray("weather").getJSONObject(0).getInt("id"));
                forecastI.setWeatherIcon(item.getJSONArray("weather").getJSONObject(0).getString("icon"));
                forecastD.mForecastItems.add(forecastI);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return forecastD;
    }

    public ArrayList<forecastItem> getForecastItems() {
        return mForecastItems;
    }

    public static class forecastItem {
        private long mDate;
        private double mTemperature;
        private int mWeatherId;
        private String mWeatherIcon;

        public long getDate() {
            return mDate;
        }

        public void setDate(long date) {
            mDate = date;
        }

        public double getTemperature() {
            return mTemperature;
        }

        public void setTemperature(double temperature) {
            mTemperature = temperature;
        }

        public int getWeatherId() {
            return mWeatherId;
        }

        public void setWeatherId(int weatherId) {
            mWeatherId = weatherId;
        }

        public String getWeatherIcon() {
            return mWeatherIcon;
        }

        public void setWeatherIcon(String weatherIcon) {
            mWeatherIcon = weatherIcon;
        }
    }
}
