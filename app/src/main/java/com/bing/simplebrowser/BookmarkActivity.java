package com.bing.simplebrowser;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<Bookmark>list;
    BookmarkDataBaseHelper bookmarkDataBaseHelper;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        int colorId = R.color.colorPrimary;
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        String colorValue = pref.getString("color","信仰绿");
        switch (colorValue){
            case"酷安绿":colorId = R.color.mgreen;break;
            case"知乎蓝":colorId = R.color.mblue;break;
            case"哔哩粉":colorId = R.color.mpink;break;
            case"网易红":colorId = R.color.mred;break;
            case"信仰绿":colorId = R.color.colorPrimary;break;
        }
        toolbar.setBackgroundColor(getResources().getColor(colorId));
        getWindow().setStatusBarColor(getResources().getColor(colorId));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        list = new ArrayList<>();
        toolbar = (Toolbar)findViewById(R.id.bookmarkToolbar);
        recyclerView = (RecyclerView)findViewById(R.id.bookmarkList);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bookmarkDataBaseHelper = new BookmarkDataBaseHelper(BookmarkActivity.this,"bookmark.db",null,2);
        SQLiteDatabase sqLiteDatabase = bookmarkDataBaseHelper.getWritableDatabase();
        //查询数据
        Cursor cursor =  sqLiteDatabase.query("bookmark",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            //遍历
            do{
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String url = cursor.getString(cursor.getColumnIndex("url"));
                Bookmark bookmark = new Bookmark(title,url);
                list.add(bookmark);
            }
            while(cursor.moveToNext());

        }
        cursor.close();

        Collections.reverse(list);
        RecyclerView recyclerView = findViewById(R.id.bookmarkList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        BookmarkAdapter adapter = new BookmarkAdapter(list, BookmarkActivity.this);
        recyclerView.setAdapter(adapter);

    }
}
