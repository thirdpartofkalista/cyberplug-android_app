package com.criss.cyberplug.list_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.criss.cyberplug.R;
import com.criss.cyberplug.types.Device;
import com.criss.cyberplug.types.DeviceViewHolder;

import java.util.ArrayList;

public class DeviceListAdapter extends ArrayAdapter<Device> implements View.OnClickListener{

//    The last position of an item in the array
    private int lastPosition = -1;

//    The devices list
    private ArrayList<Device> devices;

    private Context context;



    @Override
    public void onClick(View view) {

    }

    public DeviceListAdapter(ArrayList<Device> data, Context context) {
        super(context, R.layout.device_item, data);
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

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
            viewHolder.settingButton = view.findViewById(R.id.device_settings_button);

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

                // TODO: 03.08.2018 add functionality to the status switch
                if (isChecked) {
                    Toast.makeText(getContext(), "Device " + device.getName() + " has been turned on.", Toast.LENGTH_SHORT).show();
                }

                else {
                    Toast.makeText(getContext(), "Device " + device.getName() + " has been turned off.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        viewHolder.settingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO: 03.08.2018 launch the device settings activity for this device
            }
        });
        
        return view;
    }
}
