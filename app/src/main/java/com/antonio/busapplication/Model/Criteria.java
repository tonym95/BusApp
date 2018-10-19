package com.antonio.busapplication.Model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Antonio on 5/26/2017.
 */

public interface Criteria {

    public List<Bus> meetCriteria(List<Bus> buses, Location proximityLocation);
}
