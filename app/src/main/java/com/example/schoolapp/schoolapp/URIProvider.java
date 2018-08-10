package com.example.schoolapp.schoolapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Shawn T Fox
 * For WGU - Mobile Application Development
 *  Course C196
 *  Student ID#000545644
 */
public class URIProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.schoolapp.schoolapp";
    public static final String TERM_PATH = "terms";
    public static final String COURSE_PATH = "courses";
    public static final String ASSESSMENT_PATH = "assessments";
    public static final String MENTOR_PATH = "mentors";
    public static final String NOTE_PATH = "notes";

    public static final Uri TERM_URI = Uri.parse("content://" + AUTHORITY + "/" + TERM_PATH);
    public static final Uri COURSE_URI = Uri.parse("content://" + AUTHORITY + "/" + COURSE_PATH);
    public static final Uri ASSESSMENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ASSESSMENT_PATH);
    public static final Uri MENTOR_URI = Uri.parse("content://" + AUTHORITY + "/" + MENTOR_PATH);
    public static final Uri NOTE_URI = Uri.parse("content://" + AUTHORITY + "/" + NOTE_PATH);

    // Constant to identify the requested operation
    public static final int TERMS = 1;
    public static final int TERMS_ID = 2;
    public static final int COURSES = 3;
    public static final int COURSES_ID = 4;
    public static final int ASSESSMENTS = 5;
    public static final int ASSESSMENTS_ID = 6;
    public static final int MENTORS = 7;
    public static final int MENTORS_ID = 8;
    public static final int NOTES = 9;
    public static final int NOTES_ID = 10;

    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, TERM_PATH, TERMS);
        uriMatcher.addURI(AUTHORITY, TERM_PATH + "/#", TERMS_ID);
        uriMatcher.addURI(AUTHORITY, COURSE_PATH, COURSES);
        uriMatcher.addURI(AUTHORITY, COURSE_PATH + "/#", COURSES_ID);
        uriMatcher.addURI(AUTHORITY, ASSESSMENT_PATH, ASSESSMENTS);
        uriMatcher.addURI(AUTHORITY, ASSESSMENT_PATH + "/#", ASSESSMENTS_ID);
        uriMatcher.addURI(AUTHORITY, MENTOR_PATH, MENTORS);
        uriMatcher.addURI(AUTHORITY, MENTOR_PATH + "/#", MENTORS_ID);
        uriMatcher.addURI(AUTHORITY, NOTE_PATH, NOTES);
        uriMatcher.addURI(AUTHORITY, NOTE_PATH + "/#", NOTES_ID);
    }

    public static final String TERM_CONTENT_TYPE = "term";
    public static final String COURSE_CONTENT_TYPE = "course";
    public static final String ASSESSMENT_CONTENT_TYPE = "assessment";
    public static final String MENTOR_CONTENT_TYPE = "mentor";
    public static final String NOTE_CONTENT_TYPE = "note";

    private SQLiteDatabase db;
    private String currentTable;

    @Override
    public boolean onCreate() {
        DBOpenHelper dbOpenHelper = new DBOpenHelper(getContext());
        db = dbOpenHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case TERMS:
                return db.query(DBOpenHelper.TABLE_TERMS, DBOpenHelper.TERM_COLUMNS, selection, null,
                        null, null, DBOpenHelper.TERM_COLUMN_TERM_ID + " ASC");
            case TERMS_ID:
                return db.query(DBOpenHelper.TABLE_TERMS, DBOpenHelper.TERM_COLUMNS,
                        DBOpenHelper.TERM_COLUMN_TERM_ID + "=" + uri.getLastPathSegment(), null,
                        null, null, DBOpenHelper.TERM_COLUMN_TERM_ID + " ASC");
            case COURSES:
                return db.query(DBOpenHelper.TABLE_COURSES, DBOpenHelper.COURSE_COLUMNS, selection,
                        null, null, null, DBOpenHelper.COURSE_COLUMN_COURSE_ID + " ASC");
            case COURSES_ID:
                return db.query(DBOpenHelper.TABLE_COURSES, DBOpenHelper.COURSE_COLUMNS,
                        DBOpenHelper.COURSE_COLUMN_COURSE_ID + "=" + uri.getLastPathSegment(), null,
                        null, null, DBOpenHelper.COURSE_COLUMN_COURSE_ID + " ASC" );
            case ASSESSMENTS:
                return db.query(DBOpenHelper.TABLE_ASSESSMENTS, DBOpenHelper.ASSESSMENT_COLUMNS,
                        selection, null, null, null, DBOpenHelper.ASSESSMENT_COLUMN_ASSESSMENT_ID +
                                " ASC");
            case ASSESSMENTS_ID:
                return db.query(DBOpenHelper.TABLE_ASSESSMENTS, DBOpenHelper.ASSESSMENT_COLUMNS,
                        DBOpenHelper.ASSESSMENT_COLUMN_ASSESSMENT_ID + "=" + uri.getLastPathSegment(),
                        null, null, null, DBOpenHelper.ASSESSMENT_COLUMN_ASSESSMENT_ID +
                                " ASC");
            case MENTORS:
                return db.query(DBOpenHelper.TABLE_MENTORS, DBOpenHelper.MENTOR_COLUMNS, selection,
                        null, null, null, DBOpenHelper.MENTOR_COLUMN_MENTOR_ID + " ASC");
            case MENTORS_ID:
                return db.query(DBOpenHelper.TABLE_MENTORS, DBOpenHelper.MENTOR_COLUMNS,
                        DBOpenHelper.MENTOR_COLUMN_MENTOR_ID + "=" + uri.getLastPathSegment(),
                        null, null, null, DBOpenHelper.MENTOR_COLUMN_MENTOR_ID + " ASC");
            case NOTES:
                return db.query(DBOpenHelper.TABLE_NOTES, DBOpenHelper.NOTE_COLUMNS, selection,
                        null, null, null, DBOpenHelper.NOTE_COLUMN_NOTES_ID + " ASC");
            case NOTES_ID:
                return db.query(DBOpenHelper.TABLE_NOTES, DBOpenHelper.NOTE_COLUMNS,
                        DBOpenHelper.NOTE_COLUMN_NOTES_ID + "=" + uri.getLastPathSegment(),
                        null, null, null, DBOpenHelper.NOTE_COLUMN_NOTES_ID + " ASC");
            default:
                throw new IllegalArgumentException(
                        "Unsupported URI: " + uri);
        }
    }




    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = 0;
        switch (uriMatcher.match(uri)) {
            case TERMS:
                id = db.insert(DBOpenHelper.TABLE_TERMS, null, values);
                Log.d("DataProvider", "Inserted _Term: " + id);
                return Uri.parse(TERM_PATH + "/" + id);
            case COURSES:
                id = db.insert(DBOpenHelper.TABLE_COURSES, null, values);
                Log.d("DataProvider", "Inserted _Term: " + id);
                return Uri.parse(COURSE_PATH + "/" + id);
            case ASSESSMENTS:
                id = db.insert(DBOpenHelper.TABLE_ASSESSMENTS, null, values);
                Log.d("DataProvider", "Inserted _Assessment: " + id);
                return Uri.parse(ASSESSMENT_PATH + "/" + id);
            case MENTORS:
                id = db.insert(DBOpenHelper.TABLE_MENTORS, null, values);
                Log.d("DataProvider", "Inserted _AssessmentNote: " + id);
                return Uri.parse(MENTOR_PATH + "/" + id);
            case NOTES:
                id = db.insert(DBOpenHelper.TABLE_NOTES, null, values);
                Log.d("DataProvider", "Inserted _CourseNote: " + id);
                return Uri.parse(NOTE_PATH + "/" + id);

            default:
                throw new IllegalArgumentException(
                        "Unsupported URI: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case TERMS:
                return db.delete(DBOpenHelper.TABLE_TERMS, selection, selectionArgs);
            case COURSES:
                return db.delete(DBOpenHelper.TABLE_COURSES, selection, selectionArgs);
            case ASSESSMENTS:
                return db.delete(DBOpenHelper.TABLE_ASSESSMENTS, selection, selectionArgs);
            case MENTORS:
                return db.delete(DBOpenHelper.TABLE_MENTORS, selection, selectionArgs);
            case NOTES:
                return db.delete(DBOpenHelper.TABLE_NOTES, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(
                        "Unsupported URI: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case TERMS:
                return db.update(DBOpenHelper.TABLE_TERMS, values, selection, selectionArgs);
            case COURSES:
                return db.update(DBOpenHelper.TABLE_COURSES, values, selection, selectionArgs);
            case ASSESSMENTS:
                return db.update(DBOpenHelper.TABLE_ASSESSMENTS, values, selection, selectionArgs);
            case MENTORS:
                return db.update(DBOpenHelper.TABLE_MENTORS, values, selection, selectionArgs);
            case NOTES:
                return db.update(DBOpenHelper.TABLE_NOTES, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(
                        "Unsupported URI: " + uri);
        }
    }
}
