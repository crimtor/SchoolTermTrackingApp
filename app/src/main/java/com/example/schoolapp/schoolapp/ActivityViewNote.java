package com.example.schoolapp.schoolapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.example.schoolapp.schoolapp.URIProvider.ASSESSMENT_CONTENT_TYPE;
import static com.example.schoolapp.schoolapp.URIProvider.COURSE_CONTENT_TYPE;

/**
 * Created by Shawn T Fox
 * For WGU - Mobile Application Development
 *  Course C196
 *  Student ID#000545644
 */

public class ActivityViewNote extends AppCompatActivity {

    private static int EDIT_NOTE_REQUEST_CODE = 5003;
    private Uri noteUri;
    private ObjectNote note;
    private ObjectCourse course;
    private ObjectAssessment assessment;

    private TextView tvNoteViewText;

    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_action_school);
        setSupportActionBar(toolbar);

        tvNoteViewText = findViewById(R.id.tvNoteViewText);

        Intent intent = getIntent();
        noteUri = intent.getParcelableExtra(URIProvider.NOTE_CONTENT_TYPE);

        loadNoteData();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareSubject = "No Subject";
                if (course != null) {
                    shareSubject = "This Notes is for " + course.courseTitle + ":";
                }
                if (assessment != null){
                    shareSubject = "This Notes is for " + assessment.title + ":";
                }
                String shareBody = note.noteText;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_note_menu, menu);
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
            case R.id.action_delete_note:
                deleteNote();
                return true;
            case R.id.action_edit_note:
                Intent intent = new Intent(ActivityViewNote.this, ActivityEditNote.class);
                intent.putExtra(URIProvider.NOTE_CONTENT_TYPE, noteUri);
                startActivityForResult(intent, EDIT_NOTE_REQUEST_CODE);
        }

        return super.onOptionsItemSelected(item);
    }


    private void loadNoteData() {
        if (noteUri == null) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            noteId = Integer.parseInt(noteUri.getLastPathSegment());
            note = DataConverter.getNote(this, noteId);

            if (note != null) {
                if (note.courseId != 0){
                course = DataConverter.getCourse(this, note.courseId);
                }
                if (note.assessmentId != 0){
                assessment = DataConverter.getAssessment(this, note.assessmentId);
                }
                tvNoteViewText.setText(note.noteText);
            }
        }
    }

    public void saveNote(View view) {
        if (Environment.getExternalStorageState().equals("mounted")) {
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/notes");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir+"note" + note.noteId + ".txt");
            FileWriter writer = null;
            try {
                writer = new FileWriter(file, true);
                writer.append(note.noteText + "\n");
                writer.flush();
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "Saved note to TXT file", Toast.LENGTH_SHORT).show();
        }

    }
    public void readNoteFromText(View view) {
        String dave = "";
        if (Environment.getExternalStorageState().equals("mounted")) {
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/notes");
            File file = new File(dir+"note" + note.noteId + ".txt");
            try {
                FileInputStream fin = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
                String dRow = "";
                while ((dRow = reader.readLine()) != null ){
                    dave += dRow + "\n";
                }
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Toast.makeText(this, dave, Toast.LENGTH_LONG).show();
        }
    }


    private void deleteNote() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    // Confirm that user wishes to proceed
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            DataConverter.deleteNote(ActivityViewNote.this, noteId);
                            // Notify that delete was completed
                            Toast.makeText(ActivityViewNote.this,
                                    "Note was deleted successfully",
                                    Toast.LENGTH_SHORT).show();

                            setResult(RESULT_OK);
                            finish();
                        }
                        else {
                            Toast.makeText(ActivityViewNote.this,
                                    "If you are reading this, something went wrong",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("WARNING!!! Are you sure you want to delete this Note?");
        builder.setPositiveButton(getString(android.R.string.yes), dialogClickListener);
        builder.setNegativeButton(getString(android.R.string.no), dialogClickListener);
        builder.show();

    }

    public void goHome(View view) {
        Intent intent99 = new Intent (this, MainActivity.class);
        this.startActivity(intent99);
    }

}
