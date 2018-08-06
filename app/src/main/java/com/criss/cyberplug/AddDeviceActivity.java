package com.criss.cyberplug;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddDeviceActivity extends AppCompatActivity {

    private Button submit;

    private EditText deviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        Toolbar toolbar = findViewById(R.id.add_device_toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

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
