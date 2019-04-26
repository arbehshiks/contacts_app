package com.example.contactsplus;
import android.database.Cursor;
import android.util.Log;

import static com.example.contactsplus.MainActivity.database;
import static com.example.contactsplus.DBHelper.*;
import com.example.contactsplus.MainActivity.*;

class Contact{
    private String name,surname,phone,more;
    private int id;

    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }

    public String getSurname(){ return surname; }
    public void setSurname(String surname){ this.surname = surname; }

    public String getPhone(){ return phone; }
    public void setPhone(String phone){ this.phone = phone; }

    public String getMore(){ return more; }
    public void setMore(String more){ this.more = more; }

    public int getID(){ return id; }
    public void setID(int id){ this.id = id; }


    public static String[] getContactArray(int id, DBHelper dbHelper){
        String[] c = new String[COLUMNS.length];

        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(TABLE_CONTACTS, // a. table
                COLUMNS,                             // b. column names
                " _id = ?",                     // c. selections
                new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null);
        if (cursor != null)
            cursor.moveToFirst();

        //KEY_ID,KEY_NAME,KEY_SURNAME,KEY_PHONE,KEY_MORE
        for(int i = 0;i < COLUMNS.length; i++){ c[i] = cursor.getString(i);}
        return c;
    }


    public static int getContactIDbyName(String name,DBHelper dbHelper){
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(TABLE_CONTACTS, COLUMNS,
                " name = ?",
                new String[] { String.valueOf(name) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return Integer.parseInt(cursor.getString(0));
    }

    public static String getString(String[] s){
        return (s[0]+" "+s[1]+" "+s[2]+" "+s[3]+" "+s[4]);
    }



//    public static Contact getContact(int id, DBHelper dbHelper){
//        Contact c = new Contact();
//        database = dbHelper.getWritableDatabase();
//
//        Cursor cursor = database.query(TABLE_CONTACTS, // a. table
//                COLUMNS, // b. column names
//                " _id = ?", // c. selections
//                new String[] { String.valueOf(id) }, // d. selections args
//                null, // e. group by
//                null, // f. having
//                null, // g. order by
//                null);
//
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        //KEY_ID,KEY_NAME,KEY_SURNAME,KEY_PHONE,KEY_MORE
//        c.setID(Integer.parseInt(cursor.getString(0)));
//        c.setName(cursor.getString(1));
//        c.setSurname(cursor.getString(2));
//        c.setPhone(cursor.getString(3));
//        c.setMore(cursor.getString(4));
//
//        Log.d("getContact("+id+")", c.toString());
//        return c;
//    }
}
