package com.antonio.busapplication.Controller;

import android.util.Log;
import android.widget.Toast;

import com.antonio.busapplication.Activities.AdminActivity;
import com.antonio.busapplication.Activities.BusInfoFragment;
import com.antonio.busapplication.Activities.UserInfoFragment;
import com.antonio.busapplication.Interfaces.IAdminView;
import com.antonio.busapplication.Model.AppRegistry;
import com.antonio.busapplication.Model.ApplicationEnvironment;
import com.antonio.busapplication.Model.Bus;
import com.antonio.busapplication.Model.Report;
import com.antonio.busapplication.Model.ReportFactory;
import com.antonio.busapplication.Model.Route;
import com.antonio.busapplication.Model.User;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Antonio on 5/27/2017.
 */

public class AdminController {

    private IAdminView adminView;

    public AdminController(IAdminView adminView) {
        this.adminView = adminView;
    }

    public void generateReport() {
        ReportFactory reportFactory = new ReportFactory();
        Report report = reportFactory.getReport("csv");
        report.generate(ApplicationEnvironment.getContext(), AppRegistry.getBuses());
        report = reportFactory.getReport("pdf");
        report.generate(ApplicationEnvironment.getContext(), AppRegistry.getBuses());
        adminView.showSuccessfulMessage();
    }

    public void addUser() {
        try {
            List<User> users = User.readXmlFile(ApplicationEnvironment.getContext(), false);
            User newUser = new User(adminView.getUserData().get("username"), adminView.getUserData().get("password"),
                    Boolean.parseBoolean(adminView.getUserData().get("admin rights")));

            adminView.getUserInfoFragment().clearList();
            adminView.getUserInfoFragment().addUserToList(newUser);

            users.add(newUser);
            User.writeXml(ApplicationEnvironment.getContext(), users);
        } catch(XmlPullParserException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void updateUser() {
        try {
            List<User> users = User.readXmlFile(ApplicationEnvironment.getContext(), false);
            for (User u : users) {
                if (u.getEmail().equals(adminView.getUserData().get("username"))) {
                    Log.i("User", u.getEmail());
                    u.setPassword(adminView.getUserData().get("password"));
                    u.setAdminRights(Boolean.parseBoolean(adminView.getUserData().get("admin rights")));
                    User newUser = new User(adminView.getUserData().get("username"), adminView.getUserData().get("password"),
                            Boolean.parseBoolean(adminView.getUserData().get("admin rights")));
                    adminView.getUserInfoFragment().clearList();
                    adminView.getUserInfoFragment().addUserToList(newUser);
                }
            }

            User.writeXml(ApplicationEnvironment.getContext(), users);
        } catch(XmlPullParserException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void removeUser() {
        try {
            List<User> users = User.readXmlFile(ApplicationEnvironment.getContext(), false);
            User removedUser = null;
            adminView.getUserInfoFragment().clearList();
            for (User u : users) {
                if (u.getEmail().equals(adminView.getUserData().get("username"))) {
                    removedUser = u;
                }
            }

            users.remove(removedUser);
            adminView.showUserRemovedMessage();

            User.writeXml(ApplicationEnvironment.getContext(), users);
        } catch(XmlPullParserException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    public void viewUser() {
        try {
            List<User> users = User.readXmlFile(ApplicationEnvironment.getContext(), false);

            adminView.getUserInfoFragment().clearList();
            for (User u : users) {
                if (u.getEmail().equals(adminView.getUserData().get("username"))) {
                    adminView.getUserInfoFragment().addUserToList(u);
                }
            }
        } catch(XmlPullParserException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void addBus() {
        try {
            List<Bus> buses = Bus.readXmlFile(ApplicationEnvironment.getContext(), false);
            Bus newBus = new Bus(
                    adminView.getBusData().get("bus name"), adminView.getBusData().get("bus type"),
                    new Route(Long.parseLong(adminView.getBusData().get("route id"))));


            adminView.getBusInfoFragment().clearList();
            adminView.getBusInfoFragment().addBusToList(newBus);

            buses.add(newBus);
            AppRegistry.addBus(newBus);
            Bus.writeXml(ApplicationEnvironment.getContext(), buses);
        } catch(XmlPullParserException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void updateBus() {
        try {
            List<Bus> buses = Bus.readXmlFile(ApplicationEnvironment.getContext(), false);


            //   Log.i("From admin activity", newUser.getEmail());
            for (Bus b : buses) {
                if (b.getName().equals(adminView.getBusData().get("bus name"))) {
                    b.setType(adminView.getBusData().get("bus type"));
                    b.setRoute(new Route(Long.parseLong(adminView.getBusData().get("route id"))));
                    Bus newBus = new Bus(adminView.getBusData().get("bus name"), adminView.getBusData().get("bus type"),
                            new Route(Long.parseLong(adminView.getBusData().get("route id"))));
                    adminView.getBusInfoFragment().clearList();
                    adminView.getBusInfoFragment().addBusToList(newBus);
                }

                Bus.writeXml(ApplicationEnvironment.getContext(), buses);
            }
        } catch(XmlPullParserException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    public void removeBus() {
        try {
            List<Bus> buses = Bus.readXmlFile(ApplicationEnvironment.getContext(), false);


            Bus removedBus = null;
            adminView.getBusInfoFragment().clearList();
            for (Bus b : buses) {
                if (b.getName().equals(adminView.getBusData().get("bus name"))) {
                    removedBus = b;

                }
            }

            buses.remove(removedBus);
            adminView.showBusRemovedMessage();
            Bus.writeXml(ApplicationEnvironment.getContext(), buses);
        } catch(XmlPullParserException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    public void viewBus() {
        try {
            List<Bus> buses = Bus.readXmlFile(ApplicationEnvironment.getContext(), false);

            adminView.getBusInfoFragment().clearList();
            for (Bus b : buses) {
                if (b.getName().equals(adminView.getBusData().get("bus name"))) {
                    adminView.getBusInfoFragment().addBusToList(b);
                }
            }
        } catch(XmlPullParserException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
