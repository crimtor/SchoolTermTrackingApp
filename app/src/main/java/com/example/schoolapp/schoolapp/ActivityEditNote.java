package com.example.schoolapp.schoolapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Shawn T Fox
 * For WGU - Mobile Application Development
 *  Course C196
 *  Student ID#000545644
 */

public class ActivityEditNote extends AppCompatActivity {

    private EditText noteTextField;

    private ObjectNote note;
    private String insertOrEditNote;
    private Uri assessmentUri;
    private Uri noteUri;
    private Uri courseUri;
    private int noteId;
    private int courseId;
    private int assessmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_action_school);
        setSupportActionBar(toolbar);

        noteTextField = findViewById(R.id.etNoteEditText);

        Intent intent = getIntent();
        courseUri = intent.getParcelableExtra(URIProvider.COURSE_CONTENT_TYPE);
        assessmentUri = intent.getParcelableExtra(URIProvider.ASSESSMENT_CONTENT_TYPE);
        noteUri = intent.getParcelableExtra(URIProvider.NOTE_CONTENT_TYPE);

        if (noteUri == null) {
            insertOrEditNote = Intent.ACTION_INSERT;
            setTitle("Add New Note");
        } else {
            insertOrEditNote = Intent.ACTION_EDIT;
            setTitle("Edit Note");
            noteId = Integer.parseInt(noteUri.getLastPathSegment());
            note = DataConverter.getNote(this, noteId);
            fillTextViews(note);
        }

        if (courseUri == null) {
          courseId = 0;
        } else {
            courseId = Integer.parseInt(courseUri.getLastPathSegment());
        }
        if (assessmentUri == null) {
            assessmentId = 0;
        } else {
            assessmentId = Integer.parseInt(assessmentUri.getLastPathSegment());
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void saveNote(View view) {
        if (insertOrEditNote.equals(Intent.ACTION_INSERT)) {
            note = new ObjectNote();
            getNoteFromTextViews();

            DataConverter.insertNote(ActivityEditNote.this,
                    courseId,
                    assessmentId,
                    note.noteText
            );

            // Notify that insert was completed
            Toast.makeText(this,
                    "Note was added successfully",
                    Toast.LENGTH_LONG).show();

            setResult(RESULT_OK);

        } else if (insertOrEditNote.equals(Intent.ACTION_EDIT)) {
            getNoteFromTextViews();

            DataConverter.editNote(ActivityEditNote.this,
                    noteId,
                    note.courseId,
                    note.assessmentId,
                    note.noteText
            );

            // Notify that update was completed
            Toast.makeText(this,
                    "Note Updated Successfully",
                    Toast.LENGTH_SHORT).show();

            setResult(RESULT_OK);
        }

        finish();
    }
    private void fillTextViews(ObjectNote oldNote) {
        noteTextField.setText(note.noteText);
    }

    private void getNoteFromTextViews() {
        note.noteText = noteTextField.getText().toString().trim();
    }
    public void goHome(View view) {
        Intent intent99 = new Intent (this, MainActivity.class);
        this.startActivity(intent99);
    }


}
