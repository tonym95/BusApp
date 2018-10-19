package com.antonio.busapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.antonio.busapplication.Controller.AdminController;
import com.antonio.busapplication.Interfaces.IAdminView;
import com.antonio.busapplication.Model.AppRegistry;
import com.antonio.busapplication.Model.Bus;
import com.antonio.busapplication.Model.FragmentType;
import com.antonio.busapplication.R;
import com.antonio.busapplication.Model.Report;
import com.antonio.busapplication.Model.ReportFactory;
import com.antonio.busapplication.Model.Route;
import com.antonio.busapplication.Model.User;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class AdminActivity extends AppCompatActivity
        implements IAdminView, NavigationView.OnNavigationItemSelectedListener {

    private FragmentType fragmentType;
    private AdminController adminController = new AdminController(this);
    private HashMap<String, String> userData;
    private HashMap<String, String> busData;
    private UserInfoFragment userInfo = null;
    private BusInfoFragment busInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin2);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentType = FragmentType.USER_INFO_FRAGMENT;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.add_user) {
            Intent intent = new Intent(this, UserInputActivity.class);
            startActivityForResult(intent, 10);
        } else if (id == R.id.update_user) {
            Intent intent = new Intent(this, UserInputActivity.class);
            startActivityForResult(intent, 11);
        } else if (id == R.id.remove_user) {
            Intent intent = new Intent(this, UserInputActivity.class);
            startActivityForResult(intent, 12);
        } else if (id == R.id.view_user) {
            Intent intent = new Intent(this, UserInputActivity.class);
            startActivityForResult(intent, 13);
        } else if (id == R.id.add_bus) {
            Intent intent = new Intent(this, BusInputActivity.class);
            startActivityForResult(intent, 14);
        } else if (id == R.id.update_bus) {
            Intent intent = new Intent(this, BusInputActivity.class);
            startActivityForResult(intent, 15);
        } else if (id == R.id.remove_bus) {
            Intent intent = new Intent(this, BusInputActivity.class);
            startActivityForResult(intent, 16);
        } else if (id == R.id.view_bus) {
            Intent intent = new Intent(this, BusInputActivity.class);
            startActivityForResult(intent, 17);
        } else if (id == R.id.generate_report) {

            adminController.generateReport();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {

        if (fragmentType == FragmentType.USER_INFO_FRAGMENT) {
            userInfo = (UserInfoFragment) getSupportFragmentManager().findFragmentById(R.id.user_listview_fragment);
            busInfo = new BusInfoFragment();
        } else if (fragmentType == FragmentType.BUS_INFO_FRAGMENT) {
            userInfo = new UserInfoFragment();
            busInfo = (BusInfoFragment) getSupportFragmentManager().findFragmentById(R.id.bus_listview_fragment);
        }

        FragmentManager fm = getSupportFragmentManager();


        if (requestCode == 10) {
            if (resultCode == UserInputActivity.RESULT_OK) {

                if (fragmentType == FragmentType.BUS_INFO_FRAGMENT) {
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.bus_listview_fragment, userInfo);
                    transaction.addToBackStack(null);
                    transaction.commitAllowingStateLoss();
                    fm.executePendingTransactions();
                }

                userData = (HashMap<String, String>) data.getSerializableExtra("User data");
                adminController.addUser();
                fragmentType = FragmentType.USER_INFO_FRAGMENT;

            } else if (requestCode == 11) {
                if (resultCode == UserInputActivity.RESULT_OK) {
                    if (fragmentType == FragmentType.BUS_INFO_FRAGMENT) {
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.replace(R.id.bus_listview_fragment, userInfo);
                        transaction.addToBackStack(null);
                        transaction.commitAllowingStateLoss();
                        fm.executePendingTransactions();
                    }
                    userData = (HashMap<String, String>) data.getSerializableExtra("User data");
                    adminController.updateUser();
                    fragmentType = FragmentType.USER_INFO_FRAGMENT;
                }
            } else if (requestCode == 12) {
                if (resultCode == UserInputActivity.RESULT_OK) {

                    if (fragmentType == FragmentType.BUS_INFO_FRAGMENT) {
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.replace(R.id.bus_listview_fragment, userInfo);
                        transaction.addToBackStack(null);
                        transaction.commitAllowingStateLoss();
                        fm.executePendingTransactions();
                    }

                    userData = (HashMap<String, String>) data.getSerializableExtra("User data");
                    adminController.removeUser();
                    fragmentType = FragmentType.USER_INFO_FRAGMENT;
                }
            } else if (requestCode == 13) {
                if (resultCode == UserInputActivity.RESULT_OK) {

                    if (fragmentType == FragmentType.BUS_INFO_FRAGMENT) {
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.replace(R.id.bus_listview_fragment, userInfo);
                        transaction.addToBackStack(null);
                        transaction.commitAllowingStateLoss();
                        fm.executePendingTransactions();
                    }

                    userData = (HashMap<String, String>) data.getSerializableExtra("User data");
                    adminController.viewUser();
                    fragmentType = FragmentType.USER_INFO_FRAGMENT;

                }
            }
            if (requestCode == 14) {
                if (resultCode == UserInputActivity.RESULT_OK) {

                    if (fragmentType == FragmentType.USER_INFO_FRAGMENT) {
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.replace(R.id.user_listview_fragment, busInfo);
                        transaction.addToBackStack(null);
                        transaction.commitAllowingStateLoss();
                        fm.executePendingTransactions();
                    }

                  busData = (HashMap<String, String>) data.getSerializableExtra("Bus data");
                  adminController.addBus();
                  fragmentType = FragmentType.BUS_INFO_FRAGMENT;

                }
            }
            if (requestCode == 15) {
                if (resultCode == UserInputActivity.RESULT_OK) {

                    if (fragmentType == FragmentType.USER_INFO_FRAGMENT) {
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.replace(R.id.user_listview_fragment, busInfo);
                        transaction.addToBackStack(null);
                        transaction.commitAllowingStateLoss();
                        fm.executePendingTransactions();
                    }

                    busData = (HashMap<String, String>) data.getSerializableExtra("Bus data");
                    adminController.updateBus();
                    fragmentType = FragmentType.BUS_INFO_FRAGMENT;

                }
            }
            if (requestCode == 16) {
                if (resultCode == UserInputActivity.RESULT_OK) {

                    if (fragmentType == FragmentType.USER_INFO_FRAGMENT) {
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.replace(R.id.user_listview_fragment, busInfo);
                        transaction.addToBackStack(null);
                        transaction.commitAllowingStateLoss();
                        fm.executePendingTransactions();
                    }

                    busData = (HashMap<String, String>) data.getSerializableExtra("Bus data");
                    adminController.removeBus();
                    fragmentType = FragmentType.BUS_INFO_FRAGMENT;
                }
            }
            if (requestCode == 17) {
                if (resultCode == UserInputActivity.RESULT_OK) {

                    if (fragmentType == FragmentType.USER_INFO_FRAGMENT) {
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.replace(R.id.user_listview_fragment, busInfo);
                        transaction.addToBackStack(null);
                        transaction.commitAllowingStateLoss();
                        fm.executePendingTransactions();
                    }

                    busData = (HashMap<String, String>) data.getSerializableExtra("Bus data");
                    adminController.viewBus();
                    fragmentType = FragmentType.BUS_INFO_FRAGMENT;

                }
            }
        }
    }

    @Override
    public void showSuccessfulMessage() {
        Toast.makeText(getApplicationContext(), "Operation succesful !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public HashMap<String, String> getUserData() {
        return userData;
    }

    @Override
    public HashMap<String, String> getBusData() {
        return busData;
    }


    @Override
    public void showUserRemovedMessage() {
        Toast.makeText(getApplicationContext(), "User removed !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showBusRemovedMessage() {
        Toast.makeText(getApplicationContext(), "Bus removed !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public UserInfoFragment getUserInfoFragment() {
        return this.userInfo;
    }

    @Override
    public BusInfoFragment getBusInfoFragment() {
        return this.busInfo;
    }
}
