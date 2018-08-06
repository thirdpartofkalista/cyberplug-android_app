package com.criss.cyberplug;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddDeviceActivity extends AppCompatActivity {

    private Button submit;

    private EditText deviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        submit = findViewById(R.id.submit);
        deviceName = findViewById(R.id.device_name_edit_text);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = deviceName.getText().toString();

                Intent result = new Intent();
                result.putExtra("name", name);

                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });
    }
}
