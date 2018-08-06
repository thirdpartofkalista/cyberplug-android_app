package com.criss.cyberplug.list_adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.criss.cyberplug.R;
import com.criss.cyberplug.networking.NetworkHandler;
import com.criss.cyberplug.types.Group;

import java.util.ArrayList;

public class GroupListAdapter extends ArrayAdapter<Group> implements View.OnClickListener{


    private ArrayList<Group> devices;

    private Context context;



    @Override
    public void onClick(View view) {

    }

    public GroupListAdapter(ArrayList<Group> data, Context context, NetworkHandler networkHandler) {
        super(context, R.layout.device_item, data);
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // TODO: 03.08.2018
        return null;
    }

    // TODO: 06.08.2018 add logging and do the list adapter
}
