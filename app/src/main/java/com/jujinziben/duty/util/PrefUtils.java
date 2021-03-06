package com.jujinziben.duty.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * PrefUtils, easy to get or put data
 * <ul>
 * <strong>Preference Name</strong>
 * <li>you can change preference name by {@link #PREFERENCE_NAME}</li>
 * </ul>
 * <ul>
 * <strong>Put Value</strong>
 * <li>put string {@link #putString(String, String)}</li>
 * <li>put int {@link #putInt(String, int)}</li>
 * <li>put long {@link #putLong(String, long)}</li>
 * <li>put float {@link #putFloat(String, float)}</li>
 * <li>put boolean {@link #putBoolean(String, boolean)}</li>
 * </ul>
 * <ul>
 * <strong>Get Value</strong>
 * <li>get string {@link #getString(String)}, {@link
 * #getString(String, String)}</li>
 * <li>get int {@link #getInt(String)}, {@link #getInt(
 *String,
 * int)}</li>
 * <li>get long {@link #getLong(String)}, {@link #getLong(
 *String, long)}</li>
 * <li>get float {@link #getFloat(String)}, {@link #getFloat(
 *String, float)}</li>
 * <li>get boolean {@link #getBoolean(String)}, {@link
 * #getBoolean(String, boolean)}</li>
 * </ul>
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-3-6
 */
public class PrefUtils {

    public static String PREFERENCE_NAME;
    private static SharedPreferences sSettings;

    private PrefUtils() {
        throw new AssertionError();
    }

    public static void init(Context context) {
        if (PREFERENCE_NAME == null) {
            PREFERENCE_NAME = context.getPackageName();
        }
        sSettings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static void clear(String key) {
        SharedPreferences.Editor editor = sSettings.edit();
        editor.remove(key).apply();
    }

    public static void clearAll(String... keys) {
        for (String key : keys) {
            clear(key);
        }
    }

    /**
     * put string preferences
     *
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent
     * storage.
     */
    public static boolean putString(String key, String value) {
        if (null == value) {
            return false;
        }
        SharedPreferences.Editor editor = sSettings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * get string preferences
     *
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or null. Throws
     * ClassCastException if there is a preference with this
     * name that is not a string
     * @see #getString(String, String)
     */
    public static String getString(String key) {
        return getString(key, null);
    }

    /**
     * get string preferences
     *
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws
     * ClassCastException if there is a preference with
     * this name that is not a string
     */
    public static String getString(String key, String defaultValue) {

        return sSettings.getString(key, defaultValue);
    }

    /**
     * put int preferences
     *
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent
     * storage.
     */
    public static boolean putInt(String key, int value) {
        SharedPreferences.Editor editor = sSettings.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * get int preferences
     *
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws
     * ClassCastException if there is a preference with this
     * name that is not a int
     * @see #getInt(String, int)
     */
    public static int getInt(String key) {
        return getInt(key, -1);
    }

    /**
     * get int preferences
     *
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws
     * ClassCastException if there is a preference with
     * this name that is not a int
     */
    public static int getInt(String key, int defaultValue) {
        return sSettings.getInt(key, defaultValue);
    }

    /**
     * put long preferences
     *
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent
     * storage.
     */
    public static boolean putLong(String key, long value) {
        SharedPreferences.Editor editor = sSettings.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    /**
     * get long preferences
     *
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws
     * ClassCastException if there is a preference with this
     * name that is not a long
     * @see #getLong(String, long)
     */
    public static long getLong(String key) {
        return getLong(key, -1);
    }

    /**
     * get long preferences
     *
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws
     * ClassCastException if there is a preference with
     * this name that is not a long
     */
    public static long getLong(String key, long defaultValue) {
        return sSettings.getLong(key, defaultValue);
    }

    /**
     * put float preferences
     *
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent
     * storage.
     */
    public static boolean putFloat(String key, float value) {
        SharedPreferences.Editor editor = sSettings.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    /**
     * get float preferences
     *
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws
     * ClassCastException if there is a preference with this
     * name that is not a float
     * @see #getFloat(String, float)
     */
    public static float getFloat(String key) {
        return getFloat(key, -1);
    }

    /**
     * get float preferences
     *
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws
     * ClassCastException if there is a preference with
     * this name that is not a float
     */
    public static float getFloat(String key, float defaultValue) {
        return sSettings.getFloat(key, defaultValue);
    }

    /**
     * put boolean preferences
     *
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent
     * storage.
     */
    public static boolean putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sSettings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * get boolean preferences, default is false
     *
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or false. Throws
     * ClassCastException if there is a preference with this
     * name that is not a boolean
     * @see #getBoolean(String, boolean)
     */
    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * get boolean preferences
     *
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws
     * ClassCastException if there is a preference with
     * this name that is not a boolean
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        return sSettings.getBoolean(key, defaultValue);
    }
}