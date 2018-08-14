package com.criss.cyberplug.list_adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import com.criss.cyberplug.R;
import com.criss.cyberplug.networking.NetworkHandler;

import com.criss.cyberplug.types.list.Group;
import com.criss.cyberplug.types.list.GroupViewHolder;
import com.criss.cyberplug.types.thread_communication.MessageType;

import java.util.ArrayList;

public class GroupListAdapter extends ArrayAdapter<Group> implements View.OnClickListener{

    private static final String TAG = "GroupListAdapter";

    private int lastPosition = -1;

    private ArrayList<Group> groups;

    private Context context;

    private NetworkHandler networkHandler;

    private Handler uiHandler;


    @Override
    public void onClick(View view) {

    }

    public GroupListAdapter(ArrayList<Group> data, Context context, NetworkHandler networkHandler, Handler handler) {
        // TODO: 13.08.2018  group list item
        super(context, R.layout.device_item, data);
        this.context = context;
        this.networkHandler = networkHandler;
        this.uiHandler = handler;
        Log.i(TAG, "GroupListAdapter - instance created.");
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        final Group group = getItem(position);

        GroupViewHolder viewHolder;

        final View result;

        if (view == null){
            viewHolder = new GroupViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.device_item, parent, false);

            // create item ?                   ^
            viewHolder.groupName = view.findViewById(R.id.group_name);
            viewHolder.groupSwitch = view.findViewById(R.id.group_switch);

            viewHolder.settingsButton = view.findViewById(R.id.group_settings_button);

            result = view;

            view.setTag(viewHolder);
        }
        else{
            viewHolder = (GroupViewHolder) view.getTag();

            result = view;
        }


        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_down : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.groupName.setText(group.getName());
        viewHolder.groupSwitch.setChecked(group.getStatus());

        viewHolder.groupSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                group.setStatus(isChecked);
                Log.i(TAG, "Switch - toggled for the device with the index: " + position);

//                networkHandler.updateGroupStatus(group);

            }
        });

        viewHolder.settingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO: 03.08.2018 launch the device settings activity for this device
                Message msg = new Message();
                msg.what = MessageType.GROUP_SETTINGS.getValue();
                msg.arg1 = position;
                uiHandler.sendMessage(msg);

            }
        });

        viewHolder.groupSwitch.setChecked(group.getStatus());

        Log.i(TAG, "GroupListAdapter - return view.");

        return view;
    }

    // TODO: 06.08.2018 add logging and do the list adapter
}
