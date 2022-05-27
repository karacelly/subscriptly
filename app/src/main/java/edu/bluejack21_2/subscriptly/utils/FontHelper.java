package edu.bluejack21_2.subscriptly.utils;

import android.content.Context;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import edu.bluejack21_2.subscriptly.R;

public class FontHelper {
    private static Typeface outfitMedium, outfitSemiBold, outfitBold;

    private static void initFont (Context context) {
        outfitMedium = ResourcesCompat.getFont(context, R.font.outfit_medium);
        outfitSemiBold = ResourcesCompat.getFont(context, R.font.outfit_semi_bold);
        outfitBold = ResourcesCompat.getFont(context, R.font.outfit_bold);
    }

    public static Typeface getOutfitMedium(Context context) {
        if(outfitMedium == null) initFont(context);
        return outfitMedium;
    }

    public static Typeface getOutfitSemiBold(Context context) {
        if(outfitSemiBold == null) initFont(context);
        return outfitSemiBold;
    }

    public static Typeface getOutfitBold(Context context) {
        if(outfitBold == null) initFont(context);
        return outfitBold;
    }
}
