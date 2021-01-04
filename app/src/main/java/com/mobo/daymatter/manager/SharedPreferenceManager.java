package com.mobo.daymatter.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * sp 存储管理
 */
public class SharedPreferenceManager {
    private static final SharedPreferenceManager sOurInstance = new SharedPreferenceManager();

    public static SharedPreferenceManager getInstance() {
        return sOurInstance;
    }

    private static final String BASE_SHARED_PREFERENCES = "base_shared_preferences";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private SharedPreferenceManager() {
    }

    public SharedPreferenceManager init(Context context) {
        if (context == null || mSharedPreferences != null) {
            return this;
        }
        mSharedPreferences = context.getSharedPreferences(BASE_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        return this;
    }

    public int getInt(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }

    public boolean getBool(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    public String getString(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    public SharedPreferences.Editor putInt(String key, int value) {
        mEditor.putInt(key, value);
        return mEditor;
    }

    public SharedPreferences.Editor putLong(String key, long value) {
        mEditor.putLong(key, value);
        return mEditor;
    }

    public SharedPreferences.Editor putBool(String key, boolean value) {
        mEditor.putBoolean(key, value);
        return mEditor;
    }

    public SharedPreferences.Editor putString(String key, String value) {
        mEditor.putString(key, value);
        return mEditor;
    }

    public void putIntAndCommit(String key, int value) {
        mEditor.putInt(key, value).apply();
    }

    public void putLongAndCommit(String key, long value) {
        mEditor.putLong(key, value).apply();
    }

    public void putBoolAndCommit(String key, boolean value) {
        mEditor.putBoolean(key, value).apply();
    }

    public void putStringAndCommit(String key, String value) {
        mEditor.putString(key, value).apply();
    }
}
