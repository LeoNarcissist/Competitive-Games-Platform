package com.example.rift.jiofinal;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static com.example.rift.jiofinal.PreferencesUtility.LOGGED_IN_PREF;
import static com.example.rift.jiofinal.PreferencesUtility.UID;

public class SaveSharedPreference {
    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setLoggedIn(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGGED_IN_PREF, loggedIn);
        editor.apply();
    }

    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);
    }

    public static void setUserId(Context context, String userId) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(UID, userId);
        editor.apply();
    }

    public static String getUserId(Context context) {
        return getPreferences(context).getString(UID, null);
    }
}
