package com.childaplic.mosaic.services.shared;


public interface SharedService {

    void putArray(String key, Object[] array);

    void putArray(String key, Object[] array, CharSequence delimiter);

    void putIntArray(String key, int[] array);

    String[] getStringArray(String key);

    String[] getStringArray(String key, String delimiter);

    int[] getIntArray(String key);

    int[] getIntArray(String key, String delimiter);

    void putInt(String key, int value);

    int getInt(String key, int defaultValue);

    int getInt(String key);

    void putBoolean(String key, boolean value);

    boolean getBoolean(String key, boolean defaultValue);

    boolean getBoolean(String key);

    void putLong(String key, long value);

    long getLong(String key, long defaultValue);

    long getLong(String key);

    void putString(String key, String value);

    String getString(String key, String defaultValue);

    String getString(String key);

    void remove(String key);

    void clear();

}
