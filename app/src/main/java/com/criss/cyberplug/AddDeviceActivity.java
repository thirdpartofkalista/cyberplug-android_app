package com.criss.cyberplug;


import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.criss.cyberplug.types.intents.RequestCode;

;

public class AddDeviceActivity extends AppCompatActivity {

    private EditText deviceWifiName;

    private EditText deviceWifiPassword;

    private Button scanQR;

    private Button nextButton;

    private String mDeviceWifiName;

    private String mDeviceWifiPassword;

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            moveToConfig();
        }
    };


    public void moveToConfig() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        WifiConfiguration conf = new WifiConfiguration();
//                conf.SSID = String.format("\"%s\"", mDeviceWifiName);
//                conf.preSharedKey = String.format("\"%s\"", mDeviceWifiPassword);
        conf.SSID = "\'" + mDeviceWifiName + "\'";
        if (mDeviceWifiPassword != "") {
            conf.preSharedKey = "\'" + mDeviceWifiPassword + "\'";
        }

        int netId = wifiManager.addNetwork(conf);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
        Log.i("WIFI ", String.valueOf(netId));

        WifiInfo inf = wifiManager.getConnectionInfo();

        while (inf.getNetworkId() == -1) {
            inf = wifiManager.getConnectionInfo();
        }

        Intent result = new Intent();
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        Toolbar toolbar = findViewById(R.id.add_device_toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Add a new device");

        scanQR = findViewById(R.id.scan_button);
        deviceWifiName = findViewById(R.id.device_wifi_name_edittext);
        deviceWifiPassword = findViewById(R.id.device_password_edittext);
        nextButton = findViewById(R.id.next_button);

        mDeviceWifiName = deviceWifiName.getText().toString();
        mDeviceWifiPassword = deviceWifiPassword.getText().toString();


        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), QRcodeActivity.class);
                startActivityForResult(intent, RequestCode.SCAN_QR.getValue());
            }
        });

        nextButton.setOnClickListener(onClick);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_add_device);

        Toolbar toolbar = findViewById(R.id.add_device_toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Add a new device");

        scanQR = findViewById(R.id.scan_button);
        deviceWifiName = findViewById(R.id.device_wifi_name_edittext);
        deviceWifiPassword = findViewById(R.id.device_password_edittext);
        nextButton = findViewById(R.id.next_button);

        mDeviceWifiName = deviceWifiName.getText().toString();
        mDeviceWifiPassword = deviceWifiPassword.getText().toString();


        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), QRcodeActivity.class);
                startActivityForResult(intent, RequestCode.SCAN_QR.getValue());
            }
        });

        nextButton.setOnClickListener(onClick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestCode.SCAN_QR.getValue()) {
            if (resultCode == Activity.RESULT_OK) {
                deviceWifiName.setText(data.getStringExtra("SSID"));
                deviceWifiPassword.setText(data.getStringExtra("PASS"));

                moveToConfig();
            }
        }
    }

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
