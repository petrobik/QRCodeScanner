package com.bikshanov.qrcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ScanResultActivity extends AppCompatActivity {

    private TextView mScanResultTextView;
    private ImageView mScanResultImageVIew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        mScanResultTextView = findViewById(R.id.scan_result_textView);
        mScanResultImageVIew = findViewById(R.id.scan_result_imageView);

        Intent intent = getIntent();
        mScanResultTextView.setText(intent.getStringExtra("ScanResult"));
        Bitmap codeBitmap = BitmapFactory.decodeFile(intent.getStringExtra("ImagePath"));
        mScanResultImageVIew.setImageBitmap(codeBitmap);
    }
}
