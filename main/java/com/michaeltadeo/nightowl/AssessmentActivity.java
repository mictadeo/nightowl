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

import com.michaeltadeo.nightowl.Adapter.AssessmentAdapter;

public class AssessmentActivity extends AppCompatActivity {

    private RecyclerView recyclerview;
    private SQLiteDatabase database;
    private AssessmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        DbOpenHelper dbHelper = DbOpenHelper.getInstance(this);
        database = dbHelper.getWritableDatabase();

        recyclerview = findViewById(R.id.assessmentCycler);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AssessmentAdapter(this, getAllData());
        recyclerview.setAdapter(adapter);

        String query = "select * from courseTable";
        Cursor cursor = database.rawQuery(query, null);
        Intent intent;

        //This disables the Assessment button from the main menu if no course data has been added.
        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "Please add a course in order to add " +
                    "assessments.", Toast.LENGTH_LONG).show();

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

        Intent intent = new Intent(getBaseContext(), AssessmentAddActivity.class);
        startActivity(intent);
        adapter.swapCursor(getAllData());
        return super.onOptionsItemSelected(item);
    }

    private Cursor getAllData() {
        return database.query(
                DbOpenHelper.assessmentTable, null, null, null,
                null, null, null);
    }

    //This will refresh the activity during its "on resume" mode, by regathering data.
    @Override
    protected void onResume() {
        super.onResume();

        adapter = new AssessmentAdapter(this, getAllData());
        recyclerview.setAdapter(adapter);
    }

    public void openMainActivity(View v){
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
