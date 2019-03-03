package com.example.android.myfinanceapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.myfinanceapp.data.ExpenseContract.ExpensesEntry;

public class ExpenseProvider extends ContentProvider {

    public static final String LOG_TAG = ExpenseProvider.class.getSimpleName();
    private ExpenseDbHelper mDbHelper;

    private static final int EXPENSES = 100;
    private static final int EXPENSES_ID = 101;
    private final static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        sUriMatcher.addURI(ExpenseContract.CONTENT_AUTHORITY, ExpenseContract.PATH_EXPENSE, EXPENSES);
        sUriMatcher.addURI(ExpenseContract.CONTENT_AUTHORITY, ExpenseContract.PATH_EXPENSE+"/#", EXPENSES_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new ExpenseDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match){
            case EXPENSES:
                cursor = db.query(ExpenseContract.ExpensesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case EXPENSES_ID:
                selection = ExpenseContract.ExpensesEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(ExpensesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown Uri "+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EXPENSES:
                return ExpensesEntry.CONTENT_LIST_TYPE;
            case EXPENSES_ID:
                return ExpensesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case EXPENSES:
                return addExpense(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported by: "+uri);
        }
    }
    private Uri addExpense(Uri uri, ContentValues values){
        Integer name = values.getAsInteger(ExpensesEntry.COLUMN_VALUE);
        if(name==null || name<0 ){
            throw new IllegalArgumentException("Valid value required!");
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(ExpensesEntry.TABLE_NAME, null, values);
        if(id==-1){
            Log.e(LOG_TAG, "Failed to insert new row for "+uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch(match){
            case EXPENSES:
                rowsDeleted = db.delete(ExpensesEntry.TABLE_NAME, selection, selectionArgs);
                if(rowsDeleted!=0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case EXPENSES_ID:
                selection = ExpensesEntry._ID +"=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(ExpensesEntry.TABLE_NAME, selection, selectionArgs);
                if(rowsDeleted!=0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for: "+uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        if(contentValues.containsKey(ExpensesEntry.COLUMN_VALUE)){
            String name = contentValues.getAsString(ExpensesEntry.COLUMN_VALUE);
            if(name==null){
                throw new IllegalArgumentException("Pet requires a name");
            }
        }
        if(contentValues.containsKey(ExpensesEntry.COLUMN_CATEGORY)){
            Integer category = contentValues.getAsInteger(ExpensesEntry.COLUMN_CATEGORY);
            if(category == null || !ExpensesEntry.isValidCAtegory(category)){
                throw new IllegalArgumentException("Category required");
            }
        }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        selection = ExpensesEntry._ID + "=?";
        selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
        int rowsUpdated = db.update(ExpensesEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        if(rowsUpdated!=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
