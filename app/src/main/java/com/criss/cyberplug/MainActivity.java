package com.criss.cyberplug;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button ON;
    Button OFF;
    byte clicked = 0;
    byte clicked2 = 0;

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
                switch (clicked) {
                    case 1:
                        ON.setText("aha");
                        clicked = 0;
                        break;
                    case 0:
                        ON.setText("mhm");
                        clicked = 1;
                        break;
                }
            }
        });
        OFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (clicked2) {
                    case 1:
                        OFF.setText("yush");
                        clicked2 = 0;
                        break;
                    case 0:
                        OFF.setText("nops");
                        clicked2 = 1;
                        break;
                }
            }
        });
    }
}

