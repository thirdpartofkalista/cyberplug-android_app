package com.criss.cyberplug;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.criss.cyberplug.constants.Preferences;
import com.criss.cyberplug.networking.NetworkHandler;
import com.criss.cyberplug.types.thread_communication.MessageType;

public class CreateAccountActivity extends AppCompatActivity {

    private Preferences preferences;

    Button button;
    EditText email;
    EditText password;

    NetworkHandler networkHandler;

    Handler uiHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            NetworkHandler.MessagePayload payload = (NetworkHandler.MessagePayload) msg.obj;

            if (msg.what == MessageType.CREATE_ACCOUNT.getValue()) {
                if (msg.arg1 == 0) {
                    preferences.setToken((String) payload.data);
                    preferences.setLoggedIn(true);

                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        button = findViewById(R.id.create_button);
        email = findViewById(R.id.for_email_edittext);
        password = findViewById(R.id.for_password_edittext);

        Toolbar toolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Create account");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Landing.class);
                startActivity(intent);
            }
        });
    }
}