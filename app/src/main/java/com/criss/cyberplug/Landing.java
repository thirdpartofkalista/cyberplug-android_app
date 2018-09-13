package com.criss.cyberplug;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.criss.cyberplug.constants.Preferences;
import com.criss.cyberplug.types.intents.RequestCode;

public class Landing extends AppCompatActivity {

    Button create_account_button;
    Button log_in_button;

    boolean auto_login = false;



    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        preferences = new Preferences(getApplicationContext());
        if (auto_login == true){
            goToMain();
        }
        if (preferences.isLoggedIn()) {
            goToMain();
        }

        create_account_button = findViewById(R.id.create_account_button);
        log_in_button = findViewById(R.id.log_in_button);

        create_account_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), CreateAccountActivity.class);
                startActivityForResult(intent, RequestCode.CREATE_ACCOUNT.getValue());
            }
        });

        log_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), LoginActivity.class);
                startActivityForResult(intent, RequestCode.LOG_IN.getValue());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.CREATE_ACCOUNT.getValue()) {
            if (resultCode == Activity.RESULT_OK) {
                goToMain();
            }
        }
        else if (requestCode == RequestCode.LOG_IN.getValue()){
            if (resultCode == Activity.RESULT_OK){
                goToMain();
            }
        }
    }

    private void goToMain() {
        Intent mIntent = new Intent(getApplication(), MainActivity.class);
        startActivity(mIntent);
        finish();
    }
}
