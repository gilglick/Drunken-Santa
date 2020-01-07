package com.example.hw1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hw1.GameUser;

public class DataBase extends SQLiteOpenHelper {

    private static final String USER_TABLE = "Highscore_table";
    private static final String COL0 = "Name";
    private static final String COL1 = "Score";
    private static final String COL2 = "Longitude";
    private static final String COL3 = "Latitude";


    public DataBase(Context context) {
        super(context, USER_TABLE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        String createTable = "CREATE TABLE " + USER_TABLE + " (" + COL0 + " TEXT," + COL1 + " INTEGER," + COL2 + " TEXT," + COL3 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    public boolean addData(GameUser user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL0, user.getUserName());
        contentValues.put(COL1, user.getScore());
        contentValues.put(COL2, user.getLongitude());
        contentValues.put(COL3, user.getLatitude());
        int result = (int) db.insert(USER_TABLE, null, contentValues);
        return result >= 0;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * From " + USER_TABLE;
        return db.rawQuery(query, null);
    }

}