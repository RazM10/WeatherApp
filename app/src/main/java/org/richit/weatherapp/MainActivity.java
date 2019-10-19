package org.richit.weatherapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    String LOCATION_PROVIDERS = LocationManager.NETWORK_PROVIDER;
    LocationManager locationManager;
    LocationListener locationListener;
    final int REQUEST_CODE = 123;
    int MIN_TIME = 5000;
    float MIN_DISTANCE = 1000;

    String APP_ID="bd8a9ac5b7a888e5043810688582f61f";
    final String WEATHER_URL="http://api.openweathermap.org/data/2.5/weather";

    TextView temperature_tv;
    ImageView weatherIcon_img;
    TextView locationName_tv;
    ImageButton changeCity_img_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperature_tv=findViewById(R.id.temperature_tv);
        weatherIcon_img=findViewById(R.id.weatherIcon_img);
        locationName_tv=findViewById(R.id.locationName_tv);
        changeCity_img_btn=findViewById(R.id.changeCity_img_btn);

        changeCity_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ChangeCityController.class);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("WeatherApp", "onResume() Called");
        Log.d("WeatherApp", "Getting Wether for current Location");

        Intent intent=getIntent();
        String city=intent.getStringExtra("City");

        if(city!=null){
            Log.d("WeatherApp","Typed "+city);
        }
        else {
            getWeatherForCurrentLocation();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getWeatherForCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("WeatherApp", "onLocationChanged(): callback received");

                String longitude=String.valueOf(location.getLongitude());
                String latitude=String.valueOf(location.getLatitude());

                Log.d("WeatherApp", "longitude: "+longitude);
                Log.d("WeatherApp", "latitude: "+latitude);

                RequestParams params=new RequestParams();
                params.put("lat",latitude);
                params.put("lon",longitude);
                params.put("appid",APP_ID);
                getWeather(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("WeatherApp", "onPermissionDisable(): callback received");
            }
        };

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

            return;
        }
        locationManager.requestLocationUpdates(LOCATION_PROVIDERS, MIN_TIME, MIN_DISTANCE, locationListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==REQUEST_CODE){
            if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Log.d("WeatherApp","onRequestPR(): granted");
                getWeatherForCurrentLocation();
            }
        }
    }

    private void getWeather(RequestParams params){
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(WEATHER_URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.d("WeatherApp","onSuccess(): Json: "+response.toString());

                WeatherDataModel weatherDataModel=WeatherDataModel.fromJson(response);

                updateUI(weatherDataModel);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.d("WeatherApp","onFailure(): Fail");
            }
        });
    }

    private void updateUI(WeatherDataModel weatherDataModel){
        temperature_tv.setText(weatherDataModel.getTemperature());

        int resourceID=getResources().getIdentifier(weatherDataModel.getIconName(),"drawable",getPackageName());
        weatherIcon_img.setImageResource(resourceID);
        locationName_tv.setText(weatherDataModel.getCity());
    }
}
