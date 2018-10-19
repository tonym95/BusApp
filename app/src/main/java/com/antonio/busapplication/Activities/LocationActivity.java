package com.antonio.busapplication.Activities;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.antonio.busapplication.Controller.LocationController;
import com.antonio.busapplication.Interfaces.ILocationView;
import com.antonio.busapplication.Model.AppRegistry;
import com.antonio.busapplication.Model.LocationService;
import com.antonio.busapplication.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;


public class LocationActivity extends AppCompatActivity implements ILocationView,
        GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private static final String[] BUSES = new String[]{"35", "46B", "Fany", "Alis", "Dacos", "Normandia", "Transmixt", "Cornul", "Cotofana"};
    private static final String[] STATION = new String[]{"Cluj Napoca", "Bucuresti", "Timisoara", "Iasi", "Sibiu", "Brasov"};

    private GoogleApiClient apiClient;
    private Button getLocationBtn;
    private AutoCompleteTextView busNameView;
    private AutoCompleteTextView stationTextView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private LocationController locationController = new LocationController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, BUSES);

        busNameView = (AutoCompleteTextView)
                findViewById(R.id.busesAutoCompleteTextView);
        busNameView.setAdapter(adapter);

        //If city bus
        // ArrayAdapter<String> adapterCityStation = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, CITY_STATIONS);
        //else
        ArrayAdapter<String> adapterStation = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, STATION);

        stationTextView = (AutoCompleteTextView)
                findViewById(R.id.stationAutoCompleteTextView);
        stationTextView.setAdapter(adapterStation);

        getLocationBtn = (Button) findViewById(R.id.addLocationBtn);

        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationController.getLocation();
            }
        });


       /* TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        uuid = tManager.getDeviceId();*/

       // dbRef = fbDB.getInstance().getReference();

       // dbRef.child("users").child(uuid).child("location").setValue(user.getLocation());


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        apiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        apiClient.connect();
    }

    public void getLocation(View view) {

        AutoCompleteTextView busName = (AutoCompleteTextView) findViewById(R.id.busesAutoCompleteTextView);
        //AppRegistry.getCurrentUser().setBus(new Bus(busName.getText().toString(), "local"));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 254);
        }

        apiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        apiClient.connect();

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        } else {




            Intent intent = new Intent(this, LocationService.class);
            intent.putExtra("Username", AppRegistry.getCurrentUser().getEmail());
        /*intent.putExtra("Password", AppRegistry.getCurrentUser().getPassword());
        intent.putExtra("Admin_rights", false);*/
            intent.putExtra("Bus_name", busName.getText().toString());
            intent.putExtra("Bus_type", "local");

            startService(intent);

            Toast.makeText(getApplicationContext(), "Location added !", Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    @Override
    public void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void startLocationService() {
        Intent intent = new Intent(this, LocationService.class);
        intent.putExtra("Username", AppRegistry.getCurrentUser().getEmail());

        intent.putExtra("Bus_name", busNameView.getText().toString());
        intent.putExtra("Bus_type", "local");

        startService(intent);


        Toast.makeText(getApplicationContext(), "Location added !", Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    public void showSuccessfulMessage() {
        Toast.makeText(getApplicationContext(), "Location changed !", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public String getBusName() {
        return this.busNameView.getText().toString();
    }


    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Location Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
       // stopService()

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onTrimMemory(int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            Log.i("App", "Closed");

        }
    }
}
