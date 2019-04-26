package com.example.contactsplus;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import static com.example.contactsplus.MainActivity.c_name_View;
import static com.example.contactsplus.MainActivity.database;
import java.util.ArrayList;

public class ContactPage extends AppCompatActivity{

    private static final int REQUEST_CALL = 1;
    EditText editname,editsurname,editphone,editmore;
    TextView cpid,cpname,cpsurname,cpphone,cpmore;
    Button cpdelete,edit,editconfirm,editcancel;
    DBHelper dbHelper=new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_page);
        ImageView imageCall = findViewById(R.id.image_call);

        //Text view's(regular view after passing on this activity)
        cpid = (TextView) findViewById(R.id.cpid);cpname = (TextView) findViewById(R.id.cpname);
        cpphone = (TextView) findViewById(R.id.cpphone);cpmore = (TextView) findViewById(R.id.cpmore);

        //Edit text's(displaying after clicking on the EDIT)
        editname = (EditText) findViewById(R.id.editname);editsurname = (EditText) findViewById(R.id.editsurname);
        editmore = (EditText) findViewById(R.id.editmore);editphone = (EditText) findViewById(R.id.editphone);

        //Button's
        cpdelete = (Button) findViewById(R.id.cpdelete);edit = (Button) findViewById(R.id.edit);
        editconfirm = (Button) findViewById(R.id.editconfirm);editcancel = (Button) findViewById(R.id.editcancel);editconfirm.setVisibility(View.INVISIBLE);editcancel.setVisibility(View.INVISIBLE);

        //Extracting bundle
        Bundle extras = getIntent().getExtras();
        final String[] data = extras.getStringArray("STRING");
        final int list_id = extras.getInt("LIST_ID");
        System.out.println(list_id);

        //Sending to TextViews
        cpname.setText(data[1] + " " + data[2]);cpid.setText(data[0]);
        cpphone.setText(data[3]);cpmore.setText(data[4]);

        final ArrayList<TextView> TextViews=new ArrayList<>();TextViews.add(cpname);TextViews.add(cpphone);TextViews.add(cpmore);
        final ArrayList<Button> EditButtons=new ArrayList<>();EditButtons.add(editconfirm);EditButtons.add(editcancel);
        final ArrayList<EditText> EditText=new ArrayList<>();EditText.add(editname);EditText.add(editsurname);EditText.add(editphone);EditText.add(editmore);

        //Hints
            for(int i=0;i<4;i++){
                EditText.get(i).setText(data[i+1]);
                EditText.get(i).setVisibility(View.INVISIBLE);
            }

        // if phone is pressed
        imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

        //If press delete
        cpdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("deletedContact", data[0]);
                i.putExtra("List_id", list_id);
                setResult(RESULT_OK, i);
                finish();
            }
        });

        //If press edit
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                for(int i=0;i<3;i++){
                    TextViews.get(i).setVisibility(View.INVISIBLE);
                }
                for(int i=0;i<4;i++){
                    EditText.get(i).setVisibility(View.VISIBLE);
                }
                for(int i=0;i<2;i++){
                    EditButtons.get(i).setVisibility(View.VISIBLE);
                }
            }
        });

        //If press cancel
        editcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<3;i++){
                    TextViews.get(i).setVisibility(View.VISIBLE);
                }
                for(int i=0;i<4;i++){
                    EditText.get(i).setVisibility(View.INVISIBLE);
                }
                for(int i=0;i<2;i++){
                    EditButtons.get(i).setVisibility(View.INVISIBLE);
                }
            }
        });

        //If press confirm
        editconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<4;i++){
                    EditText.get(i).getText();
                }
                //Setting
                cpname.setText(editname.getText() + " " + editsurname.getText());
                cpphone.setText(editphone.getText());
                cpmore.setText(editmore.getText());
                for(int i=0;i<3;i++){
                    TextViews.get(i).setVisibility(View.VISIBLE);
                }
                for(int i=0;i<4;i++){
                    EditText.get(i).setVisibility(View.INVISIBLE);
                }
                for(int i=0;i<2;i++){
                    EditButtons.get(i).setVisibility(View.INVISIBLE);
                }
                //Editing contact in database
                database = dbHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(DBHelper.KEY_NAME, editname.getText().toString());
                cv.put(DBHelper.KEY_SURNAME,  editsurname.getText().toString());
                cv.put(DBHelper.KEY_PHONE,  editphone.getText().toString());
                cv.put(DBHelper.KEY_MORE,  editmore.getText().toString());
                database.update(DBHelper.TABLE_CONTACTS, cv, DBHelper.KEY_ID + "=" + data[0], null);
                dbHelper.LogDB(database);
                database.close();
                //Отут треба обновити ліст,ну після запису в базу данних
                c_name_View.set(list_id,editname.getText().toString());
            }
        });
    }




    // If Phone Call button was pressed
    public void makePhoneCall(){
        String cp_call_number = cpphone.getText().toString();
        if (cp_call_number.trim().length() > 0) {

            if (ContextCompat.checkSelfPermission(ContactPage.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ContactPage.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            }
            else {
                String dial = "tel:" + cp_call_number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        }
        else {
            Toast.makeText(ContactPage.this, "ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL ) {
            if (grantResults.length > 0 && grantResults[0]  == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            }
            else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }


}


