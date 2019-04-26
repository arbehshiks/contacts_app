package com.example.contactsplus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "contactDb";
    static final String TABLE_CONTACTS = "contacts";

    static final String KEY_ID = "_id";
    static final String KEY_NAME = "name";
    static final String KEY_SURNAME = "surname";
    static final String KEY_PHONE = "phone";
    static final String KEY_MORE = "more";

    static final String[] COLUMNS = {KEY_ID,KEY_NAME,KEY_SURNAME,KEY_PHONE,KEY_MORE};

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create database
        db.execSQL("create table " + TABLE_CONTACTS + "(" + KEY_ID
                + " integer primary key," + KEY_NAME + " text," + KEY_SURNAME + " text," + KEY_PHONE + " text," +
                KEY_MORE + " text" + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_CONTACTS);
        onCreate(db);

    }

    void CreateContact(SQLiteDatabase db, Contact contact){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_NAME, contact.getName());
        contentValues.put(DBHelper.KEY_SURNAME, contact.getSurname());
        contentValues.put(DBHelper.KEY_PHONE, contact.getPhone());
        contentValues.put(DBHelper.KEY_MORE, contact.getMore());
        db.insert(DBHelper.TABLE_CONTACTS, null, contentValues);

    }

    void LogDB(SQLiteDatabase db){
        Cursor cursor = db.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int surnameIndex = cursor.getColumnIndex(DBHelper.KEY_SURNAME);
            int phoneIndex = cursor.getColumnIndex(DBHelper.KEY_PHONE);
            int moreIndex = cursor.getColumnIndex(DBHelper.KEY_MORE);
            do{
                Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                        ", name = " + cursor.getString(nameIndex)+
                        ", surname = " + cursor.getString(surnameIndex)+
                        ", phone = " + cursor.getString(phoneIndex)+
                        ", more = " + cursor.getString(moreIndex));
            }while (cursor.moveToNext());
        }else
            Log.d("mLog","0 rows");
        cursor.close();

    }
    int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

}