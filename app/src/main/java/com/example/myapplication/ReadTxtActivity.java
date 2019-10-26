package com.example.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myapplication.bean.AppData;
import com.example.myapplication.bookopen.BookFactory;
import com.example.myapplication.database.UserDataBase;
import com.example.myapplication.model.Data;
import com.example.myapplication.test.ContentSwitchView;
import com.example.myapplication.unit.ProcessFile;
import com.example.myapplication.unit.ToolsUtil;

import java.io.File;


public class ReadTxtActivity extends Activity {
    TextView textView;//,titleTextView;
    StringBuffer sb;
    SharedPreferences sharedPreferences;
    Intent i;
    UserDataBase userDataBase;
    String filepath;
    ProcessFile processFile;
    int pos=0;
    private String userId;
    Data book,bookData;
    ContentSwitchView view;
    private boolean isLogin=false;
    
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view=new ContentSwitchView(this);
        setContentView(view);
//        textView=findViewById(R.id.readText);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isLogin=sharedPreferences.getBoolean(AppData.IS_LOGIN,false);
        if (isLogin){
            userId=sharedPreferences.getString(AppData.USER_ID,"0");
        }else{
            userId="0";
        }
        Intent i=getIntent();
        Bundle data= i.getExtras();
        book= (Data) data.getSerializable("book");
        filepath=book.getFilePath();
        userDataBase=new UserDataBase(this);
        sb=new StringBuffer();
        if (filepath!=null){
            readText(filepath);
        }else if (i.getAction()!=null){
            String oFilePath=i.getType().equals("text/plain")?i.getData().getPath():null;
            if (oFilePath!=null){
                filepath=oFilePath;
                readText(filepath);
            }
        }

    }


    public void readText(String path){
//        final BookFactory bookFactory=new BookFactory(this,textView);
//        bookFactory.openBook(path);
        view.updateData(path);
    }

    @Override
    public void onBackPressed() {
        File file=new File(filepath);
        bookData = book;
        if (bookData!=null){
        
        }else{
            bookData=new Data();
            bookData.setFilePath(file.getPath());
            bookData.setTitle(file.getName());
            bookData.setFileMd5(ToolsUtil.getFileMD5(file));
            bookData.setUserId(userId);
        }
        
        boolean isInBookself=userDataBase.queryABook(bookData.getFileMd5(),userId);
        if(!isInBookself){
            new AlertDialog.Builder(ReadTxtActivity.this)
                    .setTitle("加入书架")
                    .setMessage("该书未加入书架，是否加入书架？")
                    .setNegativeButton("加入书架", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            userDataBase.insertToBookself(bookData);
                            Intent intent=new Intent("android.intent.action.CAST_BROADCAST");
                            intent.putExtra("code",2);
                            LocalBroadcastManager.getInstance(ReadTxtActivity.this).sendBroadcast(intent);
                            sendBroadcast(intent);
                            Toast.makeText(ReadTxtActivity.this, "已添加！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .create()
                    .show();
        }else{
            userDataBase.modefyReadTime(userId,bookData.getFileMd5());
            super.onBackPressed();
        }
    }
}
