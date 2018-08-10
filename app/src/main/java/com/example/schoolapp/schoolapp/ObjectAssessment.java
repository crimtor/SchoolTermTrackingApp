package com.example.schoolapp.schoolapp;

import android.content.ContentValues;
import android.content.Context;

/**
 * Created by Shawn T Fox
 * For WGU - Mobile Application Development
 *  Course C196
 *  Student ID#000545644
 */

public class ObjectAssessment {

        public int courseId;
        public int assessmentId;
        public String assessmentCode;
        public String title;
        public String description;
        public String assessmentType;
        public String dueDate;
        public boolean assessmentNotification;

        public ObjectAssessment(int id) {
            assessmentId = id;
        }
        public ObjectAssessment() {}

        public void updateAssessment(Context context) {
            ContentValues values = new ContentValues();
            values.put(DBOpenHelper.ASSESSMENT_COLUMN_COURSE_ID, courseId);
            values.put(DBOpenHelper.ASSESSMENT_COLUMN_ASSESSMENT_ID, assessmentId);
            values.put(DBOpenHelper.ASSESSMENT_COLUMN_CODE, assessmentCode);
            values.put(DBOpenHelper.ASSESSMENT_COLUMN_TITLE, title);
            values.put(DBOpenHelper.ASSESSMENT_COLUMN_DESCRIPTION, description);
            values.put(DBOpenHelper.ASSESSMENT_COLUMN_ASSESSMENT_TYPE, assessmentType);
            values.put(DBOpenHelper.ASSESSMENT_COLUMN_DUE_DATE, dueDate);
            values.put(DBOpenHelper.ASSESSMENT_COLUMN_NOTIFICATION, assessmentNotification);

            context.getContentResolver().update(URIProvider.ASSESSMENT_URI, values,
                    DBOpenHelper.ASSESSMENT_COLUMN_ASSESSMENT_ID + "=" + assessmentId, null);
        }
    }

