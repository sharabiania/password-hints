package com.alisharabiani;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView textView = (TextView) findViewById(R.id.textView);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, AddPasswordActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });


        HintEntryDbHelper mDbHelper = new HintEntryDbHelper(getApplicationContext());
        // read from database
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

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

        Cursor cursor = db.query(
                PasswordHintContract.HintEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        textView.setText("Testing");
        if (cursor.moveToFirst()){
            do{
                String data = cursor.getString(cursor.getColumnIndex("account"));
                // do what ever you want here
                textView.append(data);

            }while(cursor.moveToNext());
        }
        cursor.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
