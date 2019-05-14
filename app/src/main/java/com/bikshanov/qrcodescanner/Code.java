package com.bikshanov.qrcodescanner;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "code_table")
public class Code {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String code;

    private String format;

    public void setDate(Date date) {
        this.date = date;
    }

    private Date date;

    public Code(String code, String format) {
        this.code = code;
        this.format = format;
        date = new Date();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getFormat() {
        return format;
    }

    public Date getDate() {
        return date;
    }
}
