package com.ardi.kamus.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class KamusPreference {
    private SharedPreferences preferences;

    public KamusPreference(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setFirstRun(boolean input){
        SharedPreferences.Editor editor = preferences.edit();
        String key = "kamus_pref";
        editor.putBoolean(key, input);
        editor.apply();
    }

    public Boolean getFirstRun(){
        String key = "kamus_pref";
        return preferences.getBoolean(key, true);
    }
}
