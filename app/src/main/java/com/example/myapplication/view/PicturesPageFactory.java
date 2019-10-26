package com.example.myapplication.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.example.myapplication.unit.BitmapUtils;
import com.example.myapplication.unit.ScreenUtils;

public class PicturesPageFactory extends PageFactory {
    private Context context;

    public int style;

    public final static int STYLE_IDS=1;
    public final static int STYLE_URLS=2;
    private int[] pictureIds;

    public PicturesPageFactory(Context context, int[] pictureIds) {
        this.context = context;
        this.pictureIds = pictureIds;
        this.style=STYLE_IDS;
        if (pictureIds.length>0){
            hasData=true;
            pageTotal=pictureIds.length;
        }
    }

    @Override
    public void drawPreviousBitmap(Bitmap bitmap, int pageNum) {
        Canvas canvas=new Canvas(bitmap);
        canvas.drawBitmap(getBitmapByIndex(pageNum-2),0,0,null);
    }

    @Override
    public void drawCurrentBitmap(Bitmap bitmap, int pageNum) {
        Canvas canvas=new Canvas(bitmap);
        canvas.drawBitmap(getBitmapByIndex(pageNum-1),0,0,null);
    }

    @Override
    public void drawNextBitmap(Bitmap bitmap, int pageNum) {
        Canvas canvas=new Canvas(bitmap);
        canvas.drawBitmap(getBitmapByIndex(pageNum),0,0,null);

    }

    @Override
    public Bitmap getBitmapByIndex(int index) {
        if (hasData){
            switch (style){
                case STYLE_IDS:
                    return getBitmapFromIds(index);
            }
        }
        return null;
    }
    private Bitmap getBitmapFromIds(int index){
        return BitmapUtils.drawableToBitmap(context.getResources().getDrawable(pictureIds[index]),
                ScreenUtils.getScreenWidth(context), ScreenUtils.getScreenHeight(context));
//        return context.getResources().getDrawable(pictureIds[index]);
    }
}
