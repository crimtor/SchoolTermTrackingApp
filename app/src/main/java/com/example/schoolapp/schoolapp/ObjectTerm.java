package com.example.schoolapp.schoolapp;

import android.content.ContentValues;
import android.content.Context;

/**
 * Created by Shawn T Fox
 * For WGU - Mobile Application Development
 *  Course C196
 *  Student ID#000545644
 */

public class ObjectTerm {

    public int termId;
    public String termTitle;
    public String termStart;
    public String termEnd;

    ObjectTerm(int id){
        termId = id;
    }
    ObjectTerm(){}

    public void updateTerm(Context context) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_COLUMN_TITLE, termTitle);
        values.put(DBOpenHelper.TERM_COLUMN_START, termStart);
        values.put(DBOpenHelper.TERM_COLUMN_END, termEnd);
        context.getContentResolver().update(URIProvider.TERM_URI, values,
                DBOpenHelper.TERM_COLUMN_TERM_ID + "=" + termId, null);
    }



}
