package com.alisharabiani;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import services.AudioService;

import java.io.File;

public class AddPasswordActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private boolean mStartRecording = false;
    private AudioService audioService;


    public void recordOnClick(View view){
        // TODO check for permissions.
        Button btn = (Button)view;
        Button playBtn = (Button)this.findViewById(R.id.playButton);
        mStartRecording = !mStartRecording;
        if(mStartRecording) {
            btn.setText("Stop Recording");
            playBtn.setVisibility(View.INVISIBLE);
            audioService.startRecording();
        }
        else{
            btn.setText("Record");
            audioService.stopRecording();
            playBtn.setVisibility(View.VISIBLE);
        }
    }

    public void playOnClick(View v){
        audioService.startPlaying();
    }

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

            audioService.saveAs(Long.toString(newRowId));



            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if(!permissionToRecordAccepted) finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Globals.DEFAULT_SERVICES);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.accountEditText);

        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);

       audioService = new AudioService(getApplicationContext());

    }
}
