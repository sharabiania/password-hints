package com.alisharabiani;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ali Sharabiani on 2016-08-11.
 */

public class HintEntryDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PasswordHint.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PasswordHintContract.HintEntry.TABLE_NAME + " (" +
                    PasswordHintContract.HintEntry._ID + " INTEGER PRIMARY KEY," +
                  //  PasswordHintContract.HintEntry.COLUMN_NAME_ID + TEXT_TYPE + COMMA_SEP +
                    PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT + TEXT_TYPE + COMMA_SEP +
                    PasswordHintContract.HintEntry.COLUMN_NAME_USERNAME + TEXT_TYPE + COMMA_SEP +
                    PasswordHintContract.HintEntry.COLUMN_NAME_PASSWORDHINT + TEXT_TYPE  +

                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PasswordHintContract.HintEntry.TABLE_NAME;


    public HintEntryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public Cursor getAllRows() {

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PasswordHintContract.HintEntry._ID,
                PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT,
                PasswordHintContract.HintEntry.COLUMN_NAME_USERNAME,
                PasswordHintContract.HintEntry.COLUMN_NAME_PASSWORDHINT

        };

        // How you want the results sorted in the resulting Cursor
//        String sortOrder =
//                PasswordHintContract.HintEntry.COLUMN_NAME_UPDATED + " DESC";

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                PasswordHintContract.HintEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        return cursor;
    }

    public Cursor getAllRows(String inputText) throws SQLException{
        SQLiteDatabase mDb = getReadableDatabase();
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(
                    PasswordHintContract.HintEntry.TABLE_NAME
                    , new String[] {
                            PasswordHintContract.HintEntry._ID,
                            PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT,
                            PasswordHintContract.HintEntry.COLUMN_NAME_USERNAME,
                            PasswordHintContract.HintEntry.COLUMN_NAME_PASSWORDHINT,
                        },
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, PasswordHintContract.HintEntry.TABLE_NAME,
                    new String[] {
                            PasswordHintContract.HintEntry._ID,
                            PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT,
                            PasswordHintContract.HintEntry.COLUMN_NAME_USERNAME,
                            PasswordHintContract.HintEntry.COLUMN_NAME_PASSWORDHINT,
                    },
                    PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
}


