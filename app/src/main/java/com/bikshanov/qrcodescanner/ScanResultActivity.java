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
    private ImageView mScanResultImageView;
    private TextView mResultFormatTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        mScanResultTextView = findViewById(R.id.scan_result_textView);
        mScanResultImageView = findViewById(R.id.scan_result_imageView);
        mResultFormatTextView = findViewById(R.id.result_format_text_view);

        Intent intent = getIntent();
        mScanResultTextView.setText(intent.getStringExtra("ScanResult"));
        mResultFormatTextView.setText(intent.getStringExtra("Format"));
        Bitmap codeBitmap = BitmapFactory.decodeFile(intent.getStringExtra("ImagePath"));
        mScanResultImageView.setImageBitmap(codeBitmap);
    }
}
