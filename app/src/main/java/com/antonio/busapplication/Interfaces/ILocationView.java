package com.antonio.busapplication.Interfaces;

/**
 * Created by Antonio on 5/27/2017.
 */

public interface ILocationView {

    void buildAlertMessageNoGps();

    void startLocationService();

    String getBusName();

    void showSuccessfulMessage();
}
