package com.bikshanov.qrcodescanner;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Main2Activity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private BottomNavigationView mBottomNavigationView;
    private Snackbar mSnackbar;
    private FloatingActionButton mFab;
    private CodeViewModel mCodeViewModel;

    public static final String KEY_CLIPBOARD_COPY = "key_clipboard";
    public static final String KEY_BEEP = "key_beep";

    boolean beepOn;
    boolean copyToClipboard;

    SharedPreferences sharedPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        beepOn = sharedPrefs.getBoolean(KEY_BEEP, false);
        copyToClipboard = sharedPrefs.getBoolean(KEY_CLIPBOARD_COPY, false);

        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_history:
                        loadFragment(new HistoryFragment());
                        return true;
                    case R.id.navigation_settings:
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
                new IntentIntegrator(Main2Activity.this)
                        .setOrientationLocked(false)
                        .setBarcodeImageEnabled(true)
                        .setCaptureActivity(CustomScannerActivity.class)
                        .setBeepEnabled(beepOn)
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
            if (result.getContents() != null) {

                BarcodeFormat barcodeFormat = Utils.getBarcodeFormat(result.getFormatName());

                Result res = new Result(result.getContents(), null, null, barcodeFormat);
                ParsedResult parsedResult = ResultParser.parseResult(res);
                String scanResult = result.getContents();
                String imagePath = result.getBarcodeImagePath();
                String format = result.getFormatName();
                String type = parsedResult.getType().toString();

                Intent intent = new Intent(Main2Activity.this, ScanResultActivity.class);
                intent.putExtra("ScanResult", scanResult);
                intent.putExtra("ImagePath", imagePath);
                intent.putExtra("Format", format);
                intent.putExtra("Type", type);

                Code code = new Code(scanResult, format, type);
                mCodeViewModel.insert(code);

                intent.putExtra("ID", code.getId());

                if (copyToClipboard) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("scan_result", parsedResult.getDisplayResult());
                    clipboard.setPrimaryClip(clip);
                }

                startActivity(intent);
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case KEY_BEEP:
                beepOn = sharedPreferences.getBoolean(KEY_BEEP, false);
                break;
            case KEY_CLIPBOARD_COPY:
                copyToClipboard = sharedPreferences.getBoolean(KEY_CLIPBOARD_COPY, false);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(this);
    }
}
