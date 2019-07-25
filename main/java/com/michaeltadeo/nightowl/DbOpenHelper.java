package com.michaeltadeo.nightowl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;

public class DbOpenHelper extends SQLiteOpenHelper {

    //Constants for identifying table & columns
    public static final String databaseName = "nightowl.db";

    public static final String mentorTable = "mentorTable";
    public static final String pMentorId = "pMentorId";
    public static final String pMentorName = "pMentorName";
    public static final String pMentorPhone = "pMentorPhone";
    public static final String pMentorEmail = "pMentorEmail";

    public static final String termTable = "termTable";
    public static final String termId = "termId";
    public static final String termTitle = "termTitle";
    public static final String termStart = "termStart";
    public static final String termEnd = "termEnd";

    public static final String courseTable = "courseTable";
    public static final String courseId = "courseId";
    public static final String courseTitle = "courseTitle";
    public static final String courseStart = "courseStart";
    public static final String courseEnd = "courseEnd";
    public static final String courseStatus = "courseStatus";
    public static final String courseNotes = "courseNotes";
    public static final String mentorName = "mentorName";
    public static final String mentorPhone = "mentorPhone";
    public static final String mentorEmail = "mentorEmail";
    public static final String courseTerm = "courseTerm";
    public static final String startDateAlert = "startDateAlert";
    public static final String endDateAlert = "endDateAlert";
    public static final String courseAlertId = "courseAlertId";

    public static final String assessmentTable = "assessmentTable";
    public static final String assessmentId = "assessmentId";
    public static final String assessmentTitle = "assessmentTitle";
    public static final String assessmentDue = "assessmentDue";
    public static final String assessmentGoal = "assessmentGoal";
    public static final String assessmentType = "assessmentType";
    public static final String assessmentCourse = "assessmentCourse";
    public static final String goalDateAlert = "goalDateAlert";
    public static final String goalAlertId = "goalAlertId";

    private static DbOpenHelper instance = null;

    public static DbOpenHelper getInstance(Context context) {

        if (instance == null) {
            instance = new DbOpenHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DbOpenHelper(Context context) {

        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create Tables

        db.execSQL("create table " + mentorTable + "(pMentorId integer primary key, " +
                " pMentorName text, pMentorPhone text, pMentorEmail text)");

        db.execSQL("create table " + termTable + "(termId integer primary key autoincrement, " +
                "termTitle text, termStart text, termEnd text)");

        db.execSQL("create table " + courseTable + "(courseId integer primary key autoincrement, " +
                "courseTitle text, courseStart text, courseEnd text, courseStatus text, " +
                "courseNotes text, mentorName text, mentorPhone text," +
                "mentorEmail text, courseTerm text, startDateAlert integer, endDateAlert integer," +
                "courseAlertId integer)");

        db.execSQL("create table " + assessmentTable + "(assessmentId integer " +
                "primary key autoincrement, " + "assessmentTitle text, assessmentDue text, " +
                "assessmentGoal text, assessmentType text, assessmentCourse text," +
                "goalDateAlert integer, goalAlertId integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists " + mentorTable);
        db.execSQL("drop table if exists " + termTable);
        db.execSQL("drop table if exists " + courseTable);
        db.execSQL("drop table if exists " + assessmentTable);
        onCreate(db);
    }

    public void addMentor (String name, String phone, String email) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(pMentorId, 1);
        contentValues.put(pMentorName, name);
        contentValues.put(pMentorPhone, phone);
        contentValues.put(pMentorEmail, email);
        db.replace(mentorTable, null, contentValues);
    }

    public void addTermData(String title, String start, String end) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(termTitle, title);
        contentValues.put(termStart, start);
        contentValues.put(termEnd, end);
        db.insert(termTable, null, contentValues);
    }

