package com.childaplic.mosaic.services.sqlite;

import android.content.ContentValues;
import android.database.Cursor;


public interface DBManager {

    void createTable(String sql);

    long insert(String table, ContentValues values);

    int update(String table, ContentValues values, String whereClause, String[] whereArgs);

    int delete(String table, String whereClause, String[] whereArgs);

    Cursor get(String table, String selection, String[] selectionArgs);

    Cursor getAll(String table);

    void close();

}
