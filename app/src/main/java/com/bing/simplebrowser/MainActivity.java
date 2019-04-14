package com.bing.simplebrowser;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {


    int colorId=R.color.colorPrimary;
    Toolbar toolbar;
    EditText editText;
    String url;
    ImageView imageView;
    String colorValue;
    String picturePath = "";
    String backgroundPath = "";
    String searchEngine="";
    HistoryDataBaseHelper historyDataBaseHelper;
    BookmarkDataBaseHelper bookmarkDataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("Bing");
        toolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(toolbar);

        bookmarkDataBaseHelper = new BookmarkDataBaseHelper(MainActivity.this,"bookmark.db",null,2);
        historyDataBaseHelper = new HistoryDataBaseHelper(MainActivity.this,"history.db",null,2);
        historyDataBaseHelper.getWritableDatabase();
        bookmarkDataBaseHelper.getDatabaseName();




//输入框的事件
        editText = (EditText) findViewById(R.id.edittext);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                }
                return false;
            }
        });

//        猫图片的事件

        imageView = (ImageView) findViewById(R.id.img);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "（提示：手势需在标题栏完成）\n双击：回到主页\n右滑：返回\n左滑：前进\n长按：复制当前网址\n下拉刷新\n";
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("手势使用说明");
                builder.setMessage(msg);
                builder.setCancelable(true);
                builder.setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.setCancelable(true);
                    }
                });
                builder.show();
            }
        });

    }
//加载menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
//menu事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.setting:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.history:
                Intent historyIntent = new Intent(MainActivity.this,HistoryActivity.class);
                startActivity(historyIntent);
                break;
            case R.id.bookmark:
                Intent bookmarkIntent = new Intent(MainActivity.this,BookmarkActivity.class);
                startActivity(bookmarkIntent);
                break;
//            case R.id.share:
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("text/plain");
//                shareIntent.putExtra("url", "https://www.coolapk.com/apk/com.bing.simplebrowser\n");
//                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(shareIntent.createChooser(shareIntent, "分享到"));
//
//                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //    跳转到webview的activity
    private void search() {
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        url = editText.getText().toString();
        intent.putExtra("url", url);
        startActivity(intent);
    }
//当activity加载时，加载设置偏好的内容
    @Override
    protected void onStart() {
        String title="";
        //偏好设置
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        boolean state = pref.getBoolean("state", true);
        picturePath = pref.getString("profilePic", "");
        backgroundPath = pref.getString("background", "");
        colorValue = pref.getString("color","信仰绿");
        searchEngine = pref.getString("search","Baidu");

        switch (colorValue){
            case"酷安绿":colorId = R.color.mgreen;break;
            case"知乎蓝":colorId = R.color.mblue;break;
            case"哔哩粉":colorId = R.color.mpink;break;
            case"网易红":colorId = R.color.mred;break;
            case"信仰绿":colorId = R.color.colorPrimary;break;
        }
        toolbar.setBackgroundColor(getResources().getColor(colorId));
        toolbar.setTitle(searchEngine);
        getWindow().setStatusBarColor(getResources().getColor(colorId));
        if(!backgroundPath.equals("")){
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main,null);
            ConstraintLayout constraintLayout = findViewById(R.id.mainLayout);
            Bitmap bitmap = BitmapFactory.decodeFile(backgroundPath);
            Drawable drawable = new BitmapDrawable(bitmap);
            constraintLayout.setBackground(drawable);

        }
        if (!picturePath.equals("")) {

            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
            drawable.setCircular(true);
            imageView.setImageDrawable(drawable);

        }
        if (!state) {
            imageView.setVisibility(View.INVISIBLE);
        } else {
            imageView.setVisibility(View.VISIBLE);
        }


        super.onStart();
    }
}
