package com.criss.cyberplug;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.criss.cyberplug.R;
import com.google.zxing.Result;

import java.util.Scanner;

public class QRcodeActivity extends AppCompatActivity {

    private CodeScanner scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        scanner = new CodeScanner(this, scannerView);

        scanner.setCamera(CodeScanner.CAMERA_BACK);

        scanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                Scanner creds = new Scanner(result.getText());
                Intent intent = new Intent();
                intent.putExtra("SSID", creds.nextLine());
                intent.putExtra("PASS", creds.nextLine());
                setResult(Activity.RESULT_OK, intent);
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
