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

import static com.example.schoolapp.schoolapp.URIProvider.COURSE_CONTENT_TYPE;

/**
 * Created by Shawn T Fox
 * For WGU - Mobile Application Development
 *  Course C196
 *  Student ID#000545644
 */

public class ActivityViewAssessment extends AppCompatActivity {

    private static final int EDIT_ASSESSMENT_REQUEST_CODE = 3003;
    private static final int NOTE_LIST_REQUEST_CODE = 4001;

    private Uri assessmentUri;
    private ObjectAssessment assessment;

    private TextView tvAssessmentViewTitle;
    private TextView tvAssessmentViewCode;
    private TextView tvAssessmentViewDescription;
    private TextView tvAssessmentViewDueDate;
    private TextView tvAssessmentViewAssessmentType;
    private TextView tvAssessmentViewNotification;

    private int assessmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assessment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_action_school);
        setSupportActionBar(toolbar);

        tvAssessmentViewTitle = findViewById(R.id.tvAssessmentViewTitle);
        tvAssessmentViewCode = findViewById(R.id.tvAssessmentViewAssessmentCode);
        tvAssessmentViewDescription = findViewById(R.id.tvAssessmentViewDescription);
        tvAssessmentViewDueDate = findViewById(R.id.tvAssessmentViewDueDate);
        tvAssessmentViewAssessmentType = findViewById(R.id.tvAssessmentViewAssessmentType);
        tvAssessmentViewNotification = findViewById(R.id.tvAssessmentViewNotifications);

        Intent intent = getIntent();
        assessmentUri = intent.getParcelableExtra(URIProvider.ASSESSMENT_CONTENT_TYPE);

        loadAssessmentData();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ActivityViewAssessment.this, ActivityEditAssessment.class);
                intent2.putExtra(URIProvider.ASSESSMENT_CONTENT_TYPE, assessmentUri);
                startActivityForResult(intent2, EDIT_ASSESSMENT_REQUEST_CODE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_assessment_menu, menu);
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
            case R.id.action_delete_assessment:
                deleteAssessment();
                return true;
            case R.id.action_notes:
                Intent intent = new Intent(ActivityViewAssessment.this, ActivityListNote.class);
                Uri uri = Uri.parse(URIProvider.ASSESSMENT_URI + "/" + assessmentId);
                intent.putExtra(URIProvider.ASSESSMENT_CONTENT_TYPE, uri);
                startActivityForResult(intent, NOTE_LIST_REQUEST_CODE);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void loadAssessmentData() {
        if (assessmentUri == null) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            assessmentId = Integer.parseInt(assessmentUri.getLastPathSegment());
            assessment = DataConverter.getAssessment(this, assessmentId);

            if (assessment != null){
            tvAssessmentViewTitle.setText(assessment.title);
            tvAssessmentViewCode.setText(assessment.assessmentCode);
            tvAssessmentViewDescription.setText(assessment.description);
            tvAssessmentViewDueDate.setText(assessment.dueDate);
            tvAssessmentViewAssessmentType.setText(assessment.assessmentType);
            tvAssessmentViewNotification.setText((Boolean.toString(assessment.assessmentNotification)));

        }
        }
    }

    public void openNotesListActivity(View view) {
        Intent intent = new Intent(ActivityViewAssessment.this, ActivityListNote.class);
        Uri uri = Uri.parse(URIProvider.ASSESSMENT_URI + "/" + assessmentId);
        intent.putExtra(URIProvider.ASSESSMENT_CONTENT_TYPE, uri);
        startActivityForResult(intent, NOTE_LIST_REQUEST_CODE);
    }


    private void deleteAssessment() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    // Confirm that user wishes to proceed
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            DataConverter.deleteAssessment(ActivityViewAssessment.this, assessmentId);
                            // Notify that delete was completed
                            Toast.makeText(ActivityViewAssessment.this,
                                    "Assessment was deleted successfully",
                                    Toast.LENGTH_SHORT).show();

                            setResult(RESULT_OK);
                            finish();
                        }
                        else {
                            Toast.makeText(ActivityViewAssessment.this,
                                    "If you are reading this, something went wrong",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("WARNING!!! Are you sure you want to delete this Assessment? \n" +
                "Doing so will delete all Notes assigned to it.");
        builder.setPositiveButton(getString(android.R.string.yes), dialogClickListener);
        builder.setNegativeButton(getString(android.R.string.no), dialogClickListener);
        builder.show();

    }

    public void goHome(View view) {
        Intent intent99 = new Intent (this, MainActivity.class);
        this.startActivity(intent99);
    }
}
