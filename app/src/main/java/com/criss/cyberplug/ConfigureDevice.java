package com.criss.cyberplug;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class ConfigureDevice extends AppCompatActivity {

    private EditText wifiName;
    private EditText wifiPassword;
    private EditText deviceName;
    private Button save;

    private String deviceUrl = "http://192.168.0.1/wifisave";

    private URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_device);

        String key = getIntent().getStringExtra("key");

        Toolbar toolbar = findViewById(R.id.configure_device_toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Configure the device");

        wifiName = findViewById(R.id.wifi_name_edittext);
        wifiPassword = findViewById((R.id.password_editview));
        deviceName = findViewById(R.id.device_name_edittext);
        save = findViewById(R.id.save_button);

        String mWifiName = wifiName.getText().toString();
        String mWifiPassword = wifiPassword.getText().toString();
        final String mDeviceName = deviceName.getText().toString();

        String mKey = null;

        try {
            mWifiName = URLEncoder.encode(mWifiName, "UTF-8");
            mWifiPassword = URLEncoder.encode(mWifiPassword, "UTF-8");
            mKey = URLEncoder.encode(key, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            url = new URL(deviceUrl + "?ssid=" + mWifiName + "&pass=" + mWifiPassword + "&key=" + mKey);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");

                    conn.connect();

                    conn.disconnect();

                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
                    wifiManager.disconnect();
                    wifiManager.reconnect();

                    Intent intention = new Intent();
                    intention.putExtra("name", mDeviceName);

                    setResult(Activity.RESULT_OK, intention);
                    finish();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
