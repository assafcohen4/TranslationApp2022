package com.app.translation.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.translation.Model.TranslateWord;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TranslatedWords";
    private static final String TABLE_TRANSLATION = "traslation";
    private static final String KEY_ID = "id";
    private static final String KEY_TRANSLATION_ID = "translation_id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_FROM_TYPE = "from_type";
    private static final String KEY_FROM_TEXT = "from_text";
    private static final String KEY_TO_TYPE = "to_type";
    private static final String KEY_TO_TEXT = "to_text";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE =
                "CREATE TABLE " + TABLE_TRANSLATION + "(" +
                        KEY_ID + " INTEGER PRIMARY KEY," +
                        KEY_TRANSLATION_ID + " TEXT," +
                        KEY_USER_ID + " TEXT," +
                        KEY_FROM_TYPE + " TEXT," +
                        KEY_FROM_TEXT + " TEXT," +
                        KEY_TO_TYPE + " TEXT," +
                        KEY_TO_TEXT + " TEXT" +
                        ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSLATION);

        // Create tables again
        onCreate(db);
    }
    // code to add the new contact
    public void addTranslatedWord(TranslateWord word) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TRANSLATION_ID, word.getTranslationID());
        values.put(KEY_USER_ID, word.getUserID());
        values.put(KEY_FROM_TYPE, word.getFromType());
        values.put(KEY_FROM_TEXT, word.getFromText());
        values.put(KEY_TO_TYPE, word.getToType());
        values.put(KEY_TO_TEXT, word.getToText());

        // Inserting Row
        db.insert(TABLE_TRANSLATION, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single contact
    public Boolean isSavedWord(String translation_id) {
        boolean isSaved = false;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TRANSLATION, new String[] { KEY_ID,
                        KEY_TRANSLATION_ID}, KEY_TRANSLATION_ID + "=?",
                new String[] { String.valueOf(translation_id) }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        if(cursor.getCount() > 0){
            isSaved = true;
        }
        return isSaved;
    }

    // code to get all contacts in a list view
    public List<TranslateWord> getAllSavedWords() {
        List<TranslateWord> savedWords = new ArrayList<TranslateWord>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TRANSLATION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TranslateWord word = new TranslateWord();
                word.setTranslationID(cursor.getString(1));
                word.setUserID(cursor.getString(2));
                word.setFromType(cursor.getString(3));
                word.setFromText(cursor.getString(4));
                word.setToType(cursor.getString(5));
                word.setToText(cursor.getString(6));
                // Adding contact to list
                savedWords.add(word);
            } while (cursor.moveToNext());
        }

        // return contact list
        return savedWords;
    }

    // code to update the single contact
    /*public int updateWord(TranslateWord word) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, word.getName());
        values.put(KEY_PH_NO, word.getPhoneNumber());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(word.getID()) });
    }*/

    // Deleting single contact
    public void deleteSavedWord(TranslateWord word) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSLATION, KEY_TRANSLATION_ID + " = ?",
                new String[] { String.valueOf(word.getTranslationID()) });
        db.close();
    }

    // Getting contacts Count
    public int getWordCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TRANSLATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    public void deleteTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TRANSLATION);
        db.close();
    }



}