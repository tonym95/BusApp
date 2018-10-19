package com.antonio.busapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.antonio.busapplication.R;

import java.util.HashMap;

public class UserInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);
    }

    public void processUser(View view) {
        EditText username = (EditText) findViewById(R.id.usernameEditText);
        EditText password = (EditText) findViewById(R.id.userPassEditText);
        EditText adminRights = (EditText) findViewById(R.id.userAdminRightsEditText);

        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put("username", username.getText().toString());
        userData.put("password", password.getText().toString());
        userData.put("admin rights", adminRights.getText().toString());

        Log.i("From input class", username.getText().toString());

        Intent resultData = new Intent();

        resultData.putExtra("User data", userData);
        setResult(this.RESULT_OK, resultData);

        finish();
    }

}
