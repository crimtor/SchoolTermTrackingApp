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
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Shawn T Fox
 * For WGU - Mobile Application Development
 *  Course C196
 *  Student ID#000545644
 */

public class ActivityViewCourse extends AppCompatActivity {

    private static final int EDIT_COURSE_REQUEST_CODE = 2003;
    private static final int LIST_ASSESSMENT_REQUEST_CODE = 3001;
    private static final int EDIT_ASSESSMENT_REQUEST_CODE = 3003;
    private static final int LIST_MENTOR_REQUEST_CODE = 4001;
    private static final int EDIT_MENTOR_REQUEST_CODE = 4003;
    private static final int LIST_NOTE_REQUEST_CODE = 5001;


    private Uri courseUri;
    private ObjectCourse course;

    private TextView tvCourseViewTitle;
    private TextView tvCourseViewStart;
    private TextView tvCourseViewEnd;
    private TextView tvCourseViewDescription;
    private TextView tvCourseViewStatus;
    private TextView tvCourseViewNotifications;
    private Menu menu;

    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_action_school);
        setSupportActionBar(toolbar);

        tvCourseViewTitle = findViewById(R.id.tvCourseViewTitle);
        tvCourseViewStart = findViewById(R.id.tvCourseViewStartDate);
        tvCourseViewEnd = findViewById(R.id.tvCourseViewEndDate);
        tvCourseViewDescription = findViewById(R.id.tvCourseViewDescription);
        tvCourseViewStatus = findViewById(R.id.tvCourseViewStatus);
        tvCourseViewNotifications = findViewById(R.id.tvCourseViewNotifications);

        Intent intent = getIntent();
        courseUri = intent.getParcelableExtra(URIProvider.COURSE_CONTENT_TYPE);

        loadCourseData();

        isCourseComplete();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityViewCourse.this, ActivityEditCourse.class);
                intent.putExtra(URIProvider.COURSE_CONTENT_TYPE, courseUri);
                startActivityForResult(intent, EDIT_COURSE_REQUEST_CODE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_course_menu, menu);
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
            case R.id.action_delete_course:
                deleteCourse();
                return true;
            case R.id.action_assessments:
                Intent intent = new Intent(ActivityViewCourse.this, ActivityListAssessment.class);
                Uri uri = Uri.parse(URIProvider.COURSE_URI + "/" + courseId);
                intent.putExtra(URIProvider.COURSE_CONTENT_TYPE, uri);
                startActivityForResult(intent, LIST_ASSESSMENT_REQUEST_CODE);
                return true;

            case R.id.action_notes:
                Intent intent2 = new Intent(ActivityViewCourse.this, ActivityListNote.class);
                Uri uri2 = Uri.parse(URIProvider.COURSE_URI + "/" + courseId);
                intent2.putExtra(URIProvider.COURSE_CONTENT_TYPE, uri2);
                startActivityForResult(intent2, LIST_NOTE_REQUEST_CODE);
                return true;

            case R.id.action_mentors:
                Intent intent3 = new Intent(ActivityViewCourse.this, ActivityListNote.class);
                Uri uri3 = Uri.parse(URIProvider.COURSE_URI + "/" + courseId);
                intent3.putExtra(URIProvider.COURSE_CONTENT_TYPE, uri3);
                startActivityForResult(intent3, LIST_NOTE_REQUEST_CODE);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadCourseData() {
        if (courseUri == null) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            courseId = Integer.parseInt(courseUri.getLastPathSegment());
            course = DataConverter.getCourse(this, courseId);

            tvCourseViewTitle.setText(course.courseTitle);
            tvCourseViewStart.setText(course.courseStart);
            tvCourseViewEnd.setText(course.courseEnd);
            tvCourseViewDescription.setText(course.description);
            tvCourseViewStatus.setText(course.status);
            tvCourseViewNotifications.setText((Boolean.toString(course.courseNotification)));

        }
    }


    private void isCourseComplete() {
        if (DataConverter.getAssessmentCount(this, courseId) == 0) {
            Intent intent = new Intent(ActivityViewCourse.this, ActivityEditAssessment.class);
            Uri uri = Uri.parse(URIProvider.COURSE_URI + "/" + courseId);
            intent.putExtra(URIProvider.COURSE_CONTENT_TYPE, uri);
            startActivityForResult(intent, EDIT_ASSESSMENT_REQUEST_CODE);
        } else if (DataConverter.getMentorCount(this, courseId) == 0) {
            Intent intent = new Intent(ActivityViewCourse.this, ActivityEditMentor.class);
            Uri uri = Uri.parse(URIProvider.COURSE_URI + "/" + courseId);
            intent.putExtra(URIProvider.COURSE_CONTENT_TYPE, uri);
            startActivityForResult(intent, EDIT_MENTOR_REQUEST_CODE);
        }
    }

    public void openAssessmentListActivity(View view) {
        Intent intent = new Intent(ActivityViewCourse.this, ActivityListAssessment.class);
        Uri uri = Uri.parse(URIProvider.COURSE_URI + "/" + courseId);
        intent.putExtra(URIProvider.COURSE_CONTENT_TYPE, uri);
        startActivityForResult(intent, LIST_ASSESSMENT_REQUEST_CODE);
    }

    public void openNotesListActivity(View view) {
        Intent intent = new Intent(ActivityViewCourse.this, ActivityListNote.class);
        Uri uri = Uri.parse(URIProvider.COURSE_URI + "/" + courseId);
        intent.putExtra(URIProvider.COURSE_CONTENT_TYPE, uri);
        startActivityForResult(intent, LIST_NOTE_REQUEST_CODE);
    }

    public void openMentorListActivity(View view) {
        Intent intent = new Intent(ActivityViewCourse.this, ActivityListMentor.class);
        Uri uri = Uri.parse(URIProvider.COURSE_URI + "/" + courseId);
        intent.putExtra(URIProvider.COURSE_CONTENT_TYPE, uri);
        startActivityForResult(intent, LIST_MENTOR_REQUEST_CODE);
    }

    private boolean deleteCourse() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    // Confirm that user wishes to proceed
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                                DataConverter.deleteCourse(ActivityViewCourse.this, courseId);
                                // Notify that delete was completed
                                Toast.makeText(ActivityViewCourse.this,
                                        "Course was deleted successfully",
                                        Toast.LENGTH_SHORT).show();

                                setResult(RESULT_OK);
                                finish();
                            }
                            else {
                                Toast.makeText(ActivityViewCourse.this,
                                        "There are still courses in this term, you must delete"
                                                + " them first and then try again.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("WARNING!!! Are you sure you want to delete this Course? \n" +
        "Doing so will delete all Assessments, Mentors and Notes assigned to it.");
                builder.setPositiveButton(getString(android.R.string.yes), dialogClickListener);
                builder.setNegativeButton(getString(android.R.string.no), dialogClickListener);
                builder.show();

        return true;
    }

    public void goHome(View view) {
        Intent intent99 = new Intent (this, MainActivity.class);
        this.startActivity(intent99);
    }
}
