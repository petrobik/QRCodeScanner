package com.bikshanov.qrcodescanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.CalendarParsedResult;
import com.google.zxing.client.result.EmailAddressParsedResult;
import com.google.zxing.client.result.ExpandedProductParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ProductParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.SMSParsedResult;
import com.google.zxing.client.result.TelParsedResult;
import com.google.zxing.client.result.URIParsedResult;
import com.journeyapps.barcodescanner.Util;

import java.util.ArrayList;
import java.util.List;

public class ScanResultActivity extends AppCompatActivity implements View.OnClickListener {

    private CodeViewModel mCodeViewModel;

    private TextView mScanResultTextView;
    private ImageView mScanResultImageView;
    private TextView mResultFormatTextView;
    private TextView mResultTypeTextView;

    String mResult;
    String mResultFormat;
    String mResultType;
    int mCodeId;

    ParsedResult mParsedResult;

    List<Button> mButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(R.string.scan_result_activity_title);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        mCodeViewModel = ViewModelProviders.of(this).get(CodeViewModel.class);

        mScanResultTextView = findViewById(R.id.scan_result_text_view);
        mScanResultImageView = findViewById(R.id.scan_result_image_view);
        mResultFormatTextView = findViewById(R.id.result_format_text_view);
        mResultTypeTextView = findViewById(R.id.result_type_text_view);

        Button searchProductButton = findViewById(R.id.product_search_button);
        Button openBrowserButton = findViewById(R.id.open_browser_button);
        Button shareButton = findViewById(R.id.share_button);
        Button sendSmsButton = findViewById(R.id.send_sms_button);
        Button searchButton = findViewById(R.id.search_button);
        Button dialButton = findViewById(R.id.dial_button);
        Button sendEmailButton = findViewById(R.id.send_email_button);
        Button addContactButton = findViewById(R.id.add_contact_button);
        Button addEventButton = findViewById(R.id.add_event_button);

        searchProductButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        openBrowserButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);
        sendSmsButton.setOnClickListener(this);
        dialButton.setOnClickListener(this);
        sendEmailButton.setOnClickListener(this);
        addEventButton.setOnClickListener(this);

        Intent intent = getIntent();
        mResult = intent.getStringExtra("ScanResult");
        mResultFormat = intent.getStringExtra("Format");
        mResultType = intent.getStringExtra("Type");

        mCodeId = intent.getIntExtra("ID", -1);

//        mCodeViewModel.getCodeById(mCodeId).observe(this, new Observer<Code>() {
//            @Override
//            public void onChanged(Code code) {
//                mResult = code.getCode();
//                mResultFormat = code.getFormat();
//                mResultType = code.getType();
//            }
//        });

//        BarcodeFormat barcodeFormat = Utils.getBarcodeFormat(mResultFormat);
//
        Result result = new Result(mResult, null, null,
                Utils.getBarcodeFormat(mResultFormat));
        mParsedResult = ResultParser.parseResult(result);

//        mParsedResult = Utils.getParsedResult(mResult, Utils.getBarcodeFormat(mResultFormat));

        mButtons = new ArrayList<>();
        mButtons.add(searchProductButton);
        mButtons.add(searchButton);
        mButtons.add(openBrowserButton);
        mButtons.add(sendSmsButton);
        mButtons.add(dialButton);
        mButtons.add(sendEmailButton);
        mButtons.add(addContactButton);
        mButtons.add(addEventButton);

        switch (mResultType) {
            case "PRODUCT":
                setButtonsVisibility(searchProductButton);
                break;
            case "URI":
                setButtonsVisibility(openBrowserButton);
                break;
            case "SMS":
                setButtonsVisibility(sendSmsButton);
                break;
            case "TEXT":
                setButtonsVisibility(searchButton);
                break;
            case "TEL":
                setButtonsVisibility(dialButton);
                break;
            case "EMAIL_ADDRESS":
                setButtonsVisibility(sendEmailButton);
                break;
            case "ADDRESSBOOK":
                setButtonsVisibility(addContactButton);
                break;
            case "CALENDAR":
                setButtonsVisibility(addEventButton);
                break;
        }

