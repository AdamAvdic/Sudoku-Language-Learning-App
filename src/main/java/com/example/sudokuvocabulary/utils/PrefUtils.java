package com.example.sudokuvocabulary.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {

    //save preferences
    public static void saveBoolPreference(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    //load saved preferences
    public static boolean loadBoolPreference(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }
}
