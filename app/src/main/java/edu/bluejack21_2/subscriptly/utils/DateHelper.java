package edu.bluejack21_2.subscriptly.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.bluejack21_2.subscriptly.models.TransactionHeader;

public class DateHelper {

    public static String formatDate(Calendar calendar, String pattern) {
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        return sdf.format(date);
    }
}
