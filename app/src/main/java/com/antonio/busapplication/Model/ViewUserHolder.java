package com.antonio.busapplication.Model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.antonio.busapplication.R;

/**
 * Created by Antonio on 4/17/2017.
 */

public class ViewUserHolder extends RecyclerView.ViewHolder{
    private TextView name;
    private TextView password;
    private TextView adminRights;

    public ViewUserHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.email);
        password = (TextView) itemView.findViewById(R.id.password);
        adminRights = (TextView) itemView.findViewById(R.id.admin_rights);
    }

    public TextView getName() {
        return name;
    }

    public TextView getPassword() {
        return password;
    }

    public TextView getAdminRights() {
        return adminRights;
    }
}
