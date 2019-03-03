package com.example.android.myfinanceapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.myfinanceapp.data.ExpenseContract.ExpensesEntry;

public class ExpenseDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME ="expenses.db";

    public ExpenseDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_EXPENSES_TABLE = "CREATE TABLE " + ExpensesEntry.TABLE_NAME +"("
                + ExpensesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ExpensesEntry.COLUMN_VALUE + " REAL NOT NULL DEFAULT 0, "
                + ExpensesEntry.COLUMN_CATEGORY + " TEXT NOT NULL, "
                + ExpensesEntry.COLUMN_DATE + " TEXT);";
        db.execSQL(SQL_CREATE_EXPENSES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
