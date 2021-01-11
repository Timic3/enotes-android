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
        super(context, TABLE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " TEXT, " +
                DESCRIPTION + " TEXT, " +
                REMINDER + " TEXT, " +
                ITEMS + " TEXT, " +
                COLOR + " TEXT, " +
                ISDRAWING + " INTEGER DEFAULT 0, " +
                IMAGE + " BLOB)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public int insert(String title, String description, String reminder, String items, String color, boolean isDrawing, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, title);
        contentValues.put(DESCRIPTION, description);
        contentValues.put(REMINDER, reminder);
        contentValues.put(ITEMS, items);
        contentValues.put(COLOR, color);
        contentValues.put(ISDRAWING, isDrawing ? 1 : 0);
        contentValues.put(IMAGE, image);

        Log.d(TAG, "insert: Adding " + title + " to " + TABLE_NAME);

        return (int) db.insert(TABLE_NAME, null, contentValues);
    }

    public void remove(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "ID=" + id, null);
    }

    public Cursor data() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(query, null);
    }
}
