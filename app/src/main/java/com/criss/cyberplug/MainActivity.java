package com.criss.cyberplug;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import com.criss.cyberplug.list_adapters.DeviceListAdapter;
import com.criss.cyberplug.list_adapters.GroupListAdapter;
import com.criss.cyberplug.types.Device;
import com.criss.cyberplug.types.Group;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private boolean shouldReloadList = false;

    private int lastPlace = 0; // 0->devices, 1->groups

    private ListView list;

    private ArrayList<Device> devices;

    private ArrayList<Group> groups;

    private DeviceListAdapter deviceListAdapter;

    private GroupListAdapter groupListAdapter;

    private Toolbar toolbar;

    private ActionBar actionBar;

    private FloatingActionButton addButton;


    private void reloadList() {

        if (lastPlace == 0) {
            list.setAdapter(deviceListAdapter);
        }

        else if (lastPlace == 1) {
            list.setAdapter(groupListAdapter);
        }

        shouldReloadList = false;
    }

    private void initializeList() {

        deviceListAdapter = new DeviceListAdapter(devices, getApplicationContext());

        groupListAdapter = new GroupListAdapter(groups, getApplicationContext());
    }


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
        NavigationView navigationView = findViewById(R.id.nav_view);

//        Initialize the list
        initializeList();
        reloadList();

//        Handle the interaction with the drawer
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        if (menuItem.getItemId() == R.id.all_devices_button) {
                            if (lastPlace != 0)
                                shouldReloadList = true;
                            lastPlace = 0;
                            menuItem.setChecked(true);
                        }
                        else if (menuItem.getItemId() == R.id.groups_button) {
                            if (lastPlace != 1)
                                shouldReloadList = true;
                            lastPlace = 1;
                            menuItem.setChecked(true);
                        }
                        else {
                            switch (menuItem.getItemId()) {
                                case R.id.settings_button:
                                    break;

                                case R.id.about_button:
                                    break;

                                case R.id.help_button:
                                    break;

                                case R.id.log_out_button:
                                    break;
                            }
                        }

                        drawerLayout.closeDrawers();

                        if (shouldReloadList)
                            reloadList();

                        return true;
                    }
                });

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

