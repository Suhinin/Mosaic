package com.childaplic.mosaic.services.shared;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.regex.Pattern;

import javax.inject.Inject;

/**
 * Created by maxim on 27.12.2017.
 */

public class SharedPreferencesService implements SharedService {

    // region Constants

    private final String TAG = SharedPreferencesService.class.getCanonicalName();

    private final String DEFAULT_ARRAY_DELIMITER = "|";

    // endregion


    private SharedPreferences preferences;

    @Inject
    public SharedPreferencesService (Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void putArray (String key, Object[] array) {
        putArray(key, array, DEFAULT_ARRAY_DELIMITER);
    }

    @Override
    public void putArray (String key, Object[] array, CharSequence delimiter) {
        String str = TextUtils.join(delimiter, array);
        putString(key, str);
    }

    @Override
    public void putIntArray(String key, int[] array) {
        Object[] objArray = new Object[array.length];
        for(int i=0; i<array.length; i++){
            objArray[i] = array[i];
        }

        putArray(key, objArray, DEFAULT_ARRAY_DELIMITER);
    }

    @Override
    public String[] getStringArray (String key) {
        return getStringArray(key, DEFAULT_ARRAY_DELIMITER);
    }

    @Override
    public String[] getStringArray (String key, String delimiter) {
        String[] array = null;

        String str = getString(key);
        if (str != null) {
            array = TextUtils.split(str, Pattern.quote(delimiter));
        }

        return array;
    }

    @Override
    public int[] getIntArray (String key) throws NumberFormatException {
        return getIntArray(key, DEFAULT_ARRAY_DELIMITER);
    }

    @Override
    public int[] getIntArray (String key, String delimiter) throws NumberFormatException {
        int[] ints = null;

        String[] strings = getStringArray(key, delimiter);
        if (strings != null) {
            ints = new int[strings.length];
            for (int i=0; i< strings.length; i++) {
                ints[i] = Integer.parseInt(strings[i]);
            }
        }

        return ints;
    }

    @Override
    public void putInt (String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    @Override
    public int getInt (String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    @Override
    public int getInt (String key) {
        return getInt(key, 0);
    }

    @Override
    public void putBoolean (String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    @Override
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    @Override
    public void putLong (String key, long value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    @Override
    public long getLong (String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    @Override
    public long getLong (String key) {
        return getLong(key, 0);
    }

    @Override
    public void putString (String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    @Override
    public String getString (String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    @Override
    public String getString (String key) {
        return preferences.getString(key, null);
    }

    @Override
    public void remove (String key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }

    @Override
    public void clear () {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}
