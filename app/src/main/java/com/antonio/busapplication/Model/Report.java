package com.antonio.busapplication.Model;

import android.content.Context;

import com.antonio.busapplication.Model.Bus;

import java.util.List;

/**
 * Created by Antonio on 4/17/2017.
 */

public interface Report {

   void generate(Context context, List<Bus> buses);

}
