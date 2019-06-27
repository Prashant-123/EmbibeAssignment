package com.embibeassignment.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.embibeassignment.models.MovieModel;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "movies";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String IMAGE = "image";
    public static final String YEAR = "year";
    public static final String RATING = "rating";
    public static final String OVERVIEW = "overview";
    public static final String GENRE = "genre";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + ID + " TEXT UNIQUE,"
                    + IMAGE + " TEXT,"
                    + TITLE + " TEXT,"
                    + YEAR + " TEXT,"
                    + OVERVIEW + " TEXT,"
                    + GENRE + " TEXT,"
                    + RATING + " TEXT"
                    + ")";

    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void clearDB() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    public void insertMovie(String id, String title, String image, String year, String rating, String overview, String genre) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id); contentValues.put(TITLE,title); contentValues.put(YEAR, year);
        contentValues.put(IMAGE, image); contentValues.put(RATING, rating); contentValues.put(OVERVIEW, overview);
        contentValues.put(GENRE, genre);
        db.insert(TABLE_NAME, null, contentValues);
    }

    public MovieModel getMovie(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( "SELECT * FROM " + TABLE_NAME + " WHERE " +
                ID + "=?", new String[] { id } );
        if (cursor != null)
            cursor.moveToFirst();

        MovieModel movie = new MovieModel(cursor.getString(cursor.getColumnIndex(TITLE)),
                cursor.getString(cursor.getColumnIndex(IMAGE)), cursor.getString(cursor.getColumnIndex(YEAR)),
                        cursor.getString(cursor.getColumnIndex(ID)), cursor.getFloat(cursor.getColumnIndex(RATING)),
                cursor.getString(cursor.getColumnIndex(OVERVIEW)),
                cursor.getString(cursor.getColumnIndex(GENRE)));
        // return contact
        return movie;
    }
}
