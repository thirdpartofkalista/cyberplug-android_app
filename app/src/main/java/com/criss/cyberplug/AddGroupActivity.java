package com.criss.cyberplug;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;

public class AddGroupActivity extends AppCompatActivity {

    private Button submit1;

    private EditText groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        Toolbar toolbar = findViewById(R.id.add_group_toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        submit1 = findViewById(R.id.submit1);
        groupName = findViewById(R.id.group_name_edit_text);

        submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = groupName.getText().toString();

                Intent result = new Intent();
                result.putExtra("name", name);

                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intention = new Intent();

        setResult(Activity.RESULT_CANCELED, intention);
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














