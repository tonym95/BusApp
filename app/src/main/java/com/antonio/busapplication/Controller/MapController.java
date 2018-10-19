package com.antonio.busapplication.Controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.antonio.busapplication.Activities.MapsFragment;
import com.antonio.busapplication.Interfaces.IMapFragmentView;
import com.antonio.busapplication.Model.AndCriteria;
import com.antonio.busapplication.Model.AppRegistry;
import com.antonio.busapplication.Model.ApplicationEnvironment;
import com.antonio.busapplication.Model.Bus;
import com.antonio.busapplication.Model.CriteriaFirstProximity;
import com.antonio.busapplication.Model.CriteriaSecondProximity;
import com.antonio.busapplication.Model.MapDrawer;
import com.antonio.busapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.antonio.busapplication.Model.ApplicationEnvironment.getContext;

/**
 * Created by Antonio on 5/27/2017.
 */

public class MapController {

    private IMapFragmentView mapFragmentView;
    private String provider;
    private Handler handler;
    private LatLng busCoords;
    private String busName;
    private Bus observedBus;

    public MapController(IMapFragmentView mapsFragment) {
        this.mapFragmentView = mapsFragment;
    }


    public void addLocation() {
        mapFragmentView.setLocationManager((LocationManager) ApplicationEnvironment.getContext().getSystemService(Context.LOCATION_SERVICE));
        provider = mapFragmentView.getLocationManager().getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mapFragmentView, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 255);
        }

        mapFragmentView.setLocation(mapFragmentView.getLocationManager().getLastKnownLocation(provider));
    }

    public void onMapReady(GoogleMap googleMap) {
        mapFragmentView.setMapReady(true);
        mapFragmentView.setMap(googleMap);

        Log.i("Location", "works");


        if (mapFragmentView.getLocation() != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(mapFragmentView.getLocation().getLatitude(), mapFragmentView.getLocation().getLongitude()))
                    .zoom(12).build();
            mapFragmentView.getMap().animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            handler = new Handler();

            Runnable mapUpdateRunnable = new Runnable() {

                @Override
                public void run() {
                    if (mapFragmentView.getOperationType() == MapsFragment.MapOperationType.VIEW_BUS) {
                        drawUserLocation();
                        drawBusMarkers();
                    } else if (mapFragmentView.getOperationType() == MapsFragment.MapOperationType.FIND_ROUTE) {
                    }
                    handler.postDelayed(this, 1000);
                }
            };

            handler.postDelayed(mapUpdateRunnable, 1000);

            mapFragmentView.setMapDrawer(new MapDrawer(mapFragmentView.getMap()));

        }
    }

    public void drawUserLocation() {
        if (AppRegistry.getCurrentUser().getLocation() != null) {
            Log.i("User location", "works");
            double latitude = AppRegistry.getCurrentUser().getLocation().getLatitude();
            double longitude = AppRegistry.getCurrentUser().getLocation().getLongitude();

            MarkerOptions marker = new MarkerOptions().position(
                    new LatLng(latitude, longitude)).title("Current user");

            // Changing marker icon
            marker.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mapFragmentView.getMap().addMarker(marker);
        }
    }

    public void drawBusMarkers() {
        if(!mapFragmentView.getBusesToSearch().isEmpty()) {


            mapFragmentView.getMap().clear();
            mapFragmentView.getMapDrawer().stopDrawing();
            Query busQuery = FirebaseDatabase.getInstance().getReference("Users").orderByChild("busName").equalTo(mapFragmentView.getBusesToSearch());




            ValueEventListener userListener = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        List<Bus> currentBuses = new ArrayList<Bus>();
                        for (DataSnapshot buses : dataSnapshot.getChildren()) {
                            busCoords = new LatLng(Double.parseDouble(buses.child("location").child("latitude").
                                    getValue().toString()),
                                    Double.parseDouble(buses.child("location").child("longitude").getValue().toString()));
                            busName = buses.child("busName").getValue().toString();

                            // currentBuses.add(new Bus(buses.getKey(), busName, busCoords));

                            if(observedBus != null) {
                                if(mapFragmentView.getBusId().equals(buses.getKey())) {
                                    Log.i("Id", "Equal");
                                    observedBus.setCoords(busCoords);
                                    observedBus.notifyObservers(ApplicationEnvironment.getContext());
                                }
                            }



                            MarkerOptions marker = new MarkerOptions().position(
                                    busCoords).title(busName);



                            // marker.
                            // Changing marker icon
                            marker.icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));


                            mapFragmentView.getBusMarkers().put(mapFragmentView.getMap().addMarker(marker), buses.getKey());
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            busQuery.addValueEventListener(userListener);


        } else {
            mapFragmentView.getMap().clear();
            Query busQuery = FirebaseDatabase.getInstance().getReference("Users");

            mapFragmentView.getMapDrawer().drawCircle(new LatLng(mapFragmentView.getLocation().getLatitude(),
                    mapFragmentView.getLocation().getLongitude()));

            ValueEventListener valueEventListener = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        List<Bus> currentBuses = new ArrayList<>();
                        for (DataSnapshot buses : dataSnapshot.getChildren()) {
                            busCoords = new LatLng(Double.parseDouble(buses.child("location").child("latitude").
                                    getValue().toString()),
                                    Double.parseDouble(buses.child("location").child("longitude").getValue().toString()));
                            busName = String.valueOf(buses.child("busName").getValue());

                            currentBuses.add(new Bus(busName, busCoords));

                        }

                        com.antonio.busapplication.Model.Criteria busFilterCriteria = new CriteriaFirstProximity();
                        com.antonio.busapplication.Model.Criteria busFilterSecondCriteria = new CriteriaSecondProximity();
                        com.antonio.busapplication.Model.Criteria busFilterAndCriteria = new AndCriteria(busFilterCriteria,
                                busFilterSecondCriteria);

                        List<Bus> proximityBuses = busFilterAndCriteria.meetCriteria(currentBuses, mapFragmentView.getLocation());

                        for(Bus b : proximityBuses) {

                            MarkerOptions marker = new MarkerOptions().position(
                                    b.getCoords()).title(b.getName());


                            // marker.
                            // Changing marker icon
                            marker.icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                            mapFragmentView.getMap().addMarker(marker);

                        }


                        // busMarkers.put(map.addMarker(marker), buses.getKey());

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            busQuery.addValueEventListener(valueEventListener);

        }

    }

    @SuppressWarnings("deprecations")
    public void displayRoute(LatLng latLng) {

                LatLng destinationLatLng = latLng;

                AsyncHttpClient client = new AsyncHttpClient();

                client.get("https://maps.googleapis.com/maps/api/directions/json?origin=" + mapFragmentView.getLocation().getLatitude()
                                + "," + mapFragmentView.getLocation().getLongitude() +
                                "&destination=" + destinationLatLng.latitude + "," +
                                destinationLatLng.longitude +
                                "&key=AIzaSyCvvKmuatz8jBsqoFHT8jmFvxua89zg0OM",
                        new JsonHttpResponseHandler() {

                            public double lat;
                            public double lng;

                            @Override
                            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, org.json.JSONObject response) {
                                Log.i("Status code Object:", String.valueOf(statusCode));


                                List<List<LatLng>> routes = new ArrayList<List<LatLng>>() ;
                                List<String> routeInstructions = new ArrayList<>();
                                JSONArray jRoutes = null;
                                JSONArray jLegs = null;
                                JSONArray jSteps = null;
                                JSONObject jOverviewPolyline = null;

                                Log.i("RESPONSE", response.toString());
                                try {

                                    jRoutes = response.getJSONArray("routes");


                                    Log.i("JROUTES", jRoutes.toString());


                                    /** Traversing all routes */
                                    for(int i = 0; i < jRoutes.length(); i++){
                                        jLegs =  jRoutes.getJSONObject(i).getJSONArray("legs");

                                        jOverviewPolyline = jRoutes.getJSONObject(i).getJSONObject("overview_polyline");

                                        String overviewPolyline = jOverviewPolyline.getString("points");

                                        List<LatLng> list = PolyUtil.decode(overviewPolyline);

                                        routes.add(list);

                                        Log.i("Works", "!!!");
                                        //List path = new ArrayList<HashMap<String, String>>();

                                        /** Traversing all legs */
                                        for(int j=0;j<jLegs.length();j++){
                                            jSteps = jLegs.getJSONObject(j).getJSONArray("steps");

                                            /** Traversing all steps */
                                            for(int k=0;k<jSteps.length();k++) {
                                                String html_instructions = fromHtml(jSteps.getJSONObject(k).getString("html_instructions"));

                                                routeInstructions.add(html_instructions);

                                                String travel_mode = jSteps.getJSONObject(k).getString("travel_mode");

                                                String distance_text = jSteps.getJSONObject(k).getJSONObject("distance").getString("text");
                                                String distance_value = jSteps.getJSONObject(k).getJSONObject("distance").getString("value");

                                                String duration_text = jSteps.getJSONObject(k).getJSONObject("duration").getString("text");
                                                String duration_value = jSteps.getJSONObject(k).getJSONObject("duration").getString("value");

                                                String start_lat = jSteps.getJSONObject(k).getJSONObject("start_location").getString("lat");
                                                String start_lon = jSteps.getJSONObject(k).getJSONObject("start_location").getString("lng");

                                                String end_lat = jSteps.getJSONObject(k).getJSONObject("end_location").getString("lat");
                                                String end_lon = jSteps.getJSONObject(k).getJSONObject("end_location").getString("lng");

                                                String polyline = "";
                                                polyline = jSteps.getJSONObject(k).getJSONObject("polyline").getString("points");

                                                Log.i("POLYLINE", polyline);

                                                //List<LatLng> list = decodePoly(polyline);

                                                // List<LatLng> list = PolyUtil.decode(polyline);

                                                /** Traversing all points */
                                            }
                                        }
                                    }

                                    PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                                    for (int z = 0; z < routes.size(); z++) {
                                        options.addAll(routes.get(z));
                                    }

                                    mapFragmentView.setRouteInstructions(routeInstructions);
                                    mapFragmentView.displayRouteInstructions();

                                    mapFragmentView.getMap().addPolyline(options);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, org.json.JSONArray response) {
                                Log.i("Status code Array:", String.valueOf(statusCode));
                            }
                        });
    }

    @SuppressWarnings("deprecation")
    public static String fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(source).toString();
        }
    }

    public void onLocationChanged(Location location) {
        AppRegistry.getCurrentUser().setLocation(location);
    }

    public void addBusObserver(Marker marker) {
        observedBus = new Bus(mapFragmentView.getBusesToSearch(), marker.getPosition());
        observedBus.addObserver(AppRegistry.getCurrentUser());
    }

}
