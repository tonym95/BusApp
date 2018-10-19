package com.antonio.busapplication.Controller;

import com.antonio.busapplication.Interfaces.IGeneralView;

/**
 * Created by Antonio on 5/27/2017.
 */

public class GeneralController {

    IGeneralView generalView;

    public GeneralController(IGeneralView generalView) {
        this.generalView = generalView;
    }

    public void enterLocation() {
        generalView.showLocationView();
    }

    public void checkBuses() {
        generalView.showNavigationView();
    }

    public void help() {
        generalView.showHelpView();
    }
}
