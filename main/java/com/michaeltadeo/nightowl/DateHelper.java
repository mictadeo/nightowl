package com.michaeltadeo.nightowl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateHelper {

    public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat
            ("MM-dd-yyyy h:mm a z", Locale.getDefault());

    public static long getDateAndTime (String dateInput) {

        try {
            Date date = DateHelper.dateTimeFormat.parse
                    (dateInput + TimeZone.getDefault().getDisplayName());
            return date.getTime();
        }
        catch (ParseException e) {
            return 0;
        }
    }
}
