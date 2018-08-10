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

import static com.example.schoolapp.schoolapp.URIProvider.COURSE_CONTENT_TYPE;
import static com.example.schoolapp.schoolapp.URIProvider.TERM_CONTENT_TYPE;

/**
 * Created by Shawn T Fox
 * For WGU - Mobile Application Development
 *  Course C196
 *  Student ID#000545644
 */

public class ActivityListAssessment extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int VIEW_ASSESSMENT_REQUEST_CODE = 3002;
    private static final int EDIT_ASSESSMENT_REQUEST_CODE = 3003;

    private CursorAdapter cursorAdapter;
    private Uri courseUri;
    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_assessment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_action_school);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        courseUri = intent.getParcelableExtra(URIProvider.COURSE_CONTENT_TYPE);
        loadCourse();

        String[] from = { DBOpenHelper.ASSESSMENT_COLUMN_TITLE, DBOpenHelper.ASSESSMENT_COLUMN_CODE,
                DBOpenHelper.ASSESSMENT_COLUMN_DUE_DATE, DBOpenHelper.ASSESSMENT_COLUMN_ASSESSMENT_TYPE,
                DBOpenHelper.ASSESSMENT_COLUMN_DESCRIPTION};
        int[] to = { R.id.tvAssessmentTitle, R.id.tvAssessmentCode, R.id.tvAssessmentDueDate,
                R.id.tvAssessmentType,  R.id.tvAssessmentDescription};

        cursorAdapter = new SimpleCursorAdapter(this, R.layout.item_assessment, null,
                from, to, 0);

        ListView lv = findViewById(R.id.assessment_list);
        lv.setAdapter(cursorAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ActivityListAssessment.this, ActivityViewAssessment.class);
                Uri uri = Uri.parse(URIProvider.ASSESSMENT_URI + "/" + id);
                intent.putExtra(URIProvider.ASSESSMENT_CONTENT_TYPE, uri);
                startActivityForResult(intent, VIEW_ASSESSMENT_REQUEST_CODE);
            }
        });

        getLoaderManager().initLoader(0, null, this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityListAssessment.this, ActivityEditAssessment.class);
                intent.putExtra(COURSE_CONTENT_TYPE, courseUri);
                startActivityForResult(intent, EDIT_ASSESSMENT_REQUEST_CODE);
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

    private void loadCourse() {
        if (courseUri == null) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            courseId = Integer.parseInt(courseUri.getLastPathSegment());
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, URIProvider.ASSESSMENT_URI,  DBOpenHelper.ASSESSMENT_COLUMNS,
                DBOpenHelper.ASSESSMENT_COLUMN_COURSE_ID + " = " + this.courseId,
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
