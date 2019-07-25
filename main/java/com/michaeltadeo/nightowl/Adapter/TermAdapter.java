package com.michaeltadeo.nightowl.Adapter;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.database.Cursor;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.content.Intent;

import com.michaeltadeo.nightowl.DbOpenHelper;
import com.michaeltadeo.nightowl.R;
import com.michaeltadeo.nightowl.TermAddActivity;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.termViewHolder> {

    private Context context;
    private Cursor cursor;

    public TermAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class termViewHolder extends RecyclerView.ViewHolder {

        public TextView titleViewer;

        public termViewHolder(View v) {
            super(v);
            titleViewer = v.findViewById(R.id.titleView);

        }
    }

    @Override
    public termViewHolder onCreateViewHolder(ViewGroup v, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.title_view, v, false);
        return new termViewHolder(view);
    }

    public void onBindViewHolder(termViewHolder viewHolder, int i) {
        if (!cursor.moveToPosition(i)) {
            return;
        }
        final Integer id = cursor.getInt(cursor.getColumnIndex(DbOpenHelper.termId));
        final String title = cursor.getString(cursor.getColumnIndex(DbOpenHelper.termTitle));
        final String startDate = cursor.getString(cursor.getColumnIndex(DbOpenHelper.termStart));
        final String endDate = cursor.getString(cursor.getColumnIndex(DbOpenHelper.termEnd));

        viewHolder.titleViewer.setText(title + ":  " + startDate + "  -  " + endDate);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TermAddActivity.class);
                intent.putExtra("termId", id);
                intent.putExtra("termTitle", title);
                intent.putExtra("termStart", startDate);
                intent.putExtra("termEnd", endDate);

                context.startActivity(intent);
            }

            ;
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