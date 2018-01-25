package com.hanibey.smartorderrepository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hanibey.smartordermodel.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanju on 30.09.2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME   = "smartOrder";
    private static final String TABLE_SETTINGS = "settings";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String sql = "CREATE TABLE " + TABLE_SETTINGS + "(id INTEGER PRIMARY KEY, client_key TEXT, language_code TEXT, order_key TEXT)";
            db.execSQL(sql);
        }
        catch (Exception ex){
            Log.e("DBHelper Create", String.valueOf(ex));
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
            onCreate(db);
        }
        catch (Exception ex){
            Log.e("DBHelper Update", String.valueOf(ex));
        }

    }

    public String insertSettings(Settings setting) {

        String message;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("client_key", setting.getClientKey());
            values.put("language_code", setting.getLanguageCode());
            values.put("order_key", setting.getOrderKey());

            db.insert(TABLE_SETTINGS, null, values);
            db.close();

            message = "İşlem başarılı.";
        }
        catch (Exception ex){
            message = String.valueOf(ex);
        }

       return  message;
    }

    public Settings getSettings() {

        Settings setting = new Settings();
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.query(TABLE_SETTINGS, new String[]{"id", "client_key", "language_code", "order_key"}, null, null, null, null, null);


            while (cursor.moveToNext()) {
                setting.setId(cursor.getInt(0));
                setting.setClientKey(cursor.getString(1));
                setting.setLanguageCode(cursor.getString(2));
                setting.setOrderKey(cursor.getString(3));
                break;
            }
        }
        catch (Exception ex){
            Log.e("DBHelper getSettings", String.valueOf(ex));
        }



        return setting;
    }

    public String getLanguageCode() {
        Settings setting = getSettings();
        String code = "";

        if(setting != null && setting.getLanguageCode() != null && !setting.getLanguageCode().equals(""))
            code = setting.getLanguageCode();

        return code;
    }

    public void updateOrderKey(int id, String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("order_key", key);
        // updating row
        db.update(TABLE_SETTINGS, values, "id=?",
                new String[] { String.valueOf(id) });
    }

    public void updateLanguageCode(int id, String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("language_code", code);
        // updating row
        db.update(TABLE_SETTINGS, values, "id=?",
                new String[] { String.valueOf(id) });
    }

    public void updateSettings(Settings setting) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("client_key", setting.getClientKey());
        values.put("language_code", setting.getLanguageCode());

        // updating row
        db.update(TABLE_SETTINGS, values, "id=?",
                new String[] { String.valueOf(setting.getId()) });
    }

    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SETTINGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        return rowCount;
    }

    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SETTINGS, null, null);
        db.close();
    }
}
