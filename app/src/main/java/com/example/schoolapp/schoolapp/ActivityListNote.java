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

import static com.example.schoolapp.schoolapp.URIProvider.ASSESSMENT_CONTENT_TYPE;
import static com.example.schoolapp.schoolapp.URIProvider.COURSE_CONTENT_TYPE;

/**
 * Created by Shawn T Fox
 * For WGU - Mobile Application Development
 *  Course C196
 *  Student ID#000545644
 */

public class ActivityListNote extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int VIEW_NOTE_REQUEST_CODE = 5002;
    private static final int EDIT_NOTE_REQUEST_CODE = 5003;

    private CursorAdapter cursorAdapter;
    private Uri courseUri;
    private int courseId;
    private Uri assessmentUri;
    private int assessmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_action_school);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();


        courseUri = intent.getParcelableExtra(URIProvider.COURSE_CONTENT_TYPE);
        assessmentUri = intent.getParcelableExtra(URIProvider.ASSESSMENT_CONTENT_TYPE);

        loadCourse();
        loadAssessment();
        setResult();

        String[] from = { DBOpenHelper.NOTE_COLUMN_NOTE_TEXT};
        int[] to = { R.id.tvNoteText };

        cursorAdapter = new SimpleCursorAdapter(this, R.layout.item_note, null, from, to, 0);

        ListView lv = findViewById(R.id.note_list);
        lv.setAdapter(cursorAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ActivityListNote.this, ActivityViewNote.class);
                Uri uri = Uri.parse(URIProvider.NOTE_URI + "/" + id);
                intent.putExtra(URIProvider.NOTE_CONTENT_TYPE, uri);
                startActivityForResult(intent, VIEW_NOTE_REQUEST_CODE);
            }
        });

        getLoaderManager().initLoader(0, null, this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityListNote.this, ActivityEditNote.class);
                if (courseUri != null) {
                    intent.putExtra(COURSE_CONTENT_TYPE, courseUri);
                }
                if (assessmentUri != null) {
                    intent.putExtra(ASSESSMENT_CONTENT_TYPE, assessmentUri);
                }
                startActivityForResult(intent, EDIT_NOTE_REQUEST_CODE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadCourse() {
        if (courseUri == null) {
            courseId = 0;
        } else {
            courseId = Integer.parseInt(courseUri.getLastPathSegment());
        }
    }
    private void loadAssessment() {
        if (assessmentUri == null) {
            assessmentId = 0;
        } else {
            assessmentId = Integer.parseInt(assessmentUri.getLastPathSegment());
        }
    }
    private  void setResult(){
        if (courseUri == null && assessmentUri == null){
            setResult(RESULT_CANCELED);
            finish();
        }
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
        if (assessmentUri != null) {
            return new CursorLoader(this, URIProvider.NOTE_URI, DBOpenHelper.NOTE_COLUMNS,
                    DBOpenHelper.NOTE_COLUMN_ASSESSMENT_ID + " = " + this.assessmentId,
                    null, null);
        }
        else {
            return new CursorLoader(this, URIProvider.NOTE_URI, DBOpenHelper.NOTE_COLUMNS,
                    DBOpenHelper.NOTE_COLUMN_COURSE_ID + " = " + this.courseId,
                    null, null);
        }

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
