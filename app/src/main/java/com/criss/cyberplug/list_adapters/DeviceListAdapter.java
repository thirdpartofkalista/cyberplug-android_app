package com.criss.cyberplug.list_adapters;

import android.content.Context;
import android.content.Intent;
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

import com.criss.cyberplug.DeviceSettings;
import com.criss.cyberplug.R;
import com.criss.cyberplug.networking.NetworkHandler;
import com.criss.cyberplug.types.intents.RequestCode;
import com.criss.cyberplug.types.list.Device;
import com.criss.cyberplug.types.list.DeviceViewHolder;
import com.criss.cyberplug.types.thread_communication.MessageType;

import java.util.ArrayList;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class DeviceListAdapter extends ArrayAdapter<Device> implements View.OnClickListener{

    private static final String TAG = "DeviceListAdapter";

//    The last position of an item in the array
    private int lastPosition = -1;

//    The devices list
    private ArrayList<Device> devices;

    private Context context;

    private NetworkHandler networkHandler;

    private Handler uiHandler;


    @Override
    public void onClick(View view) {

    }

    public DeviceListAdapter(ArrayList<Device> data, Context context, NetworkHandler networkHandler, Handler handler) {
        super(context, R.layout.device_item, data);
        this.context = context;
        this.networkHandler = networkHandler;
        this.uiHandler = handler;
        Log.i(TAG, "DeviceListAdapter - instance created.");
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {

//        Get the data item for this position
        final Device device = getItem(position);

        DeviceViewHolder viewHolder;

        final View result;

//        Check if an existing view is reused
//        otherwise, inflate the view
        if (view == null) {
//            Create a new view holder for the item
            viewHolder = new DeviceViewHolder();

//            Inflate the view
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.device_item, parent, false);

//            Retrieve the items from the device layout
            viewHolder.deviceName = view.findViewById(R.id.device_name);
            viewHolder.deviceSwitch = view.findViewById(R.id.device_switch);
            viewHolder.settingsButton = view.findViewById(R.id.device_settings_button);

            result = view;

//            Cache the viewHolder in the view tag
            view.setTag(viewHolder);
        }
        else {
//            Retrieve the viewHolder from the cache
            viewHolder = (DeviceViewHolder) view.getTag();

            result = view;
        }

//        Create and start an animation for the view
        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_down : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

//        Set the variables for the device's layout items
        viewHolder.deviceName.setText(device.getName());
        viewHolder.deviceSwitch.setChecked(device.getStatus());

//        Listening to device events
        viewHolder.deviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                device.setStatus(isChecked);
                Log.i(TAG, "Switch - toggled for the device with the index: " + position);

                networkHandler.updateDeviceStatus(device);

            }
        });
        viewHolder.settingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO: 03.08.2018 launch the device settings activity for this device
                Message msg = new Message();
                msg.what = MessageType.DEVICE_SETTINGS.getValue();
                msg.arg1 = position;
                uiHandler.sendMessage(msg);
                Log.i(TAG, "Settings button pressed.");
            }
        });

        viewHolder.deviceSwitch.setChecked(device.getStatus());

        Log.i(TAG, "DeviceListAdapter - return view.");
        return view;
    }
}
