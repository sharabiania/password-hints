package com.alisharabiani;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.io.File;
import java.io.FileOutputStream;

import static android.R.attr.id;
import static java.security.AccessController.getContext;

public class AddPasswordActivity extends AppCompatActivity {

    public void addOnClick(View view){
        // TODO: do something.
        EditText accountEditText = (EditText)findViewById(R.id.accountEditText);
        EditText usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        EditText passwordHintEditText = (EditText)findViewById(R.id.passwordHintEditText);

        String account = accountEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String passwordHint = passwordHintEditText.getText().toString();

        // Save to database.


        HintEntryDbHelper mDbHelper = new HintEntryDbHelper(getApplicationContext());

        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
     //   values.put(PasswordHintContract.HintEntry.COLUMN_NAME_ID, id);
        values.put(PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT, account);
        values.put(PasswordHintContract.HintEntry.COLUMN_NAME_USERNAME, username);
        values.put(PasswordHintContract.HintEntry.COLUMN_NAME_PASSWORDHINT, passwordHint);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                PasswordHintContract.HintEntry.TABLE_NAME,
                null,
                values);

        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

    }
}
