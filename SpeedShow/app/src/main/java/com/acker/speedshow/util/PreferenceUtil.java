package com.acker.speedshow.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by GuoJinyu on 2015/11/16.
 */
public class PreferenceUtil {

    private static PreferenceUtil singleton;
    private SharedPreferences mSharedPreferences;

    private PreferenceUtil(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PreferenceUtil getSingleton(Context context){
        if (singleton == null){
            singleton=new PreferenceUtil(context);
        }
        return singleton;
    }

    public void saveBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    public Boolean getBoolean(String key, boolean def) {
        return mSharedPreferences.getBoolean(key, def);
    }

/*    public void saveInt(String key, int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        return mSharedPreferences.getInt(key, 0);
    }

    public int getInt(String key,int def) {
        return mSharedPreferences.getInt(key, def);
    }*/
}
