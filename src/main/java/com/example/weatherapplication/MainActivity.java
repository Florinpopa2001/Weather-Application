package com.example.weatherapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    final String AppId = "f0aeccd9b7e2a1a9af2c2d6be83cbbde";
    final String Weather_URL = "https://api.openweathermap.org/data/2.5/weather";
    final String FORECAST_WEATHER_URL = "https://pro.openweathermap.org/data/2.5/forecast/hourly?";

    final long minTime = 5000;
    final float minDistance = 1000;
    final int REQUEST_CODE = 101;

    String LocationProvider = LocationManager.GPS_PROVIDER;

    TextView nameCity, weatherState, Temperature,mHumidity,mWindspeed,mFeels_like;
    ImageView mweatherIcon;

    RelativeLayout mcityFinder;

    LocationManager mlocationManager;
    LocationListener mlocationListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {





        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameCity = findViewById(R.id.cityName);
        weatherState = findViewById(R.id.weatherCondition);
        Temperature = findViewById(R.id.temperature);
        mweatherIcon = findViewById(R.id.weatherIcon);
        mcityFinder = findViewById(R.id.cityFinder);
        mHumidity = findViewById(R.id.humidity);
        mWindspeed = findViewById(R.id.windspeed);
        mFeels_like = findViewById(R.id.feels_like);

        mcityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, cityFinder.class);
                startActivity(intent);
            }
        });
    }

    /* Current LOCATION
    @Override
    protected void onResume() {
        super.onResume();
        getWeatherForCurrentLocation();
    } Current LOCATION
    */


    @Override
    protected void onResume() {
        super.onResume();
        Intent mIntent = getIntent();
        String city = mIntent.getStringExtra("City");
        if(city != null){
            getWeatherForNewCity(city);
        }
        else
        {
            getWeatherForCurrentLocation();
        }


    }

    private void getWeatherForNewCity(String city){
        RequestParams params = new RequestParams();
        params.put("q",city);
        params.put("appid",AppId);
        letsdoSomeNetworking(params);

    }

    private void getForecastWeatherData(String city) {
        RequestParams params = new RequestParams();
        params.put("q", city);
        params.put("appid", AppId);
        params.put("units", "metric");

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(FORECAST_WEATHER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(MainActivity.this, "Forecast data get success", Toast.LENGTH_SHORT).show();
                forecastData forecastD = forecastData.fromJson(response);
                updateFR(forecastD);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(MainActivity.this, "Failed to get forecast data", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void getWeatherForCurrentLocation() {
        mlocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());

                RequestParams params = new RequestParams();
                params.put("lat",latitude);
                params.put("lon",longitude);
                params.put("appid",AppId);
                letsdoSomeNetworking(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                //not able to get location
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        mlocationManager.requestLocationUpdates(LocationProvider, minTime, minDistance, mlocationListener);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this,"Location get Succesffully",Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            }
            else
            {
                //user denied the permission
            }
        }

    }

    private void letsdoSomeNetworking(RequestParams params){

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Weather_URL,params,new JsonHttpResponseHandler()
        {
            @Override
            //Dacă cererea este finalizată cu succes, metoda onSuccess este apelată,
            // care convertește răspunsul JSON primit de la server într-un obiect de tip weatherData,
            // utilizând metoda statică fromJson()
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Toast.makeText(MainActivity.this, "Data Get Succes", Toast.LENGTH_SHORT).show();

                weatherData weatherD = weatherData.fromJson(response);
                updateUI(weatherD);

                //super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                Toast.makeText(MainActivity.this, "Current Weather Data Get Failure", Toast.LENGTH_SHORT).show();
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }

    private void updateUI(weatherData weather){
        Temperature.setText(weather.getmTemperature());
        mFeels_like.setText(weather.getmFeels_like());
        mHumidity.setText(weather.getmHumidity());
        mWindspeed.setText(weather.getmWindSpeed());
        nameCity.setText(weather.getmCity());
        weatherState.setText(weather.getmWeatherType());
        int resourceID = getResources().getIdentifier(weather.getmIcon(),"drawable",getPackageName());
        mweatherIcon.setImageResource(resourceID);
    }

    private void updateFR(forecastData forecast) {

    }




    @Override
    protected void onPause() {
        super.onPause();
        if (mlocationManager != null){
            mlocationManager.removeUpdates(mlocationListener);
        }
    }
}