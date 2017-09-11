package com.jujinziben.duty.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * @author MaTianyu
 * @date 2014-07-31
 */
public class Toastor {

    private static Toast mToast;
    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    public static Toast getSingletonToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(sContext, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        return mToast;
    }

    public static Toast getSingleLongToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(sContext, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
        }
        return mToast;
    }

    public static Toast getToast(String text) {
        return Toast.makeText(sContext, text, Toast.LENGTH_SHORT);
    }

    public static Toast getLongToast(String text) {
        return Toast.makeText(sContext, text, Toast.LENGTH_LONG);
    }

    public static void showToast(int resId) {
        showToast(sContext.getString(resId));
    }

    public static void showToast(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        getSingletonToast(text).show();
    }

    public static void showLongToast(int resId) {
        showLongToast(sContext.getString(resId));
    }

    public static void showLongToast(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        getSingleLongToast(text).show();
    }
}
