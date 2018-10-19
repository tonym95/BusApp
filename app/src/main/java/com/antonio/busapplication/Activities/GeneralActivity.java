package com.antonio.busapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.antonio.busapplication.Controller.GeneralController;
import com.antonio.busapplication.Interfaces.IGeneralView;
import com.antonio.busapplication.R;

public class GeneralActivity extends AppCompatActivity implements IGeneralView {

    private GeneralController generalController = new GeneralController(this);
    private Button enterLocationButton;
    private Button checkBusesButton;
    private Button getHelpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        enterLocationButton = (Button) findViewById(R.id.enterLocationButton);
        checkBusesButton = (Button) findViewById(R.id.checkBusesButton);
        getHelpButton = (Button) findViewById(R.id.helpButton);

        enterLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generalController.enterLocation();
            }
        });

        checkBusesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generalController.checkBuses();
            }
        });

        getHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generalController.help();
            }
        });
    }

    public void showLocationView() {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }

    public void showNavigationView() {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }

    public void showHelpView() {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

}
