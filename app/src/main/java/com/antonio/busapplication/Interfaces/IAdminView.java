package com.antonio.busapplication.Interfaces;

import com.antonio.busapplication.Activities.BusInfoFragment;
import com.antonio.busapplication.Activities.UserInfoFragment;

import java.util.HashMap;

/**
 * Created by Antonio on 5/27/2017.
 */

public interface IAdminView {
    void showSuccessfulMessage();
    void showUserRemovedMessage();
    void showBusRemovedMessage();
    HashMap<String, String> getUserData();
    HashMap<String, String> getBusData();
    UserInfoFragment getUserInfoFragment();
    BusInfoFragment getBusInfoFragment();

}
