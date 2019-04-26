package com.example.contactsplus;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class MainAddContact extends AppCompatActivity{
    TextView c_notification;
    TextView c_add_title;
    EditText c_newname,c_newsur,c_newphone,c_newmore;
    String rname,rsurname,rphone,rmore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_add_contact);



        c_add_title = (TextView) findViewById(R.id.c_add_title);
        c_notification= (TextView) findViewById(R.id.c_notification);

        c_newname = (EditText) findViewById(R.id.c_addname);   // A
        c_newsur = (EditText) findViewById(R.id.c_addsurname); // B
        c_newphone = (EditText) findViewById(R.id.c_addphone); // Widgets
        c_newmore = (EditText) findViewById(R.id.c_addmore);   //
        Button addcontact =(Button) findViewById(R.id.c_add);  //

        //Taking Bundle
        Bundle extras = getIntent().getExtras();
        rname = extras.getString("NAME");
        rsurname = extras.getString("SURNAME");
        rphone = extras.getString("PHONE");
        rmore = extras.getString("MORE");



        //Clicking Add Contact
        addcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((c_newname.getText().length())+(c_newsur.getText().length()))!=0){
                    Intent i = new Intent();
                    i.putExtra("NAME",c_newname.getText()+"");
                    i.putExtra("SURNAME",c_newsur.getText()+"");
                    i.putExtra("PHONE",c_newphone.getText()+"");
                    i.putExtra("MORE",c_newmore.getText()+"");
                    setResult(RESULT_OK, i);
                    finish();
                    }
                else{
                    c_notification.setText("Enter name and surname!");
                }
            }
        });

    }

}