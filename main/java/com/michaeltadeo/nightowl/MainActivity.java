package com.michaeltadeo.nightowl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mentor, textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mentor = findViewById(R.id.mentorView);
        textView = findViewById(R.id.homeTextView);

        showMentorData();
    }

    public void openTermActivity(View v){
        Intent intent= new Intent(this, TermActivity.class);
        startActivity(intent);
    }

    public void openCourseActivity(View v){
        Intent intent= new Intent(this, CourseActivity.class);
        startActivity(intent);
    }

    public void openAssessmentActivity(View v){
        Intent intent= new Intent(this, AssessmentActivity.class);
        startActivity(intent);
    }

    public void openMentorActivity(View v){

        Intent intent= new Intent(this, MentorActivity.class);
        DbOpenHelper db = DbOpenHelper.getInstance(this);
        String query = "select * from mentorTable";
        Cursor getMentor = db.getReadableDatabase().rawQuery(query, null);
        while (getMentor.moveToNext()) {

            final String name = getMentor.getString(1);
            final String phone = getMentor.getString(2);
            final String email = getMentor.getString(3);

            intent.putExtra("pMentorName", name);
            intent.putExtra("pMentorPhone", phone);
            intent.putExtra("pMentorEmail", email);
        }
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    private void showMentorData(){

        DbOpenHelper db = DbOpenHelper.getInstance(this);
        StringBuilder mentorData = new StringBuilder();
        String query = "select * from mentorTable";
        Cursor getMentor = db.getReadableDatabase().rawQuery(query, null);

        if (getMentor.getCount() != 0) {

            while (getMentor.moveToNext()) {

                mentorData.append(getMentor.getString(1) + "   "+
                        getMentor.getString(2) + "\n" +
                        getMentor.getString(3));

                mentor.setText(mentorData);
                textView.setText("Program Mentor");
            }
        } else {
            textView.setText("1st time user Tip: Tap here to fill out the Mentor details " +
                    "then go through the 3 buttons, from left to right.");
        }
    }

    /* This item will refresh the current activity during its "on resume" part of lifecycle,
    to load recent changes such as Mentor data. */

    @Override
    protected void onResume() {
        super.onResume();

        showMentorData();
    }
}
