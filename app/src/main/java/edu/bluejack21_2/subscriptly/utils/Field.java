package edu.bluejack21_2.subscriptly.utils;

import android.text.Editable;

public class Field {

    public static String getContent(Editable editable) {
        return editable.toString().trim();
    }
}
