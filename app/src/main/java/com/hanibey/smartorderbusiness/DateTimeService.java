package com.hanibey.smartorderbusiness;

import android.text.format.DateFormat;

import java.util.Date;

/**
 * Created by Tanju on 27.12.2017.
 */

public class DateTimeService {

    public String getCurrentDate(){
        DateFormat df = new android.text.format.DateFormat();
        String date = df.format("dd.MM.yyyy hh:mm", new Date()).toString();
        return  date;
    }
}
