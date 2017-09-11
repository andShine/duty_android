package com.jujinziben.duty.util;

import android.os.Handler;
import android.os.Looper;

/**
 * @author MaTianyu
 * @date 2015-03-12
 */
public class HandlerUtils {

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    public static void runOnUiThread(Runnable runnable) {
        HANDLER.post(runnable);
    }

    public static void runOnUiThreadDelay(Runnable runnable, long delayMillis) {
        HANDLER.postDelayed(runnable, delayMillis);
    }

    public static void removeRunable(Runnable runnable) {
        HANDLER.removeCallbacks(runnable);
    }
}
