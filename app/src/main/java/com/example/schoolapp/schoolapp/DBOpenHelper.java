package com.example.schoolapp.schoolapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shawn T Fox
 * For WGU - Mobile Application Development
 *  Course C196
 *  Student ID#000545644
 */

public class DBOpenHelper  extends SQLiteOpenHelper {

    //Constants for db name and version
    private static final String DATABASE_NAME = "wgu_degreeplan.db";
    private static final int DATABASE_VERSION = 4;

    //Constants for identifying tables and columns

    // Terms Table
    public static final String TABLE_TERMS = "terms";
    public static final String TERM_COLUMN_TERM_ID = "_id";
    public static final String TERM_COLUMN_TITLE = "termTitle";
    public static final String TERM_COLUMN_START = "termStartDate";
    public static final String TERM_COLUMN_END = "termEndDate";
    public static final String TERM_COLUMN_CREATED = "createdOn";

    // Courses Table
    public static final String TABLE_COURSES = "courses";
    public static final String COURSE_COLUMN_TERM_ID = "termId";
    public static final String COURSE_COLUMN_COURSE_ID = "_id";
    public static final String COURSE_COLUMN_TITLE = "courseTitle";
    public static final String COURSE_COLUMN_START = "courseStartDate";
    public static final String COURSE_COLUMN_END = "courseEndDate";
    public static final String COURSE_COLUMN_DESCRIPTION = "courseDescription";
    public static final String COURSE_COLUMN_STATUS = "status";
    public static final String COURSE_COLUMN_CREATED = "createdOn";
    public static final String COURSE_COLUMN_NOTIFICATION = "courseNotification";

    // Assessments Table
    public static final String TABLE_ASSESSMENTS = "assessments";
    public static final String ASSESSMENT_COLUMN_COURSE_ID = "courseId";
    public static final String ASSESSMENT_COLUMN_ASSESSMENT_ID = "_id";
    public static final String ASSESSMENT_COLUMN_CODE = "assessmentCode";
    public static final String ASSESSMENT_COLUMN_TITLE = "assessmentTitle";
    public static final String ASSESSMENT_COLUMN_DESCRIPTION = "assessmentDescription";
    public static final String ASSESSMENT_COLUMN_ASSESSMENT_TYPE = "assessmentType";
    public static final String ASSESSMENT_COLUMN_DUE_DATE = "assessmentDueDate";
    public static final String ASSESSMENT_COLUMN_CREATED = "createdOn";
    public static final String ASSESSMENT_COLUMN_NOTIFICATION = "assessmentNotification";

    // Mentors Table
    public static final String TABLE_MENTORS = "mentors";
    public static final String MENTOR_COLUMN_COURSE_ID = "courseId";
    public static final String MENTOR_COLUMN_MENTOR_ID = "_id";
    public static final String MENTOR_COLUMN_MENTOR_NAME = "mentorName";
    public static final String MENTOR_COLUMN_MENTOR_PHONE = "mentorPhone";
    public static final String MENTOR_COLUMN_MENTOR_EMAIL = "mentorEmail";
    public static final String MENTOR_COLUMN_CREATED = "createdOn";

    // Notes Table
    public static final String TABLE_NOTES = "notes";
    public static final String NOTE_COLUMN_COURSE_ID = "courseId";
    public static final String NOTE_COLUMN_ASSESSMENT_ID = "assessmentId";
    public static final String NOTE_COLUMN_NOTES_ID = "_id";
    public static final String NOTE_COLUMN_NOTE_TEXT = "noteText";
    public static final String NOTE_COLUMN_CREATED = "createdOn";



    public static final String[] TERM_COLUMNS = {TERM_COLUMN_TERM_ID, TERM_COLUMN_TITLE,
            TERM_COLUMN_START, TERM_COLUMN_END, TERM_COLUMN_CREATED};

    public static final String[] COURSE_COLUMNS = {COURSE_COLUMN_TERM_ID, COURSE_COLUMN_COURSE_ID,
            COURSE_COLUMN_TITLE, COURSE_COLUMN_START, COURSE_COLUMN_END, COURSE_COLUMN_DESCRIPTION,
            COURSE_COLUMN_STATUS, COURSE_COLUMN_CREATED, COURSE_COLUMN_NOTIFICATION};

    public static final String[] ASSESSMENT_COLUMNS = {ASSESSMENT_COLUMN_COURSE_ID,
            ASSESSMENT_COLUMN_ASSESSMENT_ID, ASSESSMENT_COLUMN_CODE, ASSESSMENT_COLUMN_TITLE,
            ASSESSMENT_COLUMN_DESCRIPTION, ASSESSMENT_COLUMN_ASSESSMENT_TYPE,
            ASSESSMENT_COLUMN_DUE_DATE, ASSESSMENT_COLUMN_CREATED, ASSESSMENT_COLUMN_NOTIFICATION};

