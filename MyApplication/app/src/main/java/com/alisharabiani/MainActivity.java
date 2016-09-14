package com.alisharabiani;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
//import android.view.View.OnClickListener;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     //   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        final HintEntryDbHelper mDbHelper = new HintEntryDbHelper(getApplicationContext());
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
        final SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the password hint from this row in the database.
                String hint = cursor.getString(cursor.getColumnIndexOrThrow(PasswordHintContract.HintEntry.COLUMN_NAME_PASSWORDHINT));
                Toast.makeText(getApplicationContext(), hint, Toast.LENGTH_SHORT).show();
            }
        });

        EditText myFilter = (EditText) findViewById(R.id.myFilter);
        myFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return mDbHelper.getAllRows(constraint.toString());
            }
        });


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
