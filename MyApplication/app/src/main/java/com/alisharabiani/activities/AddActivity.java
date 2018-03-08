package com.alisharabiani.activities;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import com.alisharabiani.classes.Globals;
import com.alisharabiani.classes.HintEntryDbHelper;
import com.alisharabiani.classes.PasswordHintContract;
import com.alisharabiani.R;
import com.alisharabiani.fragments.AudioControlFragment;


public class AddActivity extends FragmentActivity implements AudioControlFragment.AudioControlEventListener{

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private Button addBtn;

    public void addOnClick(View view) {
        EditText accountEditText = (EditText) findViewById(R.id.accountEditText);
        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        EditText passwordHintEditText = (EditText) findViewById(R.id.passwordHintEditText);

        String account = accountEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String passwordHint = passwordHintEditText.getText().toString().trim();

        // Validate input data.
        boolean isValid = true;

        if (account == null || account.isEmpty()) {
            accountEditText.setError("Cannot be empty.");
            isValid = false;
        }
        if (username == null || username.isEmpty()) {
            usernameEditText.setError("Cannot be empty.");
            isValid = false;
        }
        if (passwordHint == null || passwordHint.isEmpty()) {
            passwordHintEditText.setError("Cannot be empty.");
            isValid = false;
        }
        if (isValid == true) {

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

            AudioControlFragment acFrag = (AudioControlFragment) getSupportFragmentManager().findFragmentById(R.id.acFragment);
            acFrag.saveAs(Long.toString(newRowId));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_password);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Globals.DEFAULT_SERVICES);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.accountEditText);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);

        addBtn = (Button)findViewById(R.id.addButton);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void OnRecord() {
        addBtn.setEnabled(false);
    }

    @Override
    public void OnRecordCompleted() {
        addBtn.setEnabled(true);
    }

    @Override
    public void OnPlay() {
        addBtn.setEnabled(false);
    }

    @Override
    public void OnPlayCompleted() {
        addBtn.setEnabled(true);
    }
}
