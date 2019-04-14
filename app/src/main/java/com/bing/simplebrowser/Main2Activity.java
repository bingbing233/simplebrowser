package com.bing.simplebrowser;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity  {

    WebView webView;
    SwipeRefreshLayout swipeRefreshLayout;
    WebSettings webSettings;
    Toolbar toolbar;
    ProgressBar progressBar;
    GestureDetector gestureDetector;
    String colorValue;
    String searchValue="";
    String search="";
    int colorId;
    private HistoryDataBaseHelper historyDataBaseHelper;
    private BookmarkDataBaseHelper bookmarkDataBaseHelper;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int  itemId = item.getItemId();
        switch (itemId){
            case R.id.historyItem:
                Intent historyIntent = new Intent(Main2Activity.this,HistoryActivity.class);
                startActivity(historyIntent);
                break;
            case R.id.bookmarkItem:
                Intent bookmarkIntent = new Intent(Main2Activity.this,BookmarkActivity.class);
                startActivity(bookmarkIntent);
                break;
            case R.id.colection:
                bookmarkDataBaseHelper = new BookmarkDataBaseHelper(this,"bookmark.db",null,2);
                SQLiteDatabase sqLiteDatabase= bookmarkDataBaseHelper.getWritableDatabase();
                //开始组装数据
                ContentValues values =new ContentValues();
                values.put("title",webView.getTitle());
                values.put("url",webView.getUrl());
                sqLiteDatabase.insert("bookmark",null,values);
                values.clear();
                Toast.makeText(Main2Activity.this,"收藏成功",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        webView  = (WebView)findViewById(R.id.webview);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiper);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        historyDataBaseHelper = new HistoryDataBaseHelper(this,"history.db",null,2);

        toolbar = (Toolbar)findViewById(R.id.toolBar);
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        colorValue = pref.getString("color","信仰绿");
        searchValue =pref.getString("search","Baidu");
        switch(searchValue){
            case "Baidu":search="http://www.baidu.com/s?wd=";break;
            case "Bing":search="http://cn.bing.com/search?q=";break;
            case "Google":search="http://www.google.com.hk/search?q=";break;
            case "Sogou":search="http://www.sogou.com/web?query=";break;
        }


        if(url.startsWith("www")){
            url = "http://"+url;
        }
        if(!url.startsWith("http://")&&!url.startsWith("www")&&!url.startsWith("https://")) {
            url = search+url;
        }

        switch (colorValue){
            case"酷安绿":colorId = R.color.mgreen;break;
            case"知乎蓝":colorId = R.color.mblue;break;
            case"哔哩粉":colorId = R.color.mpink;break;
            case"网易红":colorId = R.color.mred;break;
            case"信仰绿":colorId = R.color.colorPrimary;break;
        }
        toolbar.setBackgroundColor(getResources().getColor(colorId));
        getWindow().setStatusBarColor(getResources().getColor(colorId));

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        setSupportActionBar(toolbar);
        webSettings = webView.getSettings();            //设置
        webSettings.setDomStorageEnabled(true);         //支持dom
        webSettings.setJavaScriptEnabled(true);         //开启webview事务
        webView.setWebViewClient(new WebViewClient());  //允许缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webView.loadUrl(url);//加载url
        //下载服务
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                downloadByBrowser(url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {


                if (newProgress==100){
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    if (progressBar.getVisibility()==View.GONE){
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
       webView.setWebViewClient(new WebViewClient(){
           @Override
           public void onPageFinished(WebView view, String url) {
               super.onPageFinished(view, url);
               SQLiteDatabase sqLiteDatabase= historyDataBaseHelper.getWritableDatabase();
               toolbar.setTitle(webView.getTitle());
               //开始组装数据
               ContentValues values =new ContentValues();
               values.put("title",webView.getTitle());
               values.put("url",webView.getUrl());
               sqLiteDatabase.insert("history",null,values);
               Log.d("data",values.get("title").toString());
               values.clear();

           }
       });


        swipeRefreshLayout.setColorSchemeColors(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });


        gestureDetector = new GestureDetector(Main2Activity.this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public void onLongPress(MotionEvent e) {
                ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Main2Activity.this.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Label",webView.getUrl());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(Main2Activity.this,"网址已复制",Toast.LENGTH_SHORT).show();
                Log.d("url",webView.getUrl());
                super.onLongPress(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(e1.getX()-e2.getX()<100){
                    webView.goBack();
                }
                if(e1.getX()-e2.getX()>100){
                    webView.goForward();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                finish();
                return super.onDoubleTap(e);
            }
        });

        toolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

    }


    private void downloadByBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

}
