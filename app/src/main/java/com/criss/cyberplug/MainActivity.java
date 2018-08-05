package com.criss.cyberplug;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.criss.cyberplug.list_adapters.DeviceListAdapter;
import com.criss.cyberplug.list_adapters.GroupListAdapter;
import com.criss.cyberplug.networking.NetworkHandler;
import com.criss.cyberplug.types.Device;
import com.criss.cyberplug.types.Group;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private int lastPlace = 0; // 0->devices, 1->groups

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


//    Handle the interaction with the drawer menu items
    private NavigationView.OnNavigationItemSelectedListener drawerOnNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Toast.makeText(getApplicationContext(), "works", Toast.LENGTH_LONG).show();

            int id = menuItem.getItemId();

            switch (id) {
                case R.id.all_devices_button:
                    lastPlace = 0;
                    reloadList();
                    menuItem.setChecked(true);
                    break;

                case R.id.groups_button:
                    lastPlace = 1;
                    reloadList();
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

//    Handle the interaction with the floating button
    private View.OnClickListener addButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            devices.add(new Device(0, "device 0", false, true));
            Toast.makeText(getApplicationContext(), "works", Toast.LENGTH_LONG).show();
            reloadList();
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


    private void reloadList() {

        if (lastPlace == 0) {
            list.setAdapter(deviceListAdapter);
        }

        else if (lastPlace == 1) {
            list.setAdapter(groupListAdapter);
        }
    }

    private void initializeList() {

        devices = new ArrayList<>();

        groups = new ArrayList<>();

        deviceListAdapter = new DeviceListAdapter(devices, getApplicationContext(), networkHandler);

        groupListAdapter = new GroupListAdapter(groups, getApplicationContext(), networkHandler);

//        reloadList();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

//        Initialize networking
        networkHandler = new NetworkHandler();

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

