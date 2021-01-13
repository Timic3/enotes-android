package com.enotes.remote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "notes";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String REMINDER = "reminder";
    private static final String ITEMS = "items";
    private static final String COLOR = "color";
    private static final String ISDRAWING = "is_drawing";
    private static final String IMAGE = "image";

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String notes = "CREATE TABLE notes (ID INTEGER PRIMARY KEY, " +
                IMAGE + " BLOB)";
        db.execSQL(notes);
        String drawings = "CREATE TABLE drawings (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IMAGE + " BLOB)";
        db.execSQL(drawings);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS drawings");
        onCreate(db);
    }

    public int insert(String table, String id, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (id != null) {
            contentValues.put("ID", id);
        }
        contentValues.put(IMAGE, image);

        Log.d(TAG, "insert: Adding " + id + " to " + TABLE_NAME);

        return (int) db.insert(table, null, contentValues);
    }

    public void remove(String table, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, "ID=" + id, null);
    }

    public Cursor select(String table, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + table + " WHERE ID=" + id;
        return db.rawQuery(query, null);
    }

    public Cursor data() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM drawings";
        return db.rawQuery(query, null);
    }
}
