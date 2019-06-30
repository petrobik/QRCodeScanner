package com.bikshanov.qrcodescanner;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;

import java.util.ArrayList;
import java.util.Locale;

public class Utils {

    private static final String[] EMAIL_TYPE_STRINGS = {"home", "work", "mobile"};
    private static final String[] PHONE_TYPE_STRINGS = {"home", "work", "mobile", "fax", "pager", "main"};
    private static final String[] ADDRESS_TYPE_STRINGS = {"home", "work"};
    private static final int[] EMAIL_TYPE_VALUES = {
            ContactsContract.CommonDataKinds.Email.TYPE_HOME,
            ContactsContract.CommonDataKinds.Email.TYPE_WORK,
            ContactsContract.CommonDataKinds.Email.TYPE_MOBILE,
    };
    private static final int[] PHONE_TYPE_VALUES = {
            ContactsContract.CommonDataKinds.Phone.TYPE_HOME,
            ContactsContract.CommonDataKinds.Phone.TYPE_WORK,
            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
            ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK,
            ContactsContract.CommonDataKinds.Phone.TYPE_PAGER,
            ContactsContract.CommonDataKinds.Phone.TYPE_MAIN,
    };
    private static final int[] ADDRESS_TYPE_VALUES = {
            ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME,
            ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK,
    };

    private static final int NO_TYPE = -1;

    private static int toPhoneContractType(String typeString) {
        return doToContractType(typeString, PHONE_TYPE_STRINGS, PHONE_TYPE_VALUES);
    }

    private static int toAddressContractType(String typeString) {
        return doToContractType(typeString, ADDRESS_TYPE_STRINGS, ADDRESS_TYPE_VALUES);
    }

    private static int toEmailContractType(String typeString) {
        return doToContractType(typeString, EMAIL_TYPE_STRINGS, EMAIL_TYPE_VALUES);
    }

    private static int doToContractType(String typeString, String[] types, int[] values) {
        if (typeString == null) {
            return NO_TYPE;
        }
        for (int i = 0; i < types.length; i++) {
            String type = types[i];
            if (typeString.startsWith(type) || typeString.startsWith(type.toUpperCase(Locale.ENGLISH))) {
                return values[i];
            }
        }
        return NO_TYPE;
    }

    public static BarcodeFormat getBarcodeFormat(@NonNull String formatName) {

        BarcodeFormat barcodeFormat = BarcodeFormat.QR_CODE;

        switch (formatName) {
            case "AZTEC":
                barcodeFormat = BarcodeFormat.AZTEC;
                break;
            case "CODABAR":
                barcodeFormat = BarcodeFormat.CODABAR;
                break;
            case "CODE_128":
                barcodeFormat = BarcodeFormat.CODE_128;
                break;
            case "CODE_39":
                barcodeFormat = BarcodeFormat.CODE_39;
                break;
            case "CODE_93":
                barcodeFormat = BarcodeFormat.CODE_93;
                break;
            case "DATA_MATRIX":
                barcodeFormat = BarcodeFormat.DATA_MATRIX;
                break;
            case "EAN_13":
                barcodeFormat = BarcodeFormat.EAN_13;
                break;
            case "EAN_8":
                barcodeFormat = BarcodeFormat.EAN_8;
                break;
            case "ITF":
                barcodeFormat = BarcodeFormat.ITF;
                break;
            case "MAXICODE":
                barcodeFormat = BarcodeFormat.MAXICODE;
                break;
            case "PDF_417":
                barcodeFormat = BarcodeFormat.PDF_417;
                break;
            case "QR_CODE":
                barcodeFormat = BarcodeFormat.QR_CODE;
                break;
            case "RSS_14":
                barcodeFormat = BarcodeFormat.RSS_14;
                break;
            case "RSS_EXPANDED":
                barcodeFormat = BarcodeFormat.RSS_EXPANDED;
                break;
            case "UPC_A":
                barcodeFormat = BarcodeFormat.UPC_A;
                break;
            case "UPC_E":
                barcodeFormat = BarcodeFormat.UPC_E;
                break;
        }

        return barcodeFormat;
    }

