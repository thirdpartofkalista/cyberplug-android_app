package com.criss.cyberplug;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.criss.cyberplug.constants.MessageType;
import com.criss.cyberplug.constants.RequestCode;
import com.criss.cyberplug.list_adapters.DeviceListAdapter;
import com.criss.cyberplug.list_adapters.GroupListAdapter;
import com.criss.cyberplug.networking.NetworkHandler;
import com.criss.cyberplug.types.Device;
import com.criss.cyberplug.types.Group;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private DrawerLayout drawerLayout;

    private byte lastPlace = 0; // 0->devices, 1->groups

    private ListView list;

    private ArrayList<Device> devices;

    private ArrayList<Group> groups;

    private DeviceListAdapter deviceListAdapter;

    private GroupListAdapter groupListAdapter;

    private Toolbar toolbar;

    private ActionBar actionBar;

    private FloatingActionButton addButton;

    private NavigationView navigationView;

    private NetworkHandler networkHandler;



//    ----------------------------------------------------------------------------
//    ----------List methods------------------------------------------------------
//    ----------------------------------------------------------------------------

//    Reloads the local list with a list received from the server
    private void reloadList(final Device[] list) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean shouldUpdateList = false;
                try {
                    if (list != null) {
                        devices.clear();
                        devices.addAll(Arrays.asList(list));
                        shouldUpdateList = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                } finally {
                    Message msg = new Message();
                    msg.what = MessageType.LIST_UPDATE_UI.getValue();
                    if (shouldUpdateList) {
                        msg.arg1 = 1;
                    }
                    else {
                        msg.arg1 = 0;
                    }
                    uiHandler.sendMessage(msg);
                }
            }
        });
        thread.start();
    }

//    Requests a device list from the server
    private void requestDeviceList() {
        networkHandler.getDeviceList();
    }

//    Updates the list ui
    private void updateListUi() {

        if (lastPlace == 0) {
            list.setAdapter(deviceListAdapter);
        }

        else if (lastPlace == 1) {
            list.setAdapter(groupListAdapter);
        }

    }

//    Initialized the list
    private void initializeList() {

        devices = new ArrayList<>();

        groups = new ArrayList<>();

        deviceListAdapter = new DeviceListAdapter(devices, getApplicationContext(), networkHandler);

        groupListAdapter = new GroupListAdapter(groups, getApplicationContext(), networkHandler);

        requestDeviceList();

    }
//    ----------------------------------------------------------------------------
//    ----------------------------------------------------------------------------
//    ----------------------------------------------------------------------------



//    ----------------------------------------------
//    ----------UI Message Handler------------------
//    ----------------------------------------------

//    This receives messages from other threads in order to update the ui
//    or to carry out other tasks
    private Handler uiHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == MessageType.LIST_RELOAD.getValue()) {
                reloadList((Device[]) msg.obj);
            }

            if (msg.what == MessageType.LIST_UPDATE_UI.getValue()) {
                updateListUi();
            }

        }
    };

//    ----------------------------------------------------------------------------
//    ----------------------------------------------------------------------------
//    ----------------------------------------------------------------------------



//    ----------------------------------------------------------------------------
//    ----------Event listeners---------------------------------------------------
//    ----------------------------------------------------------------------------

//    Handle the interaction with the drawer
    private NavigationView.OnNavigationItemSelectedListener drawerOnNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Toast.makeText(getApplicationContext(), "works", Toast.LENGTH_LONG).show();

            int id = menuItem.getItemId();

            switch (id) {
                case R.id.all_devices_button:
                    lastPlace = 0;
                    updateListUi();
                    menuItem.setChecked(true);
                    break;

                case R.id.groups_button:
                    lastPlace = 1;
                    updateListUi();
                    menuItem.setChecked(true);
                    break;

                case R.id.about_button:
                    break;

                case R.id.help_button:
                    break;

                case R.id.log_out_button:
                    break;
            }

            drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        }
    };

//    Handle the interaction with the "Add new device" floating button
    private View.OnClickListener addButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent mIntent = new Intent(getApplication(), AddDeviceActivity.class);

            startActivityForResult(mIntent, RequestCode.ADD_NEW_DEVICE.getValue());

        }
    };

//    Handle the interaction with the action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


//    Retrieve data from activities that we expect a result from
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RequestCode.ADD_NEW_DEVICE.getValue()) {

            if(resultCode == Activity.RESULT_OK){

                Device device = new Device(devices.size() + 1, data.getStringExtra("name"), false, true);

                devices.add(device);

                networkHandler.addDevice(device);

                updateListUi();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "No device added", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == RequestCode.DEVICE_SETTINGS.getValue()) {
            // TODO: 06.08.2018 device settings
        }

        if (requestCode == RequestCode.APP_SETTINGS.getValue()) {
            // TODO: 06.08.2018 app settings
        }
    }
//    ----------------------------------------------------------------------------
//    ----------------------------------------------------------------------------
//    ----------------------------------------------------------------------------



//    ----------------------------------------------------------------------------
//    ----------Activity methods--------------------------------------------------
//    ----------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Initialize networking
        networkHandler = new NetworkHandler(uiHandler);

//        Enable the toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Enable the actionbar
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

//        find view by ids
        drawerLayout = findViewById(R.id.drawer_layout);
        list = findViewById(R.id.main_list);
        addButton = findViewById(R.id.add_button);
        navigationView = findViewById(R.id.nav_view);
        addButton = findViewById(R.id.add_button);

//        Initialize the list
        initializeList();

//        Set event listeners
        navigationView.setNavigationItemSelectedListener(drawerOnNavigationItemSelectedListener);
        addButton.setOnClickListener(addButtonOnClickListener);

    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onRestart() {

        super.onRestart();
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

}

