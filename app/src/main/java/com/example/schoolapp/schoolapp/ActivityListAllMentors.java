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

/**
 * Created by Shawn T Fox
 * For WGU - Mobile Application Development
 *  Course C196
 *  Student ID#000545644
 */

public class ActivityListAllMentors extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int VIEW_MENTOR_REQUEST_CODE = 4002;

    private CursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_mentors);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_action_school);
        setSupportActionBar(toolbar);

        String[] from = { DBOpenHelper.MENTOR_COLUMN_MENTOR_NAME, DBOpenHelper.MENTOR_COLUMN_MENTOR_PHONE,
                DBOpenHelper.MENTOR_COLUMN_MENTOR_EMAIL};
        int[] to = { R.id.tvMentorName, R.id.tvMentorPhone, R.id.tvMentorEmail };

        cursorAdapter = new SimpleCursorAdapter(this, R.layout.item_mentor, null, from, to, 0);

        ListView lv = findViewById(R.id.all_mentor_list);
        lv.setAdapter(cursorAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ActivityListAllMentors.this, ActivityViewMentor.class);
                Uri uri = Uri.parse(URIProvider.MENTOR_URI + "/" + id);
                intent.putExtra(URIProvider.MENTOR_CONTENT_TYPE, uri);
                startActivityForResult(intent, VIEW_MENTOR_REQUEST_CODE);
            }
        });

        getLoaderManager().initLoader(0, null, this);

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
        return new CursorLoader(ActivityListAllMentors.this, URIProvider.MENTOR_URI,  null,
                null,null, null);
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
