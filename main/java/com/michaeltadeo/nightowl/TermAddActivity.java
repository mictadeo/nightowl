package com.michaeltadeo.nightowl;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.michaeltadeo.nightowl.Model.Term;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.widget.TextView.BufferType.EDITABLE;

public class TermAddActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText termTitle, startDate, endDate;
    private TextView termLabel, termGuide, courseView, courseLabel;
    private DatePickerDialog startDatePicker, endDatePicker;
    private SimpleDateFormat dateFormat;
    private DbOpenHelper database;
    private Cursor cursor;
    private Term term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_add);
        termTitle = findViewById(R.id.termTitleField);
        startDate = findViewById(R.id.termStartField);
        endDate = findViewById(R.id.termEndField);
        termLabel = findViewById(R.id.addTermLabel);
        termGuide = findViewById(R.id.termGuideLabel);
        dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        database = DbOpenHelper.getInstance(this);
        courseView = findViewById(R.id.courseMonitor);
        courseLabel = findViewById(R.id.termCourseLabel);

        setDatePickers();
        getIncomingIntent();

    }

    @Override
    public void onClick(View v) {
        if (v == startDate) {
            startDatePicker.show();
        }
        if (v == endDate) {
            endDatePicker.show();
        }
    }

    private void setDatePickers(){

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

    // This item will add the trash icon on the action bar at the top right.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (getIncomingIntent()) {
            getMenuInflater().inflate(R.menu.delete, menu);

        }
        return true;
    }

    /*These items configure the selection of trash icon. It checks for an assigned course
    to the selected Term to prevent deletion. If no courses are assigned, it will ask for
    confirmation & delete the term once it is confirmed.*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String query = "select * from courseTable where courseTerm = '"+ (term.getTitle()) + "'";
        cursor = database.getReadableDatabase().rawQuery(query, null);

        if(cursor.getCount() != 0) {
            Toast.makeText(getApplicationContext(), "Please reassign or delete courses that" +
                    " are associated, in order to delete this term.", Toast.LENGTH_LONG).show();
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
                    database.deleteTermData(term.getId());
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

        if (termTitle.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter a title in order to save " +
                    "this term.", Toast.LENGTH_LONG).show();
            return;
        }

        else if (getIntent().hasExtra("termTitle")) {
            Integer id = term.getId();
            database.modifyTermData(id, termTitle.getText().toString(),
                    startDate.getText().toString(),
                    endDate.getText().toString());

            if( term.getTitle()!= termTitle.getText().toString())
                database.modifyTermTitle(term.getTitle(), termTitle.getText().toString());

            finish();
        }

        else {
            database.addTermData(termTitle.getText().toString(),
                    startDate.getText().toString(),
                    endDate.getText().toString());
            finish();
        }
    }
    /*This will check for incoming data if an item is selected on the previous activity, & load
    them on the screen. If no items were selected, it will set the screen to 'Add item' mode. */
    @SuppressLint("SetTextI18n")
    private boolean getIncomingIntent(){

        if(getIntent().hasExtra("termTitle") &&
                getIntent().hasExtra("termStart") &&
                getIntent().hasExtra("termEnd")) {

            Integer id = getIntent().getIntExtra("termId", -1);
            String title = getIntent().getStringExtra("termTitle");
            String start = getIntent().getStringExtra("termStart");
            String end = getIntent().getStringExtra("termEnd");
            StringBuilder assignedCourses = new StringBuilder();

            String query = "select * from courseTable";
            cursor = database.getReadableDatabase().rawQuery(query, null);
            while (cursor.moveToNext()){

                if (cursor.getString(9).equals(title)){
                    assignedCourses.append(cursor.getString(1) + "  -  ");
                    courseView.setText(assignedCourses);
                    courseLabel.setVisibility(View.VISIBLE);
                    courseView.setVisibility(View.VISIBLE);
                }
            }
            term = new Term(id, title, start, end);
            setTerm(term);
            return true;
        }
        else {
            termLabel.setText("Add Term");
            termGuide.setText(R.string.tip3);
            return false;
        }
    }

    private void setTerm(Term term) {

        termTitle.setText(term.getTitle(),EDITABLE);
        startDate.setText(term.getStartDate(), EDITABLE);
        endDate.setText(term.getEndDate(), EDITABLE);
    }
}
