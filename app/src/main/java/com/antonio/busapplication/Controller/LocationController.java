package com.antonio.busapplication.Controller;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.antonio.busapplication.Activities.LocationActivity;
import com.antonio.busapplication.Interfaces.ILocationView;
import com.antonio.busapplication.Model.AppRegistry;
import com.antonio.busapplication.Model.ApplicationEnvironment;
import com.antonio.busapplication.Model.LocationService;

/**
 * Created by Antonio on 5/27/2017.
 */

public class LocationController {

    private ILocationView locationView;

    public LocationController(ILocationView locationActivity) {
        this.locationView = locationActivity;
    }


    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(ApplicationEnvironment.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(ApplicationEnvironment.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)locationView, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 254);
        }

        final LocationManager manager = (LocationManager) ApplicationEnvironment.getContext().getSystemService(Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            locationView.buildAlertMessageNoGps();
        } else {
            if(!isMyServiceRunning(LocationService.class))
            locationView.startLocationService();
            else {
                AppRegistry.getCurrentUser().setBusName(locationView.getBusName());
                locationView.showSuccessfulMessage();
            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) ApplicationEnvironment.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
