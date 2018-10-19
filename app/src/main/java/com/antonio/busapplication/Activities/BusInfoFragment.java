package com.antonio.busapplication.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.antonio.busapplication.Model.Bus;
import com.antonio.busapplication.R;

import java.util.ArrayList;
import java.util.List;



public class BusInfoFragment extends Fragment {
    private List<Bus> buses;
    private ListviewBusAdapter adapter;
    private ListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.removeAllViews();
        View busView = inflater.inflate(R.layout.fragment_bus_info, container, false);
        buses = new ArrayList<Bus>();
        //ArrayList<User> listUsers = getUsers();


        Log.i("NOT", "CREATED");
        lv = (ListView) busView.findViewById(R.id.busListView);
        adapter = new ListviewBusAdapter(getActivity(), getBuses());
        lv.setAdapter(adapter);

        return busView;
    }

    public void addBusToList(Bus b) {
        buses.add(b);
        adapter.notifyDataSetChanged();
    }


    public void clearList() {
        buses.clear();
        adapter.notifyDataSetChanged();
    }

    public List<Bus> getBuses() {
        return buses;
    }

}