    public static void sendSms(@NonNull Context context, String number, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + number));
        intent.putExtra("sms_body", body);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static void openUrl(@NonNull Context context, String url) {
        if (url.startsWith("HTTP://")) {
            url = "http://" + url.substring(4);
        } else if (url.startsWith("HTTPS://")) {
            url = "https://" + url.substring(5);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static void openProductSearch(@NonNull Context context, String url) {
        Uri uri = Uri.parse("http://www.google.com/m/products?q=" + url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static void webSearch(@NonNull Context context, String query) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra("query", query);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static void dialPhone(@NonNull Context context, String uri) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static void sendEmail(@NonNull Context context, String[] to, String[] cc,
                                 String[] bcc, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        if (to != null && to.length != 0) {
            intent.putExtra(Intent.EXTRA_EMAIL, to);
        }
        if (cc != null && cc.length != 0) {
            intent.putExtra(Intent.EXTRA_CC, cc);
        }
        if (bcc != null && bcc.length != 0) {
            intent.putExtra(Intent.EXTRA_BCC, bcc);
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.setType("text/plain");
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static ParsedResult getParsedResult(String contents, BarcodeFormat format) {
        Result result = new Result(contents, null, null, format);

        return ResultParser.parseResult(result);
    }

    public static void addContact(Context context, String[] names,
                             String[] nicknames,
                             String pronunciation,
                             String[] phoneNumbers,
                             String[] phoneTypes,
                             String[] emails,
                             String[] emailTypes,
                             String note,
                             String instantMessenger,
                             String address,
                             String addressType,
                             String org,
                             String title,
                             String[] urls,
                             String birthday,
                             String[] geo) {

        Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT, ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, names != null && names.length > 0 ? names[0] : 0);

        intent.putExtra(ContactsContract.Intents.Insert.PHONETIC_NAME, pronunciation);

        if (phoneNumbers != null) {
            int phoneCount = Math.min(phoneNumbers.length, Contents.PHONE_KEYS.length);
            for (int x = 0; x < phoneCount; x++) {
                intent.putExtra(Contents.PHONE_KEYS[x], phoneNumbers[x]);
                if (phoneTypes != null && x < phoneTypes.length) {
                    int type = toPhoneContractType(phoneTypes[x]);
                    if (type >= 0) {
                        intent.putExtra(Contents.PHONE_TYPE_KEYS[x], type);
                    }
                }
            }
        }

        if (emails != null) {
            int emailCount = Math.min(emails.length, Contents.EMAIL_KEYS.length);
            for (int x = 0; x < emailCount; x++) {
                intent.putExtra(Contents.EMAIL_KEYS[x], emails[x]);
                if (emailTypes != null && x < emailTypes.length) {
                    int type = toEmailContractType(emailTypes[x]);
                    if (type >= 0) {
                        intent.putExtra(Contents.EMAIL_TYPE_KEYS[x], type);
                    }
                }
            }
        }

        ArrayList<ContentValues> data = new ArrayList<>();
        if (urls != null) {
            for (String url : urls) {
                if (url != null && !url.isEmpty()) {
                    ContentValues row = new ContentValues(2);
                    row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                    row.put(ContactsContract.CommonDataKinds.Website.URL, url);
                    data.add(row);
                    break;
                }
            }
        }

        if (birthday != null) {
            ContentValues row = new ContentValues(3);
            row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE);
            row.put(ContactsContract.CommonDataKinds.Event.TYPE, ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY);
            row.put(ContactsContract.CommonDataKinds.Event.START_DATE, birthday);
            data.add(row);
        }

        if (nicknames != null) {
            for (String nickname : nicknames) {
                if (nickname != null && !nickname.isEmpty()) {
                    ContentValues row = new ContentValues(3);
                    row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE);
                    row.put(ContactsContract.CommonDataKinds.Nickname.TYPE,
                            ContactsContract.CommonDataKinds.Nickname.TYPE_DEFAULT);
                    row.put(ContactsContract.CommonDataKinds.Nickname.NAME, nickname);
                    data.add(row);
                    break;
                }
            }
        }

        if (!data.isEmpty()) {
            intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, data);
        }

        StringBuilder aggregatedNotes = new StringBuilder();
        if (note != null) {
            aggregatedNotes.append('\n').append(note);
        }
        if (geo != null && geo.length >= 2) {
            aggregatedNotes.append('\n').append(geo[0]).append(',').append(geo[1]);
        }

        if (aggregatedNotes.length() > 0) {
            // Remove extra leading '\n'
            intent.putExtra(ContactsContract.Intents.Insert.NOTES, aggregatedNotes.substring(1));
        }

        if (instantMessenger != null && instantMessenger.startsWith("xmpp:")) {
            intent.putExtra(ContactsContract.Intents.Insert.IM_PROTOCOL, ContactsContract.CommonDataKinds.Im.PROTOCOL_JABBER);
            intent.putExtra(ContactsContract.Intents.Insert.IM_HANDLE, instantMessenger.substring(5));
        } else {
            intent.putExtra(ContactsContract.Intents.Insert.IM_HANDLE, instantMessenger);
        }

        intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
        if (addressType != null) {
            int type = toAddressContractType(addressType);
            if (type >= 0) {
                intent.putExtra(ContactsContract.Intents.Insert.POSTAL_TYPE, type);
            }
        }
        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, org);
        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, title);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static void addCalendarEvent(Context context,
                                        String summary,
                                        long start,
                                        boolean allDay,
                                        long end,
                                        String location,
                                        String description,
                                        String[] attendees) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", start);
        if (allDay) {
            intent.putExtra("allDay", true);
        }
        if (end < 0L) {
            if (allDay) {
                // + 1 day
                end = start + 24 * 60 * 60 * 1000;
            } else {
                end = start;
            }
        }
        intent.putExtra("endTime", end);
        intent.putExtra("title", summary);
        intent.putExtra("eventLocation", location);
        intent.putExtra("description", description);
        if (attendees != null) {
            intent.putExtra(Intent.EXTRA_EMAIL, attendees);
        }

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}