package com.bing.simplebrowser;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class SettingFragment extends PreferenceFragment {
    SwitchPreference switchPreference;
    Preference verPreference;
    Preference profilePreference;
    Preference backgroundPreference;
    ListPreference colorPreference;
    ListPreference searchPreference;
    String picturePath ="";
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);

        //当前版本
        verPreference = findPreference("version");
        verPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity(), "你好，探索者者！", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        //头像开关
        switchPreference = (SwitchPreference)findPreference("profile");
        switchPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if(switchPreference.isChecked()){
                    profileState(true);
                }
                else {
                    profileState(false);
                }
                return false;
            }
        });
        //自定义背景
        backgroundPreference = findPreference("background");
        backgroundPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
                else
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,2);
                }
                return false;
            }
        });

        //自定义头像
        profilePreference =findPreference("whichProfile");
        profilePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
                else
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,1);
                }

                return false;
            }
        });
        //强调色
        colorPreference = (ListPreference) findPreference("color");
        colorPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String colorValue="";
                CharSequence[] entries = colorPreference.getEntries();
                int index = colorPreference.findIndexOfValue((String)newValue);
                colorPreference.setSummary(entries[index]);
                mainColor(entries[index].toString());
                Log.d("color",entries[index].toString());
                return false;
            }
        });

    searchPreference = (ListPreference) findPreference("search");
    searchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String searchEngine="";
            CharSequence[]entries = searchPreference.getEntries();
            int index = searchPreference.findIndexOfValue((String)newValue);
            searchEngine = entries[index].toString();
            searchPreference.setSummary(entries[index]);
            chooseSearchEngine(searchEngine);
            return false;
        }
    });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1 && resultCode == Activity.RESULT_OK && data !=null){
            //获取系统返回照片的url
            Uri selectedImage = data.getData();
            String [] filePathColumns = {MediaStore.Images.Media.DATA};
            //从系统表上查询指定url的照片
            Cursor cursor = getContext().getContentResolver().query(selectedImage,filePathColumns,null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
            //获取照片路径
           picturePath = cursor.getString(columnIndex);
            cursor.close();
            SharedPreferences.Editor editor = this.getContext().getSharedPreferences("data",Context.MODE_PRIVATE).edit();
                editor.putString("profilePic",picturePath);
                editor.apply();
        }
        if(requestCode ==2 && resultCode == Activity.RESULT_OK && data !=null){
            //获取系统返回照片的url
            Uri selectedImage = data.getData();
            String [] filePathColumns = {MediaStore.Images.Media.DATA};
            //从系统表上查询指定url的照片
            Cursor cursor = getContext().getContentResolver().query(selectedImage,filePathColumns,null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
            //获取照片路径
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            SharedPreferences.Editor editor = this.getContext().getSharedPreferences("data",Context.MODE_PRIVATE).edit();
            editor.putString("background",picturePath);
            editor.apply();
        }
    }


    private void profileState(Boolean state){
      SharedPreferences.Editor editor = this.getContext().getSharedPreferences("data", Context.MODE_PRIVATE).edit();
        editor.putBoolean("state",state);
        editor.apply();
    }
    private void mainColor(String color){
        SharedPreferences.Editor editor = this.getContext().getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        editor.putString("color",color);
        editor.apply();
    }
     private void chooseSearchEngine(String searchEngine){
        SharedPreferences.Editor editor = this.getContext().getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        editor.putString("search",searchEngine);
        editor.apply();
     }
}
