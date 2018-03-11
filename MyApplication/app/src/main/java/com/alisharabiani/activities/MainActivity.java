package com.alisharabiani.activities;

import com.alisharabiani.R;
import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.alisharabiani.classes.*;
import com.alisharabiani.services.*;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    // The request code to start add a new row activity.
    private static final int ADD_ROW_REQUEST = 1;
    // The request code to start an update record activity.
    private static final int UPDATE_ROW_REQUEST = 2;
    private static final String LOG_TAG = "AS_MainActivity";

    private AlertDialog.Builder alertDialogBuilder;
    private SimpleCursorAdapter dataAdapter;
    private ASLogService log;
    private ASAudioService audioService;
    private Cursor cursor;
    private ListView listView;
    private HintEntryDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioService = new ASAudioService(getApplicationContext());
        log = new ASLogService(LOG_TAG);
        audioService.setOnPlayCompletion();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDbHelper = new HintEntryDbHelper(getApplicationContext());
        listView = (ListView) findViewById(R.id.listView1);

        cursor = mDbHelper.getAllRows();

        // Desired columns to be bound.
        String[] PROJECTION = new String[] {
                //  PasswordHintContract.HintEntry._ID,
                PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT,
                PasswordHintContract.HintEntry.COLUMN_NAME_USERNAME,
                PasswordHintContract.HintEntry.COLUMN_NAME_PASSWORDHINT,
        };

        // The XML defined view which the data will be bound to.
        int[] to = new int[]{
                //       R.id.Id,
                R.id.service_textview,
                R.id.account_textview,
                //       R.id.passwordhint,
        };


        dataAdapter = new ASListViewAdapter(
                this,
                R.layout.record_info,
                cursor,
                PROJECTION,
                to,
                0,
                audioService
        );


        listView.setAdapter(dataAdapter);

        View emptyView = findViewById(R.id.empty_list_view);

        listView.setEmptyView(emptyView);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the password hint from this row in the database.
                String hint = cursor.getString(cursor.getColumnIndexOrThrow(PasswordHintContract.HintEntry.COLUMN_NAME_PASSWORDHINT));
                //Toast.makeText(getApplicationContext(), hint, Toast.LENGTH_SHORT).show();

                View view1 = findViewById(R.id.coordinator_layout);
                Snackbar snackbar = Snackbar.make(view1, hint, Snackbar.LENGTH_LONG);
                snackbar.show();

            }
        });



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
                alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener(){
                   public void onClick(DialogInterface dialogInterface, int which){
                       Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                       RecordModel recordModel = mDbHelper.findById((int) id);
                       intent.putExtra(Globals.RECORD_ID_INTENT_EXTRA, recordModel.getId());
                       intent.putExtra(Globals.SERVICE_NAME_INTENT_EXTRA, recordModel.getServiceName());
                       intent.putExtra(Globals.ACCOUNT_NAME_INTENT_EXTRA, recordModel.getAccountName());
                       intent.putExtra(Globals.PASSWORD_HINT_INTENT_EXTRA, recordModel.getPasswordHint());
                       startActivityForResult(intent, UPDATE_ROW_REQUEST);
                   }
                });
                alertDialogBuilder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        mDbHelper.deleteById(String.valueOf(id));
                        if(!audioService.deleteIfExists(String.valueOf(id))){
                            Log.e("Audio Service","Unable to delete audio hint " + id);
                        }
                        cursor = mDbHelper.getAllRows();
                        dataAdapter.swapCursor(cursor);
                        dataAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "Record deleted.", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialogBuilder.setCancelable(true);
                RecordModel model = mDbHelper.findById((int) id);
                alertDialogBuilder.setIcon(model.getIcon());
                alertDialogBuilder.setTitle(model.getServiceName());
                alertDialogBuilder.setMessage(model.getAccountName());
                alertDialogBuilder.create().show();

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
               // listView.setAdapter(dataAdapter);
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

                Intent intent = new Intent(MainActivity.this, AddActivity.class);

                startActivityForResult(intent, ADD_ROW_REQUEST);
            }
        });


        // Initialize the dialog builder.
        alertDialogBuilder = new AlertDialog.Builder(this);

        // DEBUG
        String[] files = fileList();
        String[] cacheFiles = getCacheDir().list();
        File[] c = getCacheDir().listFiles();

        String fileList = "/Files/\n";
        for(String item : files)
            fileList += item + "\n";

        log.i(fileList);
    }

    @Override
    protected void onDestroy(){
        audioService.destroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == ADD_ROW_REQUEST) {
            if(resultCode == RESULT_OK) {
                //listView.setAdapter(buildDataAdapter(mDbHelper));
                cursor = mDbHelper.getAllRows();
                dataAdapter.swapCursor(cursor);
                dataAdapter.notifyDataSetChanged();

             //   listView.setAdapter(dataAdapter);
                Toast.makeText(getApplicationContext(), "Record added.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == UPDATE_ROW_REQUEST){
            if(resultCode == RESULT_OK) {
                cursor = mDbHelper.getAllRows();
                dataAdapter.swapCursor(cursor);
                dataAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Record updated.", Toast.LENGTH_SHORT).show();
            }
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
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
