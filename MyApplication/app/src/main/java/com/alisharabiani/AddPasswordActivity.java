package com.alisharabiani;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.io.File;
import java.io.FileOutputStream;

public class AddPasswordActivity extends AppCompatActivity {

    public void addOnClick(View view){
        // TODO: do something.
        EditText accountEditText = (EditText)findViewById(R.id.accountEditText);
        EditText usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        EditText passwordHintEditText = (EditText)findViewById(R.id.passwordHintEditText);

        String account = accountEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String passwordHint = passwordHintEditText.getText().toString();

        // Save data to file.
        File file = new File(getFilesDir(), Globals.DATA_FILE_NAME);


        String string = account + "\n" + username + "\n" + passwordHint + "\n";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(Globals.DATA_FILE_NAME, Context.MODE_APPEND);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

    }
}
