package com.bing.simplebrowser;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class BookmarkDataBaseHelper extends SQLiteOpenHelper {
    private Context  context;
    final static String CREATE_BOOKMARK ="create table bookmark("
            + "id integer primary key autoincrement,"
            + "url text,"
            + "title text)";

    public BookmarkDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOKMARK);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
