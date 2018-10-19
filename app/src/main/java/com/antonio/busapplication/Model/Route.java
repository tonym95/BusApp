package com.antonio.busapplication.Model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 4/7/2017.
 */

public class Route {
    private List<LatLng> polyLine;
    private Long id;
    private List<Station> routeStations;

    public Route(Long routeId, List<Station> routeStations) {
        this.id = routeId;
        this.routeStations = new ArrayList<Station>(routeStations);
    }

    public Route(Long routeId) {
        this.id = routeId;
    }

    public List<Station> getRouteStations() {
        return routeStations;
    }

    public void setRouteStations(List<Station> routeStations) {
        this.routeStations = routeStations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
