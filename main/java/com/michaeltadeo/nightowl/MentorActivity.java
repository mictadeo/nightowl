package com.michaeltadeo.nightowl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MentorActivity extends AppCompatActivity {

    private EditText mentor, phone, email;
    private DbOpenHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor);

        database = DbOpenHelper.getInstance(this);
        mentor = findViewById(R.id.mentorName);
        phone = findViewById(R.id.mentorPhone);
        email = findViewById(R.id.mentorEmail);

        getIncomingIntent();
    }
    /*When save button is pressed, this will check if mentor's name is entered or not. If it's been
            entered, this function will save and add the data to the database.*/
    public void saveButtonPressed (View v) {

        if (mentor.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter a your mentor's name." ,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        database.addMentor(mentor.getText().toString(),
                phone.getText().toString(),
                email.getText().toString() );

        finish();
    }

    /*This will check for incoming data if an item is selected on the previous activity, & load
        them on the screen. */
    private boolean getIncomingIntent(){

        if(getIntent().hasExtra("pMentorName")
                && getIntent().hasExtra("pMentorPhone")
                && getIntent().hasExtra("pMentorEmail")) {

            mentor.setText(getIntent().getStringExtra("pMentorName"));
            phone.setText(getIntent().getStringExtra("pMentorPhone"));
            email.setText(getIntent().getStringExtra("pMentorEmail"));

            return true;
        }
        else {

            return false;
        }
    }
}
