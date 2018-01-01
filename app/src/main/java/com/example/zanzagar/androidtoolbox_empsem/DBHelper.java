package com.example.zanzagar.androidtoolbox_empsem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Sensor_info.db";
    public static final String LOGS_TABLE_NAME = "logs";
    public static final String LOGS_COLUMN_ID = "id";
    public static final String LOGS_COLUMN_NAME = "name";
    public static final String LOGS_COLUMN_INFO = "info";
    public static final String LOGS_COLUMN_TIMESTAMP = "timestamp";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table logs " +
                        "(id integer primary key, name text,info text,timestamp text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS logs");
        onCreate(db);
    }

    public boolean insertLog (String name, String info, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("info", info);
        contentValues.put("timestamp", timestamp);
        db.insert("logs", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from logs where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, LOGS_TABLE_NAME);
        return numRows;
    }

    public boolean updateLog (Integer id, String name, String info, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("info", info);
        contentValues.put("timestamp", timestamp);
        db.update("logs", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteLog (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("logs",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }
    public boolean deleteLogs () {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ LOGS_TABLE_NAME);
        return true;
    }

    public ArrayList<String> getAllLogs() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from logs", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(LOGS_COLUMN_NAME))+"\n"+res.getString(res.getColumnIndex(LOGS_COLUMN_INFO))+"\n"+res.getString(res.getColumnIndex(LOGS_COLUMN_TIMESTAMP)));
            res.moveToNext();
        }
        return array_list;
    }
}