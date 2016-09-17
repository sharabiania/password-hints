package com.alisharabiani;

import android.app.Activity;
import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {

    AlertDialog.Builder builder;

    SimpleCursorAdapter dataAdapter;

    // The request code to start add a new row activity.
    static final int ADD_ROW_REQUEST = 1;

    Cursor cursor;

    ListView listView;

    HintEntryDbHelper mDbHelper;

    private SimpleCursorAdapter buildDataAdapter(HintEntryDbHelper dbHelper) {
        HintEntryDbHelper mDbHelper = dbHelper;
        cursor = mDbHelper.getAllRows();

        // Desired columns to be bound.
        String[] PROJECTION = new String[] {
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
        dataAdapter = new SimpleCursorAdapter(
                this,
                R.layout.record_info,
                cursor,
                PROJECTION,
                to,
                0
        );

        return dataAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbHelper = new HintEntryDbHelper(getApplicationContext());
        listView = (ListView) findViewById(R.id.listView1);

        listView.setAdapter(buildDataAdapter(mDbHelper));


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



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
                builder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        mDbHelper.deleteById(String.valueOf(id));
                        listView.setAdapter(buildDataAdapter(mDbHelper));
                        Toast.makeText(getApplicationContext(), "Record has been deleted.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setCancelable(true);
                builder.setTitle("Delete?");
                RecordModel model = mDbHelper.findById(String.valueOf(id));
                builder.setMessage(model.getAccountName() + "\n" + model.getUsername());
                builder.create().show();


                return true;
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


        // Set click event for the floating button.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddPasswordActivity.class);

                startActivityForResult(intent, ADD_ROW_REQUEST);
            }
        });


        // Initialize the dialog builder.
        builder = new AlertDialog.Builder(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == ADD_ROW_REQUEST) {
            if(resultCode == RESULT_OK)
            listView.setAdapter(buildDataAdapter(mDbHelper));
        }
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
