package edu.bluejack21_2.subscriptly.utils;

import android.util.Log;

import java.text.NumberFormat;
import java.util.Locale;

public class Currency {

    private static Locale localeID = new Locale("IND", "ID");
    private static NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    public static String formatToRupiah(Double number) {
        String rupiah = formatRupiah.format(number);
        String[] split = rupiah.split(",");
        int length = split[0].length();
        Log.d("splitted[0]", split[0].toString());
        return split[0].substring(0, 2) + " " + split[0].substring(2, length);
    }
}
