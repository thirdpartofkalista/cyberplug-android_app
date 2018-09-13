package com.criss.cyberplug;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.criss.cyberplug.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.Scanner;

public class QRcodeActivity extends AppCompatActivity {

    private CodeScanner scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        final CodeScannerView scannerView = findViewById(R.id.scanner_view);
        scanner = new CodeScanner(this, scannerView);

        scanner.setCamera(CodeScanner.CAMERA_BACK);


        scanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String[] creds = result.getText().split("\n");
//                        String creds = result.getText();
                        Intent intent = new Intent();
                        intent.putExtra("SSID", creds[0]);
                        intent.putExtra("PASS", creds[1]);
//                        Toast.makeText(getApplicationContext(), creds[0] + " -- " + creds[1], Toast.LENGTH_LONG).show();
//                        Toast.makeText(getApplicationContext(), result.getBarcodeFormat().toString(), Toast.LENGTH_LONG).show();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        scanner.startPreview();
    }

    @Override
    protected void onPause() {
        scanner.releaseResources();
        super.onPause();
    }
}
