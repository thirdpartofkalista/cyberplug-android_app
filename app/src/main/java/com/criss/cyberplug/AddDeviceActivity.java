package com.criss.cyberplug;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.criss.cyberplug.types.intents.RequestCode;

;

public class AddDeviceActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    private EditText deviceWifiName;

    private EditText deviceWifiPassword;

    private Button scanQR;

    private Button nextButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        final String key = getIntent().getStringExtra("key");

        int fragment = getIntent().getIntExtra("fragment", 0);

        fragmentManager = getFragmentManager();

        Toolbar toolbar = findViewById(R.id.add_device_toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Add a new device");

        if (fragment == 0) {
            FragmentTransaction fragmentTransaction;
        }



//        scanQR = findViewById(R.id.scan_button);
//        deviceWifiName = findViewById(R.id.device_wifi_name_edittext);
//        deviceWifiPassword = findViewById(R.id.device_password_edittext);
//        nextButton = findViewById(R.id.next_button);
//
//        final String mDeviceWifiName = "\"" + deviceWifiName.getText().toString() + "\"";
//        final String mDeviceWifiPassword = "\"" + deviceWifiPassword.getText().toString() + "\"";
//
//
//
//
//        nextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                WifiConfiguration conf = new WifiConfiguration();
//                conf.SSID = mDeviceWifiName;
//                conf.preSharedKey = mDeviceWifiPassword;
//
//                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
//
//                int netId = wifiManager.addNetwork(conf);
//                wifiManager.disconnect();
//                wifiManager.enableNetwork(netId, true);
//                wifiManager.reconnect();
//
//                Intent mIntent = new Intent(getApplication(), ConfigureDevice.class);
//                mIntent.putExtra("key", key);
//                startActivityForResult(mIntent, RequestCode.CONFIGURE_DEVICE.getValue());
//            }
//        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RequestCode.CONFIGURE_DEVICE.getValue()) {
//            if (requestCode == RESULT_OK) {
//                String deviceName = data.getStringExtra("name");
//
//                Intent intention = new Intent();
//                intention.putExtra("name", deviceName);
//
//                setResult(Activity.RESULT_OK, intention);
//                finish();
//            }
//        }
//    }

    @Override
    public void onBackPressed() {

        Intent result = new Intent();

        setResult(Activity.RESULT_CANCELED, result);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
