package com.michaeltadeo.nightowl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.LinearLayoutManager;
import android.database.Cursor;
import android.view.Menu;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.michaeltadeo.nightowl.Adapter.CourseAdapter;

public class CourseActivity extends AppCompatActivity {

    private RecyclerView recyclerview;
    private SQLiteDatabase database;
    private CourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        DbOpenHelper dbHelper = DbOpenHelper.getInstance(this);
        database = dbHelper.getWritableDatabase();

        recyclerview = findViewById(R.id.courseCycler);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseAdapter(this, getAllData());
        recyclerview.setAdapter(adapter);

        String query = "select * from termTable";
        Cursor cursor = database.rawQuery(query, null);
        Intent intent;

        //This will disable the Course button from the main menu if term data has not been added.
        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "Please add a Term in order to add " +
                    "courses.", Toast.LENGTH_LONG).show();

            intent = new Intent(this,
                    MainActivity.class);
            startActivity(intent);
            finish();}
    }

    //This item will add the " + " (add button) on the top right of action bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    /*When the ' + ' is selected or tapped, the activity's adapter will swap data through
    cursor, open the next activity & then return to the previous activity including changes.*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent(getBaseContext(), CourseAddActivity.class);
        startActivity(intent);
        adapter.swapCursor(getAllData());
        return super.onOptionsItemSelected(item);
    }

    public Cursor getAllData() {
        return database.query(
                DbOpenHelper.courseTable, null, null, null,
                null, null, null);
    }

    //This will refresh the activity during its "on resume" mode, by regathering data.
    @Override
    protected void onResume() {
        super.onResume();

        adapter = new CourseAdapter(this, getAllData());
        recyclerview.setAdapter(adapter);
    }

    public void openMainActivity(View v){
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openAssessmentActivity(View v){
        Intent intent= new Intent(this, AssessmentActivity.class);
        startActivity(intent);
    }
}
