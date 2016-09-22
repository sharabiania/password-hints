package com.alisharabiani;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddPasswordActivity extends AppCompatActivity {

    public void addOnClick(View view){
        EditText accountEditText = (EditText)findViewById(R.id.accountEditText);
        EditText usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        EditText passwordHintEditText = (EditText)findViewById(R.id.passwordHintEditText);

        String account = accountEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String passwordHint = passwordHintEditText.getText().toString().trim();

        // Validate input data.
        boolean isValid = true;

        if(account == null || account.isEmpty()) {
            accountEditText.setError("Cannot be empty.");
            isValid = false;
        }
        if(username == null || username.isEmpty()) {
            usernameEditText.setError("Cannot be empty.");
            isValid = false;
        }
        if(passwordHint == null || passwordHint.isEmpty()) {
            passwordHintEditText.setError("Cannot be empty.");
            isValid = false;
        }
        if(isValid == true) {

            // Save to database.
            HintEntryDbHelper mDbHelper = new HintEntryDbHelper(getApplicationContext());

            // Gets the data repository in write mode
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();

            values.put(PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT, account);
            values.put(PasswordHintContract.HintEntry.COLUMN_NAME_USERNAME, username);
            values.put(PasswordHintContract.HintEntry.COLUMN_NAME_PASSWORDHINT, passwordHint);

            // Insert the new row, returning the primary key value of the new row
            // it returns -1 if an error has occurred
            long newRowId;
            newRowId = db.insert(
                    PasswordHintContract.HintEntry.TABLE_NAME,
                    null,
                    values);

            //Intent data = new Intent();

            if (newRowId != -1) {
                //data.putExtra(Globals.IS_SUCCESSFUL_INTENT_EXTRA, true);
                //setResult(RESULT_OK, data);
                setResult(RESULT_OK);
            } else {
                //data.putExtra(Globals.IS_SUCCESSFUL_INTENT_EXTRA, false);
                //setResult(RESULT_CANCELED, data);
                setResult(RESULT_CANCELED);
            }

            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(toolbar);
//        //getSupportActionBar().setDisplayShowTitleEnabled(false);

    }
}