//        mScanResultTextView.setText(mResult);
        mScanResultTextView.setText(mParsedResult.getDisplayResult());
        mResultFormatTextView.setText(mResultFormat);
        mResultTypeTextView.setText(mResultType);

        if (intent.getStringExtra("ImagePath") != null) {
            mScanResultImageView.setVisibility(View.VISIBLE);
            Bitmap codeBitmap = BitmapFactory.decodeFile(intent.getStringExtra("ImagePath"));
            mScanResultImageView.setImageBitmap(codeBitmap);
        } else {
            mScanResultImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_browser_button:
                URIParsedResult uriResult = (URIParsedResult) mParsedResult;
                String uri = uriResult.getURI();
                Utils.openUrl(this, uri);
                break;
            case R.id.product_search_button:
                if (mParsedResult instanceof ProductParsedResult) {
                    Utils.openProductSearch(this, ((ProductParsedResult) mParsedResult).getNormalizedProductID());
                } else if (mParsedResult instanceof ExpandedProductParsedResult) {
                    Utils.openProductSearch(this, ((ExpandedProductParsedResult) mParsedResult).getRawText());
                }
                break;
            case R.id.share_button:
                shareContent(mParsedResult.getDisplayResult());
                break;
            case R.id.send_sms_button:
                SMSParsedResult smsResult = (SMSParsedResult) mParsedResult;
                Utils.sendSms(this, smsResult.getNumbers()[0], smsResult.getBody());
                break;
            case R.id.search_button:
                Utils.webSearch(this, mParsedResult.getDisplayResult());
                break;
            case R.id.dial_button:
                TelParsedResult telResult = (TelParsedResult) mParsedResult;
                Utils.dialPhone(this, telResult.getTelURI());
                break;
            case R.id.send_email_button:
                EmailAddressParsedResult emailResult = (EmailAddressParsedResult) mParsedResult;
                Utils.sendEmail(this, emailResult.getTos(), emailResult.getCCs(),
                        emailResult.getBCCs(), emailResult.getSubject(), emailResult.getBody());
                break;
            case R.id.add_contact_button:
                AddressBookParsedResult addressResult = (AddressBookParsedResult) mParsedResult;
                String[] addresses = addressResult.getAddresses();
                String address1 = addresses == null || addresses.length < 1 ? null : addresses[0];
                String[] addressTypes = addressResult.getAddressTypes();
                String address1Type = addressTypes == null || addressTypes.length < 1 ? null : addressTypes[0];
                Utils.addContact(this,
                        addressResult.getNames(),
                        addressResult.getNicknames(),
                        addressResult.getPronunciation(),
                        addressResult.getPhoneNumbers(),
                        addressResult.getPhoneTypes(),
                        addressResult.getEmails(),
                        addressResult.getEmailTypes(),
                        addressResult.getNote(),
                        addressResult.getInstantMessenger(),
                        address1,
                        address1Type,
                        addressResult.getOrg(),
                        addressResult.getTitle(),
                        addressResult.getURLs(),
                        addressResult.getBirthday(),
                        addressResult.getGeo());
                break;
            case R.id.add_event_button:
                CalendarParsedResult calendarResult = (CalendarParsedResult) mParsedResult;
                String description = calendarResult.getDescription();
                String organizer = calendarResult.getOrganizer();

                if (organizer != null) {
                    if (description == null) {
                        description = organizer;
                    } else {
                        description = description + '\n' + organizer;
                    }
                }

                Utils.addCalendarEvent(this,
                        calendarResult.getSummary(),
                        calendarResult.getStartTimestamp(),
                        calendarResult.isStartAllDay(),
                        calendarResult.getEndTimestamp(),
                        calendarResult.getLocation(),
                        calendarResult.getDescription(),
                        calendarResult.getAttendees());
                break;
        }
    }

    private void shareContent(String share) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, share);
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "Share"));
    }

    private void setButtonsVisibility(Button button) {
        for (Button b: mButtons) {
            if (b.getId() == button.getId()) {
                b.setVisibility(View.VISIBLE);
            } else {
                b.setVisibility(View.GONE);
            }
        }
    }
}
