package com.example.androidassignments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "Messages.db";
    private static int VERSION_NUM =3;
    static final String KEY_ID = "_id";
    static final String TABLE_NAME = "MessageTable";
    static final String KEY_MESSAGE = "MessageKey";
    static final String ACTIVITY_NAME= "ChatDatabaseHelper";
    private static final String DATABASE_CREATE = "create table " + TABLE_NAME +"(" + KEY_ID + " integer primary key autoincrement, " + KEY_MESSAGE + " text not null);"
    ;
    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
        Log.i(ACTIVITY_NAME, "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion=" + i + " newVersion=" + i1);
    }

    public void onDowngrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i(ACTIVITY_NAME, "Why you Downgrade?, oldVersion=" + i + " newVersion=" + i1);
    }
}
