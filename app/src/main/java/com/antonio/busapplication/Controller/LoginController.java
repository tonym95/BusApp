package com.antonio.busapplication.Controller;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.antonio.busapplication.Activities.AdminActivity;
import com.antonio.busapplication.Activities.GeneralActivity;
import com.antonio.busapplication.Activities.MainActivity;
import com.antonio.busapplication.Interfaces.ILoginView;
import com.antonio.busapplication.Model.AppRegistry;
import com.antonio.busapplication.Model.ApplicationEnvironment;
import com.antonio.busapplication.Model.Bus;
import com.antonio.busapplication.Model.Route;
import com.antonio.busapplication.Model.User;
import com.antonio.busapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by Antonio on 5/27/2017.
 */

public class LoginController {

    private ILoginView loginView;
    private FirebaseAuth mAuth;
    private List<User> users;
    private List<Bus> buses;
    private boolean fromAssets = true;

    public LoginController(ILoginView loginView) {
        this.loginView = loginView;
    }

    public void loginWithoutAccount() {
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInAnonymously()
                .addOnCompleteListener((Activity) loginView, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginView.showUserView();
                        } else {
                            loginView.showErrorMessage();
                        }
                    }
                });
    }

    public void login() {
        mAuth = FirebaseAuth.getInstance();

        users = new ArrayList<>();
        buses = new ArrayList<>();


        try {
            if(fromAssets) {
                users = User.readXmlFile(ApplicationEnvironment.getContext(), fromAssets);
                buses = Bus.readXmlFile(ApplicationEnvironment.getContext(), fromAssets);
            } else {
                users.add(new User("admin", "admin", true));
                users.add(new User("antonio", "pass123", false));
                buses.add(new Bus("35", "local", new Route((long) 1)));
            }
        } catch(IOException e) {
            e.printStackTrace();
        } catch(XmlPullParserException e) {
            e.printStackTrace();
        }

        try {
            User.writeXml(ApplicationEnvironment.getContext(), users);
            Bus.writeXml(ApplicationEnvironment.getContext(), buses);
        } catch(IOException e) {
            e.printStackTrace();
        }


        for(Bus b : buses) {
            AppRegistry.addBus(b);
        }

        for(User u : users) {
            AppRegistry.addUser(u);
        }

        String email = loginView.getEmail();
        String password = loginView.getPassword();


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) loginView, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //  Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                            AppRegistry.setCurrentUser(new User(user.getEmail()));

                            if (user.getEmail().equals("antomarc19@yahoo.com")) {
                                loginView.showAdminView();
                            } else {
                                loginView.showUserView();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            loginView.showErrorMessage();
                        }
                    }
                });
    }

    private boolean validateLinkForm() {
        String email = loginView.getEmail();
        String pass = loginView.getPassword();
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            loginView.getEmailView().setError("Required.");
            valid = false;
        } else {
            loginView.getEmailView().setError(null);
        }


        if (TextUtils.isEmpty(pass)) {
            loginView.getPasswordView().setError("Required.");
            valid = false;
        } else {
            loginView.getPasswordView().setError(null);
        }

        return valid;
    }

    public void linkAccount() {

        mAuth = FirebaseAuth.getInstance();

        // Make sure form is valid
        if (!validateLinkForm()) {
            return;
        }

        // Get email and password from form
        String email = loginView.getEmail();
        String password = loginView.getPassword();

        // Create EmailAuthCredential with email and password
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);


        // [START link_credential]
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener((Activity) loginView, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            AppRegistry.setCurrentUser(new User(user.getEmail()));
                            loginView.showUserView();
                        } else {
                            loginView.showErrorMessage();
                        }

                    }
                });
    }

    public void registerAccount() {
        mAuth = FirebaseAuth.getInstance();

        String email = loginView.getEmail();
        String pass = loginView.getPassword();

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener((Activity) loginView, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginView.showUserCreatedMessage();

                        } else {
                            loginView.showErrorMessage();
                        }

                    }
                });

    }
}
