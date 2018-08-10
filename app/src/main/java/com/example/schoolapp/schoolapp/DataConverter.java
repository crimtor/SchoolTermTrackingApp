package com.example.schoolapp.schoolapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shawn T Fox
 * For WGU - Mobile Application Development
 *  Course C196
 *  Student ID#000545644
 */

public class DataConverter {

    static void insertTerm(Context context, String termTitle, String termStart, String termEnd) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_COLUMN_TITLE, termTitle);
        values.put(DBOpenHelper.TERM_COLUMN_START, termStart);
        values.put(DBOpenHelper.TERM_COLUMN_END, termEnd);

        context.getContentResolver().insert(URIProvider.TERM_URI, values);
    }

    static void insertCourse(Context context, int termId, String courseTitle, String courseStart,
                                   String courseEnd, String description, String status, int notification) {

        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_COLUMN_TERM_ID, termId);
        values.put(DBOpenHelper.COURSE_COLUMN_TITLE, courseTitle);
        values.put(DBOpenHelper.COURSE_COLUMN_START, courseStart);
        values.put(DBOpenHelper.COURSE_COLUMN_END, courseEnd);
        values.put(DBOpenHelper.COURSE_COLUMN_DESCRIPTION, description);
        values.put(DBOpenHelper.COURSE_COLUMN_STATUS, status);
        values.put(DBOpenHelper.COURSE_COLUMN_NOTIFICATION, notification);

        context.getContentResolver().insert(URIProvider.COURSE_URI, values);

    }

    static void insertAssessment(Context context, int courseId,  String title, String code,
                                 String type, String description, String dueDate,
                                       int assessmentNotification) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENT_COLUMN_COURSE_ID, courseId);
        values.put(DBOpenHelper.ASSESSMENT_COLUMN_TITLE, title);
        values.put(DBOpenHelper.ASSESSMENT_COLUMN_CODE, code);
        values.put(DBOpenHelper.ASSESSMENT_COLUMN_ASSESSMENT_TYPE, type);
        values.put(DBOpenHelper.ASSESSMENT_COLUMN_DESCRIPTION, description);
        values.put(DBOpenHelper.ASSESSMENT_COLUMN_DUE_DATE, dueDate);
        values.put(DBOpenHelper.ASSESSMENT_COLUMN_NOTIFICATION, assessmentNotification);

        context.getContentResolver().insert(URIProvider.ASSESSMENT_URI, values);
    }

    static void insertMentor(Context context, int courseId, String mentorName,
                                   String mentorPhone, String mentorEmail) {

        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.MENTOR_COLUMN_COURSE_ID, courseId);
        values.put(DBOpenHelper.MENTOR_COLUMN_MENTOR_NAME, mentorName);
        values.put(DBOpenHelper.MENTOR_COLUMN_MENTOR_PHONE, mentorPhone);
        values.put(DBOpenHelper.MENTOR_COLUMN_MENTOR_EMAIL, mentorEmail);

        context.getContentResolver().insert(URIProvider.MENTOR_URI, values);
    }

    static void insertNote(Context context, int courseId, int assessmentId, String noteText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_COLUMN_COURSE_ID, courseId);
        values.put(DBOpenHelper.NOTE_COLUMN_ASSESSMENT_ID, assessmentId);
        values.put(DBOpenHelper.NOTE_COLUMN_NOTE_TEXT, noteText);

        context.getContentResolver().insert(URIProvider.NOTE_URI, values);
    }

    static ObjectTerm getTerm(Context context, int id) {
        Cursor cursor = context.getContentResolver().query(URIProvider.TERM_URI,
                DBOpenHelper.TERM_COLUMNS, DBOpenHelper.TERM_COLUMN_TERM_ID + "=" + id,
                null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            String termTitle = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_COLUMN_TITLE));
            String termStart = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_COLUMN_START));
            String termEnd = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_COLUMN_END));

            ObjectTerm t = new ObjectTerm();
            t.termId = id;
            t.termTitle = termTitle;
            t.termStart = termStart;
            t.termEnd = termEnd;

            cursor.close();
            return t;
        }
        return null;
    }

    static ObjectCourse getCourse(Context context, int id) {
        Cursor cursor = context.getContentResolver().query(URIProvider.COURSE_URI,
                DBOpenHelper.COURSE_COLUMNS, DBOpenHelper.COURSE_COLUMN_COURSE_ID + "=" + id,
                null, null);

        if (cursor != null) {
        cursor.moveToFirst();
        Integer termId = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.COURSE_COLUMN_TERM_ID));
        String courseTitle = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_COLUMN_TITLE));
        String courseStart = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_COLUMN_START));
        String courseEnd = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_COLUMN_END));
        String courseDescription =
                cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_COLUMN_DESCRIPTION));
        String status = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_COLUMN_STATUS));
        Boolean courseNotifications =
                (cursor.getInt(cursor.getColumnIndex(DBOpenHelper.COURSE_COLUMN_NOTIFICATION))==1);

        ObjectCourse c = new ObjectCourse();
        c.termId = termId;
        c.courseId = id;
        c.courseTitle = courseTitle;
        c.courseStart = courseStart;
        c.courseEnd = courseEnd;
        c.description = courseDescription;
        c.status = status;
        c.courseNotification = courseNotifications;

        cursor.close();

        return c;
        }

        return null;
    }

    static ObjectAssessment getAssessment(Context context, int id) {
        Cursor cursor = context.getContentResolver().query(URIProvider.ASSESSMENT_URI,
                DBOpenHelper.ASSESSMENT_COLUMNS, DBOpenHelper.ASSESSMENT_COLUMN_ASSESSMENT_ID +
                        "=" + id, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Integer courseId = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_COLUMN_COURSE_ID));
            String aCode = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_COLUMN_CODE));
            String aTitle = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_COLUMN_DESCRIPTION));
            String aType = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_COLUMN_ASSESSMENT_TYPE));
            String dueDate = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_COLUMN_DUE_DATE));

            ObjectAssessment a = new ObjectAssessment();
            a.assessmentId = id;
            a.courseId = courseId;
            a.assessmentCode = aCode;
            a.title = aTitle;
            a.description = description;
            a.assessmentType = aType;
            a.dueDate = dueDate;

            cursor.close();
            return a;
        }
        return  null;
    }

    static ObjectMentor getMentor(Context context, int id) {
        Cursor cursor = context.getContentResolver().query(URIProvider.MENTOR_URI,
                DBOpenHelper.MENTOR_COLUMNS, DBOpenHelper.MENTOR_COLUMN_MENTOR_ID + "=" + id,
                null, null);


        if (cursor != null) {
            cursor.moveToFirst();
            Integer courseId = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.MENTOR_COLUMN_COURSE_ID));
            String mentorName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.MENTOR_COLUMN_MENTOR_NAME));
            String mentorEmail = cursor.getString(cursor.getColumnIndex(DBOpenHelper.MENTOR_COLUMN_MENTOR_EMAIL));
            String mentorPhone = cursor.getString(cursor.getColumnIndex(DBOpenHelper.MENTOR_COLUMN_MENTOR_PHONE));

            ObjectMentor m = new ObjectMentor();
            m.courseId = courseId;
            m.mentorName = mentorName;
            m.mentorEmail = mentorEmail;
            m.mentorPhone = mentorPhone;

            cursor.close();
            return m;
        }
        return  null;
    }


    static ObjectNote getNote(Context context, int id) {
        Cursor cursor = context.getContentResolver().query(URIProvider.NOTE_URI,
                DBOpenHelper.NOTE_COLUMNS, DBOpenHelper.NOTE_COLUMN_NOTES_ID + "=" + id,
                null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Integer courseId = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.NOTE_COLUMN_COURSE_ID));
            Integer assessmentId = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.NOTE_COLUMN_ASSESSMENT_ID));
            String noteText = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_COLUMN_NOTE_TEXT));

            ObjectNote n = new ObjectNote();
            n.noteId = id;
            n.assessmentId = assessmentId;
            n.courseId = courseId;
            n.noteText = noteText;

            cursor.close();
            return n;
        }
        return null;
    }

    static void editTerm (Context context, int termId, String termTitle, String termStart, String termEnd) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_COLUMN_TITLE, termTitle);
        values.put(DBOpenHelper.TERM_COLUMN_START, termStart);
        values.put(DBOpenHelper.TERM_COLUMN_END, termEnd);
        context.getContentResolver().update(URIProvider.TERM_URI, values,
                DBOpenHelper.TERM_COLUMN_TERM_ID + "=" + termId, null);
    }

    static void editCourse(Context context, int courseId, int termId, String courseTitle, String courseStart,
                                   String courseEnd, String description, String status, int notification) {

        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_COLUMN_TERM_ID, termId);
        values.put(DBOpenHelper.COURSE_COLUMN_TITLE, courseTitle);
        values.put(DBOpenHelper.COURSE_COLUMN_START, courseStart);
        values.put(DBOpenHelper.COURSE_COLUMN_END, courseEnd);
        values.put(DBOpenHelper.COURSE_COLUMN_DESCRIPTION, description);
        values.put(DBOpenHelper.COURSE_COLUMN_STATUS, status);
        values.put(DBOpenHelper.COURSE_COLUMN_NOTIFICATION, notification);

        context.getContentResolver().update(URIProvider.COURSE_URI, values,
                DBOpenHelper.COURSE_COLUMN_COURSE_ID + "=" + courseId, null);

    }

    static void editAssessment(Context context,  int assessmentId, int courseId, String title,
                               String assessmentCode, String assessmentType, String description,
                               String dueDate, int assessmentNotification) {

        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENT_COLUMN_COURSE_ID, courseId);
        values.put(DBOpenHelper.ASSESSMENT_COLUMN_TITLE, title);
        values.put(DBOpenHelper.ASSESSMENT_COLUMN_CODE, assessmentCode);
        values.put(DBOpenHelper.ASSESSMENT_COLUMN_ASSESSMENT_TYPE, assessmentType);
        values.put(DBOpenHelper.ASSESSMENT_COLUMN_DESCRIPTION, description);
        values.put(DBOpenHelper.ASSESSMENT_COLUMN_DUE_DATE, dueDate);
        values.put(DBOpenHelper.ASSESSMENT_COLUMN_NOTIFICATION, assessmentNotification);

        context.getContentResolver().update(URIProvider.ASSESSMENT_URI, values,
                DBOpenHelper.ASSESSMENT_COLUMN_ASSESSMENT_ID + "=" + assessmentId, null);
    }

    static void editMentor(Context context, int mentorId, int courseId, String mentorName,
                           String mentorPhone, String mentorEmail) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.MENTOR_COLUMN_COURSE_ID, courseId);
        values.put(DBOpenHelper.MENTOR_COLUMN_MENTOR_NAME, mentorName);
        values.put(DBOpenHelper.MENTOR_COLUMN_MENTOR_PHONE, mentorPhone);
        values.put(DBOpenHelper.MENTOR_COLUMN_MENTOR_EMAIL, mentorEmail);

        context.getContentResolver().update(URIProvider.MENTOR_URI, values,
                DBOpenHelper.MENTOR_COLUMN_MENTOR_ID + "=" + mentorId, null);
    }

    static void editNote(Context context, int noteId, int courseId, int assessmentId,
                                String noteText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_COLUMN_COURSE_ID, courseId);
        values.put(DBOpenHelper.NOTE_COLUMN_ASSESSMENT_ID, assessmentId);
        values.put(DBOpenHelper.NOTE_COLUMN_NOTE_TEXT, noteText);
        values.put(DBOpenHelper.NOTE_COLUMN_COURSE_ID, courseId);

        context.getContentResolver().update(URIProvider.NOTE_URI, values,
                DBOpenHelper.NOTE_COLUMN_NOTES_ID + "=" + noteId, null);
    }

    static void deleteTerm(Context context, int id) {
        context.getContentResolver().delete(URIProvider.TERM_URI,
                DBOpenHelper.TERM_COLUMN_TERM_ID + " = " + id, null);
    }

    static void deleteCourse(Context context, int id) {
        int assessmentCount = DataConverter.getAssessmentCount(context, id);
        int mentorCount = DataConverter.getMentorCount(context, id);
        int noteCount = DataConverter.getCourseNoteCount(context, id);

        if (assessmentCount != 0){
            Cursor assessmentCursor = context.getContentResolver().query(URIProvider.ASSESSMENT_URI,
                    DBOpenHelper.ASSESSMENT_COLUMNS, DBOpenHelper.ASSESSMENT_COLUMN_COURSE_ID
                            + "=" + id,
                    null, null);
            if (assessmentCursor != null) {
            while (assessmentCursor.moveToNext()) {
                DataConverter.deleteAssessment(context,
                        assessmentCursor.getInt(assessmentCursor.getColumnIndex(DBOpenHelper.ASSESSMENT_COLUMN_ASSESSMENT_ID)));
            }
            assessmentCursor.close();
            }

        }

        if (mentorCount != 0){
            Cursor mentorCursor = context.getContentResolver().query(URIProvider.MENTOR_URI,
                    DBOpenHelper.MENTOR_COLUMNS, DBOpenHelper.MENTOR_COLUMN_COURSE_ID + "=" + id,
                    null, null);

            if (mentorCursor != null) {
                while (mentorCursor.moveToNext()) {
                    DataConverter.deleteMentor(context,
                            mentorCursor.getInt(mentorCursor.getColumnIndex(DBOpenHelper.MENTOR_COLUMN_MENTOR_ID)));
                }
                mentorCursor.close();
            }
        }

        if (noteCount != 0) {
            Cursor noteCursor = context.getContentResolver().query(URIProvider.NOTE_URI,
                    DBOpenHelper.NOTE_COLUMNS, DBOpenHelper.NOTE_COLUMN_COURSE_ID + "=" + id,
                    null, null);
            if (noteCursor != null) {
                while (noteCursor.moveToNext()) {
                    DataConverter.deleteNote(context,
                            noteCursor.getInt(noteCursor.getColumnIndex(DBOpenHelper.NOTE_COLUMN_NOTES_ID)));
                }
                noteCursor.close();
            }
        }

        context.getContentResolver().delete(URIProvider.COURSE_URI,
                DBOpenHelper.COURSE_COLUMN_COURSE_ID + " = " + id, null);

    }

    static void deleteAssessment(Context context, int id) {
        int noteCount = DataConverter.getAssessmentNoteCount(context, id);
        if (noteCount != 0) {
            Cursor noteCursor = context.getContentResolver().query(URIProvider.NOTE_URI,
                    DBOpenHelper.NOTE_COLUMNS, DBOpenHelper.NOTE_COLUMN_ASSESSMENT_ID + "=" + id,
                    null, null);
            if (noteCursor != null) {
                while (noteCursor.moveToNext()) {
                    DataConverter.deleteNote(context,
                            noteCursor.getInt(noteCursor.getColumnIndex(DBOpenHelper.NOTE_COLUMN_NOTES_ID)));
                }
                noteCursor.close();
            }
        }

        context.getContentResolver().delete(URIProvider.ASSESSMENT_URI,
                DBOpenHelper.ASSESSMENT_COLUMN_ASSESSMENT_ID + " = " + id, null);
    }

    static void deleteMentor(Context context, int id) {
        context.getContentResolver().delete(URIProvider.MENTOR_URI,
                DBOpenHelper.MENTOR_COLUMN_MENTOR_ID + " = " + id, null);
    }

    static void deleteNote(Context context, int id) {
        context.getContentResolver().delete(URIProvider.NOTE_URI,
                DBOpenHelper.NOTE_COLUMN_NOTES_ID + " = " + id, null);
    }

    static int getCourseCount(Context context, int termId) {
        Cursor cursor = context.getContentResolver().query(URIProvider.COURSE_URI,
                DBOpenHelper.COURSE_COLUMNS,DBOpenHelper.COURSE_COLUMN_TERM_ID+ "=" + termId,
                null, null );
        int numRows = 0;
        if (cursor != null) {
            numRows = cursor.getCount();
            cursor.close();
        }
        return numRows;
    }

    static int getCourseCompletedCount(Context context, int termId) {
        Cursor cursor = context.getContentResolver().query(URIProvider.COURSE_URI,
                DBOpenHelper.COURSE_COLUMNS, DBOpenHelper.COURSE_COLUMN_TERM_ID + " = "
                        + termId + " AND " + DBOpenHelper.COURSE_COLUMN_STATUS + " = " +
                        "'Completed'", null, null);
        int numRows = 0;
        if (cursor != null) {
            numRows = cursor.getCount();
            cursor.close();
        }
        return numRows;
    }

    static int getAssessmentCount(Context context, int courseId) {
        Cursor cursor = context.getContentResolver().query(URIProvider.ASSESSMENT_URI,
                DBOpenHelper.ASSESSMENT_COLUMNS,DBOpenHelper.ASSESSMENT_COLUMN_COURSE_ID
                        + "=" + courseId,null, null );
        int numRows = 0;
        if (cursor != null) {
            numRows = cursor.getCount();
            cursor.close();
        }
        return numRows;
    }

    static int getMentorCount(Context context, int courseId) {
        Cursor cursor = context.getContentResolver().query(URIProvider.MENTOR_URI,
                DBOpenHelper.MENTOR_COLUMNS,DBOpenHelper.MENTOR_COLUMN_COURSE_ID
                        + "=" + courseId,null, null );
        int numRows = 0;
        if (cursor != null) {
            numRows = cursor.getCount();
            cursor.close();
        }
        return numRows;
    }

    private static int getCourseNoteCount(Context context, int courseId) {
        Cursor cursor = context.getContentResolver().query(URIProvider.NOTE_URI,
                DBOpenHelper.NOTE_COLUMNS,DBOpenHelper.NOTE_COLUMN_COURSE_ID
                        + "=" + courseId,null, null );
        int numRows = 0;
        if (cursor != null) {
            numRows = cursor.getCount();
            cursor.close();
            return numRows;
        }
        return 0;
    }

    private static int getAssessmentNoteCount(Context context, int assessmentId) {
        Cursor cursor = context.getContentResolver().query(URIProvider.NOTE_URI,
                DBOpenHelper.NOTE_COLUMNS,DBOpenHelper.NOTE_COLUMN_ASSESSMENT_ID
                        + "=" + assessmentId,null, null );
        int numRows = 0;
        if (cursor != null) {
            numRows = cursor.getCount();

            cursor.close();
            return numRows;
        }
        return 0;
    }

    static ArrayList<Integer> getTermIds (Context context){
        ArrayList<Integer> results = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(URIProvider.TERM_URI,
                DBOpenHelper.TERM_COLUMNS, null, null, null);

        if (cursor != null) {
            int termId = cursor.getColumnIndex(DBOpenHelper.TERM_COLUMN_TERM_ID);

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Integer rowId = cursor.getInt(termId);
                results.add(rowId);
            }

            cursor.close();
            return results;
        }
        return  null;
    }

    static ArrayList<Integer> getCourseIds (Context context){
        ArrayList<Integer> results = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(URIProvider.COURSE_URI,
                DBOpenHelper.COURSE_COLUMNS, null, null, null);

        if (cursor != null) {
            int courseId = cursor.getColumnIndex(DBOpenHelper.COURSE_COLUMN_COURSE_ID);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Integer rowId = cursor.getInt(courseId);
                results.add(rowId);
            }

            cursor.close();
            return results;
        }
        return null;
    }

    static ArrayList<Integer> getAssessmentIds (Context context){
        ArrayList<Integer> results = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(URIProvider.ASSESSMENT_URI,
                DBOpenHelper.ASSESSMENT_COLUMNS, null, null, null);

        if (cursor != null) {
        int assessmentId = cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_COLUMN_ASSESSMENT_ID);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
        {
            Integer rowId = cursor.getInt(assessmentId);
            results.add(rowId);
        }

        cursor.close();
        return results;
    }
    return null;
    }



    static ArrayList<ObjectCourse> getAlertCourses(Context context) {

        ArrayList<ObjectCourse> results = new ArrayList<>();
        ArrayList<Integer> courseIds = DataConverter.getCourseIds(context);
        if (courseIds != null) {
            for (int i = 0; i < courseIds.size(); i++) {
                Cursor cursor = context.getContentResolver().query(URIProvider.COURSE_URI,
                        DBOpenHelper.COURSE_COLUMNS, DBOpenHelper.COURSE_COLUMN_COURSE_ID
                                + "=" + courseIds.get(i) + " AND " + DBOpenHelper.COURSE_COLUMN_STATUS + " != " +
                                "'Completed'" + " AND " + DBOpenHelper.COURSE_COLUMN_NOTIFICATION + " = 1"
                        ,null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    Integer termId = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.COURSE_COLUMN_TERM_ID));
                    String courseTitle = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_COLUMN_TITLE));
                    String courseStart = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_COLUMN_START));
                    String courseEnd = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_COLUMN_END));
                    String courseDescription =
                            cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_COLUMN_DESCRIPTION));
                    String status = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_COLUMN_STATUS));
                    Boolean courseNotifications =
                            (cursor.getInt(cursor.getColumnIndex(DBOpenHelper.COURSE_COLUMN_NOTIFICATION)) == 1);

                    ObjectCourse c = new ObjectCourse();
                    c.termId = termId;
                    c.courseId = i;
                    c.courseTitle = courseTitle;
                    c.courseStart = courseStart;
                    c.courseEnd = courseEnd;
                    c.description = courseDescription;
                    c.status = status;
                    c.courseNotification = courseNotifications;
                    cursor.close();

                    results.add(c);
                }
            }

            return results;
        }
        return null;
    }

    static ArrayList<ObjectAssessment> getAlertAssessments(Context context) {

        ArrayList<ObjectAssessment> results = new ArrayList<>();
        ArrayList<Integer> assessmentIds = DataConverter.getAssessmentIds(context);

        if (assessmentIds != null) {
            for (int i = 0; i < assessmentIds.size(); i++) {
                Cursor cursor = context.getContentResolver().query(URIProvider.ASSESSMENT_URI,
                        DBOpenHelper.ASSESSMENT_COLUMNS, DBOpenHelper.ASSESSMENT_COLUMN_ASSESSMENT_ID +
                                "=" + assessmentIds.get(i) + " AND " + DBOpenHelper.ASSESSMENT_COLUMN_NOTIFICATION + " = "
                                + "1", null, null);


                if (cursor != null && cursor.moveToFirst()) {
                    Integer courseId = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_COLUMN_COURSE_ID));
                    String aCode = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_COLUMN_CODE));
                    String aTitle = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_COLUMN_TITLE));
                    String description = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_COLUMN_DESCRIPTION));
                    String aType = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_COLUMN_ASSESSMENT_TYPE));
                    String dueDate = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_COLUMN_DUE_DATE));

                    ObjectAssessment a = new ObjectAssessment();
                    a.assessmentId = i;
                    a.courseId = courseId;
                    a.assessmentCode = aCode;
                    a.title = aTitle;
                    a.description = description;
                    a.assessmentType = aType;
                    a.dueDate = dueDate;
                    cursor.close();

                    results.add(a);
                }
            }

                return results;
            }
            return  null;
    }



}
