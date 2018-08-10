package com.example.schoolapp.schoolapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

/**
 * Created by Shawn T Fox
 * For WGU - Mobile Application Development
 *  Course C196
 *  Student ID#000545644
 */

public class ActivityEditCourse extends AppCompatActivity {

    private EditText courseTitleField;
    private TextView startDateField;
    private TextView endDateField;
    private TextView statusField;
    private DatePickerDialog.OnDateSetListener startDateListener;
    private DatePickerDialog.OnDateSetListener endDateListener;
    private EditText courseDescriptionField;
    private ToggleButton notificationToggle;
    private ObjectCourse course;
    private String insertOrEditCourse;
    private CharSequence stati[];
    private Uri termUri;
    private Uri courseUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_action_school);
        setSupportActionBar(toolbar);

        courseTitleField = findViewById(R.id.etCourseEditTitle);
        startDateField = findViewById(R.id.tvCourseEditStart);
        endDateField = findViewById(R.id.tvCourseEditEnd);
        statusField = findViewById(R.id.tvCourseEditStatus);
        courseDescriptionField = findViewById(R.id.etCourseEditDescription);
        notificationToggle = findViewById(R.id.tbCourseEditNotifications);

        Intent intent = getIntent();
        courseUri = intent.getParcelableExtra(URIProvider.COURSE_CONTENT_TYPE);
        termUri = intent.getParcelableExtra(URIProvider.TERM_CONTENT_TYPE);

        setupStartDate();
        setupEndDate();
        setupStatus();

        startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String startDate = month + 1 + "/" + dayOfMonth + "/" + year;
                startDateField.setText(startDate);
            }
        };
        endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String endDate = month + 1 + "/" + dayOfMonth + "/" + year;
                endDateField.setText(endDate);
            }
        };

        if (courseUri == null) {
            insertOrEditCourse = Intent.ACTION_INSERT;
            setTitle("Create New Course");
        } else {
            insertOrEditCourse = Intent.ACTION_EDIT;
            setTitle("Edit Course");
            int courseId = Integer.parseInt(courseUri.getLastPathSegment());
            course= DataConverter.getCourse(this, courseId);
            fillTextViews(course);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void saveCourse(View view) {
        if (insertOrEditCourse.equals(Intent.ACTION_INSERT)) {
            course = new ObjectCourse();
            getTermFromTextViews();
            int notification = (course.courseNotification) ? 1:0;

            DataConverter.insertCourse(this,
                    Integer.parseInt(termUri.getLastPathSegment()),
                    course.courseTitle,
                    course.courseStart,
                    course.courseEnd,
                    course.description,
                    course.status,
                    notification);

            // Notify that insert was completed
            Toast.makeText(this,
                    "Course was added successfully",
                    Toast.LENGTH_LONG).show();

            setResult(RESULT_OK);

        } else if (insertOrEditCourse.equals(Intent.ACTION_EDIT)) {
            getTermFromTextViews();
            int notification = (course.courseNotification) ? 1:0;

            DataConverter.editCourse(this,
                    course.courseId,
                    course.termId,
                    course.courseTitle,
                    course.courseStart,
                    course.courseEnd,
                    course.description,
                    course.status,
                    notification);

            // Notify that update was completed
            Toast.makeText(this,
                    "Course was updated successfully",
                    Toast.LENGTH_SHORT).show();

            setResult(RESULT_OK);
        }

        finish();
    }

    private void fillTextViews(ObjectCourse oldCourse) {
        courseTitleField.setText(course.courseTitle);
        startDateField.setText(course.courseStart);
        endDateField.setText(course.courseEnd);
        courseDescriptionField.setText(course.description);
        statusField.setText(course.status);
        notificationToggle.setChecked(course.courseNotification);
    }

    private void getTermFromTextViews() {
        course.courseTitle = courseTitleField.getText().toString().trim();
        course.courseStart = startDateField.getText().toString().trim();
        course.courseEnd = endDateField.getText().toString().trim();
        course.description = courseDescriptionField.getText().toString().trim();
        course.status = statusField.getText().toString().trim();
        course.courseNotification = notificationToggle.isChecked();
    }

    private void setupStartDate() {
        startDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int year = cal.get(Calendar.YEAR);

                DatePickerDialog dpd = new DatePickerDialog(ActivityEditCourse.this,
                        android.R.style.Theme_DeviceDefault_Dialog, startDateListener, year, month, day);
                dpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dpd.show();
            }
        });
    }
    private void setupEndDate() {
        endDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int year = cal.get(Calendar.YEAR);

                DatePickerDialog dpd = new DatePickerDialog(ActivityEditCourse.this,
                        android.R.style.Theme_DeviceDefault_Dialog, endDateListener, year, month +2, day);
                dpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dpd.show();
            }
        });
    }

    private void setupStatus() {
        statusField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stati = new CharSequence[]{"Plan to Take", "In Progress", "Completed", "Dropped"};

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityEditCourse.this);
                builder.setItems(stati, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        statusField.setText(stati[which]);
                    }
                });
                builder.show();
            }
        });
    }
    public void goHome(View view) {
        Intent intent99 = new Intent (this, MainActivity.class);
        this.startActivity(intent99);
    }

}