    public void modifyTermData(Integer id, String title, String start, String end) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(termTitle, title);
        contentValues.put(termStart, start);
        contentValues.put(termEnd, end);
        db.update(termTable, contentValues, "termId="+ id, null);
    }

    public void modifyTermTitle(String previousTitle, String newTitle) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(courseTerm, newTitle);
        db.update(courseTable, contentValues, "courseTerm = '" +
                previousTitle + "'", null);
    }

    public void deleteTermData(Integer id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(termTable, "termId="+ id, null);
    }

    public void addCourseData(String title, String start, String end, String status, String notes,
                              String name, String phone, String email, String term, Integer alert1,
                              Integer alert2, Integer alertId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(courseTitle, title);
        contentValues.put(courseStart, start);
        contentValues.put(courseEnd, end);
        contentValues.put(courseStatus, status);
        contentValues.put(courseNotes, notes);
        contentValues.put(mentorName, name);
        contentValues.put(mentorPhone, phone);
        contentValues.put(mentorEmail, email);
        contentValues.put(courseTerm, term);
        contentValues.put(startDateAlert, alert1);
        contentValues.put(endDateAlert, alert2);
        contentValues.put(courseAlertId, alertId);
        db.insert(courseTable, null, contentValues);
    }

    public void modifyCourseData(Integer id, String title, String start, String end, String status,
                                 String notes, String name, String phone, String email,
                                 String term, Integer alert1, Integer alert2, Integer alertId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(courseTitle, title);
        contentValues.put(courseStart, start);
        contentValues.put(courseEnd, end);
        contentValues.put(courseStatus, status);
        contentValues.put(courseNotes, notes);
        contentValues.put(mentorName, name);
        contentValues.put(mentorPhone, phone);
        contentValues.put(mentorEmail, email);
        contentValues.put(courseTerm, term);
        contentValues.put(startDateAlert, alert1);
        contentValues.put(endDateAlert, alert2);
        contentValues.put(courseAlertId, alertId);
        db.update(courseTable, contentValues, "courseId="+ id, null);
    }

    public void modifyCourseTitle(String previousTitle, String newTitle) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(assessmentCourse, newTitle);
        db.update(assessmentTable, contentValues, "assessmentCourse = '"
                + previousTitle + "'", null);
    }


    public void deleteCourseData(Integer id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(courseTable, "courseId="+ id, null);
    }

    public void addAssessmentData(String title, String dueDate, String goalDate,
                                  String type, String course, Integer alert1, Integer alertId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(assessmentTitle, title);
        contentValues.put(assessmentDue, dueDate);
        contentValues.put(assessmentGoal, goalDate);
        contentValues.put(assessmentType, type);
        contentValues.put(assessmentCourse, course);
        contentValues.put(goalDateAlert, alert1);
        contentValues.put(goalAlertId, alertId);
        db.insert(assessmentTable, null, contentValues);
    }

    public void modifyAssessmentData(Integer id, String title, String dueDate, String goalDate,
                                     String type, String course, Integer alert1, Integer alertId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(assessmentTitle, title);
        contentValues.put(assessmentDue, dueDate);
        contentValues.put(assessmentGoal, goalDate);
        contentValues.put(assessmentType, type);
        contentValues.put(assessmentCourse, course);
        contentValues.put(goalDateAlert, alert1);
        contentValues.put(goalAlertId, alertId);
        db.update(assessmentTable, contentValues, "assessmentId="+ id, null);
    }

    public void deleteAssessmentData(Integer id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(assessmentTable, "assessmentId="+ id, null);
    }

    public Integer getItemAtPosition (SpinnerAdapter adapter, String string) {

        for (int i = 0; i < adapter.getCount(); i++)
            if (adapter.getItem(i).toString().equals(string))
                return i;
        return -1;
    }

    /*These items will configure the function to gather data from the specified table & fill the
    spinner container when the function is called.*/
    public ArrayList<String> getSpinnerData(String table) {

        String query = "select * from " + table;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<String> spinnerData = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                spinnerData.add(cursor.getString(1) );
            } while (cursor.moveToNext());
        }
        cursor.close();
        return spinnerData;
    }
}
