package com.example.schoolapp.schoolapp;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.test.mock.MockApplication;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Shawn T Fox
 * For WGU - Mobile Application Development
 *  Course C196
 *  Student ID#000545644
 */

public class MainActivity extends AppCompatActivity {

private  static  final  int REQUEST_EXTERNAL_STORAGE = 1;
private static String[] PERMISSIONS_STORAGE = {
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
};
private ArrayList<Integer> termIds;
private int totalCourses;
private int coursesCompleted;
private float percentage;
private int progressCompletion;
private ProgressBar progressBar;
private ArrayList<ObjectAssessment> alarmAssessments;
private ArrayList<ObjectCourse> alarmCourses;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_action_school);
        setSupportActionBar(toolbar);

        verifyStoragePermissions(MainActivity.this);
        progressBar = findViewById(R.id.pbMainActivity);
        termIds = DataConverter.getTermIds(MainActivity.this);
        if (termIds != null) {
            for (int i = 0; i < termIds.size(); i++) {
                totalCourses += (DataConverter.getCourseCount(MainActivity.this, termIds.get(i)));
                coursesCompleted += (DataConverter.getCourseCompletedCount(MainActivity.this, termIds.get(i)));
            }
            percentage = (float) coursesCompleted / (float) totalCourses;
            progressCompletion = (int)(percentage * 100);
            progressBar.setProgress(progressCompletion);
        }
        alarmAssessments = DataConverter.getAlertAssessments(this);
        alarmCourses = DataConverter.getAlertCourses(this);
        if (alarmAssessments.size() != 0){
            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date alarmDate = null;
            for (int i = 0; i < alarmAssessments.size(); i++) {
                try {
                    alarmDate = sdf.parse(alarmAssessments.get(i).dueDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (alarmDate != null){
                    Date now = new Date();
                    if (sdf.format(alarmDate).equals(sdf.format(now))){
                        assessmentDueDateAlert(i);
                    }
                }
            }
        }
        if (alarmCourses.size() != 0){
            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date alarmStartDate = null;
            Date alarmEndDate = null;
            for (int i = 0; i < alarmCourses.size(); i++) {
                try {
                    alarmStartDate = sdf.parse(alarmCourses.get(i).courseStart);
                    alarmEndDate = sdf.parse(alarmCourses.get(i).courseEnd);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (alarmStartDate != null){
                    Date now = new Date();
                    if (sdf.format(alarmStartDate).equals(sdf.format(now))){
                        courseStartAlert(i);
                    }
                }
                if (alarmEndDate != null){
                    Date now = new Date();
                    if (sdf.format(alarmEndDate).equals(sdf.format(now))){
                        courseEndAlert(i);
                    }
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            case R.id.action_terms:
                Intent intent = new Intent (this, ActivityListTerm.class);
                this.startActivity(intent);
                return true;

            case R.id.action_create_data:
                makeSampleData();
                recreate();
                return true;

            case R.id.action_delete_all_data:
                deleteData();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean deleteData() {
            DialogInterface.OnClickListener dialogClickListener =
                    new DialogInterface.OnClickListener() {
                        @Override
                        // Confirm that user wishes to proceed
                        public void onClick(DialogInterface dialog, int button) {
                            if (button == DialogInterface.BUTTON_POSITIVE) {

                                // Doing an easy delete
                                getContentResolver().delete(URIProvider.TERM_URI, null, null);
                                getContentResolver().delete(URIProvider.COURSE_URI, null, null);
                                getContentResolver().delete(URIProvider.ASSESSMENT_URI, null, null);
                                getContentResolver().delete(URIProvider.MENTOR_URI, null, null);
                                getContentResolver().delete(URIProvider.NOTE_URI, null, null);

                                Toast.makeText(MainActivity.this,
                                        "Everything has been deleted.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("WARNING! ! ! ! Do you really want to delete Everything?")
                    .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                    .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                    .show();

            return true;
    }

    public void openListOfTerms(View view) {
        Intent intent = new Intent (this, ActivityListTerm.class);
        this.startActivity(intent);
    }

    public void openListOfMentors(View view) {
        Intent intent3 = new Intent (this, ActivityListAllMentors.class);
        this.startActivity(intent3);
    }
    public static void verifyStoragePermissions(Activity activity){
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    private void courseStartAlert(Integer i) {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    // Confirm that user wishes to proceed
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {

                            // Notify that delete was completed
                            Toast.makeText(MainActivity.this,
                                    "Hurray, Your class starts today.",
                                    Toast.LENGTH_SHORT).show();

                            setResult(RESULT_OK);
                        }
                        else {
                            Toast.makeText(MainActivity.this,
                                    "If you are reading this, something went wrong",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("ALERT!!! Your Class " + alarmCourses.get(i).courseTitle + " is starting" +
                " today. Get on it!");
        builder.setPositiveButton(getString(android.R.string.ok), dialogClickListener);
        builder.show();

    }
    private void courseEndAlert(Integer i) {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    // Confirm that user wishes to proceed
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {

                            // Notify that delete was completed
                            Toast.makeText(MainActivity.this,
                                    "Hurry, Your class is supposed to end today.",
                                    Toast.LENGTH_SHORT).show();

                            setResult(RESULT_OK);
                        }
                        else {
                            Toast.makeText(MainActivity.this,
                                    "If you are reading this, something went wrong",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("ALERT!!! Your Class " + alarmCourses.get(i).courseTitle + " was supposed to" +
                " be completed today. Get it done stat!");
        builder.setPositiveButton(getString(android.R.string.ok), dialogClickListener);
        builder.show();

    }
    private void assessmentDueDateAlert(Integer i) {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    // Confirm that user wishes to proceed
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {

                            // Notify that delete was completed
                            Toast.makeText(MainActivity.this,
                                    "Hurry, Your assessment is due today.",
                                    Toast.LENGTH_SHORT).show();

                            setResult(RESULT_OK);
                        }
                        else {
                            Toast.makeText(MainActivity.this,
                                    "If you are reading this, something went wrong",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("ALERT!!! Your Assessment " + alarmAssessments.get(i).title + " is due" +
                " today. Go get on it, now!");
        builder.setPositiveButton(getString(android.R.string.ok), dialogClickListener);
        builder.show();

    }

    private void makeSampleData() {
        DataConverter.insertTerm(MainActivity.this, "1st Term", "03/23/2018",
                "09/23/2019");
        DataConverter.insertTerm(MainActivity.this, "2nd Term", "09/23/2018",
                "03/23/2019");
        DataConverter.insertTerm(MainActivity.this, "3rd Term", "03/23/2019",
                "09/23/2019");
        DataConverter.insertTerm(MainActivity.this, "4th Term", "09/23/2019",
                "09/23/2020");
        ArrayList<Integer>termIds = DataConverter.getTermIds(MainActivity.this);
        if (termIds != null){
            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date alarmStartDate = new Date();
            DataConverter.insertCourse(MainActivity.this, termIds.get(0),
                    "Course that will Trigger Alarm", sdf.format(alarmStartDate),
                    "06/06/2018", "This is the alarm class", "In Progress",
                    1);
            for (int i =0; i<termIds.size(); i++){
                DataConverter.insertCourse(MainActivity.this, termIds.get(i), "Course in Term " +
                        (i+1), "03/24/2018", "05/24/2018", "Course type stuff",
                        "In Progress", 1);
            }
        }
        ArrayList<Integer>courseIds = DataConverter.getCourseIds(MainActivity.this);
        if (courseIds != null){
            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date alarmStartDate = new Date();
            DataConverter.insertAssessment(MainActivity.this, courseIds.get(0),
                    "Assessment that will Trigger Alarm", "ALRM101", "Performance",
                    "Making Alarms and Having Fun.", sdf.format(alarmStartDate), 1);
            for (int i = 0; i<courseIds.size(); i++){
                DataConverter.insertAssessment(MainActivity.this, courseIds.get(i),
                        "Assessment for Course in Term " + (i), "ABC"+i+(i*5),
                        "Objective", "Assessment type Stuff", "03/25/2018",
                        1);
                DataConverter.insertMentor(MainActivity.this, courseIds.get(i),
                        "Mentor for Course in Term " + (i), "555-555-5555",
                        "Mentor.Name@email.com");
                DataConverter.insertNote(MainActivity.this, courseIds.get(i), 0,
                        "This is a note that is inside of a Course.");
            }
        }
        ArrayList<Integer>assessmentIds = DataConverter.getAssessmentIds(MainActivity.this);
        if (assessmentIds != null){
            for (int i=0; i<assessmentIds.size(); i++){
                DataConverter.insertNote(MainActivity.this, 0, assessmentIds.get(i),
                        "This is a note that is inside of an Assessment");
            }
        }
    }

}
