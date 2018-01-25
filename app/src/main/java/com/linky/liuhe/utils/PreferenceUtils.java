package com.linky.liuhe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author linky
 * @date 1/24/18
 */

public class PreferenceUtils {

    private static final String SHARE_PREFERENCE = "share_pref";

    public static SharedPreferences getSharePref(Context context) {
        return context.getSharedPreferences(SHARE_PREFERENCE, Context.MODE_PRIVATE);
    }

    public static void putInt(SharedPreferences pre, String key, int value) {
        SharedPreferences.Editor editor = pre.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(SharedPreferences pre, String key, int defVal) {
        return pre.getInt(key, defVal);
    }
}
