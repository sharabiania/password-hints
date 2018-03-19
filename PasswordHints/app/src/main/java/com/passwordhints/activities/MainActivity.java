package com.passwordhints.activities;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import com.passwordhints.BuildConfig;
import com.passwordhints.R;
import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.passwordhints.classes.*;
import com.passwordhints.services.ASAudioService;
import com.passwordhints.services.ASLogService;
import com.google.android.gms.ads.MobileAds;

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
    private DrawerLayout mDrawerLayout;
    private int sortOrder = 0;
    private String searchFilter = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // NOTE AdMob App ID here
         MobileAds.initialize(this, getString(R.string.app_id));
        // NOTE AdMob Hello World App ID
        // MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        audioService = new ASAudioService(getApplicationContext());
        log = new ASLogService(LOG_TAG);
        audioService.setOnPlayCompletion();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getTitle().toString()){
                    case "Default":
                        sortOrder = 0;
                        item.setChecked(true);
                        break;
                    case "Z-A":
                        sortOrder = 2;
                        item.setChecked(true);
                        break;
                    case "Privacy Policy":
                        item.setChecked(false);
                        Intent intent = new Intent(MainActivity.this, PrivacyActivity.class);
                        startActivity(intent);
                        break;
                }

                mDrawerLayout.closeDrawers();
                cursor = mDbHelper.getAllRows(searchFilter, sortOrder);
                dataAdapter.swapCursor(cursor);
                dataAdapter.getFilter().filter(searchFilter);
                dataAdapter.notifyDataSetChanged();
                return true;
            }
        });

        mDbHelper = new HintEntryDbHelper(getApplicationContext());
        listView = (ListView) findViewById(R.id.listView1);
        cursor = mDbHelper.getAllRows(null, sortOrder);

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
                alertDialogBuilder.setIcon(RecordModel.getIcon(model.getServiceName()));
                alertDialogBuilder.setTitle(model.getServiceName());
                alertDialogBuilder.setMessage(model.getAccountName());
                alertDialogBuilder.create().show();

                return true;
            }
        });


        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return mDbHelper.getAllRows(constraint.toString(), sortOrder);
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

        if(BuildConfig.DEBUG) {
            String[] files = fileList();
            String[] dbList = databaseList();

            String s = "/Files/\n";
            for (String item : files)
                s += item + "\n";
            log.i(s);

            s = "/Databases/\n";
            for (String item : dbList)
                s += item + "\n";
            log.i(s);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy(){
        audioService.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                s = s.trim();
                searchFilter = s;
                if(s != "")
                    dataAdapter.getFilter().filter(s.toString());
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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


}
