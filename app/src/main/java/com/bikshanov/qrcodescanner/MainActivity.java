package com.bikshanov.qrcodescanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private Button mScanButton;
    private TextView mResultTextView;
    private TextView mTypeTextView;
    private ImageView mCodeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScanButton = findViewById(R.id.scan_button);
        mResultTextView = findViewById(R.id.result_textView);
        mTypeTextView = findViewById(R.id.type_textView);
        mCodeImageView = findViewById(R.id.barcode_imageView);

        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
//        integrator.setBarcodeImageEnabled(true);
//        integrator.setOrientationLocked(false);
//        integrator.initiateScan();


        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setBarcodeImageEnabled(true);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();

//                new IntentIntegrator(MainActivity.this)
//                        .setOrientationLocked(false)
//                        .setCaptureActivity(CustomScannerActivity.class)
//                        .initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Result res = new Result(result.getContents(), null, null, BarcodeFormat.QR_CODE);
                ParsedResult parsedResult = ResultParser.parseResult(res);
//                mResultTextView.setText(result.getContents());
                mResultTextView.setText(parsedResult.getDisplayResult());
                mTypeTextView.setText(parsedResult.getType().toString());
                Bitmap codeBitmap = BitmapFactory.decodeFile(result.getBarcodeImagePath());
                mCodeImageView.setImageBitmap(codeBitmap);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void getCodeInfo(ParsedResult parsedResult) {
        switch (parsedResult.getType()) {
            case ADDRESSBOOK:
                AddressBookParsedResult vCard = (AddressBookParsedResult) parsedResult;


        }
    }
}