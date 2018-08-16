package com.criss.cyberplug;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.criss.cyberplug.constants.Url;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class ConfigureDevice extends AppCompatActivity {

    private static final String TAG = "ConfigureDevice";

    private EditText wifiName;
    private EditText wifiPassword;
    private EditText deviceName;
    private Button save;

    private final String deviceUrl = "http://192.168.4.1/wifisave";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_device);

        final String key = getIntent().getStringExtra("key");

        Toolbar toolbar = findViewById(R.id.configure_device_toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Configure the device");

        wifiName = findViewById(R.id.wifi_name_edittext);
        wifiPassword = findViewById((R.id.password_editview));
        deviceName = findViewById(R.id.device_name_edittext);
        save = findViewById(R.id.save_button);

        final String nWifiName = wifiName.getText().toString();
        final String nWifiPassword = wifiPassword.getText().toString();
        final String nDeviceName = deviceName.getText().toString();


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

//                            String mWifiName = URLEncoder.encode(nWifiName, "UTF-8");
//                            String mWifiPassword = URLEncoder.encode(nWifiPassword, "UTF-8");
//                            String mKey = URLEncoder.encode(key, "UTF-8");
//                            String mDeviceName = URLEncoder.encode(nDeviceName, "UTF-8");

                            String mWifiName = URLEncoder.encode("Around25", "UTF-8");
                            String mWifiPassword = URLEncoder.encode("a25network", "UTF-8");
                            String mKey = URLEncoder.encode(key, "UTF-8");
                            String mDeviceName = URLEncoder.encode("numele", "UTF-8");

                            StringBuilder stringBuilder = new StringBuilder(deviceUrl);
                            stringBuilder.append("?s=")
                                    .append(mWifiName)
                                    .append("&p=")
                                    .append(mWifiPassword)
                                    .append("&devkey=")
                                    .append(key)
                                    .append("&name=")
                                    .append(mDeviceName);

                            URL url = new URL(stringBuilder.toString());

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
//                            conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                            conn.setDoOutput(false);

                            conn.connect();

                            conn.disconnect();

                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
                            wifiManager.disconnect();
                            wifiManager.reconnect();

                            Intent intention = new Intent();
                            intention.putExtra("name", nDeviceName);


                            setResult(Activity.RESULT_OK, intention);
                            finish();
                        }
                    }
                });
                thread.start();

            }
        });

    }
}
