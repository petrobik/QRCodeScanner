package com.bikshanov.qrcodescanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.ViewfinderView;

public class CustomScannerActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private CaptureManager mCapture;
    private DecoratedBarcodeView mBarcodeScannerView;
    private ViewfinderView mViewfinderView;
    private boolean flashOn = false;
    private ImageButton switchFlashlightButton;

    public static final String KEY_AUTO_FOCUS = "key_auto_focus";

    boolean useAutoFocus;

    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(R.string.scan_activity_title);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scanner);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        useAutoFocus = sharedPrefs.getBoolean(KEY_AUTO_FOCUS, true);

        mBarcodeScannerView = findViewById(R.id.zxing_barcode_scanner);

        mBarcodeScannerView.getBarcodeView().getCameraSettings().setAutoFocusEnabled(useAutoFocus);

        mViewfinderView = findViewById(R.id.zxing_viewfinder_view);

        switchFlashlightButton = findViewById(R.id.switch_flashlight);

        switchFlashlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFlashlight();
            }
        });

        if (!hasFlash()) {
            switchFlashlightButton.setVisibility(View.GONE);
        }

        mCapture = new CaptureManager(this, mBarcodeScannerView);
        mCapture.initializeFromIntent(getIntent(), savedInstanceState);
        mCapture.decode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCapture.onResume();
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCapture.onPause();
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCapture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCapture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mBarcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight() {
        if (!flashOn) {
            mBarcodeScannerView.setTorchOn();
            switchFlashlightButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_off));
            flashOn = true;
        } else {
            mBarcodeScannerView.setTorchOff();
            switchFlashlightButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_on));
            flashOn = false;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key == KEY_AUTO_FOCUS) {
            useAutoFocus = sharedPreferences.getBoolean(KEY_AUTO_FOCUS, true);
            mBarcodeScannerView.getBarcodeView().getCameraSettings().setAutoFocusEnabled(useAutoFocus);
        }
    }
}
