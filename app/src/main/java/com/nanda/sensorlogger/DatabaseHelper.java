package com.nanda.sensorlogger;

import android.database.Cursor;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "sensor.db";
    public static final String TABLE_NAME = "sensor_log";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE " + TABLE_NAME + "(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "tanggal TEXT," +
                        "jam TEXT," +
                        "x REAL," +
                        "y REAL," +
                        "z REAL," +
                        "status TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);

    }

    public boolean insertData(String tanggal, String jam, float x, float y, float z, String status){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("tanggal", tanggal);
        values.put("jam", jam);
        values.put("x", x);
        values.put("y", y);
        values.put("z", z);
        values.put("status", status);

        long result = db.insert(TABLE_NAME, null, values);

        return result != -1;
    }

    public Cursor getAllData(){

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM " + TABLE_NAME,null);

    }
    public void deleteAllData(){

        SQLiteDatabase db=this.getWritableDatabase();

        db.delete(TABLE_NAME,null,null);

    }

}