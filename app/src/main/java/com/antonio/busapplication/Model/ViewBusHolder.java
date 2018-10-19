package com.antonio.busapplication.Model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.antonio.busapplication.R;

/**
 * Created by Antonio on 4/18/2017.
 */

public class ViewBusHolder extends RecyclerView.ViewHolder {
    private TextView busName;
    private TextView busType;
    private TextView routeId;

    public ViewBusHolder(View itemView) {
        super(itemView);
        busName = (TextView) itemView.findViewById(R.id.busname);
        busType = (TextView) itemView.findViewById(R.id.bustype);
        routeId = (TextView) itemView.findViewById(R.id.routeid);
    }

    public TextView getName() {
        return busName;
    }

    public TextView getPassword() {
        return busType;
    }

    public TextView getAdminRights() {
        return routeId;
    }
}
