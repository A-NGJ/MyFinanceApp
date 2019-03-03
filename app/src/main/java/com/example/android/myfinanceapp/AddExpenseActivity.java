package com.example.android.myfinanceapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.myfinanceapp.data.ExpenseContract;
import com.example.android.myfinanceapp.data.ExpenseContract.ExpensesEntry;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddExpenseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RadioGroup mRadioGroup;
    private int mCategory = 0;
    private EditText mEditText;
    private RadioButton mCheckedButton;
    private Uri mCurrentUri;
    public static final int EXISTING_EXPENSE_LOADER = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expense);

        Intent intent = getIntent();
        mCurrentUri=intent.getData();

        if(mCurrentUri==null){
            setTitle(getString(R.string.activity_editor_new_expense));
            invalidateOptionsMenu();
        }else{
            setTitle(getString(R.string.ectivity_editor_edit_expense));
        }

        mEditText = (EditText) findViewById(R.id.expense_edit_text);
        mRadioGroup = (RadioGroup) findViewById(R.id.add_expense_radio_group);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mCheckedButton = (RadioButton) group.findViewById(checkedId);
            }
        });
        if(mCurrentUri!=null) {
            getSupportLoaderManager().initLoader(EXISTING_EXPENSE_LOADER, null, this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_add_expense, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(mCurrentUri==null){
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                addExpense();
                finish();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_delete:
                deleteExpense();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void addExpense(){

        String expenseString = mEditText.getText().toString().trim();
        float expenseFloat = 0;
        if(!expenseString.isEmpty()) {
            expenseFloat = Float.parseFloat(expenseString);
        }

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        if(mCurrentUri==null && TextUtils.isEmpty(expenseString)){
            return;
        }
        onRadioButtonClicked(mCheckedButton);

        ContentValues values = new ContentValues();

        values.put(ExpensesEntry.COLUMN_VALUE, expenseFloat);
        values.put(ExpensesEntry.COLUMN_CATEGORY, mCategory);
        values.put(ExpensesEntry.COLUMN_DATE, formattedDate );

        if(mCurrentUri==null) {
            Uri newUri = getContentResolver().insert(ExpensesEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.error_expense_save), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.expense_save_successful) + "category: " + String.valueOf(mCategory), Toast.LENGTH_SHORT).show();
            }
        }else{
            int rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);
            if(rowsAffected==0){
                Toast.makeText(this,"Error with editing expense", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Expense edited successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void deleteExpense()
    {
        if (mCurrentUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentUri, null, null);
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_button_bills:
                if (checked)
                    mCategory=0;
                    break;
            case R.id.radio_button_food:
                if (checked)
                    mCategory=1;
                    break;
            case R.id.radio_button_entertainment:
                if(checked)
                    mCategory=2;
                    break;
            case R.id.radio_button_car:
                if(checked)
                    mCategory=3;
                    break;
            default:
                mCategory=0;
                break;
        }
    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        if(mCurrentUri==null){
            return null;
        }
        String projection[] = {
                ExpensesEntry._ID,
                ExpensesEntry.COLUMN_VALUE,
                ExpensesEntry.COLUMN_CATEGORY,
                ExpensesEntry.COLUMN_DATE
        };
        return new CursorLoader(
                this,
                mCurrentUri,
                projection,
                null,
                null,
                null
        );

        }


    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if(cursor==null || cursor.getCount()<1){
            return;
        }
        if(cursor.moveToFirst()) {
            int expenseIndex = cursor.getColumnIndex(ExpensesEntry.COLUMN_VALUE);
            int categoryIndex = cursor.getColumnIndex(ExpensesEntry.COLUMN_CATEGORY);

            String expenseString = cursor.getString(expenseIndex);
            Integer category = cursor.getInt(categoryIndex);

            mEditText.setText(expenseString);
            switch (category){
                case ExpensesEntry.CATEGORY_BILLS:
                    mRadioGroup.check(R.id.radio_button_bills);
                    break;
                case ExpensesEntry.CATEGORY_FOOD:
                    mRadioGroup.check(R.id.radio_button_food);
                    break;
                case ExpensesEntry.CATEGORY_ENTERTAINMENT:
                    mRadioGroup.check(R.id.radio_button_entertainment);
                    break;
                case ExpensesEntry.CATEGORY_CAR:
                default:
                    mRadioGroup.check(R.id.radio_button_car);
                    break;

            }
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mEditText.setText("");
        mRadioGroup.check(R.id.radio_button_bills);
    }
}
