package com.passwordhints.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.passwordhints.services.ASLogService;

/**
 * Created by Ali Sharabiani on 2016-08-11.
 */

public class HintEntryDbHelper extends SQLiteOpenHelper {
    private ASLogService log;
    private static final String LOG_TAG = "AS_DbHelper";

    // NOTE If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PasswordHint.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PasswordHintContract.HintEntry.TABLE_NAME + " (" +
                    PasswordHintContract.HintEntry._ID + " INTEGER PRIMARY KEY," +
                    PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT + TEXT_TYPE + COMMA_SEP +
                    PasswordHintContract.HintEntry.COLUMN_NAME_USERNAME + TEXT_TYPE + COMMA_SEP +
                    PasswordHintContract.HintEntry.COLUMN_NAME_PASSWORDHINT + TEXT_TYPE  +

                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PasswordHintContract.HintEntry.TABLE_NAME;


    public HintEntryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        log = new ASLogService(LOG_TAG);
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

    /**
     * Get all rows in the database ordered by service name.
     * @return cursor object containing sorted table.
     */
    public Cursor getAllRows() {

        return getAllRows(null, 0);
    }

    /**
     * Get all rows in the table.
     * @param inputText the search string.
     * @return if search string is null, returns all rows; otherwise, the found records.
     * @throws SQLException
     */
    public Cursor getAllRows(String inputText, int sortOrder) throws SQLException{
        String so = PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT;
        switch (sortOrder){
            case 0:
                so = PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT;
                break;
            case 1:
                so = PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT;
                break;
            case 2:
                so = PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT + " DESC";
                break;
        }
        SQLiteDatabase mDb = getReadableDatabase();
        Cursor mCursor;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(
                    PasswordHintContract.HintEntry.TABLE_NAME
                    , new String[] {
                            PasswordHintContract.HintEntry._ID,
                            PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT,
                            PasswordHintContract.HintEntry.COLUMN_NAME_USERNAME,
                            PasswordHintContract.HintEntry.COLUMN_NAME_PASSWORDHINT,
                        },
                    null,
                    null,
                    null,
                    null,
                    so// Sort order.
            );

        }
        else {
            mCursor = mDb.query(PasswordHintContract.HintEntry.TABLE_NAME,
                    new String[] {
                            PasswordHintContract.HintEntry._ID,
                            PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT,
                            PasswordHintContract.HintEntry.COLUMN_NAME_USERNAME,
                            PasswordHintContract.HintEntry.COLUMN_NAME_PASSWORDHINT,
                    },
                    PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT + " like '%" + inputText + "%'",
                    null,
                    null,
                    null,
                    null,
                    null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Delete a record based by id.
     * @param id id of the row to be deleted
     */
    public void deleteById(String id) {

        // Define 'where' part of the query.
        String selection = PasswordHintContract.HintEntry._ID + " = ? ";

        // Specify arguments in placeholder order.
        String[] selectionArgs = { id };

        // Issue SQL statement.
        SQLiteDatabase db = getWritableDatabase();
        db.delete(PasswordHintContract.HintEntry.TABLE_NAME, selection, selectionArgs);
        //db.close();
    }

    /**
     * Finds a record based by id.
     * @param id id of the row to be returned.
     */
    public RecordModel findById(int id){
        String stringId = String.valueOf(id);
        SQLiteDatabase db = getReadableDatabase();
        String where = PasswordHintContract.HintEntry._ID + " = ?";
        String[] whereArgs = { stringId };
        Cursor cursor = db.query(PasswordHintContract.HintEntry.TABLE_NAME, null, where, whereArgs, null, null, null);
        if(cursor != null){

            cursor.moveToFirst();
            String serviceName = cursor.getString(cursor.getColumnIndex(PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT));
            String accountName = cursor.getString(cursor.getColumnIndex(PasswordHintContract.HintEntry.COLUMN_NAME_USERNAME));
            String passwordHint = cursor.getString(cursor.getColumnIndex(PasswordHintContract.HintEntry.COLUMN_NAME_PASSWORDHINT));

            RecordModel model = new RecordModel();
            model.setId(id);
            model.setServiceName(serviceName);
            model.setAccountName(accountName);
            model.setPasswordHint(passwordHint);

            return model;
        }
        else
            return null;
    }

    /**
     * Updates a record in the database.
     * @param id id of the record to be updated.
     * @param model the new values for the record.
     * @return true if one row has been affected.
     */
    public boolean update(int id, RecordModel model){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT, model.getServiceName());
        newValues.put(PasswordHintContract.HintEntry.COLUMN_NAME_USERNAME, model.getAccountName());
        newValues.put(PasswordHintContract.HintEntry.COLUMN_NAME_PASSWORDHINT, model.getPasswordHint());
        String[] args = new String []{ String.valueOf(id)};
        int numberOfRowsAffected = db.update(
                PasswordHintContract.HintEntry.TABLE_NAME,
                newValues,
                PasswordHintContract.HintEntry._ID + "=?",
                args
                );

        if(numberOfRowsAffected == 1) {
            return true;
        }
        else return false;
    }

    public long insert(RecordModel model) {
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put(PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT, model.getServiceName());
        values.put(PasswordHintContract.HintEntry.COLUMN_NAME_USERNAME, model.getAccountName());
        values.put(PasswordHintContract.HintEntry.COLUMN_NAME_PASSWORDHINT, model.getPasswordHint());

        // Insert the new row, returning the primary key value of the new row
        // it returns -1 if an error has occurred
        long newRowId;
        newRowId = db.insert(
                PasswordHintContract.HintEntry.TABLE_NAME,
                null,
                values);

        //Intent data = new Intent();

        if (newRowId == -1) {
            String errorMessage = String.format("Could not create a new row: %s - %s - %s.",
                    model.getServiceName(), model.getAccountName(), model.getPasswordHint());
            log.e(errorMessage);
        }
        else{
            String message = String.format("Create:%s %s - %s - %s.",
                    Long.toString(newRowId),
                    model.getServiceName(), model.getAccountName(), model.getPasswordHint());
            log.i(message);
        }

        return newRowId;
    }
}


