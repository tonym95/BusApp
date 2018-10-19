package com.antonio.busapplication.Controller;

import com.antonio.busapplication.Activities.SelectBusActivity;
import com.antonio.busapplication.Interfaces.ISelectBusView;

/**
 * Created by Antonio on 5/27/2017.
 */

public class SelectBusController {
    private ISelectBusView selectBusView;

    public SelectBusController(ISelectBusView selectBusView) {
        this.selectBusView = selectBusView;
    }

    public void findBus() {
        selectBusView.returnResult();
    }
}
