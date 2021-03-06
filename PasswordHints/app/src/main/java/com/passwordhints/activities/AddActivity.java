package com.passwordhints.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.passwordhints.classes.ASAutoCompleteAdapter;
import com.passwordhints.classes.Globals;
import com.passwordhints.classes.HintEntryDbHelper;
import com.passwordhints.R;
import com.passwordhints.classes.RecordModel;
import com.passwordhints.fragments.AudioControlFragment;

import java.util.Arrays;


public class AddActivity extends AppCompatActivity implements AudioControlFragment.AudioControlEventListener{

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private Button addBtn;
    private AdView mAdView;

    public void addOnClick(View view) {
        AutoCompleteTextView accountEditText = (AutoCompleteTextView) findViewById(R.id.accountEditText);
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
                setResult(RESULT_CANCELED);
                return;
            }

            AudioControlFragment acFrag = (AudioControlFragment) getSupportFragmentManager().findFragmentById(R.id.acFragment);
            acFrag.saveAs(Long.toString(newRowId));
            setResult(RESULT_OK);

            finish();
            return;

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
        //requestWindowFeature(Window.FEATURE_ACTION_BAR);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add");

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Globals.DEFAULT_SERVICES);
        ASAutoCompleteAdapter adapter = new ASAutoCompleteAdapter(this, Arrays.asList(Globals.DEFAULT_SERVICES));
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
