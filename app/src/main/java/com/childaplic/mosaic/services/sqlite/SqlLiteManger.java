package com.childaplic.mosaic.services.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Inject;

import com.childaplic.mosaic.R;


public class SqlLiteManger extends SQLiteOpenHelper implements DBManager {

    private static final String DATABASE_NAME = "mosaic";

    @Inject
    public SqlLiteManger(Context context) {
        super(context, DATABASE_NAME, null, context.getResources().getInteger(R.integer.database_version));
        getReadableDatabase();
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // empty
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO temp remove all tables
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        while (c.moveToNext()) {
            if ("sqlite_sequence".equals(c.getString(0)) == false) {
                db.execSQL("DROP TABLE IF EXISTS " + c.getString(0));
            }
        }
    }

    @Override
    public void createTable(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    @Override
    public long insert(String table, ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();

        return db.insert(table, null, values);
    }

    @Override
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = getWritableDatabase();

        return db.update(table, values, whereClause, whereArgs);
    }

    @Override
    public int delete(String table, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(table, whereClause, whereArgs);
    }

    @Override
    public Cursor get(String table, String selection, String[] selectionArgs) {
        SQLiteDatabase db = getReadableDatabase();

        return db.query(table, null, selection, selectionArgs, null, null, null);
    }

    @Override
    public Cursor getAll(String table) {
        SQLiteDatabase db = getReadableDatabase();

        return db.query(table, null, null, null, null, null, null);
    }
}
