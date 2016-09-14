package com.alisharabiani;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView textView = (TextView) findViewById(R.id.textView);
      //  setSupportActionBar(toolbar);


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

//

//        // read from database
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();


//        textView.setText("Testing");
//        if (cursor.moveToFirst()){
//            do{
//                String data = cursor.getString(cursor.getColumnIndex("account")) + " --- ";
//                // do what ever you want here
//                textView.append(data);
//
//            }while(cursor.moveToNext());
//        }

        displayListView();
    }

    private void displayListView() {
        HintEntryDbHelper mDbHelper = new HintEntryDbHelper(getApplicationContext());
        Cursor cursor = mDbHelper.getAllRows();

        // Desired columns to be bound.
        String[] columns = new String[] {
            PasswordHintContract.HintEntry._ID,
            PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT,
            PasswordHintContract.HintEntry.COLUMN_NAME_USERNAME,
            PasswordHintContract.HintEntry.COLUMN_NAME_PASSWORDHINT,
        };

        // The XML defined view which the data will be bound to.
        int[] to = new int[]{
            R.id.Id,
            R.id.account,
            R.id.username,
            R.id.passwordhint,
        };

        // Create the adapter using the cursor pointing to the desired data
        // as well as the layout information.
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(
            this,
            R.layout.record_info,
            cursor,
            columns,
            to,
            0
        );

        ListView listView = (ListView) findViewById(R.id.listView1);
        // Assign adapter to ListView.
        listView.setAdapter(dataAdapter);



     //   cursor.close();
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
