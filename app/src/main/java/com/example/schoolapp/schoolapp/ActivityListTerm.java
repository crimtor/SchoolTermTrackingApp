package com.example.schoolapp.schoolapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import static com.example.schoolapp.schoolapp.URIProvider.TERM_CONTENT_TYPE;

/**
 * Created by Shawn T Fox
 * For WGU - Mobile Application Development
 *  Course C196
 *  Student ID#000545644
 */

public class ActivityListTerm extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private CursorAdapter cursorAdapter;
    public static final int VIEW_TERM_REQUEST_CODE = 1002;
    private static final int EDIT_TERM_REQUEST_CODE = 1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_term);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_action_school);
        setSupportActionBar(toolbar);

        String[] from = { DBOpenHelper.TERM_COLUMN_TITLE, DBOpenHelper.TERM_COLUMN_START, DBOpenHelper.TERM_COLUMN_END };
        int[] to = { R.id.tvTermTitle, R.id.tvTermStartDate, R.id.tvTermEndDate };

        cursorAdapter = new SimpleCursorAdapter(this, R.layout.item_term, null, from, to, 0);

        ListView lv = findViewById(R.id.term_list);
        lv.setAdapter(cursorAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer iId = (int) id;
                Intent intent = new Intent(ActivityListTerm.this, ActivityViewTerm.class);
                Uri uri = Uri.parse(URIProvider.TERM_URI + "/" + iId);
                intent.putExtra(URIProvider.TERM_CONTENT_TYPE, uri);
                startActivityForResult(intent, VIEW_TERM_REQUEST_CODE);
            }
        });

        getLoaderManager().initLoader(0, null, this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityListTerm.this, ActivityEditTerm.class);
                startActivityForResult(intent, EDIT_TERM_REQUEST_CODE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
                getLoaderManager().restartLoader(0, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, URIProvider.TERM_URI, null, null,
                null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        cursorAdapter.swapCursor(null);
    }
    public void goHome(View view) {
        Intent intent99 = new Intent (this, MainActivity.class);
        this.startActivity(intent99);
    }
}
