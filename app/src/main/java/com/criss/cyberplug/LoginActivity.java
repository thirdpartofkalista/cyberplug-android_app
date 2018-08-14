package com.criss.cyberplug;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.criss.cyberplug.constants.Preferences;
import com.criss.cyberplug.networking.NetworkHandler;
import com.criss.cyberplug.types.thread_communication.MessageType;

public class LoginActivity extends AppCompatActivity {

    private Preferences preferences;

    EditText userName;

    EditText passField;

    Button login;

    NetworkHandler networkHandler;

    Handler uiHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            NetworkHandler.MessagePayload payload = (NetworkHandler.MessagePayload) msg.obj;

            if (msg.what == MessageType.LOGIN.getValue()) {
                preferences.setToken((String) payload.data);
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        networkHandler = new NetworkHandler(uiHandler, "");

        preferences = new Preferences(getApplicationContext());

        if (preferences.isLoggedIn()) {
            launchMainActivity();
        }
        else {

            userName = findViewById(R.id.username_edit_text);
            passField = findViewById(R.id.password_edit_text);
            login = findViewById(R.id.login_button);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String user = userName.getText().toString();
                    String password = passField.getText().toString();
                    preferences.setUserName(user);
                    preferences.setLoggedIn(true);

                    try {
                        networkHandler.login(user, password);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    launchMainActivity();
                }
            });

        }
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
