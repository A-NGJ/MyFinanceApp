package com.example.android.myfinanceapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.myfinanceapp.data.ExpenseContract;
import com.example.android.myfinanceapp.data.ExpenseContract.ExpensesEntry;
import com.example.android.myfinanceapp.data.ExpenseDbHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity  {

    private ExpenseDbHelper mExpenseHelper;
    private static final String LOG_TAG=MainActivity.class.getSimpleName();
    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
    public ExpenseCursorAdapter mExpenseCursorAdapter;
    public ExpenseCursorAdapter getExpenseCursorAdapter(){
        return mExpenseCursorAdapter;
    }
    private static final int EXPENSE_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        PagerAdapter pagerAdapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.slighting_tabs);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.catalog_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_insert_dummy_data:
                addExpense();
                return true;
            case R.id.action_delete_all_entries:
                getContentResolver().delete(ExpensesEntry.CONTENT_URI, null,null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addExpense(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        ContentValues contentValues = new ContentValues();

        contentValues.put(ExpensesEntry.COLUMN_VALUE, 8.88);
        contentValues.put(ExpensesEntry.COLUMN_CATEGORY, ExpensesEntry.CATEGORY_BILLS);
        contentValues.put(ExpensesEntry.COLUMN_DATE, formattedDate);

        getContentResolver().insert(ExpensesEntry.CONTENT_URI, contentValues);
    }

}
