package com.example.myapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myapplication.bean.AppData;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_USER = "CREATE TABLE  "+ AppData.USER_INFO + "(" +
            AppData.USER_ID+" text," +
            AppData.USER_PASSWORD+" text," +
            AppData.USER_NICKNAME+" text," +
            AppData.USER_HEADIMG+" text," +
            AppData.USER_AGE+" integer," +
            AppData.USER_CITY+" text," +
            AppData.USER_SIGNATURE+" text," +
            AppData.USER_EMAIL+" text" +
            ")";
    private static final String CREATE_BOOKSELF = "CREATE TABLE " + AppData.USER_BOOKSELF + "(" +
            AppData.USER_ID+" text," +
            AppData.BOOK_TITLE+" text," +
            AppData.BOOK_FILE_PATH+" text," +
            AppData.BOOK_IMAGE_PATH+" text," +
            AppData.BOOK_FILE_MD5+" text," +
            AppData.BOOK_IMPORT_TIME+" text," +
            AppData.BOOK_READ_TIME+" text," +
            AppData.BOOK_OPEN_TIMES+" integer," +
            AppData.BOOK_PROGRESS+" integer" +
            ")";
    private static final String CREATE_BOOK_COMMENT="CREATE TABLE "+AppData.BOOK_COMMENT+"(" +
            AppData.USER_ID +" text," +
            AppData.BOOK_FILE_MD5+" text," +
            AppData.FIEID_BOOK_COMMENT+" text" +
            ")";


    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_BOOKSELF);
        db.execSQL(CREATE_BOOK_COMMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
