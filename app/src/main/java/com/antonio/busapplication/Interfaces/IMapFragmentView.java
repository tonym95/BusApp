package com.antonio.busapplication.Interfaces;

import android.location.Location;
import android.location.LocationManager;

import com.antonio.busapplication.Activities.MapsFragment;
import com.antonio.busapplication.Model.MapDrawer;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Antonio on 5/27/2017.
 */

public interface IMapFragmentView {
    void setOperationType(MapsFragment.MapOperationType viewBus);

    LocationManager getLocationManager();

    void setLocation(Location location);
    void setLocationManager(LocationManager locationManager);

    Location getLocation();

    void setMap(GoogleMap googleMap);

    void setMapReady(boolean b);

    GoogleMap getMap();

    MapsFragment.MapOperationType getOperationType();

    String getBusesToSearch();

    MapDrawer getMapDrawer();

    String getBusId();

    HashMap<Marker, String> getBusMarkers();

    void setRouteInstructions(List<String> routeInstructions);

    void displayRouteInstructions();

    void setMapDrawer(MapDrawer mapDrawer);
}
