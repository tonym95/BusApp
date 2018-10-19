package com.antonio.busapplication.Model;

import android.Manifest;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.antonio.busapplication.Model.AppRegistry;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by Antonio on 4/14/2017.
 */

public class LocationService extends Service {

    private LocationManager locationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private ActivityRecognitionResult result;
    private FirebaseDatabase firebaseDB;
    private DatabaseReference dbRef;
    private FirebaseAuth auth;
    private String userId;

    private User user = new User();

    public class LocationListener implements android.location.LocationListener {

        public LocationListener(String provider) {
            AppRegistry.setCurrentUser(new User());
//            Log.i("User", user.getEmail());
            AppRegistry.getCurrentUser().setLocation(new Location(provider));

            //FirebaseApp.initializeApp(getApplicationContext());
            //auth = FirebaseAuth.getInstance();

            dbRef.child("Users").child(userId).setValue(AppRegistry.getCurrentUser());

        }

        @Override
        public void onLocationChanged(Location location) {
            AppRegistry.getCurrentUser().setLocation(location);


            /*
            DetectedActivity detectedActivity = result.getMostProbableActivity();

            Log.i("Detected Activity type", String.valueOf(detectedActivity.getType()));*/

            dbRef.child("Users").child(userId).setValue(AppRegistry.getCurrentUser());
           // dbRef.child("Users").child(userId).child("location").setValue(location);
           // dbRef.child("Users").child(userId).child("bus_name").setValue(user.getBus().getName());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {
            Query deletingQuery = FirebaseDatabase.getInstance().getReference("Users")
                    .orderByChild("email").equalTo(auth.getCurrentUser()
                            .getEmail());

            deletingQuery.addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    dataSnapshot.getRef().removeValue();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    LocationListener[] locationListeners;

    @Override
    public void onCreate() {
        initializeLocationManager();

        FirebaseApp.initializeApp(getApplicationContext());

        firebaseDB = FirebaseDatabase.getInstance();
        dbRef = firebaseDB.getReference();

        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();


       locationListeners = new LocationListener[]{
                new LocationListener(LocationManager.GPS_PROVIDER),
                new LocationListener(LocationManager.NETWORK_PROVIDER)
        };

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
               return;
            }
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListeners[1]);

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                locationListeners[0]);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(ActivityRecognitionResult.hasResult(intent))
            result = ActivityRecognitionResult.extractResult(intent);
        AppRegistry.setCurrentUser(new User(intent.getStringExtra("Username")));
        AppRegistry.getCurrentUser().setBusName(intent.getStringExtra("Bus_name"));
        if(intent.getStringExtra("Username").toString().equals("antomarc19@yahoo.com"))
            AppRegistry.getCurrentUser().setAdminRights(true);
        else
            AppRegistry.getCurrentUser().setAdminRights(false);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("Service", "Destroyed");

        Query deletingQuery = FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("email").equalTo(auth.getCurrentUser()
                        .getEmail());

        deletingQuery.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (locationManager != null) {
            for (int i = 0; i < locationListeners.length; i++) {
                try {
                   locationManager.removeUpdates(locationListeners[i]);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i("Service", "Stopped");

        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initializeLocationManager() {
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}
