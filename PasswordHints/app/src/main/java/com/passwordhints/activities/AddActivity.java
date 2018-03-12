package com.passwordhints.activities;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import com.passwordhints.classes.Globals;
import com.passwordhints.classes.HintEntryDbHelper;
import com.passwordhints.R;
import com.passwordhints.classes.RecordModel;
import com.passwordhints.fragments.AudioControlFragment;


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
            RecordModel model = new RecordModel();
            model.setServiceName(account);
            model.setAccountName(username);
            model.setPasswordHint(passwordHint);
            HintEntryDbHelper mDbHelper = new HintEntryDbHelper(getApplicationContext());
            long newRowId = mDbHelper.insert(model);
            if(newRowId == -1) {
                // TODO show an error message to the user.
                return;
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

        setContentView(R.layout.activity_add);

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
