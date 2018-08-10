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

import java.net.URI;

import static com.example.schoolapp.schoolapp.URIProvider.TERM_CONTENT_TYPE;

/**
 * Created by Shawn T Fox
 * For WGU - Mobile Application Development
 *  Course C196
 *  Student ID#000545644
 */

public class ActivityListCourse extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int VIEW_COURSE_REQUEST_CODE = 2002;
    private static final int EDIT_COURSE_REQUEST_CODE = 2003;

    private CursorAdapter cursorAdapter;
    private Uri termUri;
    private int termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_course);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_action_school);
        setSupportActionBar(toolbar);

        String[] from = { DBOpenHelper.COURSE_COLUMN_TITLE, DBOpenHelper.COURSE_COLUMN_START,
                DBOpenHelper.COURSE_COLUMN_END,  DBOpenHelper.COURSE_COLUMN_STATUS};
        int[] to = { R.id.tvCourseTitle, R.id.tvCourseStartDate, R.id.tvCourseEndDate, R.id.tvCourseStatus };

        cursorAdapter = new SimpleCursorAdapter(this, R.layout.item_course, null, from, to, 0);

        ListView lv = findViewById(R.id.course_list);
        lv.setAdapter(cursorAdapter);

        Intent intent = getIntent();
        termUri = intent.getParcelableExtra(URIProvider.TERM_CONTENT_TYPE);
        loadTerm();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 Intent intent = new Intent(ActivityListCourse.this, ActivityViewCourse.class);
                 Uri uri = Uri.parse(URIProvider.COURSE_URI + "/" + id);
                 intent.putExtra(URIProvider.COURSE_CONTENT_TYPE, uri);
                 startActivityForResult(intent, VIEW_COURSE_REQUEST_CODE);
                  }
            });

        getLoaderManager().initLoader(0, null, this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityListCourse.this, ActivityEditCourse.class);
                intent.putExtra(TERM_CONTENT_TYPE, termUri);
                startActivityForResult(intent, EDIT_COURSE_REQUEST_CODE);
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

    private void loadTerm() {
        if (termUri == null) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            termId = Integer.parseInt(termUri.getLastPathSegment());
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, URIProvider.COURSE_URI, DBOpenHelper.COURSE_COLUMNS,
                DBOpenHelper.COURSE_COLUMN_TERM_ID + " = " + this.termId, null,
                null);
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
