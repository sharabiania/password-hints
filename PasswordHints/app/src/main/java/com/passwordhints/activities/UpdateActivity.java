package com.passwordhints.activities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import com.passwordhints.classes.Globals;
import com.passwordhints.classes.HintEntryDbHelper;
import com.passwordhints.R;
import com.passwordhints.classes.RecordModel;
import com.passwordhints.fragments.AudioControlFragment;

public class UpdateActivity extends FragmentActivity implements AudioControlFragment.AudioControlEventListener{

    private AudioControlFragment acFrag;
    private int recordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Globals.DEFAULT_SERVICES);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.service_name_id);

        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);

        RecordModel recordModel = new RecordModel();
        recordModel.setId(getIntent().getExtras().getInt(Globals.RECORD_ID_INTENT_EXTRA));
        recordModel.setServiceName(getIntent().getExtras().getString(Globals.SERVICE_NAME_INTENT_EXTRA));
        recordModel.setAccountName(getIntent().getExtras().getString(Globals.ACCOUNT_NAME_INTENT_EXTRA));
        recordModel.setPasswordHint(getIntent().getExtras().getString(Globals.PASSWORD_HINT_INTENT_EXTRA));

        AutoCompleteTextView serviceNameEditText = (AutoCompleteTextView) findViewById(R.id.service_name_id);
        EditText accountNameEditText = (EditText) findViewById(R.id.account_name_id);
        EditText passwordHintEditText = (EditText) findViewById(R.id.password_hint_id);

        serviceNameEditText.setText(recordModel.getServiceName());
        accountNameEditText.setText(recordModel.getAccountName());
        passwordHintEditText.setText(recordModel.getPasswordHint());
        recordId = recordModel.getId();
    }

    @Override
    protected void onStart(){
        super.onStart();
        acFrag = (AudioControlFragment) getSupportFragmentManager().findFragmentById(R.id.update_acFragment);
        acFrag.loadFile(String.valueOf(recordId));
    }

    public void updateOnClick(View view) {
        HintEntryDbHelper db = new HintEntryDbHelper(getApplicationContext());

        //int recordId = getIntent().getExtras().getInt(Globals.RECORD_ID_INTENT_EXTRA);

        EditText serviceNameEditText = (EditText) findViewById(R.id.service_name_id);
        EditText accountNameEditText = (EditText) findViewById(R.id.account_name_id);
        EditText passwordHintEditText = (EditText) findViewById(R.id.password_hint_id);

        String newServiceName = serviceNameEditText.getText().toString().trim();
        String newAccountName = accountNameEditText.getText().toString().trim();
        String newHint = passwordHintEditText.getText().toString().trim();

        // Validate input data.
        boolean isValid = true;

        if(newServiceName == null || newServiceName.isEmpty()) {
            serviceNameEditText.setError("Cannot be empty.");
            isValid = false;
        }
        if(newAccountName == null || newAccountName.isEmpty()) {
            accountNameEditText.setError("Cannot be empty.");
            isValid = false;
        }
        if(newHint == null || newHint.isEmpty()) {
            passwordHintEditText.setError("Cannot be empty.");
            isValid = false;
        }

        if(isValid == true) {

            RecordModel model = new RecordModel();
            model.setId(recordId);
            model.setServiceName(newServiceName);
            model.setAccountName(newAccountName);
            model.setPasswordHint(newHint);

            boolean isSuccessful = db.update(recordId, model);

            if(isSuccessful) {
                setResult(RESULT_OK);
            }
            else {
                setResult(RESULT_CANCELED);
            }

            acFrag.saveAs(Long.toString(recordId));
            finish();
        }
    }

    @Override
    public void OnRecord() {

    }

    @Override
    public void OnRecordCompleted() {

    }

    @Override
    public void OnPlay() {

    }

    @Override
    public void OnPlayCompleted() {

    }
}
