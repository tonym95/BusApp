package com.antonio.busapplication.Model;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Antonio on 4/12/2017.
 */

public class AppRegistry {

    private static List<Bus> buses = new ArrayList<Bus>();
    private static List<User> users = new ArrayList<User>();
    private static TreeMap<Bus, Integer> userNo = new TreeMap<Bus, Integer>();
    private static User currentUser;

    public static void addBus(Bus b) {
        buses.add(b);
    }

    public static void addUser(User u) {
        users.add(u);
    }

    public static void setCurrentUser(User u) {
        currentUser = u;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static List<Bus> getBuses() {
        return buses;
    }

    /*public static User getCurrentUser() {
        return currentUser;
    }*/


    public static List<Bus> getBusesByName(String busName) {
        List<Bus> busesToShow = new ArrayList<Bus>();

        for (Bus b : buses) {
            if(b.getName().equals(busName))
                busesToShow.add(b);
        }

        return busesToShow;
    }
}
