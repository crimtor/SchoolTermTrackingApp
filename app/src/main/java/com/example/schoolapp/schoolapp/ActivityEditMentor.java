package com.example.schoolapp.schoolapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by Shawn T Fox
 * For WGU - Mobile Application Development
 *  Course C196
 *  Student ID#000545644
 */

public class ActivityEditMentor extends AppCompatActivity {

    private EditText mentorNameField;
    private EditText mentorPhoneField;
    private EditText mentorEmailField;
    private ObjectMentor mentor;
    private String insertOrEditMentor;
    private Uri mentorUri;
    private Uri courseUri;
    private int mentorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mentor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_action_school);
        setSupportActionBar(toolbar);

        mentorNameField = findViewById(R.id.etMentorEditName);
        mentorPhoneField = findViewById(R.id.etMentorEditPhone);
        mentorEmailField = findViewById(R.id.etMentorEditEmail);

        Intent intent = getIntent();
        courseUri = intent.getParcelableExtra(URIProvider.COURSE_CONTENT_TYPE);
        mentorUri = intent.getParcelableExtra(URIProvider.MENTOR_CONTENT_TYPE);

        if (mentorUri == null) {
            insertOrEditMentor = Intent.ACTION_INSERT;
            setTitle("Add New Mentor");
        } else {
            insertOrEditMentor = Intent.ACTION_EDIT;
            setTitle("Edit Mentor");
            mentorId = Integer.parseInt(mentorUri.getLastPathSegment());
            mentor = DataConverter.getMentor(this, mentorId);
            fillTextViews(mentor);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void saveMentor(View view) {
        if (insertOrEditMentor.equals(Intent.ACTION_INSERT)) {
            mentor = new ObjectMentor();
            getMentorFromTextViews();

            DataConverter.insertMentor(this,
                    Integer.parseInt(courseUri.getLastPathSegment()),
                    mentor.mentorName,
                    mentor.mentorPhone,
                    mentor.mentorEmail
                    );

            // Notify that insert was completed
            Toast.makeText(this,
                    "Mentor was added successfully",
                    Toast.LENGTH_LONG).show();

            setResult(RESULT_OK);

        } else if (insertOrEditMentor.equals(Intent.ACTION_EDIT)) {
            getMentorFromTextViews();

                    DataConverter.editMentor(this,
                            mentorId,
                            mentor.courseId,
                            mentor.mentorName,
                            mentor.mentorPhone,
                            mentor.mentorEmail
                    );

            // Notify that update was completed
            Toast.makeText(this,
                    "Mentor was updated successfully",
                    Toast.LENGTH_SHORT).show();

            setResult(RESULT_OK);
        }

        finish();
    }
    private void fillTextViews(ObjectMentor oldMentor) {
        mentorNameField.setText(mentor.mentorName);
        mentorPhoneField.setText(mentor.mentorPhone);
        mentorEmailField.setText(mentor.mentorEmail);
    }

    private void getMentorFromTextViews() {
        mentor.mentorName = mentorNameField.getText().toString().trim();
        mentor.mentorPhone = mentorPhoneField.getText().toString().trim();
        mentor.mentorEmail = mentorEmailField.getText().toString().trim();
    }
    public void goHome(View view) {
        Intent intent99 = new Intent (this, MainActivity.class);
        this.startActivity(intent99);
    }


}
