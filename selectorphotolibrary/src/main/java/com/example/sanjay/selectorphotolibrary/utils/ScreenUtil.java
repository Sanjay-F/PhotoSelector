package com.example.sanjay.selectorphotolibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by Sanjay on 2015/12/23 on project android.
 */
public class ScreenUtil {


    public static  int getHeightPixels(Activity activity) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static  int getWidthPixels(Activity activity) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getStatusBarHeight(@NonNull Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;

    }

    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return 0;
    }
}
