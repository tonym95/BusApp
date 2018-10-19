package com.antonio.busapplication.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.antonio.busapplication.Model.Bus;
import com.antonio.busapplication.Model.ViewBusHolder;
import com.antonio.busapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 4/18/2017.
 */

public class ListviewBusAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private static List<Bus> listBuses = new ArrayList<Bus>();

    public ListviewBusAdapter(Context userInfoFragment, List<Bus> results) {
        listBuses = results;
        inflater = LayoutInflater.from(userInfoFragment);
    }


    @Override
    public int getCount() {
        return listBuses.size();
    }

    @Override
    public Bus getItem(int i) {
        return listBuses.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewBusHolder viewBusHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.bus_listview, null);
            viewBusHolder = new ViewBusHolder(view);
            view.setTag(viewBusHolder);

        } else {
            viewBusHolder = (ViewBusHolder) view.getTag();
        }

      //  Log.i("From adapter:", listBuses.get(i).getEmail());

        viewBusHolder.getName().setText(listBuses.get(i).getName());
        viewBusHolder.getPassword().setText(listBuses.get(i).getType());
        viewBusHolder.getAdminRights().setText(listBuses.get(i).getRoute().getId().toString());
        return view;
    }
}
