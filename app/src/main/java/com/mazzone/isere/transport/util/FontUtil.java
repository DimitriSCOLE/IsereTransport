package com.mazzone.isere.transport.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.widget.TextView;

public class FontUtil {

    private Typeface typeFace = null;

    public final static String ROBOTO_MONO_PATH = "fonts/roboto-mono-regular.ttf";

    public FontUtil(@NonNull Context context, @NonNull String fontPath) {
        //create typeFace
        if (!fontPath.isEmpty())
            typeFace = Typeface.createFromAsset(context.getResources().getAssets(), fontPath);
    }

    public FontUtil setTypeFace(TextView view) {
        //Set typeface
        if (typeFace != null && view != null)
            view.setTypeface(typeFace);
        return this;
    }
}
