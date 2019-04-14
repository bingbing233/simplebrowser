package com.bing.simplebrowser;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class HistoryDataBaseHelper extends SQLiteOpenHelper {
    public static final String CREAT_HISTORY = "create table history ("
            + "id integer primary key autoincrement,"
            + "url text,"
            + "title text)";

    private Context context;
    public HistoryDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAT_HISTORY);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
