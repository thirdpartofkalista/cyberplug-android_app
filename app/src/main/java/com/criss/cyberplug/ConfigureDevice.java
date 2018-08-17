package com.criss.cyberplug;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiInfo;
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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String nWifiName = wifiName.getText().toString();
                final String nWifiPassword = wifiPassword.getText().toString();
                final String nDeviceName = deviceName.getText().toString();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        HttpURLConnection conn = null;

                        try {

//                            String mWifiName = URLEncoder.encode(nWifiName, "UTF-8");
//                            String mWifiPassword = URLEncoder.encode(nWifiPassword, "UTF-8");
//                            String mKey = URLEncoder.encode(key, "UTF-8");

                            String mWifiName = URLEncoder.encode("Around25", "UTF-8");
                            String mWifiPassword = URLEncoder.encode("a25network", "UTF-8");
                            String mKey = URLEncoder.encode("name@example.com", "UTF-8");

                            String finalUrl = deviceUrl + "?s=" +
                                    mWifiName +
                                    "&p=" +
                                    mWifiPassword +
                                    "&devkey=" +
                                    mKey;

                            Log.i(TAG, finalUrl);

                            URL url = new URL(finalUrl);

                            conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");

                            conn.setDoOutput(false);
                            conn.setDoInput(true);

                            conn.connect();

                            Log.i(TAG, "Response code: " + String.valueOf(conn.getResponseCode()) + "; response message: " + String.valueOf(conn.getResponseMessage()));


                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {

                            try {
                                conn.disconnect();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(getApplication().WIFI_SERVICE);
                                if (wifiManager != null) {
                                    wifiManager.disconnect();
                                }
                                if (wifiManager != null) {
                                    wifiManager.reconnect();
                                }

                                WifiInfo inf = wifiManager.getConnectionInfo();

                                while (inf.getNetworkId() == -1) {
                                    inf = wifiManager.getConnectionInfo();
                                }


                                Intent intention = new Intent();
                                intention.putExtra("name", nDeviceName);


                                setResult(Activity.RESULT_OK, intention);
                                finish();
                            }
                        }
                    }
                });
                thread.start();

            }
        });

    }
}
