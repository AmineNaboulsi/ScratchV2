package com.aadevelopers.onlyscratchiapp.sharedPref;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    private static final String PREF_NAME = "scracth_and_win";


    public PrefManager(Context context) {
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setString(String PREF_NAME, String VAL) {
        editor.putString(PREF_NAME, VAL);
        editor.commit();
    }

    public String getString(String PREF_NAME) {
        if (pref.contains(PREF_NAME)) {
            return pref.getString(PREF_NAME, null);
        }
        return "";
    }


}
