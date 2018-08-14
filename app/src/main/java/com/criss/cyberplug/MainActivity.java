package com.criss.cyberplug;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.criss.cyberplug.constants.Authentication;
import com.criss.cyberplug.types.thread_communication.MessageType;
import com.criss.cyberplug.constants.Preferences;
import com.criss.cyberplug.types.intents.RequestCode;
import com.criss.cyberplug.list_adapters.DeviceListAdapter;
import com.criss.cyberplug.list_adapters.GroupListAdapter;
import com.criss.cyberplug.networking.NetworkHandler;
import com.criss.cyberplug.types.list.Device;
import com.criss.cyberplug.types.list.Group;

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

    private Preferences preferences;

    private Authentication authentication;



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

        deviceListAdapter = new DeviceListAdapter(devices, getApplicationContext(), networkHandler, uiHandler);

        groupListAdapter = new GroupListAdapter(groups, getApplicationContext(), networkHandler, uiHandler);

        requestDeviceList();

    }
//    ----------------------------------------------------------------------------
//    ----------------------------------------------------------------------------
//    ----------------------------------------------------------------------------

    public void makeShortToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void makeLongToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }



//    ----------------------------------------------
//    ----------UI Message Handler------------------
//    ----------------------------------------------

//    This receives messages from other threads in order to update the ui
//    or to carry out other tasks
    private Handler uiHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            NetworkHandler.MessagePayload payload = (NetworkHandler.MessagePayload) msg.obj;

            boolean nullPayload = payload == null;

/*
            Template:
            if (msg.what == MessageType.SOMETHING.getValue()) {
                if (!nullPayload) {
                    if (!(payload.exceptions.size() > 0)) {
                        if (payload.data != null) {
//                            do something
                        }
                        else {
//                            do something
                        }
                    }
                    else {
//                        handle exceptions
                    }
                }
            }
*/

            if (msg.what == MessageType.NOT_PROVIDED.getValue()) {
                if (!nullPayload) {
                    if (payload.exceptions.size() > 0) {
//                        for (int i = 0; i < payload.exceptions.size(); i++) {
//
//                        }
                        makeShortToast("Encountered an error.");
                    }
                }
            }

            if (msg.what == MessageType.LIST_RELOAD.getValue()) {
                if (!nullPayload) {
                    if (!(payload.exceptions.size() > 0)) {
                        if (payload.data != null) {
                            makeShortToast("Received the list.");
                            reloadList((Device[]) payload.data);
                        }
                        else {
                            makeShortToast("The list is empty.");
                        }
                    }
                }
            }

            if (msg.what == MessageType.LIST_UPDATE_UI.getValue()) {
                updateListUi();
            }

            if (msg.what == MessageType.DEVICE_NEW.getValue()) {
                if (!nullPayload) {
                    if (!(payload.exceptions.size() > 0)) {
                        makeShortToast("Device added succesfully on the server.");
                    }
                    else {
//                        handle exceptions
                    }
                }
            }

            if (msg.what == MessageType.DEVICE_UPDATE_STATUS.getValue()) {
                if (!nullPayload) {
                    if (!(payload.exceptions.size() > 0)) {
                        makeShortToast("Device status succesfully changed.");
                    }
                    else {
//                        handle exceptions
                    }
                }
            }

            if (msg.what == MessageType.DEVICE_SETTINGS.getValue()) {
                if (true) {
                    Intent intent = new Intent(getApplicationContext(), DeviceSettings.class);
                    intent.putExtra("index", msg.arg1);
                    startActivityForResult(intent, RequestCode.DEVICE_SETTINGS.getValue());
                    Log.i(TAG, "Started settings.");
                }
            }
            if (msg.what == MessageType.GROUP_SETTINGS.getValue()){
                if (true) {
                    Intent intent = new Intent(getApplicationContext(), GroupSettings.class);
                    intent.putExtra("index", msg.arg1);
                    startActivityForResult(intent, RequestCode.GROUP_SETTINGS.getValue());

                }
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

            makeShortToast(menuItem.getTitle().toString());

            int id = menuItem.getItemId();

            switch (id) {
                case R.id.devices_list_button:
                    lastPlace = 0;
                    updateListUi();
                    menuItem.setChecked(true);
                    break;

                case R.id.groups_list_button:
                    lastPlace = 1;
                    updateListUi();
                    menuItem.setChecked(true);
                    break;

                case R.id.about_button:
                    Intent a = new Intent(/*getApplication(), AboutActivity.class*/);
//                    startActivity(a);
                    break;

                case R.id.help_button:
                    Intent b = new Intent(/*getApplication(), AboutActivity.class*/);
//                    startActivity(b);
                    break;

                case R.id.log_out_button:
                    preferences.setLoggedIn(false);
                    preferences.setUserName("");
                    Intent c = new Intent(getApplication(), LoginActivity.class);
                    startActivity(c);
                    finish();
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
        if (lastPlace == 0){
            Intent mIntent = new Intent(getApplication(), AddDeviceActivity.class);
            mIntent.putExtra("key", preferences.getUserName());
            startActivityForResult(mIntent, RequestCode.ADD_NEW_DEVICE.getValue());
        }
        else if (lastPlace == 1){
            Intent mIntent = new Intent(getApplication(), AddGroupActivity.class);
            startActivityForResult(mIntent, RequestCode.ADD_NEW_GROUP.getValue());
        }
//            Intent mIntent = new Intent(getApplication(), AddDeviceActivity.class);

//            startActivityForResult(mIntent, RequestCode.ADD_NEW_DEVICE.getValue());

        }
    };

//    Handle the interaction with the action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.select_menu_button:
                makeShortToast("select");
                break;

            case R.id.select_all_menu_button:
                makeShortToast("select all");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.action_bar_menu, menu);

        return true;
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
                makeLongToast("No device added.");
            }
        }

        if (requestCode == RequestCode.ADD_NEW_GROUP.getValue()){

            if (resultCode == Activity.RESULT_OK){
                Group group = new Group(groups.size() + 1, data.getStringExtra("name"), false);
                groups.add(group);
//                networkHandler.addGroup(group);
                updateListUi();
            }
            if (requestCode == Activity.RESULT_CANCELED)
                makeLongToast("No group added");
        }

        if (requestCode == RequestCode.DEVICE_SETTINGS.getValue()) {
            // TODO: 06.08.2018 device settings
            if (requestCode == Activity.RESULT_OK){
                if (data.getBooleanExtra("delete", false)) {
                    Log.i(TAG, "should delete device.");
                    devices.remove(data.getIntExtra("index", 0));
                    updateListUi();
                }
            }
        }
        if (requestCode == RequestCode.GROUP_SETTINGS.getValue()){
            if (requestCode == Activity.RESULT_OK){
                if (data.getBooleanExtra("delete", false)){
                    groups.remove(data.getIntExtra("index", 0));
                    updateListUi();
                }
            }
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
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        }
        else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Check preferences
        preferences = new Preferences(getApplicationContext());

//        Initialize networking
        networkHandler = new NetworkHandler(uiHandler, new Authentication(preferences.getUserName()));

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
        View navHeader = navigationView.getHeaderView(0);
        addButton = findViewById(R.id.add_button);

//        Initialize the list
        initializeList();

//        Set event listeners
        navigationView.setNavigationItemSelectedListener(drawerOnNavigationItemSelectedListener);
        addButton.setOnClickListener(addButtonOnClickListener);

//        Update nav view username text field
        TextView userNameTextView = navHeader.findViewById(R.id.username_text_view);
        userNameTextView.setText(preferences.getUserName());

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

