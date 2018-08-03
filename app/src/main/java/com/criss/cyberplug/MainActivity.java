package com.criss.cyberplug;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import com.criss.cyberplug.list_adapters.DeviceListAdapter;
import com.criss.cyberplug.list_adapters.GroupListAdapter;
import com.criss.cyberplug.types.Device;
import com.criss.cyberplug.types.Group;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mainPage;

    private boolean shouldReloadList = false;

    private int lastPlace = 0; // 0->devices, 1->groups

    private ListView list;

    private ArrayList<Device> devices;

    private ArrayList<Group> groups;

    private DeviceListAdapter deviceListAdapter;

    private GroupListAdapter groupListAdapter;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPage = findViewById(R.id.drawer_layout);

        initializeList();
        reloadList();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        if (menuItem.getItemId() == R.id.all_devices_button) {
                            lastPlace = 0;
                            menuItem.setChecked(true);
                            shouldReloadList = true;
                        }
                        else if (menuItem.getItemId() == R.id.groups_button) {
                            lastPlace = 1;
                            menuItem.setChecked(true);
                            shouldReloadList = true;
                        }
                        else {

                        }

                        mainPage.closeDrawers();

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

