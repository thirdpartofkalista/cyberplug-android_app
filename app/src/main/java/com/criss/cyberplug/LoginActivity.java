package com.criss.cyberplug;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.criss.cyberplug.constants.Preferences;

public class LoginActivity extends AppCompatActivity {

    private Preferences preferences;

    EditText userName;

    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = new Preferences(getApplicationContext());

        if (preferences.isLoggedIn()) {
            launchMainActivity();
        }
        else {

            userName = findViewById(R.id.username_edit_text);
            login = findViewById(R.id.login_button);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String user = userName.getText().toString();
                    preferences.setUserName(user);
                    preferences.setLoggedIn(true);
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
