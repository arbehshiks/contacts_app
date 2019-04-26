package com.example.contactsplus;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.content.Intent;
import java.util.ArrayList;
import java.util.Locale;
import android.widget.SearchView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import static com.example.contactsplus.Contact.*;
import static com.example.contactsplus.SharedPref.*;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    TextView c_title;
    ListView c_view;                        //list
    FloatingActionButton c_addButton;       //going to the *add* page
    SearchView c_searchBar;                 //search bar
    int ID_addContact = 2;                   //intent
    int ID_contactPage = 3;                  //intent
    ArrayAdapter<String> ad;                 //Adapter
    static ArrayList<String> c_name_View;    //array list

    //Database initialization
    DBHelper dbHelper = new DBHelper(this);
    static SQLiteDatabase database;

    static int contact_permission_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        c_title = (TextView) findViewById(R.id.c_main_title);
        c_view = (ListView) findViewById(R.id.c_listView);
        c_searchBar = (SearchView) findViewById(R.id.c_searchBar);
        c_addButton = (FloatingActionButton) findViewById(R.id.c_addButton);

        c_name_View = contactsArrayForView();

        if(getFromPref(getApplicationContext(), "contact_permission").equals("false")) {
            //Pop up permission window
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, contact_permission_ID);
        }

        //Adapter
        ad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, c_name_View);
        updateArrayListView();
        c_searchBar.setOnQueryTextListener(this);

            c_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    int c_ID = getContactIDbyName(c_name_View.get((int)id),dbHelper);
                    String[] string = getContactArray(c_ID, dbHelper);
                    Intent i = new Intent(MainActivity.this, ContactPage.class);
                    i.putExtra("STRING", string);
                    i.putExtra("LIST_ID", (int)id);


                    startActivityForResult(i, ID_contactPage);

                }
            });

            //Button add contact
            c_addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, MainAddContact.class);
                    String rname = "";
                    String rphone = "";
                    String rsurname = "";
                    String rmore = "";
                    i.putExtra("NAME", rname);
                    i.putExtra("SURNAME", rsurname);
                    i.putExtra("PHONE", rphone);
                    i.putExtra("MORE", rmore);
                    startActivityForResult(i, ID_addContact);
                }
            });
        }

        //Reading of new contact and adding it to DB
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            if (resultCode == RESULT_OK) {
                if (requestCode == ID_addContact) {
                    Bundle extras = data.getExtras();

                    // New contact object
                    Contact c = new Contact();
                    c.setName(extras.getString("NAME"));
                    c.setSurname(extras.getString("SURNAME"));
                    c.setPhone(extras.getString("PHONE"));
                    c.setMore(extras.getString("MORE"));

                    //Write to database
                    database = dbHelper.getWritableDatabase();
                    dbHelper.CreateContact(database, c);
                    dbHelper.LogDB(database);
                    database.close();
                    c_name_View.add(extras.getString("NAME")); // + " " + extras.getString("SURNAME")
                    updateArrayListView();

                }
                if (requestCode == ID_contactPage) {
                    Bundle extras = data.getExtras();
                    String del_id = extras.getString("deletedContact");
                    int list_id = extras.getInt("List_id");
                    c_name_View.remove(list_id);
                    database = dbHelper.getWritableDatabase();
                    database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_ID + "=" + del_id, null);
                    dbHelper.LogDB(database);
                    database.close();
                    updateArrayListView();
                }
            }

        }
        //Write your real contacts into database
        protected void addPhoneContactsIntoDatabase () {
            Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            ContentValues contentValues = new ContentValues();
            database = dbHelper.getWritableDatabase();
            if (cursor.moveToFirst()) {
                do {
                    contentValues.put(DBHelper.KEY_NAME, cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                    contentValues.put(DBHelper.KEY_PHONE, cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    contentValues.put(DBHelper.KEY_SURNAME, "");
                    contentValues.put(DBHelper.KEY_MORE, "");
                    database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);

                } while (cursor.moveToNext());
            } else
                Log.d("mLog", "0 rows");
            dbHelper.LogDB(database);
            database.close();
            cursor.close();
        }
        protected ArrayList<String> contactsArrayForView () {
            ArrayList<String> new_arr = new ArrayList<String>();
            database = dbHelper.getWritableDatabase();
            Cursor c = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
            if (c.moveToFirst()) {
                do {
                    int nameIndex = c.getColumnIndex(DBHelper.KEY_NAME);
                    String n = c.getString(nameIndex);
                    //int surnameIndex = c.getColumnIndex(DBHelper.KEY_SURNAME);
                    new_arr.add(n); // + " " + c.getString(surnameIndex)


                } while (c.moveToNext());

            } else
                Log.d("mLog", "0 rows");
            c.close();
            database.close();

            return new_arr;
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode ==  contact_permission_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setToPref(getApplicationContext(),"contact_permission", "true");
                    c_name_View.removeAll(contactsArrayForView());
                    addPhoneContactsIntoDatabase();
                    c_name_View.addAll(contactsArrayForView());
                    updateArrayListView();
                }

            }
            else{
            Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
        }
        }

    public void updateArrayListView() {
        c_view.setAdapter(ad);
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        c_name_View.clear();
        if (charText.length() == 0) {
            c_name_View.addAll(contactsArrayForView());
        } else {
            for (String name : contactsArrayForView()) {
                if (name.toLowerCase(Locale.getDefault()).contains(charText)) {
                    c_name_View.add(name);
                }
            }
        }
        ad.notifyDataSetChanged();
    }
    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        filter(text);
        return false;
    }


}