package com.criss.cyberplug;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GroupSettings extends AppCompatActivity {

    Button delete;

    Intent intent;

    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_settings);

        intent = getIntent();
        index = intent.getIntExtra("index", -1);

        delete = findViewById(R.id.delete_group_button);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = new Intent();
                result.putExtra("delete", true);
                result.putExtra("index", index);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });




    }
}
