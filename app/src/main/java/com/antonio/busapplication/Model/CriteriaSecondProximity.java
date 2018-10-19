package com.antonio.busapplication.Model;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 5/27/2017.
 */

public class CriteriaSecondProximity implements Criteria{
    @Override
    public List<Bus> meetCriteria(List<Bus> buses, Location proximityLocation) {
        List<Bus> proximityBuses = new ArrayList<>();

        float results[] = new float[2];
        for(Bus bus : buses) {
            Location.distanceBetween(proximityLocation.getLatitude(), proximityLocation.getLongitude(),
                    bus.getCoords().latitude, bus.getCoords().longitude, results);
            if(results[0] > 500 && results[0] <= 1000);
            proximityBuses.add(bus);
        }
        return proximityBuses;
    }
}
