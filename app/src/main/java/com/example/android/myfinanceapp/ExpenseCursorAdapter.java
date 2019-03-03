package com.example.android.myfinanceapp;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.myfinanceapp.data.ExpenseContract.ExpensesEntry;

public class ExpenseCursorAdapter extends CursorAdapter {

    public ExpenseCursorAdapter(Context context, Cursor c){
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView categoryTextView = (TextView) view.findViewById(R.id.category);
        TextView dateTextView = (TextView) view.findViewById(R.id.date);

        int nameColumnIndex = cursor.getColumnIndex(ExpensesEntry.COLUMN_VALUE);
        int categoryColumnIndex = cursor.getColumnIndex(ExpensesEntry.COLUMN_CATEGORY);
        int dateColumnIndex = cursor.getColumnIndex(ExpensesEntry.COLUMN_DATE);

        Float nameColumnFloat = cursor.getFloat(nameColumnIndex);
        Integer categoryColumnInt = cursor.getInt(categoryColumnIndex);
        String dateColumnString = cursor.getString(dateColumnIndex);

        String categoryName = getCategoryName(categoryColumnInt);

        nameTextView.setText(String.valueOf(nameColumnFloat));
        categoryTextView.setText(categoryName);
        dateTextView.setText(dateColumnString);
    }
    @NonNull
    private String getCategoryName(int index){
        switch (index){
            case ExpensesEntry.CATEGORY_BILLS:
                return "Bills";
            case ExpensesEntry.CATEGORY_ENTERTAINMENT:
                return "Entertainment";
            case ExpensesEntry.CATEGORY_FOOD:
                return "Food";
            case ExpensesEntry.CATEGORY_CAR:
                return "Car";
            default:
                throw new IllegalArgumentException("Invalid category index: "+String.valueOf(index));
        }
    }
}
