package com.bikshanov.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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

import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private Snackbar mSnackbar;
    private FloatingActionButton mFab;
    private CodeViewModel mCodeViewModel;



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
//                        mSnackbar = Snackbar.make(findViewById(R.id.main_layout), item.getTitle().toString() + " checked", Snackbar.LENGTH_SHORT);
//                        mSnackbar.setAnchorView(R.id.fab);
//                        mSnackbar.show();
                        loadFragment(new HistoryFragment());
                        return true;
                    case R.id.navigation_settings:
//                        mSnackbar = Snackbar.make(findViewById(R.id.main_layout), item.getTitle().toString() + " checked", Snackbar.LENGTH_SHORT);
//                        mSnackbar.setAnchorView(R.id.fab);
//                        mSnackbar.show();
                        loadFragment(new SettingsFragment());
                        return true;
                }
                return false;
            }
        });

        mCodeViewModel = ViewModelProviders.of(this).get(CodeViewModel.class);

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

        if (savedInstanceState == null) {
            loadFragment(new HistoryFragment());
        }
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
                String format = parsedResult.getType().toString();
//                mResultTextView.setText(parsedResult.getDisplayResult());
//                mTypeTextView.setText(parsedResult.getType().toString());
//                Bitmap codeBitmap = BitmapFactory.decodeFile(result.getBarcodeImagePath());
//                mCodeImageView.setImageBitmap(codeBitmap);

                Intent intent = new Intent(Main2Activity.this, ScanResultActivity.class);
                intent.putExtra("ScanResult", scanResult);
                intent.putExtra("ImagePath", imagePath);
                intent.putExtra("Format", format);

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
//                } else {
//                    startActivity(intent);
//                }

                startActivity(intent);

                Code code = new Code(scanResult, format);
                mCodeViewModel.insert(code);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
