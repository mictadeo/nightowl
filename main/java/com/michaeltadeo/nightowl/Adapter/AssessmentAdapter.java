package com.michaeltadeo.nightowl.Adapter;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.database.Cursor;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.content.Intent;

import com.michaeltadeo.nightowl.AssessmentAddActivity;
import com.michaeltadeo.nightowl.DbOpenHelper;
import com.michaeltadeo.nightowl.R;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.assessmentViewHolder> {

    private Context context;
    private Cursor cursor;

    public AssessmentAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor=cursor;
    }

    @Override
    public int getItemCount() { return cursor.getCount(); }

    public class assessmentViewHolder extends RecyclerView.ViewHolder {

        public TextView titleViewer;

        public assessmentViewHolder(View v) {
            super(v);
            titleViewer = itemView.findViewById(R.id.titleView);
        }
    }

    @Override
    public assessmentViewHolder onCreateViewHolder(ViewGroup v, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.title_view, v, false);
        return new assessmentViewHolder(view);

    }

    public void onBindViewHolder(assessmentViewHolder viewHolder, int i) {

        if (!cursor.moveToPosition(i)) {
            return;

        }

        final Integer id = cursor.getInt(cursor.getColumnIndex(DbOpenHelper.assessmentId));
        final String title = cursor.getString(cursor.getColumnIndex(DbOpenHelper.assessmentTitle));
        final String dueDate = cursor.getString(cursor.getColumnIndex(DbOpenHelper.assessmentDue));
        final String goalDate = cursor.getString(cursor.getColumnIndex(DbOpenHelper.assessmentGoal));
        final String type = cursor.getString(cursor.getColumnIndex(DbOpenHelper.assessmentType));
        final String course = cursor.getString(cursor.getColumnIndex(DbOpenHelper.assessmentCourse));
        final Integer alert = cursor.getInt(cursor.getColumnIndex(DbOpenHelper.goalDateAlert));
        final Integer alertId = cursor.getInt(cursor.getColumnIndex(DbOpenHelper.goalAlertId));

        viewHolder.titleViewer.setText(title + ": " + type + " Assessment");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AssessmentAddActivity.class);
                intent.putExtra("assessmentId", id);
                intent.putExtra("assessmentTitle", title);
                intent.putExtra("assessmentDue", dueDate);
                intent.putExtra("assessmentGoal", goalDate);
                intent.putExtra("assessmentType", type);
                intent.putExtra("assessmentCourse", course);
                intent.putExtra("goalAlert", alert);
                intent.putExtra("goalAlertId", alertId);
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
