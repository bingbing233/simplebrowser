package com.bing.simplebrowser;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.Toolbar;

public class SettingActivity extends AppCompatActivity {
    Toolbar toolbar ;
    String colorValue;
    int colorId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getFragmentManager().beginTransaction().replace(R.id.frame,new SettingFragment()).commit();
        toolbar = (Toolbar)findViewById(R.id.toolBar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SharedPreferences pre = getSharedPreferences("data",MODE_PRIVATE);
        colorValue = pre.getString("color","信仰绿");
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
}
