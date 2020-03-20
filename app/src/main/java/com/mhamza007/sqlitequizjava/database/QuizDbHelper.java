package com.mhamza007.sqlitequizjava.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.mhamza007.sqlitequizjava.model.Question;

import java.util.ArrayList;
import java.util.List;

import static com.mhamza007.sqlitequizjava.model.QuizContract.QuestionsTable.*;

public class QuizDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SqliteQuizJava.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public QuizDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                TABLE_NAME + " ( " +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_QUESTION + " TEXT, " +
                COLUMN_OPTION1 + " TEXT, " +
                COLUMN_OPTION2 + " TEXT, " +
                COLUMN_OPTION3 + " TEXT, " +
                COLUMN_ANSWER_NUMBER + " INTEGER" +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private void fillQuestionsTable() {
        Question q1 = new Question("A is correct", "A", "B", "C", 1);
        addQuestion(q1);
        Question q2 = new Question("B is correct", "A", "B", "C", 2);
        addQuestion(q2);
        Question q3 = new Question("C is correct", "A", "B", "C", 3);
        addQuestion(q3);
        Question q4 = new Question("A is correct again", "A", "B", "C", 1);
        addQuestion(q4);
        Question q5 = new Question("B is correct again", "A", "B", "C", 2);
        addQuestion(q5);
    }

    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_QUESTION, question.getQuestion());
        cv.put(COLUMN_OPTION1, question.getOption1());
        cv.put(COLUMN_OPTION2, question.getOption2());
        cv.put(COLUMN_OPTION3, question.getOption3());
        cv.put(COLUMN_ANSWER_NUMBER, question.getAnswerNumber());
        db.insert(TABLE_NAME, null, cv);
    }

    public List<Question> getAllQuestions() {
        List<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION)));
                question.setOption1(cursor.getString(cursor.getColumnIndex(COLUMN_OPTION1)));
                question.setOption2(cursor.getString(cursor.getColumnIndex(COLUMN_OPTION2)));
                question.setOption3(cursor.getString(cursor.getColumnIndex(COLUMN_OPTION3)));
                question.setAnswerNumber(cursor.getInt(cursor.getColumnIndex(COLUMN_ANSWER_NUMBER)));
                questionList.add(question);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return questionList;
    }
}
