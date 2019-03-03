package com.example.android.myfinanceapp;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.content.ContentValues;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import com.example.android.myfinanceapp.data.ExpenseContract;
import com.example.android.myfinanceapp.data.ExpenseContract.ExpensesEntry;

import java.util.Map;


public class HistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    ExpenseCursorAdapter mCursorAdapter;
    private static final int EXPENSE_LOADER=0;
    public HistoryFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.expense_list, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        View emptyView = rootView.findViewById(R.id.empty_text_view);
        listView.setEmptyView(emptyView);


        mCursorAdapter = new ExpenseCursorAdapter(getActivity().getApplicationContext(), null);
        listView.setAdapter(mCursorAdapter);

        getLoaderManager().initLoader(EXPENSE_LOADER, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), AddExpenseActivity.class);
                Uri currentUri = ContentUris.withAppendedId(ExpensesEntry.CONTENT_URI, id);
                intent.setData(currentUri);
                startActivity(intent);
            }
        });

        return rootView;
    }
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] projection = {
                ExpensesEntry._ID,
                ExpensesEntry.COLUMN_VALUE,
                ExpensesEntry.COLUMN_CATEGORY,
                ExpensesEntry.COLUMN_DATE
        };
        return new CursorLoader(getActivity().getApplicationContext(),
                ExpensesEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

}
