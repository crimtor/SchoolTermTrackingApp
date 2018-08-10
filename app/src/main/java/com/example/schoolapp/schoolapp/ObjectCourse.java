package com.example.schoolapp.schoolapp;

import android.content.ContentValues;
import android.content.Context;

/**
 * Created by Shawn T Fox
 * For WGU - Mobile Application Development
 *  Course C196
 *  Student ID#000545644
 */

public class ObjectCourse {

    public int termId;
    public int courseId;
    public String courseTitle;
    public String description;
    public String courseStart;
    public String courseEnd;
    public boolean courseNotification;
    public String status;

    public ObjectCourse(int id){
        courseId = id;
    }
    public ObjectCourse() {}


    public void updateCourse(Context context) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_COLUMN_TERM_ID, termId);
        values.put(DBOpenHelper.COURSE_COLUMN_TITLE, courseTitle);
        values.put(DBOpenHelper.COURSE_COLUMN_DESCRIPTION, description);
        values.put(DBOpenHelper.COURSE_COLUMN_START, courseStart);
        values.put(DBOpenHelper.COURSE_COLUMN_END, courseEnd);
        values.put(DBOpenHelper.COURSE_COLUMN_NOTIFICATION, (courseNotification) ? 1 : 0);
        values.put(DBOpenHelper.COURSE_COLUMN_STATUS, status);

        context.getContentResolver().update(URIProvider.COURSE_URI, values,
                DBOpenHelper.COURSE_COLUMN_COURSE_ID + "=" + courseId, null);
    }

}
