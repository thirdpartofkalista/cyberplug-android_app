package com.criss.cyberplug;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button ON;
    Button OFF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ON = findViewById(R.id.button);
        OFF = findViewById(R.id.button2);

        ON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //turn on cyberplug
                ON.setText("turned on cyberplug yes");
            }
        });
        OFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OFF.setText("turned off bitch nigga no cyberplug");
            }
        });
    }
}

