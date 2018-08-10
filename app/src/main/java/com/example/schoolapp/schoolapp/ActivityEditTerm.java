package com.example.schoolapp.schoolapp;

import android.app.DatePickerDialog;
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

import java.util.Calendar;

/**
 * Created by Shawn T Fox
 * For WGU - Mobile Application Development
 *  Course C196
 *  Student ID#000545644
 */

public class ActivityEditTerm extends AppCompatActivity {

    private EditText termTitleField;
    private TextView startDateField;
    private TextView endDateField;
    private DatePickerDialog.OnDateSetListener startDateListener;
    private DatePickerDialog.OnDateSetListener endDateListener;
    private ObjectTerm term;
    private Uri termUri;
    private String insertOrEditTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_term);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_action_school);
        setSupportActionBar(toolbar);

        termTitleField = findViewById(R.id.etTermEditTitle);
        startDateField = findViewById(R.id.tvTermEditStartDate);
        endDateField = findViewById(R.id.tvTermEditEndDate);
        setupStartDate();
        setupEndDate();
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

        Intent intent = getIntent();
        termUri = intent.getParcelableExtra(URIProvider.TERM_CONTENT_TYPE);

        // no uri means we're creating a new term
        if (termUri == null) {
            insertOrEditTerm = Intent.ACTION_INSERT;
            setTitle("Make New Term");
        } else {
            insertOrEditTerm = Intent.ACTION_EDIT;
            setTitle("Edit Term");
            int termId = Integer.parseInt(termUri.getLastPathSegment());
            term = DataConverter.getTerm(this, termId);
            fillTextViews(term);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void saveTerm(View view) {
        if (insertOrEditTerm == Intent.ACTION_INSERT) {
            term = new ObjectTerm();
            getTermFromTextViews();

            DataConverter.insertTerm(this,
                    term.termTitle,
                    term.termStart,
                    term.termEnd );

            // Notify that insert was completed
            Toast.makeText(this,
                    "Term was added successfully",
                    Toast.LENGTH_LONG).show();

            setResult(RESULT_OK);

        } else if (insertOrEditTerm == Intent.ACTION_EDIT) {
            getTermFromTextViews();
            DataConverter.editTerm(this,
                    term.termId,
                    term.termTitle,
                    term.termStart,
                    term.termEnd);

            // Notify that update was completed
            Toast.makeText(this,
                    "Term was updated successfully",
                    Toast.LENGTH_SHORT).show();

            setResult(RESULT_OK);
        }

        finish();
    }

    private void fillTextViews(ObjectTerm oldTerm) {
        termTitleField.setText(oldTerm.termTitle);
        startDateField.setText(oldTerm.termStart);
        endDateField.setText(oldTerm.termEnd);
    }

    private void getTermFromTextViews() {
        term.termTitle = termTitleField.getText().toString().trim();
        term.termStart = startDateField.getText().toString().trim();
        term.termEnd = endDateField.getText().toString().trim();
    }

    private void setupStartDate() {
        startDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int year = cal.get(Calendar.YEAR);

                DatePickerDialog dpd = new DatePickerDialog(ActivityEditTerm.this,
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

                DatePickerDialog dpd = new DatePickerDialog(ActivityEditTerm.this,
                        android.R.style.Theme_DeviceDefault_Dialog, endDateListener, year, month +6, day);
                dpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dpd.show();
            }
        });
    }
    public void goHome(View view) {
        Intent intent99 = new Intent (this, MainActivity.class);
        this.startActivity(intent99);
    }

}
