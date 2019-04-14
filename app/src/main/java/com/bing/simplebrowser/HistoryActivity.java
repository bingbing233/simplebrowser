package com.bing.simplebrowser;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    HistoryDataBaseHelper historyDataBaseHelper;
Toolbar toolbar;
List<History>historyList = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
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
        setContentView(R.layout.activity_history);
        toolbar = (Toolbar) findViewById(R.id.historyToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        historyDataBaseHelper = new HistoryDataBaseHelper(HistoryActivity.this,"history.db",null,2);
        SQLiteDatabase sqLiteDatabase = historyDataBaseHelper.getWritableDatabase();
        //查询数据
        Cursor cursor =  sqLiteDatabase.query("history",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            //遍历
            do{
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String url = cursor.getString(cursor.getColumnIndex("url"));
                History history = new History(title,url);
                historyList.add(history);
            }
            while(cursor.moveToNext());

        }
        cursor.close();

        Collections.reverse(historyList);
        RecyclerView recyclerView = findViewById(R.id.historyList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        HistoryAdapter adapter = new HistoryAdapter(historyList,HistoryActivity.this);
        recyclerView.setAdapter(adapter);

    }

}
