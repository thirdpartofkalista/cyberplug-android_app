package com.criss.cyberplug;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mainPage;

    private boolean shouldReloadList = false;

    private int lastPlace = 0; // 0->devices, 1->groups

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPage = findViewById(R.id.drawer_layout);

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

    private void reloadList() {
        if (lastPlace == 0) {
//            do something
        }

        else if (lastPlace == 1) {
//            do smth else
        }

        shouldReloadList = false;
    }


}

