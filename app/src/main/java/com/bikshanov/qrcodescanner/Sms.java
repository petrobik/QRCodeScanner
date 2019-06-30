package com.bikshanov.qrcodescanner;

public class Sms {
    public static String[] breakString(String s) {
        String[] smsElements = s.split("\n");

        return smsElements;
    }
}
