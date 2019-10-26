package com.example.myapplication.unit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

public class BitmapUtils {
    public static Bitmap drawableToBitmap(Drawable drawable){
       Bitmap bitmap=Bitmap.createBitmap(drawable.getIntrinsicWidth(),
               drawable.getIntrinsicHeight(),
               drawable.getOpacity()!= PixelFormat.OPAQUE?Bitmap.Config.ARGB_8888
                       :Bitmap.Config.RGB_565);
        Canvas canvas=new Canvas(bitmap);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    public static Bitmap drawableToBitmap(Drawable drawable,int newW,int newH){
       Bitmap bitmap=Bitmap.createBitmap(drawable.getIntrinsicWidth(),
               drawable.getIntrinsicHeight(),
               drawable.getOpacity()!= PixelFormat.OPAQUE?Bitmap.Config.ARGB_8888
                       :Bitmap.Config.RGB_565);
        Canvas canvas=new Canvas(bitmap);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return changeBitmapSize(bitmap,newW,newH);
    }
    public static Bitmap changeBitmapSize(Bitmap bitmap,int newW,int newH){
        int oldW=bitmap.getWidth();
        int oldH=bitmap.getHeight();
        float scaleWidth=((float)newW)/oldW;
        float scaleHeight=((float)newH)/oldH;
        Matrix matrix=new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        bitmap=Bitmap.createBitmap(bitmap,0,0,oldW,oldH,matrix,true);
        return bitmap;
    }
}
