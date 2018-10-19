package com.antonio.busapplication.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.antonio.busapplication.Controller.LoginController;
import com.antonio.busapplication.Interfaces.ILoginView;
import com.antonio.busapplication.Model.AppRegistry;
import com.antonio.busapplication.Model.Bus;
import com.antonio.busapplication.Model.LocationService;
import com.antonio.busapplication.R;
import com.antonio.busapplication.Model.Route;
import com.antonio.busapplication.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ILoginView {

    private LoginController loginController = new LoginController(this);
    private EditText emailView;
    private EditText passView;
    private Button registerButton;
    private Button loginButton;
    private Button enterWithoutAccountButton;
    private Button linkAccountButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailView = (EditText) findViewById(R.id.editTextName);
        passView = (EditText) findViewById(R.id.editTextPassword);

        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);
        enterWithoutAccountButton = (Button) findViewById(R.id.enterWithoutAccountButton);
        linkAccountButton = (Button) findViewById(R.id.linkButton);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                loginController.login();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                loginController.registerAccount();
            }
        });

        enterWithoutAccountButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                loginController.loginWithoutAccount();
            }
        });

        linkAccountButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                loginController.linkAccount();
            }
        });

        FirebaseApp.initializeApp(this);
    }


    @Override
    public void onPause() {
        super.onPause();

        if(isFinishing()) {
            Intent intent = new Intent(this, LocationService.class);
            stopService(intent);
        }

    }

    @Override
    public void showUserView() {
        Intent intent = new Intent(MainActivity.this, GeneralActivity.class);
        startActivity(intent);
    }

    @Override
    public void showAdminView() {
        Intent intent = new Intent(MainActivity.this, AdminActivity.class);
        startActivity(intent);
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(MainActivity.this, "Authentication failed.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getEmail() {
        return emailView.getText().toString();
    }

    @Override
    public String getPassword() {
        return passView.getText().toString();
    }

    @Override
    public TextView getEmailView() {
        return this.emailView;
    }

    @Override
    public TextView getPasswordView() {
        return this.passView;
    }

    @Override
    public void showUserCreatedMessage() {
        Toast.makeText(MainActivity.this, "User created !", Toast.LENGTH_SHORT).show();
    }


}
