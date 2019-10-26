package com.example.myapplication.view;

import android.graphics.Bitmap;
import android.view.View;

public abstract class PageFactory{
    public boolean hasData=false;//是否含有数据
    public int pageTotal=0;//页面总数

    public PageFactory() {

    }
    public abstract void drawPreviousBitmap(Bitmap bitmap,int pageNum);
    public abstract void drawCurrentBitmap(Bitmap bitmap,int pageNum);
    public abstract void drawNextBitmap(Bitmap bitmap,int pageNum);
    public abstract Bitmap getBitmapByIndex(int index);//通过索引在集合中获取相应位置

}
