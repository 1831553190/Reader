package com.example.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.unit.ProcessFile;
import com.example.myapplication.unit.ViewUtils;

public class MyContentView extends View {


    Paint paint;
    String content="加载中";
    int viewWidth;
    int viewHeight;
    int defaultWidth;
    int defaultHeight;
    ProcessFile processFile;
    public MyContentView(Context context) {
        super(context);
        init();

    }

    public MyContentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        paint=new Paint();
        defaultWidth=600;
        defaultHeight=800;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x=event.getX();
        float y=event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                if (x<=viewWidth/3){
                    content= processFile.getPrevious();
                }else if (x>viewWidth*2/3){
                    content= processFile.getNext();
                }
        }

        return super.onTouchEvent(event);
    }

    private void test(View view){
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawText(content,0,0,paint);
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=ViewUtils.measureSize(defaultWidth,widthMeasureSpec);
        int height= ViewUtils.measureSize(defaultHeight,heightMeasureSpec);
        setMeasuredDimension(width,height);
        viewWidth=width;
        viewHeight=height;
    }
}
