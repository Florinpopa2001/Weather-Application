package com.example.weatherapplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class weatherData {

    private String mTemperature,mIcon,mCity,mWeatherType,mFeels_like, mHumidity, mWindSpeed;
    private int mCondition;
//    long timestamp = System.currentTimeMillis();
//    Date currentDate = new Date(timestamp);
//    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    String formattedDate = formatter.format(currentDate);

    public static weatherData fromJson(JSONObject jsonObject){
        try{
            weatherData weatherD = new weatherData();
            weatherD.mCity = jsonObject.getString("name");
            weatherD.mCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.mWeatherType = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.mIcon = updateWeatherIcon(weatherD.mCondition);

            double tempResult = jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedValue = (int)Math.rint(tempResult);
            weatherD.mTemperature=Integer.toString(roundedValue);

            double mFeels_likeResult = jsonObject.getJSONObject("main").getDouble("feels_like") -273.15;
            int roundedValue1 = (int)Math.rint(mFeels_likeResult);
            weatherD.mFeels_like = Integer.toString(roundedValue1);

            int humidity = jsonObject.getJSONObject("main").getInt("humidity");
            weatherD.mHumidity = Integer.toString(humidity);

            double windSpeedM_S = jsonObject.getJSONObject("wind").getDouble("speed");
            double windSpeedKm_h1 = windSpeedM_S * 3.6;
            int windSpeedKm_h = (int)Math.rint(windSpeedKm_h1);
            weatherD.mWindSpeed = Double.toString(windSpeedKm_h);



            JSONObject forecast = jsonObject.getJSONObject("list");


            return weatherD;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String updateWeatherIcon(int condition){
        if (condition >= 0 && condition <=300){
            return "thunderstorm1";
        }
        else if (condition > 300 && condition <=500){
            return "lightrain";
        }
        else if (condition > 500 && condition <=600){
            return "shower";
        }
        else if (condition > 600 && condition <=700){
            return "snow2";
        }

        else if (condition > 700 && condition <=771){
            return "fog";
        }
        else if (condition > 771 && condition <800){
            return "overcast";
        }
        else if (condition == 800 ){
            return "sunny";
        }
        else if(condition >= 801 && condition <= 804 ){
            return "cloudy";
        }
        else if (condition >= 900 && condition <= 902 ){
            return "thunderstorm1";
        }
        else if(condition == 903 ){
            return "snow1";
        }
        else if(condition == 904){
            return "sunny";
        }
        if (condition >= 905 && condition <=1000){
            return "thunderstorm2";
        }

        return "dunno";

    }

    public String getmTemperature() {
        return mTemperature + "°C";
    }

    public String getmFeels_like() {
        return mFeels_like + "°C";
    }

    public String getmHumidity() { return mHumidity + "%";}

    public String getmWindSpeed() { return mWindSpeed + "km/h";}

    public String getmIcon() {
        return mIcon;
    }

    public String getmCity() {
        return mCity;
    }

    public String getmWeatherType() {
        return mWeatherType;
    }
}
