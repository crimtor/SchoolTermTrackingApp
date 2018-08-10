package com.example.schoolapp.schoolapp;

import android.content.ContentValues;
import android.content.Context;

/**
 * Created by Shawn T Fox
 * For WGU - Mobile Application Development
 *  Course C196
 *  Student ID#000545644
 */

public class ObjectNote {


    public int courseId;
    public int assessmentId;
    public int noteId;
    public String noteText;

    public ObjectNote(int id) {
        noteId = id;
    }
    ObjectNote(){}

    public void updateNote(Context context) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_COLUMN_COURSE_ID, courseId);
        values.put(DBOpenHelper.NOTE_COLUMN_ASSESSMENT_ID, assessmentId);
        values.put(DBOpenHelper.NOTE_COLUMN_NOTE_TEXT, noteText);
        values.put(DBOpenHelper.NOTE_COLUMN_COURSE_ID, courseId);

        context.getContentResolver().update(URIProvider.NOTE_URI, values,
                DBOpenHelper.NOTE_COLUMN_NOTES_ID + "=" + noteId, null);
    }
}
