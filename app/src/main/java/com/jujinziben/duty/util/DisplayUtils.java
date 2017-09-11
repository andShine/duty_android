package com.jujinziben.duty.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * @author MaTianyu
 * @date 2015-04-19
 */
public class DisplayUtils {

    private static final String TAG = DisplayUtils.class.getSimpleName();
    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    /**
     * 获取 显示信息
     */
    public static DisplayMetrics getDisplayMetrics() {
        DisplayMetrics dm = sContext.getResources().getDisplayMetrics();
        return dm;
    }

    public static int getHeight() {
        return getDisplayMetrics().heightPixels;
    }

    public static int getWidth() {
        return getDisplayMetrics().widthPixels;
    }

    /**
     * 打印 显示信息
     */
    public static DisplayMetrics printDisplayInfo() {
        DisplayMetrics dm = getDisplayMetrics();

        StringBuilder sb = new StringBuilder();
        sb.append("_______  显示信息:  ");
        sb.append("\ndensity         :").append(dm.density);
        sb.append("\ndensityDpi      :").append(dm.densityDpi);
        sb.append("\nheightPixels    :").append(dm.heightPixels);
        sb.append("\nwidthPixels     :").append(dm.widthPixels);
        sb.append("\nscaledDensity   :").append(dm.scaledDensity);
        sb.append("\nxdpi            :").append(dm.xdpi);
        sb.append("\nydpi            :").append(dm.ydpi);
        Log.i(TAG, sb.toString());

        return dm;
    }

    /**
     * 根据手机的分辨率从dp的单位转成为px(像素)
     */
    public static int dp2Px(float dp) {
        float scale = getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从px(像素)的单位转成为dp
     */
    public static int px2dp(float px) {
        float scale = getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从sp的单位转成为px
     */
    public static int sp2px(float sp) {
        final float fontScale = getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从px的单位转成为sp
     */
    public static int px2sp(float px) {
        final float fontScale = getDisplayMetrics().scaledDensity;
        return (int) (px / fontScale + 0.5f);
    }
}
