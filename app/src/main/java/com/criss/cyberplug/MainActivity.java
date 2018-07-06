package com.criss.cyberplug;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    Button ON;
    Button OFF;

    String status;


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

                try{
                    sendRequest(ON.getText().toString());
                }
                catch(Exception ex){

                }
            }
        });
        OFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    sendRequest(OFF.getText().toString());
                }
                catch(Exception ex){

                }
            }
        });
    }

    public void sendRequest(String x) throws UnsupportedEncodingException, MalformedURLException {
        String data = URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(x, "UTF-8");
        try {
            URL url = new URL("https://www.google.com/upstream");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

