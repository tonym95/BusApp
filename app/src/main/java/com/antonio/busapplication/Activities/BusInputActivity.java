package com.antonio.busapplication.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.antonio.busapplication.R;

import java.util.HashMap;

public class BusInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_input);
    }

    public void processBus(View view) {
        EditText busName = (EditText) findViewById(R.id.busNameInputEditText);
        EditText busType = (EditText) findViewById(R.id.busTypeInputEditText);
        EditText routeId = (EditText) findViewById(R.id.busRouteInputEditText);

        HashMap<String, String> busData = new HashMap<String, String>();

        busData.put("bus name", busName.getText().toString());
        busData.put("bus type", busType.getText().toString());
        busData.put("route id", routeId.getText().toString());

       // Log.i("From input class", username.getText().toString());

        Intent resultData = new Intent();

        resultData.putExtra("Bus data", busData);
        setResult(this.RESULT_OK, resultData);

        finish();
    }
}
