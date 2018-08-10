package com.example.schoolapp.schoolapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Shawn T Fox
 * For WGU - Mobile Application Development
 *  Course C196
 *  Student ID#000545644
 */

public class ActivityViewTerm extends AppCompatActivity {

    private static final int EDIT_TERM_REQUEST_CODE = 1003;
    private static final int LIST_COURSE_REQUEST_CODE = 2001;

    private Uri termUri;
    private ObjectTerm term;

    private TextView tvTermViewTitle;
    private TextView tvTermViewStart;
    private TextView tvTermViewEnd;

    private int termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_term);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_action_school);
        setSupportActionBar(toolbar);

        tvTermViewTitle = findViewById(R.id.tvTermTitle);
        tvTermViewStart = findViewById(R.id.tvTermViewStartDate);
        tvTermViewEnd = findViewById(R.id.tvTermViewEndDate);

        Intent intent = getIntent();
        termUri = intent.getParcelableExtra(URIProvider.TERM_CONTENT_TYPE);

        loadTermData();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityViewTerm.this, ActivityEditTerm.class);
                intent.putExtra(URIProvider.TERM_CONTENT_TYPE, termUri);
                startActivityForResult(intent, EDIT_TERM_REQUEST_CODE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_term_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_delete_term:
                deleteTerm();
                return true;
            case R.id.action_courses:
                Intent intent = new Intent(ActivityViewTerm.this, ActivityListCourse.class);
                intent.putExtra(URIProvider.TERM_CONTENT_TYPE, termUri);
                startActivityForResult(intent, LIST_COURSE_REQUEST_CODE);
                return true;

            case R.id.action_create_data:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void loadTermData() {
        if (termUri == null) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            termId = Integer.parseInt(termUri.getLastPathSegment());
            term = DataConverter.getTerm(this, termId);

            tvTermViewTitle.setText(term.termTitle);
            tvTermViewStart.setText(term.termStart);
            tvTermViewEnd.setText(term.termEnd);
        }
    }

    public void openCourseListActivity(View view) {
            Intent intent = new Intent(ActivityViewTerm.this, ActivityListCourse.class);
            intent.putExtra(URIProvider.TERM_CONTENT_TYPE, termUri);
            startActivityForResult(intent, LIST_COURSE_REQUEST_CODE);

    }
    private boolean deleteTerm() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    // Confirm that user wishes to proceed
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            int courseCount =
                                    DataConverter.getCourseCount(ActivityViewTerm.this, termId);
                            if (courseCount == 0) {
                                DataConverter.deleteTerm(ActivityViewTerm.this, termId);
                                // Notify that delete was completed
                                Toast.makeText(ActivityViewTerm.this,
                                        "Term was deleted successfully",
                                        Toast.LENGTH_SHORT).show();

                                setResult(RESULT_OK);
                                finish();
                            }
                            else {
                                Toast.makeText(ActivityViewTerm.this,
                                        "There are still courses in this term, you must delete"
                                        + " them first and then try again.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this term?")
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();

        return true;
    }


    public void goHome(View view) {
        Intent intent99 = new Intent (this, MainActivity.class);
        this.startActivity(intent99);
    }
}
