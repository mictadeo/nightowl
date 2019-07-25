package com.michaeltadeo.nightowl;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.michaeltadeo.nightowl.Adapter.TermAdapter;

public class TermActivity extends AppCompatActivity {

    private RecyclerView recyclerview;
    private SQLiteDatabase database;
    private TermAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

        DbOpenHelper dbHelper = DbOpenHelper.getInstance(this);
        database = dbHelper.getWritableDatabase();

        recyclerview = findViewById(R.id.termCycler);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TermAdapter(this, getAllData());
        recyclerview.setAdapter(adapter);
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

        Intent termIntent = new Intent(getBaseContext(), TermAddActivity.class);
        startActivity(termIntent);
        adapter.swapCursor(getAllData());
        return super.onOptionsItemSelected(item);
    }

    private Cursor getAllData() {
        return database.query(
                DbOpenHelper.termTable, null, null, null,
                null, null, null);
    }

    //This will refresh the activity during its "on resume" mode, by regathering data.
    @Override
    protected void onResume() {
        super.onResume();

        adapter = new TermAdapter(this, getAllData());
        recyclerview.setAdapter(adapter);
    }

    public void openMainActivity(View v){
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openCourseActivity(View v){
        Intent intent= new Intent(this, CourseActivity.class);
        startActivity(intent);
    }
}
