package com.criss.cyberplug;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class DeviceSettings extends AppCompatActivity {

    String TAG;

    Button delete;

    Intent intent;

    int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_settings);

        intent = getIntent();
        index = intent.getIntExtra("index", -1);

        delete = findViewById(R.id.delete_button);

        Log.i(TAG, "started activity");

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent result = new Intent();
                result.putExtra("delete", true);
                result.putExtra("index", index);
                Log.i(TAG, "delete: true, index:" + index);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });

    }
}