package com.michaeltadeo.nightowl.Adapter;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.database.Cursor;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.content.Intent;

import com.michaeltadeo.nightowl.CourseAddActivity;
import com.michaeltadeo.nightowl.DbOpenHelper;
import com.michaeltadeo.nightowl.R;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.courseViewHolder> {

    private Context context;
    private Cursor cursor;

    public CourseAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor=cursor;
    }

    @Override
    public int getItemCount() { return cursor.getCount(); }

    public class courseViewHolder extends RecyclerView.ViewHolder {

        public TextView titleViewer;

        public courseViewHolder(View v) {
            super(v);
            titleViewer = v.findViewById(R.id.titleView);
        }
    }

    @Override
    public courseViewHolder onCreateViewHolder(ViewGroup v, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.title_view, v, false);
        return new courseViewHolder(view);

    }

    public void onBindViewHolder(courseViewHolder viewHolder, int i) {

        if (!cursor.moveToPosition(i)) {
            return;

        }

        final Integer id = cursor.getInt(cursor.getColumnIndex(DbOpenHelper.courseId));
        final String title = cursor.getString(cursor.getColumnIndex(DbOpenHelper.courseTitle));
        final String startDate = cursor.getString(cursor.getColumnIndex(DbOpenHelper.courseStart));
        final String endDate = cursor.getString(cursor.getColumnIndex(DbOpenHelper.courseEnd));
        final String status = cursor.getString(cursor.getColumnIndex(DbOpenHelper.courseStatus));
        final String notes = cursor.getString(cursor.getColumnIndex(DbOpenHelper.courseNotes));
        final String name = cursor.getString(cursor.getColumnIndex(DbOpenHelper.mentorName));
        final String phone = cursor.getString(cursor.getColumnIndex(DbOpenHelper.mentorPhone));
        final String email = cursor.getString(cursor.getColumnIndex(DbOpenHelper.mentorEmail));
        final String term = cursor.getString(cursor.getColumnIndex(DbOpenHelper.courseTerm));
        final Integer alert1 = cursor.getInt(cursor.getColumnIndex(DbOpenHelper.startDateAlert));
        final Integer alert2 = cursor.getInt(cursor.getColumnIndex(DbOpenHelper.endDateAlert));
        final Integer alertId = cursor.getInt(cursor.getColumnIndex(DbOpenHelper.courseAlertId));

        viewHolder.titleViewer.setText(title + ": " + status + " (term: " + term +")");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CourseAddActivity.class);
                intent.putExtra("courseId", id);
                intent.putExtra("courseTitle", title);
                intent.putExtra("courseStart", startDate);
                intent.putExtra("courseEnd", endDate);
                intent.putExtra("courseStatus", status);
                intent.putExtra("courseNotes", notes);
                intent.putExtra("mentorName", name);
                intent.putExtra("mentorPhone", phone);
                intent.putExtra("mentorEmail", email);
                intent.putExtra("courseTerm", term);
                intent.putExtra("startDateAlert", alert1);
                intent.putExtra("endDateAlert", alert2);
                intent.putExtra("courseAlertId", alertId);
                context.startActivity(intent);
            }
        });
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }

        cursor  = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
