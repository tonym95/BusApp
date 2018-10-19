package com.antonio.busapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.antonio.busapplication.Controller.SelectBusController;
import com.antonio.busapplication.Interfaces.ISelectBusView;
import com.antonio.busapplication.R;

public class SelectBusActivity extends AppCompatActivity implements ISelectBusView {

    private EditText busView;
    private Button findBusButton;
    private SelectBusController selectBusController = new SelectBusController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bus);

        busView = (EditText) findViewById(R.id.findBusEditText);

        findBusButton = (Button) findViewById(R.id.findBusButton);

        findBusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBusController.findBus();
            }
        });
    }

    public void returnResult() {
        Intent resultData = new Intent();
        resultData.putExtra("Bus", busView.getText().toString());
        setResult(this.RESULT_OK, resultData);

        finish();
    }


}
