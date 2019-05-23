package com.bikshanov.qrcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ScanResultActivity extends AppCompatActivity {

    private TextView mScanResultTextView;
    private ImageView mScanResultImageView;
    private TextView mResultFormatTextView;
    private TextView mResultTypeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(R.string.scan_result_activity_title);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        mScanResultTextView = findViewById(R.id.scan_result_text_view);
        mScanResultImageView = findViewById(R.id.scan_result_image_view);
        mResultFormatTextView = findViewById(R.id.result_format_text_view);
        mResultTypeTextView = findViewById(R.id.result_type_text_view);

        Intent intent = getIntent();
        mScanResultTextView.setText(intent.getStringExtra("ScanResult"));
        mResultFormatTextView.setText(intent.getStringExtra("Format"));
        mResultTypeTextView.setText(intent.getStringExtra("Type"));
        if (intent.getStringExtra("ImagePath") != null) {
            mScanResultImageView.setVisibility(View.VISIBLE);
            Bitmap codeBitmap = BitmapFactory.decodeFile(intent.getStringExtra("ImagePath"));
            mScanResultImageView.setImageBitmap(codeBitmap);
        } else {
            mScanResultImageView.setVisibility(View.GONE);
        }
    }
}
