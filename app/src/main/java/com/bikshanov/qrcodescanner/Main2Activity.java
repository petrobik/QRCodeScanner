package com.bikshanov.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeResult;

public class Main2Activity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private Snackbar mSnackbar;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_history:
                        mSnackbar = Snackbar.make(findViewById(R.id.main_layout), item.getTitle().toString() + " checked", Snackbar.LENGTH_SHORT);
                        mSnackbar.setAnchorView(R.id.fab);
                        mSnackbar.show();
                        return true;
                    case R.id.navigation_settings:
                        mSnackbar = Snackbar.make(findViewById(R.id.main_layout), item.getTitle().toString() + " checked", Snackbar.LENGTH_SHORT);
                        mSnackbar.setAnchorView(R.id.fab);
                        mSnackbar.show();
                        return true;
                }
                return false;
            }
        });

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                IntentIntegrator integrator = new IntentIntegrator(Main2Activity.this);
//                integrator.setBarcodeImageEnabled(true);
//                integrator.setOrientationLocked(false);
//                integrator.initiateScan();

                new IntentIntegrator(Main2Activity.this)
                        .setOrientationLocked(false)
                        .setBarcodeImageEnabled(true)
                        .setCaptureActivity(CustomScannerActivity.class)
                        .initiateScan();
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
                String scanResult = parsedResult.getDisplayResult();
                String imagePath = result.getBarcodeImagePath();
//                mResultTextView.setText(parsedResult.getDisplayResult());
//                mTypeTextView.setText(parsedResult.getType().toString());
//                Bitmap codeBitmap = BitmapFactory.decodeFile(result.getBarcodeImagePath());
//                mCodeImageView.setImageBitmap(codeBitmap);

                Intent intent = new Intent(Main2Activity.this, ScanResultActivity.class);
                intent.putExtra("ScanResult", scanResult);
                intent.putExtra("ImagePath", imagePath);

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
//                } else {
//                    startActivity(intent);
//                }

                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
