package com.example.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.Preference;
import android.preference.PreferenceManager;

import com.example.myapplication.model.Data;
import com.example.myapplication.bean.AppData;
import com.example.myapplication.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDataBase {
    private SQLiteDatabase sqLiteDatabase;

    private DataBaseHelper dataBaseHelper;
    SharedPreferences sharedPreferences;
    String orderBy=null;

    public UserDataBase(Context context) {
        dataBaseHelper = new DataBaseHelper(context, AppData.FILENAME, null, AppData.VERSION);
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        orderBy=sharedPreferences.getString("sort_style",null);
    }




    //**********************插入***************
    public void addUser(User user) {
        int a=(int)(Math.random()*3);
        ContentValues contentValues = new ContentValues();
        contentValues.put(AppData.USER_ID, user.getUserId());
        contentValues.put(AppData.USER_PASSWORD, user.getPassword());
        contentValues.put(AppData.USER_HEADIMG, a);
        contentValues.put(AppData.USER_EMAIL, user.getEmail());
        contentValues.put(AppData.USER_NICKNAME, user.getNickName());
        sqLiteDatabase.insert(AppData.USER_INFO, null, contentValues);
    }

    public void insertToBookself(Data data){
        ContentValues values=new ContentValues();
        values.put(AppData.USER_ID,data.getUserId());
        values.put(AppData.BOOK_FILE_PATH,data.getFilePath());
        values.put(AppData.BOOK_IMAGE_PATH,0);
        values.put(AppData.BOOK_IMPORT_TIME,System.currentTimeMillis());
        values.put(AppData.BOOK_TITLE,data.getTitle());
        values.put(AppData.BOOK_FILE_MD5,data.getFileMd5());
        sqLiteDatabase.insert(AppData.USER_BOOKSELF,null,values);
    }





    //********************查询*******************


    // 查询用户书架
    public List<Data> queryBookSelf(String id){
        List<Data> bookList= new ArrayList<Data>();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from "+AppData.USER_BOOKSELF+" where "+AppData.USER_ID+"='"+id+"' "+"ORDER BY "+orderBy,null);//.query(AppData.USERBOOKSELF,new String[]{"userId"},null,new String[]{userId},null,null,null);
        if(cursor.getCount()!=0) {
            while (cursor.moveToNext()) {
                Data data = new Data();
                data.setUserId(cursor.getString(cursor.getColumnIndex(AppData.USER_ID)));
                data.setFilePath(cursor.getString(cursor.getColumnIndex(AppData.BOOK_FILE_PATH)));
                data.setImgPath(cursor.getString(cursor.getColumnIndex(AppData.BOOK_IMAGE_PATH)));
                data.setImportTime(cursor.getLong(cursor.getColumnIndex(AppData.BOOK_IMPORT_TIME)));
                data.setTitle(cursor.getString(cursor.getColumnIndex(AppData.BOOK_TITLE)));
                data.setFileMd5(cursor.getString(cursor.getColumnIndex(AppData.BOOK_FILE_MD5)));
                bookList.add(data);
            }
        }
        cursor.close();
        return bookList;
    }



    //查询用户书架中是否存在该本书
    public boolean queryABook(String md5,String userId){
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+AppData.USER_BOOKSELF+" where "+AppData.BOOK_FILE_MD5+"='"+md5+"' AND "+AppData.USER_ID+"='"+userId+"'", null);
        if (cursor.getCount()==0){
            cursor.close();
        }else {
            while (cursor.moveToNext()){
                cursor.close();
                return true;
            }

        }
        return  false;
    }


    //查询用户数据
    public User queryUser(String userId) {
        if (userId!=null||(!userId.equals(""))){
            User user = new User();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from "+AppData.USER_INFO+" where "+AppData.USER_ID+"='"+userId+"'", null);
    
            if (cursor.getCount()==0){
                cursor.close();
                return null;
            }else {
                while (cursor.moveToNext()) {
                    user.setUserId(cursor.getString(cursor.getColumnIndex(AppData.USER_ID)));
                    user.setPassword(cursor.getString(cursor.getColumnIndex(AppData.USER_PASSWORD)));
                    user.setNickName(cursor.getString(cursor.getColumnIndex(AppData.USER_NICKNAME)));
                    user.setUserSignature(cursor.getString(cursor.getColumnIndex(AppData.USER_SIGNATURE)));
                    user.setHeadImg(cursor.getString(cursor.getColumnIndex(AppData.USER_HEADIMG)));
                    user.setAge(cursor.getInt(cursor.getColumnIndex(AppData.USER_AGE)));
                    user.setEmail(cursor.getString(cursor.getColumnIndex(AppData.USER_EMAIL)));
                    user.setCity(cursor.getString(cursor.getColumnIndex(AppData.USER_CITY)));
                    cursor.close();
                    return user;
                }
            }
        }
        return null;
    }







    //**********修改***********

    public void modefyReadTime(String id,String md5){
        ContentValues values=new ContentValues();
        values.put(AppData.USER_ID,id);
        values.put(AppData.BOOK_READ_TIME,System.currentTimeMillis());
        sqLiteDatabase.update(AppData.USER_BOOKSELF,values,AppData.USER_ID+"='"+id+"' AND "+AppData.BOOK_FILE_MD5+"='"+md5+"'",null);
        
    }
    
    

    public boolean modefyUserInfo(User userInfo){
        if(queryUser(userInfo.getUserId())!=null){
            ContentValues values=new ContentValues();
            values.put(AppData.USER_SIGNATURE,userInfo.getUserId());
            values.put(AppData.USER_PASSWORD,userInfo.getPassword());
            values.put(AppData.USER_NICKNAME,userInfo.getNickName());
            values.put(AppData.USER_EMAIL,userInfo.getEmail());
            values.put(AppData.USER_AGE,userInfo.getAge());
            values.put(AppData.USER_CITY,userInfo.getCity());
            values.put(AppData.USER_SIGNATURE,userInfo.getUserSignature());
            sqLiteDatabase.update(AppData.USER_INFO,values,AppData.USER_ID+"="+userInfo.getUserId(),null);
        }
        return false;
    }







    //**************删除****************

    public void deleteBookComment(String userId,String bookMd5){
        sqLiteDatabase.delete(AppData.BOOK_COMMENT,AppData.FIEID_BOOK_COMMENT+"='"+bookMd5+"' AND "+AppData.USER_ID+"='"+userId+"'",null);
    }


    public void deleteBook(Data data){
        sqLiteDatabase.delete(AppData.USER_BOOKSELF,AppData.BOOK_FILE_MD5+"='"+data.getFileMd5()+"' AND "+AppData.USER_ID+"='"+data.getUserId()+"'",null);
    }
}
