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

public class ActivityViewMentor extends AppCompatActivity {

    private static final int EDIT_MENTOR_REQUEST_CODE = 4003;

    private Uri mentorUri;
    private ObjectMentor mentor;

    private TextView tvMentorViewName;
    private TextView tvMentorViewPhone;
    private TextView tvMentorViewEmail;

    private int mentorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mentor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_action_school);
        setSupportActionBar(toolbar);

        tvMentorViewName = findViewById(R.id.tvMentorViewName);
        tvMentorViewPhone = findViewById(R.id.tvMentorViewPhone);
        tvMentorViewEmail = findViewById(R.id.tvMentorViewEmail);

        Intent intent = getIntent();
        mentorUri = intent.getParcelableExtra(URIProvider.MENTOR_CONTENT_TYPE);

        loadMentorData();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityViewMentor.this, ActivityEditMentor.class);
                intent.putExtra(URIProvider.MENTOR_CONTENT_TYPE, mentorUri);
                startActivityForResult(intent, EDIT_MENTOR_REQUEST_CODE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_mentor_menu, menu);
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
            case R.id.action_delete_mentor:
                deleteMentor();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void loadMentorData() {
        if (mentorUri == null) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            mentorId = Integer.parseInt(mentorUri.getLastPathSegment());
            mentor = DataConverter.getMentor(this, mentorId);

            tvMentorViewName.setText(mentor.mentorName);
            tvMentorViewPhone.setText(mentor.mentorPhone);
            tvMentorViewEmail.setText(mentor.mentorEmail);
        }
    }


    private void deleteMentor() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    // Confirm that user wishes to proceed
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            DataConverter.deleteMentor(ActivityViewMentor.this, mentorId);
                            // Notify that delete was completed
                            Toast.makeText(ActivityViewMentor.this,
                                    "Mentor was deleted successfully",
                                    Toast.LENGTH_SHORT).show();

                            setResult(RESULT_OK);
                            finish();
                        }
                        else {
                            Toast.makeText(ActivityViewMentor.this,
                                    "If you are reading this, something went wrong",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("WARNING!!! Are you sure you want to delete this Mentor?");
        builder.setPositiveButton(getString(android.R.string.yes), dialogClickListener);
        builder.setNegativeButton(getString(android.R.string.no), dialogClickListener);
        builder.show();

    }

    public void goHome(View view) {
        Intent intent99 = new Intent (this, MainActivity.class);
        this.startActivity(intent99);
    }
}