    public static final String[] MENTOR_COLUMNS = {MENTOR_COLUMN_COURSE_ID, MENTOR_COLUMN_MENTOR_ID,
            MENTOR_COLUMN_MENTOR_NAME, MENTOR_COLUMN_MENTOR_PHONE, MENTOR_COLUMN_MENTOR_EMAIL,
            MENTOR_COLUMN_CREATED};

    public static final String[] NOTE_COLUMNS = {NOTE_COLUMN_COURSE_ID, NOTE_COLUMN_ASSESSMENT_ID,
            NOTE_COLUMN_NOTES_ID, NOTE_COLUMN_NOTE_TEXT, NOTE_COLUMN_CREATED};




    public static final String NOTE_TABLE_CREATE =
            "CREATE TABLE " + TABLE_NOTES + " (" +
                    NOTE_COLUMN_NOTES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NOTE_COLUMN_COURSE_ID + " INTEGER, " +
                    NOTE_COLUMN_ASSESSMENT_ID + " INTEGER, " +
                    NOTE_COLUMN_NOTE_TEXT + " TEXT, " +
                    NOTE_COLUMN_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + NOTE_COLUMN_COURSE_ID + ") REFERENCES " + TABLE_COURSES +
                    "(" + COURSE_COLUMN_COURSE_ID + ")" +
                    "FOREIGN KEY(" + NOTE_COLUMN_ASSESSMENT_ID + ") REFERENCES " + TABLE_ASSESSMENTS
                    + "(" + ASSESSMENT_COLUMN_ASSESSMENT_ID + ")" + ")";

    public static final String MENTOR_TABLE_CREATE =
            "CREATE TABLE " + TABLE_MENTORS + " (" +
                    MENTOR_COLUMN_MENTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MENTOR_COLUMN_COURSE_ID + " INTEGER, " +
                    MENTOR_COLUMN_MENTOR_NAME + " TEXT, " +
                    MENTOR_COLUMN_MENTOR_PHONE + " TEXT, " +
                    MENTOR_COLUMN_MENTOR_EMAIL + " TEXT, " +
                    MENTOR_COLUMN_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + MENTOR_COLUMN_COURSE_ID + ") REFERENCES " + TABLE_COURSES +
                    "(" + COURSE_COLUMN_COURSE_ID + ")" + ")";


    public static final String ASSESSMENT_TABLE_CREATE =
            "CREATE TABLE " + TABLE_ASSESSMENTS + " (" +
                    ASSESSMENT_COLUMN_ASSESSMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ASSESSMENT_COLUMN_COURSE_ID + " INTEGER, " +
                    ASSESSMENT_COLUMN_CODE + " TEXT, " +
                    ASSESSMENT_COLUMN_TITLE + " TEXT, " +
                    ASSESSMENT_COLUMN_DESCRIPTION + " TEXT, " +
                    ASSESSMENT_COLUMN_ASSESSMENT_TYPE + " TEXT, " +
                    ASSESSMENT_COLUMN_DUE_DATE + " TEXT, " +
                    ASSESSMENT_COLUMN_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    ASSESSMENT_COLUMN_NOTIFICATION + " INTEGER, " +
                    "FOREIGN KEY(" + ASSESSMENT_COLUMN_COURSE_ID + ") REFERENCES " + TABLE_COURSES +
                    "(" + COURSE_COLUMN_COURSE_ID + ")" + ")";


    public static final String COURSE_TABLE_CREATE =
            "CREATE TABLE " + TABLE_COURSES + " (" +
                    COURSE_COLUMN_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSE_COLUMN_TERM_ID + " INTEGER, " +
                    COURSE_COLUMN_TITLE + " TEXT, " +
                    COURSE_COLUMN_START + " DATE, " +
                    COURSE_COLUMN_END + " DATE, " +
                    COURSE_COLUMN_DESCRIPTION + " TEXT, " +
                    COURSE_COLUMN_STATUS + " TEXT, " +
                    COURSE_COLUMN_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    COURSE_COLUMN_NOTIFICATION + " INTEGER, " +
                    "FOREIGN KEY(" + COURSE_COLUMN_TERM_ID + ") REFERENCES " + TABLE_TERMS + "(" +
                    TERM_COLUMN_TERM_ID + ")" + ")";

    public static final String TERM_TABLE_CREATE =
            "CREATE TABLE " + TABLE_TERMS + " (" +
                    TERM_COLUMN_TERM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TERM_COLUMN_TITLE + " TEXT, " +
                    TERM_COLUMN_START + " DATE, " +
                    TERM_COLUMN_END + " DATE, " +
                    TERM_COLUMN_CREATED + " TEXT default CURRENT_TIMESTAMP" +
                    ")";


    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TERM_TABLE_CREATE);
        db.execSQL(COURSE_TABLE_CREATE);
        db.execSQL(ASSESSMENT_TABLE_CREATE);
        db.execSQL(MENTOR_TABLE_CREATE);
        db.execSQL(NOTE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

}
