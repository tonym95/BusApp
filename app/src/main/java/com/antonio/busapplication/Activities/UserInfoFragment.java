package com.antonio.busapplication.Activities;



import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.antonio.busapplication.R;
import com.antonio.busapplication.Model.User;

import java.util.ArrayList;
import java.util.List;


public class UserInfoFragment extends Fragment {

    private List<User> users;
    private BaseAdapter adapter;
    private ListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View userView = inflater.inflate(R.layout.fragment_user_info, container, false);
        //ArrayList<User> listUsers = getUsers();
        users = new ArrayList<User>();


        lv = (ListView) userView.findViewById(R.id.userListView);
        adapter = new ListviewUserAdapter(getActivity(), getUsers());
        lv.setAdapter(adapter);

        return userView;
    }

    public void addUserToList(User u) {
        users.add(u);
        adapter.notifyDataSetChanged();
    }


    public void clearList() {
        users.clear();
        adapter.notifyDataSetChanged();
    }

    public List<User> getUsers() {
        return users;
    }


    public void setUsers(List<User> users) {
        this.users = users;
    }

}
