package com.michaeltadeo.nightowl;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.michaeltadeo.nightowl.Model.Assessment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import static android.widget.TextView.BufferType.EDITABLE;


public class AssessmentAddActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText assessmentTitle;
    private TextView assessmentLabel, assessmentGuide, dueDate, goalDate;
    private DatePickerDialog dueDatePicker, goalDatePicker;
    private SimpleDateFormat dateFormat;
    private DbOpenHelper database;
    private Assessment assessment;
    private RadioButton objective;
    private RadioGroup buttonGroup;
    private Spinner spinner;
    private Integer newAlertId, goalDateAlert;
    private Switch goalDateSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_add);

        assessmentLabel = findViewById(R.id.addAssessmentLabel);
        assessmentGuide = findViewById(R.id.assessmentLabelGuide);
        assessmentTitle = findViewById(R.id.assessmentTitleField);
        dueDate = findViewById(R.id.assessmentDueDateField);
        goalDate = findViewById(R.id.assessmentGoalField);
        dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        database = DbOpenHelper.getInstance(this);
        buttonGroup = findViewById(R.id.radioGroup);
        objective = findViewById(R.id.objectiveButton);
        spinner = findViewById(R.id.courseSpinner);
        goalDateSwitch = findViewById(R.id.goalSwitch);
        goalDateAlert = 0;

        //This item will setup the spinner container & fill it with titles of saved Courses.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout
                .simple_spinner_dropdown_item, database.getSpinnerData("courseTable"));
        spinner.setAdapter(adapter);

        setDatePickers();
        getIncomingIntent();
    }

    @Override
    public void onClick(View v) {
        if (v == dueDate) {
            dueDatePicker.show();
        }
        if (v == goalDate) {
            goalDateSwitch.setChecked(false);
            goalDatePicker.show();
        }
    }

    private void setDatePickers(){

        dueDate.setOnClickListener(this);
        goalDate.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        dueDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                dueDate.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        goalDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                goalDate.setText(dateFormat.format(newDate.getTime()));

            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        dueDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dueDatePicker.show();
                }
            }
        });

        goalDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    goalDatePicker.show();
                }
            }
        });
    }
    // This will set the time picker when the alert switch is turned on.
    public void setTimePicker (View v){

        if (goalDate.getText().toString().isEmpty()) {
            goalDateSwitch.setChecked(false);
            Toast.makeText(getApplicationContext(), "Please pick a date before setting an " +
                    "alert.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (goalDateSwitch.isChecked()){

            Calendar calendar = Calendar.getInstance();
            TimePickerDialog goalTimePicker = new TimePickerDialog
                    (AssessmentAddActivity.this,
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
                                    String datetime = goalDate.getText().toString() + "  " + hourOfDay + ":"
                                            + minuteString + " " + AM_PM + " " + TimeZone.getDefault()
                                            .getDisplayName(true, TimeZone.SHORT);

                                    goalDate.setText(datetime);
                                }
                            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                            false);
            goalTimePicker.show();

            goalTimePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    goalDateSwitch.setChecked(false);
                }
            });
        }
        else  {
            String date = goalDate.getText().toString().substring(0, 10);
            goalDate.setText("");
            goalDate.setText(date);
        }
    }

    // This item will add the trash icon on the action bar at the top right.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (getIncomingIntent()) {
            getMenuInflater().inflate(R.menu.delete, menu);

        }
        return true;
    }

    /*These items configure the selection of trash icon. It will ask for confirmation & delete the
    assessment once it is confirmed.*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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
                database.deleteAssessmentData(assessment.getId());
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.setTitle("Confirm Delete");
        alert.show();

        return super.onOptionsItemSelected(item);
    }

    //This will save / add data entry to the database when save button is pressed.
    public void saveButtonPressed (View v) {

        if (goalDateSwitch.isChecked()) goalDateAlert = 1;

        RadioButton type;
        if (assessmentTitle.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter a title in order to save " +
                    "this assessment.", Toast.LENGTH_LONG).show();
            return;
        }
        else if (getIntent().hasExtra("assessmentTitle")) {
            Integer id = assessment.getId();

            int selectedButton = buttonGroup.getCheckedRadioButtonId();
            type = findViewById(selectedButton);
            database.modifyAssessmentData(id, assessmentTitle.getText().toString(),
                    dueDate.getText().toString(),
                    goalDate.getText().toString(),
                    type.getText().toString(),
                    spinner.getSelectedItem().toString(),
                    goalDateAlert,
                    assessment.getAlertId());

            if (goalDateAlert == 1 && DateHelper.getDateAndTime(goalDate.getText().toString())
                    > System.currentTimeMillis()) enableAlert ();

            if (goalDateAlert == 0 && assessment.getAlert1() == 1)
                disableAlert ();

            finish();
        }

        else {
            newAlertId = AlertReceiver.getNewAlertId(AssessmentAddActivity.this);
            int selectedButton = buttonGroup.getCheckedRadioButtonId();
            type = findViewById(selectedButton);
            database.addAssessmentData(assessmentTitle.getText().toString(),
                    dueDate.getText().toString(),
                    goalDate.getText().toString(),
                    type.getText().toString(),
                    spinner.getSelectedItem().toString(),
                    goalDateAlert,
                    newAlertId);
            AlertReceiver.incrementNewAlertId(AssessmentAddActivity.this);

            if (goalDateAlert == 1 && DateHelper.getDateAndTime(goalDate.getText().toString())
                    > System.currentTimeMillis()) enableAlert ();

            finish();
        }
    }

    private void enableAlert()
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class );
        intent.putExtra("assessmentTitle", assessmentTitle.getText().toString());
        intent.putExtra("courseTitle", spinner.getSelectedItem().toString());

        Integer alertId;

        if (assessment != null)
            alertId = assessment.getAlertId();
        else
            alertId = newAlertId;

        intent.putExtra("id", alertId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alertId, intent,
                0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, DateHelper.getDateAndTime(goalDate.getText()
                .toString()), pendingIntent);
    }

    private void disableAlert()
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class );
        intent.putExtra("assessmentTitle", assessmentTitle.getText().toString());
        intent.putExtra("courseTitle", spinner.getSelectedItem().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                assessment.getAlertId(), intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    /*This will check for incoming data if an item is selected on the previous activity, & load
    them on the screen. If no items were selected, it will set the screen to 'Add item' mode. */
    private boolean getIncomingIntent(){
        if(getIntent().hasExtra("assessmentTitle") &&
                getIntent().hasExtra("assessmentDue") &&
                getIntent().hasExtra("assessmentGoal") &&
                getIntent().hasExtra("assessmentType") &&
                getIntent().hasExtra("goalAlert")) {

            Integer id = getIntent().getIntExtra("assessmentId", -1);
            String title = getIntent().getStringExtra("assessmentTitle");
            String due = getIntent().getStringExtra("assessmentDue");
            String goal = getIntent().getStringExtra("assessmentGoal");
            String type = getIntent().getStringExtra("assessmentType");
            String course = getIntent().getStringExtra("assessmentCourse");
            Integer alert = getIntent().getIntExtra("goalAlert", -1);
            Integer alertId = getIntent().getIntExtra("goalAlertId", -1);

            objective.setChecked(type.equals("Objective"));
            assessment = new Assessment(id, title, due, goal, type, course, alert, alertId);
            setAssessment(assessment);

            return true;
        }
        else {

            assessmentLabel.setText("Add Assessment");
            assessmentGuide.setText(R.string.tip3);
            return false;
        }
    }
    private void setAssessment(Assessment assessment) {

        assessmentTitle.setText(assessment.getTitle(),EDITABLE);
        dueDate.setText(assessment.getDueDate(), EDITABLE);
        goalDate.setText(assessment.getGoalDate());
        spinner.setSelection(database.getItemAtPosition(spinner.getAdapter(),
                assessment.getCourse()));
        goalDateSwitch.setChecked(isChecked(assessment.getAlert1()));
    }

    private Boolean isChecked(Integer i)
    {
        if (i == 1)
            return true;
        return false;
    }
}
