package com.assignment4.priti.knowyourgovernment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by  priti on 11/3/2018
 */
public class Locator {

    private static final String TAG = "Locator";
    private MainActivity mainActivity;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public Locator(MainActivity activity){
        mainActivity = activity;
         if(checkPermission()){
             setUpLocationManager();
             determineLocation();
         }
    }

    public void setUpLocationManager(){

        if(!checkPermission()){
            return;
        }

        locationManager =  (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mainActivity.doLocationWork(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }

            @Override
            public void onProviderEnabled(String provider) { }

            @Override
            public void onProviderDisabled(String provider) { }
        };

    }


    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 9090);
            Log.d(TAG, "LocationPermission: Waiting for permission");
            return false;
        } else {
            Log.d(TAG, "LocationPermission: Have permission");
            return true;
        }
    }

    public void determineLocation(){
        if (!checkPermission())
            return;

        if(locationManager == null){
            setUpLocationManager();
        }


        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location != null){
            Toast.makeText(mainActivity, "NETWORK_PROVIDER was chosen", Toast.LENGTH_SHORT).show();
            mainActivity.doLocationWork(location.getLatitude(), location.getLongitude());
            return;
        } else {
            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (location != null) {
                Toast.makeText(mainActivity, "PASSIVE_PROVIDER was chosen", Toast.LENGTH_SHORT).show();
                mainActivity.doLocationWork(location.getLatitude(), location.getLongitude());
                return;
            } else {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    Toast.makeText(mainActivity, "GPS_PROVIDER was chosen", Toast.LENGTH_SHORT).show();
                    mainActivity.doLocationWork(location.getLatitude(), location.getLongitude());
                    return;
                }
            }
        }


        mainActivity.noLocationAvailable();
        return;

    }

}
