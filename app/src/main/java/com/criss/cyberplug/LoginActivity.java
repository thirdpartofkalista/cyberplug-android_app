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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.criss.cyberplug.constants.Preferences;
import com.criss.cyberplug.networking.NetworkHandler;
import com.criss.cyberplug.types.thread_communication.MessageType;

public class LoginActivity extends AppCompatActivity {

    private Preferences preferences;

    EditText email;

    EditText passField;

    Button login;

    NetworkHandler networkHandler;

    Handler uiHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            NetworkHandler.MessagePayload payload = (NetworkHandler.MessagePayload) msg.obj;

            if (msg.what == MessageType.LOGIN.getValue()) {
                if (msg.arg1 == 0) {
                    Log.i("TOKEN", (String) payload.data);
                    preferences.setToken((String) payload.data);
                    preferences.setLoggedIn(true);

                    setResult(Activity.RESULT_OK);
                    finish();
                }
                else {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        networkHandler = new NetworkHandler(uiHandler, "");

        Toolbar toolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Login");

        preferences = new Preferences(getApplicationContext());

        email = findViewById(R.id.email_edittext);
        passField = findViewById(R.id.password_edittext_login);
        login = findViewById(R.id.login_button);

        login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String mEmail = email.getText().toString();
                    String password = passField.getText().toString();
                    preferences.setEmail(mEmail);

                    networkHandler.login(mEmail, password);

                }
            });

    }
}
