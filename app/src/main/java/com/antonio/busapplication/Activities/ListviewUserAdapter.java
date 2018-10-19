package com.antonio.busapplication.Activities;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.antonio.busapplication.Model.User;
import com.antonio.busapplication.Model.ViewUserHolder;
import com.antonio.busapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 4/17/2017.
 */

public class ListviewUserAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private static List<User> listUsers = new ArrayList<User>();

    public ListviewUserAdapter(Context userInfoFragment, List<User> results) {
        listUsers = results;
        inflater = LayoutInflater.from(userInfoFragment);
    }


    @Override
    public int getCount() {
        return listUsers.size();
    }

    @Override
    public User getItem(int i) {
        return listUsers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewUserHolder viewUserHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.listview, null);
            viewUserHolder = new ViewUserHolder(view);
            view.setTag(viewUserHolder);

        } else {
            viewUserHolder = (ViewUserHolder) view.getTag();
        }

        Log.i("From adapter:", listUsers.get(i).getEmail());

        viewUserHolder.getName().setText(listUsers.get(i).getEmail());
        viewUserHolder.getPassword().setText(listUsers.get(i).getPassword());
        viewUserHolder.getAdminRights().setText(listUsers.get(i).getAdminRights().toString());
        return view;
    }
}
