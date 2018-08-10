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
import java.util.Objects;

/**
 * Created by Shawn T Fox
 * For WGU - Mobile Application Development
 *  Course C196
 *  Student ID#000545644
 */

public class ActivityEditAssessment extends AppCompatActivity {

    private static final int EDIT_MENTOR_REQUEST_CODE = 4003;

    private EditText assessmentTitleField;
    private EditText assessmentCodeField;
    private TextView dueDateField;
    private TextView assessmentTypeField;
    private DatePickerDialog.OnDateSetListener dueDateListener;
    private EditText assessmentDescriptionField;
    private ToggleButton notificationToggle;

    private ObjectAssessment assessment;
    private String insertOrEditAssessment;
    private CharSequence stati[];

    private Uri assessmentUri;
    private Uri courseUri;

    private int assessmentId;
    private int courseId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_action_school);
        setSupportActionBar(toolbar);

        assessmentTitleField = findViewById(R.id.etAssessmentEditTitle);
        assessmentCodeField = findViewById(R.id.etAssessmentEditAssessmentCode);
        dueDateField = findViewById(R.id.tvAssessmentEditDueDate);
        assessmentTypeField = findViewById(R.id.tvAssessmentEditAssessmentType);
        assessmentDescriptionField = findViewById(R.id.etAssessmentEditDescription);
        notificationToggle = findViewById(R.id.tbAssessmentEditNotifications);

        Intent intent = getIntent();
        courseUri = intent.getParcelableExtra(URIProvider.COURSE_CONTENT_TYPE);
        assessmentUri = intent.getParcelableExtra(URIProvider.ASSESSMENT_CONTENT_TYPE);

        setupDueDate();
        setupAssessmentType();

        dueDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String endDate = month + 1 + "/" + dayOfMonth + "/" + year;
                dueDateField.setText(endDate);
            }
        };

        if (assessmentUri == null) {
            insertOrEditAssessment = Intent.ACTION_INSERT;
            setTitle("Add New Assessment");
        } else {
            insertOrEditAssessment = Intent.ACTION_EDIT;
            setTitle("Edit Assessment");
            assessmentId = Integer.parseInt(assessmentUri.getLastPathSegment());
            assessment= DataConverter.getAssessment(this, assessmentId);
            fillTextViews(assessment);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void saveAssessment(View view) {
        if (insertOrEditAssessment.equals(Intent.ACTION_INSERT)) {
            assessment = new ObjectAssessment();
            loadCourseData();
            if (getAssessmentFromTextViews()) {
                getAssessmentFromTextViews();
                int notification = (assessment.assessmentNotification) ? 1 : 0;

                DataConverter.insertAssessment(this,
                        Integer.parseInt(courseUri.getLastPathSegment()),
                        assessment.title,
                        assessment.assessmentCode,
                        assessment.assessmentType,
                        assessment.description,
                        assessment.dueDate,
                        notification);

                // Notify that insert was completed
                Toast.makeText(this,
                        "Assessment was added successfully",
                        Toast.LENGTH_LONG).show();

                if (DataConverter.getMentorCount(this, courseId) == 0) {
                    Intent intent = new Intent(ActivityEditAssessment.this,
                            ActivityEditMentor.class);
                    Uri uri = Uri.parse(URIProvider.COURSE_URI + "/" + courseId);
                    intent.putExtra(URIProvider.COURSE_CONTENT_TYPE, uri);
                    startActivityForResult(intent, EDIT_MENTOR_REQUEST_CODE);
                }

                setResult(RESULT_OK);
                finish();
            }

        } else if (insertOrEditAssessment.equals(Intent.ACTION_EDIT)) {
            if (getAssessmentFromTextViews()) {
                getAssessmentFromTextViews();
                int notification = (assessment.assessmentNotification) ? 1 : 0;

                DataConverter.editAssessment(this,
                        assessmentId,
                        assessment.courseId,
                        assessment.title,
                        assessment.assessmentCode,
                        assessment.assessmentType,
                        assessment.description,
                        assessment.dueDate,
                        notification);

                // Notify that update was completed
                Toast.makeText(this,
                        "Assessment was updated successfully",
                        Toast.LENGTH_SHORT).show();

                setResult(RESULT_OK);
                finish();
            }
        }

    }

    private void loadCourseData() {
        if (courseUri == null) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            courseId = Integer.parseInt(courseUri.getLastPathSegment());
        }
    }

    private void fillTextViews(ObjectAssessment oldAssessment) {
        assessmentTitleField.setText(assessment.title);
        assessmentCodeField.setText(assessment.assessmentCode);
        assessmentTypeField.setText(assessment.assessmentType);
        assessmentDescriptionField.setText(assessment.description);
        dueDateField.setText(assessment.dueDate);
        notificationToggle.setChecked(assessment.assessmentNotification);
    }

    private boolean getAssessmentFromTextViews() {
        if (assessmentTitleField.getText().toString().equals("") ||
                assessmentCodeField.getText().toString().equals("")||
                assessmentDescriptionField.getText().toString().equals("")){
            Toast.makeText(ActivityEditAssessment.this,
                    "Title, Code or Description missing, please fill out first",
                    Toast.LENGTH_LONG).show();
            return false;

        }else {
            assessment.title = assessmentTitleField.getText().toString().trim();
            assessment.assessmentCode = assessmentCodeField.getText().toString().trim();
            assessment.assessmentType = assessmentTypeField.getText().toString().trim();
            assessment.description = assessmentDescriptionField.getText().toString().trim();
            assessment.dueDate = dueDateField.getText().toString().trim();
            assessment.assessmentNotification = notificationToggle.isChecked();
            return true;
        }
    }

    private void setupDueDate() {
        dueDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int year = cal.get(Calendar.YEAR);

                DatePickerDialog dpd = new DatePickerDialog(ActivityEditAssessment.this,
                        android.R.style.Theme_DeviceDefault_Dialog, dueDateListener, year, month, day);
                dpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dpd.show();
            }
        });
    }

    private void setupAssessmentType() {
        assessmentTypeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stati = new CharSequence[]{"Performance", "Objective"};

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityEditAssessment.this);
                builder.setItems(stati, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        assessmentTypeField.setText(stati[which]);
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



