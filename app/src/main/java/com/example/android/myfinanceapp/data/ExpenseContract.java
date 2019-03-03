package com.example.android.myfinanceapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ExpenseContract {

    public static final String CONTENT_AUTHORITY="com.example.android.myfinanceapp";
    public static final Uri BASE_CONTENT_URI= Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_EXPENSE = "expenses";

        public static abstract class ExpensesEntry implements BaseColumns{
            public static final String CONTENT_LIST_TYPE =
                    ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXPENSE;
            public static final String CONTENT_ITEM_TYPE =
                    ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXPENSE;

            public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EXPENSE);

            public static final String TABLE_NAME = "expenses";

            public static final String _ID = BaseColumns._ID;
            public static final String COLUMN_VALUE = "value";
            public static final String COLUMN_CATEGORY = "category";
            public static final String COLUMN_DATE = "date";

            public static final int CATEGORY_BILLS =0;
            public static final int CATEGORY_FOOD =1;
            public static final int CATEGORY_ENTERTAINMENT=2;
            public static final int CATEGORY_CAR = 3;

            public static boolean isValidCAtegory(int category){
                if(category == CATEGORY_BILLS || category == CATEGORY_FOOD || category == CATEGORY_ENTERTAINMENT || category == CATEGORY_CAR){
                    return true;
                }else return false;
            }
        }

}
