package com.antonio.busapplication.Model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 5/26/2017.
 */

public class CriteriaFirstProximity implements Criteria {

    @Override
    public List<Bus> meetCriteria(List<Bus> buses, Location proximityLocation) {
        List<Bus> proximityBuses = new ArrayList<>();

        float results[] = new float[2];
        for(Bus bus : buses) {
            Location.distanceBetween(proximityLocation.getLatitude(), proximityLocation.getLongitude(),
                    bus.getCoords().latitude, bus.getCoords().longitude, results);
            if(results[0] <= 500);
                proximityBuses.add(bus);
        }
        return proximityBuses;
    }
}
