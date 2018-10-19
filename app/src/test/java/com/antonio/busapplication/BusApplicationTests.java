package com.antonio.busapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.antonio.busapplication.Activities.AdminActivity;
import com.antonio.busapplication.Activities.GeneralActivity;
import com.antonio.busapplication.Activities.LocationActivity;
import com.antonio.busapplication.Activities.MainActivity;
import com.antonio.busapplication.Activities.NavigationActivity;
import com.antonio.busapplication.Activities.UserInfoFragment;
import com.antonio.busapplication.Activities.UserInputActivity;
import com.antonio.busapplication.Model.AppRegistry;
import com.antonio.busapplication.Model.ApplicationEnvironment;
import com.antonio.busapplication.Model.LocationService;
import com.antonio.busapplication.Model.User;
import com.google.android.gms.maps.model.LatLng;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowActivity.IntentForResult;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.shadows.ShadowLocationManager;
import org.robolectric.shadows.ShadowToast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Antonio on 4/24/2017.
 */


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class BusApplicationTests  {

    LatLng busCoords;


    @Test
    public void givenWrongUsernameAndPassword_login_showErrorMessage() {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFromAssets(false);
        EditText username = (EditText) mainActivity.findViewById(R.id.editTextName);
        EditText password = (EditText) mainActivity.findViewById(R.id.editTextPassword);
        Button loginButton = (Button) mainActivity.findViewById(R.id.loginButton);
        username.setText("NotAUsername");
        password.setText("NotAPassword");

        loginButton.performClick();

        Assert.assertEquals("Login Error !", ShadowToast.getTextOfLatestToast());

    }
/*
    @Test
    public void givenAdminNameAndPassword_login_showAdminWindow() {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.initFirebase();
        mainActivity.setFromAssets(false);
        EditText username = (EditText) mainActivity.findViewById(R.id.editTextName);
        EditText password = (EditText) mainActivity.findViewById(R.id.editTextPassword);
        Button loginButton = (Button) mainActivity.findViewById(R.id.button);
        username.setText("antomarc19@yahoo.com");
        password.setText("pass123");

        loginButton.performClick();

        ShadowActivity shadowActivity = Shadows.shadowOf(mainActivity);
        Intent intent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(intent);

        Assert.assertEquals(AdminActivity.class, shadowIntent.getIntentClass());
    }

    @Test
    public void givenUserNameAndPassword_login_showUserWindow() {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.initFirebase();
        mainActivity.setFromAssets(false);
        EditText username = (EditText) mainActivity.findViewById(R.id.editTextName);
        EditText password = (EditText) mainActivity.findViewById(R.id.editTextPassword);
        Button loginButton = (Button) mainActivity.findViewById(R.id.button);
        username.setText("antomarc20@gmail.com");
        password.setText("pass123");

        loginButton.performClick();

        ShadowActivity shadowActivity = Shadows.shadowOf(mainActivity);
        Intent intent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(intent);

        Assert.assertEquals(GeneralActivity.class, shadowIntent.getIntentClass());
    }*/

    @Test
    public void usingAdminAccount_addNewUser_showUserInputWindow() {
        AdminActivity adminActivity = Robolectric.setupActivity(AdminActivity.class);

       // MenuItem addUserButton = (MenuItem) adminActivity.findViewById(R.id.add_user);

        MenuItem addUserItem = new RoboMenuItem(R.id.add_user);

        adminActivity.onNavigationItemSelected(addUserItem);

        ShadowActivity shadowActivity = Shadows.shadowOf(adminActivity);
        IntentForResult intentForResult = shadowActivity.getNextStartedActivityForResult();
        ShadowIntent intent = Shadows.shadowOf(intentForResult.intent);

        Assert.assertEquals(UserInputActivity.class, intent.getIntentClass());

    }

    @Test
    public void usingAdminAccount_addNewUser_getInputFromUserWindow() {
        UserInputActivity userInputActivity = Robolectric.setupActivity(UserInputActivity.class);

        EditText username = (EditText) userInputActivity.findViewById(R.id.usernameEditText);
        EditText password = (EditText) userInputActivity.findViewById(R.id.userPassEditText);
        EditText adminRights = (EditText) userInputActivity.findViewById(R.id.userAdminRightsEditText);
        Button processUserButton = (Button) userInputActivity.findViewById(R.id.button7);

        username.setText("NewUser");
        password.setText("NewPassword");
        adminRights.setText("false");

        processUserButton.performClick();

        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put("username", username.getText().toString());
        userData.put("password", password.getText().toString());
        userData.put("admin rights", adminRights.getText().toString());

        Intent resultData = new Intent();

        resultData.putExtra("User data", userData);

        ShadowActivity shadowUserInputActivity = Shadows.shadowOf(userInputActivity);

        Assert.assertEquals(resultData.filterEquals(shadowUserInputActivity.getResultIntent()), true);


    }

    @Test
    public void usingAdminAccount_addNewUser_displayNewUser() {

        AdminActivity adminActivity = Robolectric.setupActivity(AdminActivity.class);

        HashMap<String, String> userData = new HashMap<String, String>();

        List<User> users = new ArrayList<>();
        User newUser = new User("NewUser", "NewPassword", false);

        users.add(newUser);

        try {
            User.writeXml(RuntimeEnvironment.application, users);
        } catch(IOException e) {

        }

        userData.put("username", "NewUser");
        userData.put("password", "NewPassword");
        userData.put("admin rights", "false");

        Intent resultData = new Intent();

        resultData.putExtra("User data", userData);

        adminActivity.onActivityResult(10, Activity.RESULT_OK, resultData);

        UserInfoFragment userInfoFragment = (UserInfoFragment)
                adminActivity.getSupportFragmentManager().findFragmentById(R.id.user_listview_fragment);

        Assert.assertEquals(userInfoFragment.getUsers().contains(newUser), true);
    }

    @Test
    public void usingAdminAccount_removeUser_displayMessage() {

        AdminActivity adminActivity = Robolectric.setupActivity(AdminActivity.class);

        HashMap<String, String> userData = new HashMap<String, String>();

        List<User> users = new ArrayList<>();
        User newUser = new User("RemovingUser", "RemovingPassword", false);

        users.add(newUser);

        try {
            User.writeXml(RuntimeEnvironment.application, users);
        } catch(IOException e) {

        }

        userData.put("username", "RemovingUser");
        userData.put("password", "RemovingPassword");
        userData.put("admin rights", "false");

        Intent resultData = new Intent();

        resultData.putExtra("User data", userData);

        adminActivity.onActivityResult(12, Activity.RESULT_OK, resultData);

        Assert.assertEquals("User removed !", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void usingAdminAccount_updateUser_displayUser() {

        AdminActivity adminActivity = Robolectric.setupActivity(AdminActivity.class);

        HashMap<String, String> userData = new HashMap<String, String>();

        List<User> users = new ArrayList<>();
        User newUser = new User("NewUser", "Password", false);

        users.add(newUser);

        try {
            User.writeXml(RuntimeEnvironment.application, users);
        } catch(IOException e) {

        }

        userData.put("username", "NewUser");
        userData.put("password", "NewPassword");
        userData.put("admin rights", "false");

        Intent resultData = new Intent();

        resultData.putExtra("User data", userData);

        adminActivity.onActivityResult(11, Activity.RESULT_OK, resultData);

        UserInfoFragment userInfoFragment = (UserInfoFragment)
                adminActivity.getSupportFragmentManager().findFragmentById(R.id.user_listview_fragment);


        Assert.assertEquals(userInfoFragment.getUsers().get(0).getPassword(), "NewPassword");
    }

    @Test
    public void usingUserAccount_viewBusOnMap_openDisplayBusWindow() {

        GeneralActivity generalActivity = Robolectric.setupActivity(GeneralActivity.class);

        Button checkBusButton = (Button) generalActivity.findViewById(R.id.checkBusesButton);

        checkBusButton.performClick();

        ShadowActivity shadowActivity = Shadows.shadowOf(generalActivity);

        Intent intent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(intent);

        Assert.assertEquals(NavigationActivity.class, shadowIntent.getIntentClass());
    }

    @Test
    public void usingUserLocation_addLocationToServer_verifyLocation() {

        LocationService.LocationListener locationListener = mock(LocationService.LocationListener.class);

        LocationActivity locationActivity = Robolectric.setupActivity(LocationActivity.class);

        Button button = (Button) locationActivity.findViewById(R.id.button5);

        AutoCompleteTextView busName = (AutoCompleteTextView) locationActivity.findViewById(R.id.busesAutoCompleteTextView);

        AppRegistry.setCurrentUser(new User("test@mail.com"));

        busName.setText("35");

        button.performClick();

        LocationManager locationManager = (LocationManager) ApplicationEnvironment.getContext().getSystemService(Context.LOCATION_SERVICE);

        ShadowLocationManager slm = Shadows.shadowOf(locationManager);

        Location location = new Location(locationManager.GPS_PROVIDER);
        location.setLatitude(10.0);
        location.setLongitude(20.0);

        slm.simulateLocation(location);

        ShadowActivity shadowActivity = Shadows.shadowOf(locationActivity);

        Intent intent = shadowActivity.getNextStartedService();
        ShadowIntent shadowIntent = Shadows.shadowOf(intent);

        Assert.assertEquals(LocationService.class, shadowIntent.getIntentClass());

        //verify(locationListener).onLocationChanged(location);

   /*     LocationActivity locationActivity = Robolectric.setupActivity(LocationActivity.class);

        Button button = (Button) locationActivity.findViewById(R.id.button5);

        AutoCompleteTextView busName = (AutoCompleteTextView) locationActivity.findViewById(R.id.busesAutoCompleteTextView);

        AppRegistry.setCurrentUser(new User("test@mail.com"));

        busName.setText("35");

        String busesToSearch = busName.getText().toString();

        LocationManager locationManager = (LocationManager) ApplicationEnvironment.getContext().getSystemService(Context.LOCATION_SERVICE);

        ShadowLocationManager slm = Shadows.shadowOf(locationManager);



       // button.performClick();

        Query busQuery = FirebaseDatabase.getInstance().getReference("Users").orderByChild("busName").equalTo(busesToSearch);

        Location location = new Location("Test");
        location.setLatitude(10.0);
        location.setLongitude(20.0);

        slm.simulateLocation(location);

        button.performClick();

        ValueEventListener userListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Bus> currentBuses = new ArrayList<Bus>();
                    for (DataSnapshot buses : dataSnapshot.getChildren()) {
                        busCoords = new LatLng(Double.parseDouble(buses.child("location").child("latitude").
                                getValue().toString()),
                                Double.parseDouble(buses.child("location").child("longitude").getValue().toString()));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        busQuery.addValueEventListener(userListener);

        Assert.assertEquals(busCoords, new LatLng(location.getLatitude(), location.getLongitude()));

        busQuery.removeEventListener(userListener);

       // locationManager.removeTestProvider("Test");
*/
    }

}
