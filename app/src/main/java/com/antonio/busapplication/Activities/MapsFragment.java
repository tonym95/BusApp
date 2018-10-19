package com.antonio.busapplication.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.location.LocationListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.antonio.busapplication.Controller.MapController;
import com.antonio.busapplication.Interfaces.IMapFragmentView;
import com.antonio.busapplication.Model.AppRegistry;
import com.antonio.busapplication.Model.ApplicationEnvironment;
import com.antonio.busapplication.Model.Bus;
import com.antonio.busapplication.Model.CriteriaFirstProximity;
import com.antonio.busapplication.Model.MapDrawer;
import com.antonio.busapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapsFragment extends Fragment implements IMapFragmentView,
        OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener,
        OnMapClickListener {

    private MapController mapController = new MapController(this);
    private MapView mapView;
    private GoogleMap map;

    private Bus observedBus;
    private String busesToSearch = "";
    private List<String> routeInstructions;
    private Location location;

    private Boolean mapReady = false;
    private LocationManager locationManager;
    private FirebaseDatabase firebaseDB;
    private DatabaseReference dbRef;
    private String id;

    private HashMap<Marker, String> busMarkers;
    private MapOperationType operationType = MapOperationType.VIEW_BUS;

    private MapDrawer mapDrawer;

    public enum MapOperationType {
        VIEW_BUS, FIND_ROUTE
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(com.antonio.busapplication.R.layout.fragment_maps, container,
                false);
        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.onResume();// needed to get the map to display immediately

        mapController.addLocation();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        busMarkers = new HashMap<>();

        mapView.getMapAsync(this);



        firebaseDB = FirebaseDatabase.getInstance();
        dbRef = firebaseDB.getReference();
        // Perform any camera updates here
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        mapController.onMapReady(googleMap);

        map.setOnMarkerClickListener(this);
        map.setOnMapClickListener(this);

        Toast.makeText(ApplicationEnvironment.getContext(), "Showing buses under 500 meters radius", Toast.LENGTH_LONG).show();
    }


    @Override
    public void displayRouteInstructions() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
        builderSingle.setTitle("Map Instructions:");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.map_instructionsview,
                routeInstructions);//create adapter with your markers list

        builderSingle.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }
        );//to exit directly if you want

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });//set your adapter and listener
        builderSingle.show();
    }




    @Override
    public void onLocationChanged(Location location) {
        mapController.onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public MapOperationType getOperationType() {
        return this.operationType;
    }

    @Override
    public String getBusesToSearch() {
        return busesToSearch;
    }

    @Override
    public MapDrawer getMapDrawer() {
        return mapDrawer;
    }

    @Override
    public void setMapDrawer(MapDrawer mapDrawer) {
        this.mapDrawer = mapDrawer;
    }



    @Override
    public String getBusId() {
        return id;
    }

    @Override
    public HashMap<Marker, String> getBusMarkers() {
        return busMarkers;
    }

    @Override
    public void setRouteInstructions(List<String> routeInstructions) {
        this.routeInstructions = routeInstructions;
    }

    public void setOperationType(MapOperationType operationType) {
        this.operationType = operationType;
    }

    @Override
    public LocationManager getLocationManager() {
        return locationManager;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public void setMapReady(boolean b) {
        this.mapReady = b;
    }

    @Override
    public GoogleMap getMap() {
        return map;
    }

    @Override
    public void setMap(GoogleMap googleMap) {
        this.map = googleMap;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public void setLocationManager(LocationManager locationManager) {
        this.locationManager = locationManager;
    }


    public void setBusesToSearch(String busesToSearch) {
        this.busesToSearch = busesToSearch;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mapController.addBusObserver(marker);

        Log.i("Marker", "Clicked");
        id = busMarkers.get(marker);

        Toast.makeText(getActivity().getApplicationContext(), "Bus will be added to notification bar.", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onMapClick(LatLng point) {
        if(operationType == MapOperationType.FIND_ROUTE) {
            Log.i("DISPLAY", "ROUTE");
            map.clear();
            mapDrawer.stopDrawing();
            mapController.displayRoute(point);
        }
    }

}
