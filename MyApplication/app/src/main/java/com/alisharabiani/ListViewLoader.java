package com.alisharabiani;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * Created by Ali Sharabiani on 2016-09-13.
 */
public class ListViewLoader
        extends ListActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * This is the Adapter being used to display the list's data.
     */
    SimpleCursorAdapter mAdapter;

    /**
     * These are the Contracts rows that we will retrieve.
     */
    static final String[] PROJECTION = new String[] {
            PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT,
            PasswordHintContract.HintEntry.COLUMN_NAME_USERNAME,
            PasswordHintContract.HintEntry.COLUMN_NAME_PASSWORDHINT
    };

    /**
     * This is the select criteria.
     */
    static final String SELECTION = "(("
            + PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT + " NOTNULL) AND ("
            + PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT + " != '' ))";


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Create a progress bar to display while the list loads.
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);

        // Must add the progress bar to the root of the layout.
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(progressBar);

        // For the cursor adapter, specify which columns go into which views.
        String[] fromColumn = {
            PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT
        };

        int[] toViews = {android.R.id.text1}; // The TextView in simple_list_item_1

        // Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
        mAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                null,
                fromColumn,
                toViews,
                0);

        setListAdapter(mAdapter);

        // Prepare the loader, Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * Called when a new loader needs to be created.
     * @param id
     * @param args
     * @return
     */
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        // TODO: find out what should be as the second paramenter for this.
        return new CursorLoader(this,
                null,
                PROJECTION, SELECTION, null, null);
    }


    /**
     * Called when a previously created loader has finished loading.
     * @param loader
     * @param data
     */
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        // Swap the new cursor in. (The framework will take care of closing the
        // old cursor once we return.)
        mAdapter.swapCursor(data);
    }

    /**
     * Called when a previuosly created folder is reset, making the data unavailable.
     * @param loader
     */
    public void onLoaderReset(Loader<Cursor> loader){
        // This is called when the last Cursor provided ti onLoadFinished()
        // above is about to be closed. We need to make sure we are no longer using it.
        mAdapter.swapCursor(null);
    }

//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id){
//        // Do something when a list item is clicked.
//
//        // Get the cursor, positioned to the corresponding row in the result set
//        Cursor cursor = (Cursor) l.getItemAtPosition(position);
//
//        // Get the password hint from this row in the database.
//        String hint = cursor.getString(cursor.getColumnIndexOrThrow(PasswordHintContract.HintEntry.COLUMN_NAME_PASSWORDHINT));
//        Toast.makeText(getApplicationContext(), hint, Toast.LENGTH_SHORT).show();
//    }

}
