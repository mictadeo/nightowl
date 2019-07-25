package com.michaeltadeo.nightowl;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.michaeltadeo.nightowl.Model.Course;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import static android.widget.TextView.BufferType.EDITABLE;

public class CourseAddActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText courseTitle, status, notes, name, phone, email;
    private TextView startDate, endDate, courseLabel, courseGuide, assessmentView, assessmentLabel;
    private DatePickerDialog startDatePicker, endDatePicker;
    private Switch startDateSwitch, endDateSwitch;
    private Integer startDateAlert, endDateAlert, newAlertId;
    private SimpleDateFormat dateFormat;
    private DbOpenHelper database;
    private Course course;
    private Spinner spinner;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_add);

        courseTitle = findViewById(R.id.courseTitleField);
        startDate = findViewById(R.id.courseStartField);
        endDate = findViewById(R.id.courseEndField);
        status = findViewById(R.id.courseStatusField);
        notes = findViewById(R.id.courseNotesField);
        name = findViewById(R.id.mentorNameField);
        phone = findViewById(R.id.mentorPhoneField);
        email = findViewById(R.id.mentorEmailField);
        courseLabel = (findViewById(R.id.addCourseLabel));
        courseGuide = findViewById(R.id.courseGuideLabel);
        dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        database = DbOpenHelper.getInstance(this);
        assessmentLabel = findViewById(R.id.courseAssessmentLabel);
        assessmentView =findViewById(R.id.assessmentMonitor);
        spinner = findViewById(R.id.termSpinner);
        startDateSwitch = findViewById(R.id.courseSwitch1);
        endDateSwitch = findViewById(R.id.courseSwitch2);
        startDateAlert = 0;
        endDateAlert = 0;

        //This item will setup the spinner container & fill it with titles of saved Terms.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout
                .simple_spinner_dropdown_item, database.getSpinnerData("termTable"));
        spinner.setAdapter(adapter);

        setDatePickers();
        getIncomingIntent();
    }

    @Override
    public void onClick(View v) {
        if (v == startDate) {
            startDateSwitch.setChecked(false);
            startDatePicker.show();
        }
        if (v == endDate) {
            endDateSwitch.setChecked(false);
            endDatePicker.show();
        }
    }

    private void setDatePickers() {

        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        startDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                startDate.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        endDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                endDate.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        startDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    startDatePicker.show();
                }
            }
        });

        endDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    endDatePicker.show();
                }
            }
        });
    }
    /*The following will set the time pickers for course's start date & end date, when the alert
    switch is turned on. */
    public void startDateTimePicker (View v){

        if (startDate.getText().toString().isEmpty()) {
            startDateSwitch.setChecked(false);
            Toast.makeText(getApplicationContext(), "Please pick a date before setting an " +
                    "alert.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (startDateSwitch.isChecked()){

            Calendar calendar = Calendar.getInstance();
            TimePickerDialog startTimePicker = new TimePickerDialog(CourseAddActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String AM_PM;

                            if (hourOfDay < 12) {
                                AM_PM = "AM";
                            } else {
                                AM_PM = "PM";
                            }
                            if (hourOfDay > 12) {
                                hourOfDay = hourOfDay - 12;
                            }
                            if (hourOfDay == 0) {
                                hourOfDay = 12;
                            }
                            String minuteString = Integer.toString(minute);
                            if (minute < 10) {
                                minuteString = "0" + minuteString;
                            }
                            String datetime = startDate.getText().toString() + "  " + hourOfDay +
                                    ":" + minuteString + " " + AM_PM + " " + TimeZone.getDefault()
                                    .getDisplayName(true, TimeZone.SHORT);

                            startDate.setText(datetime);
                        }
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    false);
            startTimePicker.show();

            startTimePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    startDateSwitch.setChecked(false);
                }
            });
        }
        else  {
            String date = startDate.getText().toString().substring(0, 10);
            startDate.setText("");
            startDate.setText(date);
        }
    }

    public void endDateTimePicker (View v){

        if (endDate.getText().toString().isEmpty()) {
            endDateSwitch.setChecked(false);
            Toast.makeText(getApplicationContext(), "Please pick a date before setting an " +
                    "alert.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (endDateSwitch.isChecked()){

            Calendar calendar = Calendar.getInstance();
            TimePickerDialog endTimePicker = new TimePickerDialog(CourseAddActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String AM_PM;

                            if (hourOfDay < 12) {
                                AM_PM = "AM";
                            } else {
                                AM_PM = "PM";
                            }
                            if (hourOfDay > 12) {
                                hourOfDay = hourOfDay - 12;
                            }
                            if (hourOfDay == 0) {
                                hourOfDay = 12;
                            }
                            String minuteString = Integer.toString(minute);
                            if (minute < 10) {
                                minuteString = "0" + minuteString;
                            }
                            String datetime = endDate.getText().toString() + "  " + hourOfDay + ":"
                                    + minuteString + " " + AM_PM + " " + TimeZone.getDefault()
                                    .getDisplayName(true, TimeZone.SHORT);

                            endDate.setText(datetime);
                        }
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    false);
            endTimePicker.show();

            endTimePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    endDateSwitch.setChecked(false);
                }
            });
        }
        else  {
            String date = endDate.getText().toString().substring(0, 10);
            endDate.setText("");
            endDate.setText(date);
        }
    }

    //This configures the note share button to enable sharing via SMS / Email.
    public void shareNotes (View v){

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, notes.getText().toString());
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    // This item will add the trash icon on the action bar at the top right.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (getIncomingIntent()) {

            getMenuInflater().inflate(R.menu.delete, menu);
        }
        return true;
    }

    /*These items configure the selection of trash icon. It checks for an associated assessment to
    prevent deletion. If there are no associations, it will ask for confirmation & delete the course
    once it is confirmed.*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String query = "select * from assessmentTable where assessmentCourse = '"+
                (course.getTitle()) + "'"; cursor = database.getReadableDatabase()
                .rawQuery(query, null);

        if(cursor.getCount() != 0) {
            Toast.makeText(getApplicationContext(), "Please go back & delete associated " +
                    "assessments in order to delete this course.", Toast.LENGTH_LONG).show();
        }
        else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to delete?")
                    .setCancelable(true)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    database.deleteCourseData(course.getId());
                    finish();
                }
            });

            AlertDialog alert = builder.create();
            alert.setTitle("Confirm Delete");
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }


    //This will save / add data entry to the database when save button is pressed.
    public void saveButtonPressed (View v) {

        if (startDateSwitch.isChecked()) startDateAlert = 1;

        if (endDateSwitch.isChecked()) endDateAlert = 1;

        if (courseTitle.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter a title in order to save " +
                    "this course.", Toast.LENGTH_LONG).show();
            return;
        }

        else if (getIntent().hasExtra("courseTitle")) {
            Integer id = course.getId();

            database.modifyCourseData(id, courseTitle.getText().toString(),
                    startDate.getText().toString(),
                    endDate.getText().toString(),
                    status.getText().toString(),
                    notes.getText().toString(),
                    name.getText().toString(),
                    phone.getText().toString(),
                    email.getText().toString(),
                    spinner.getSelectedItem().toString(),
                    startDateAlert,
                    endDateAlert,
                    course.getAlertId());

            if( course.getTitle()!= courseTitle.getText().toString())
                database.modifyCourseTitle(course.getTitle(), courseTitle.getText().toString());


            if (startDateAlert == 1 && DateHelper.getDateAndTime(startDate.getText().toString())
                    > System.currentTimeMillis()) enableAlert ("startDateAlert");

            if (endDateAlert == 1 && DateHelper.getDateAndTime(endDate.getText().toString())
                    > System.currentTimeMillis()) enableAlert ("endDateAlert");

            if (startDateAlert == 0 && course.getAlert1() == 1)
                disableAlert ("startDateAlert");

            if (endDateAlert == 0 && course.getAlert2() == 1)
                disableAlert ("endDateAlert");

            finish();
        }

        else {
            newAlertId = AlertReceiver.getNewAlertId(CourseAddActivity.this);
            database.addCourseData(courseTitle.getText().toString(),
                    startDate.getText().toString(),
                    endDate.getText().toString(),
                    status.getText().toString(),
                    notes.getText().toString(),
                    name.getText().toString(),
                    phone.getText().toString(),
                    email.getText().toString(),
                    spinner.getSelectedItem().toString(),
                    startDateAlert,
                    endDateAlert,
                    newAlertId);
            AlertReceiver.incrementNewAlertId(CourseAddActivity.this);
            AlertReceiver.incrementNewAlertId(CourseAddActivity.this);

            if (startDateAlert == 1 && DateHelper.getDateAndTime(startDate.getText().toString())
                    > System.currentTimeMillis()) enableAlert ("startDateAlert");

            if (endDateAlert == 1 && DateHelper.getDateAndTime(endDate.getText().toString())
                    > System.currentTimeMillis()) enableAlert ("endDateAlert");

            finish();
        }
    }

    private void enableAlert (String setAlert) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class );
        intent.putExtra("courseTitle", courseTitle.getText().toString());
        intent.putExtra("termTitle", spinner.getSelectedItem().toString());

        Integer id;
        String date = "";

        if (course != null)
            id = course.getAlertId();

        else id = newAlertId;

        intent.putExtra("id", id);

        if (setAlert == "startDateAlert") {

            intent.putExtra(setAlert, "Course Alert!");
            date = startDate.getText().toString();
        }
        else if (setAlert == "endDateAlert") {

            id++; intent.putExtra(setAlert, "Course Alert!");
            date = endDate.getText().toString();
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, DateHelper.getDateAndTime(date), pendingIntent);
    }

    private void disableAlert (String title)
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class );
        intent.putExtra("courseTitle", courseTitle.getText().toString());
        intent.putExtra("termTitle", spinner.getSelectedItem().toString());
        intent.putExtra(title, "");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                course.getAlertId(), intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    /*This will check for incoming data if an item is selected on the previous activity, & load
    them on the screen. If no items were selected, it will set the screen to 'Add item' mode. */
    @SuppressLint("SetTextI18n")
    private boolean getIncomingIntent(){

        if(getIntent().hasExtra("courseTitle")
                && getIntent().hasExtra("courseStart")
                && getIntent().hasExtra("courseEnd")
                && getIntent().hasExtra("courseStatus")
                && getIntent().hasExtra("courseNotes")
                && getIntent().hasExtra("mentorName")
                && getIntent().hasExtra("mentorPhone")
                && getIntent().hasExtra("mentorEmail")
                && getIntent().hasExtra("startDateAlert")
                && getIntent().hasExtra("endDateAlert")){

            Integer id = getIntent().getIntExtra("courseId", -1);
            String title = getIntent().getStringExtra("courseTitle");
            String start = getIntent().getStringExtra("courseStart");
            String end = getIntent().getStringExtra("courseEnd");
            String status= getIntent().getStringExtra("courseStatus");
            String notes = getIntent().getStringExtra("courseNotes");
            String name = getIntent().getStringExtra("mentorName");
            String phone = getIntent().getStringExtra("mentorPhone");
            String email = getIntent().getStringExtra("mentorEmail");
            String term = getIntent().getStringExtra("courseTerm");
            Integer alert1 = getIntent().getIntExtra("startDateAlert", -1);
            Integer alert2 = getIntent().getIntExtra("endDateAlert", -1);
            Integer alertId = getIntent().getIntExtra("courseAlertId", -1);
            StringBuilder assessmentCourse = new StringBuilder();

            String query = "select * from assessmentTable";
            cursor = database.getReadableDatabase().rawQuery(query, null);
            while (cursor.moveToNext()){

                if (cursor.getString(5).equals(title)){
                    assessmentCourse.append(cursor.getString(1) + " (" +
                            cursor.getString(4) + " assessment)\n");
                    assessmentView.setText(assessmentCourse);
                    assessmentLabel.setVisibility(View.VISIBLE);
                    assessmentView.setVisibility(View.VISIBLE);
                }
            }

            course = new Course(id, title, start, end, status, notes, name, phone, email, term,
                    alert1, alert2, alertId);

            setCourse(course);
            return true;
        }
        else {
            courseLabel.setText("Add Course");
            courseGuide.setText(R.string.tip3);
            return false;
        }
    }
    private void setCourse(Course course) {
        courseTitle.setText(course.getTitle(), EDITABLE);
        startDate.setText(course.getStartDate(), EDITABLE);
        endDate.setText(course.getEndDate(), EDITABLE);
        status.setText(course.getStatus(), EDITABLE);
        notes.setText(course.getNotes(), EDITABLE);
        name.setText(course.getName(), EDITABLE);
        phone.setText(course.getPhone(), EDITABLE);
        email.setText(course.getEmail(), EDITABLE);
        spinner.setSelection(database.getItemAtPosition(spinner.getAdapter(), course.getTerm()));
        startDateSwitch.setChecked(isChecked(course.getAlert1()));
        endDateSwitch.setChecked(isChecked(course.getAlert2()));
    }

    private Boolean isChecked(Integer i)
    {
        if (i == 1)
            return true;
        return false;
    }
}
