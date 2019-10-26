package com.example.myapplication.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myapplication.unit.ViewUtils;

public class CoverPageView extends View {
    private float xDown;//初始触摸的x坐标；
    private float scrollPagerLeft;//滑动页左边界

    private MyPoint touchPoint;
    private int touchStyle;
    int defaultWidth;
    int defaultHeight;
    int viewWidth;
    int viewHeight;
    int pageNum;
    private int pageState;
    private int scrollTime;
    float testDown;

    Scroller scroller;
    PageFactory pageFactory;
    Bitmap currentPage;
    Bitmap nextPage;
    Bitmap priviousPage;
    public static final int TOUCH_MIDDLE=0;
    public static final int TOUCH_LEFT=1;
    public static final int TOUCH_RIGHT=2;
    public static final int PAGE_STAY=0;
    public static final int PAGE_NEXT=1;
    public static final int PAGE_PRIVAOUS=2;

    private void init(Context context){
        defaultWidth=600;
        defaultHeight=1000;
        pageNum=1;
        scrollPagerLeft=0;
        pageState=PAGE_STAY;
        scrollTime=1000;
        touchStyle=TOUCH_RIGHT;
        touchPoint=new MyPoint(-1,-1);
        scroller=new Scroller(context,new LinearInterpolator());
    }


    public CoverPageView(Context context) {
        super(context);
        init(context);
    }

    public CoverPageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setPageFactory(final PageFactory factory){
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                if(factory.hasData){
                    pageFactory=factory;
                    pageFactory.drawCurrentBitmap(currentPage,pageNum);
                    postInvalidate();
                }
                return true;
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (pageFactory!=null){
            if (touchPoint.x==-1&&touchPoint.y==-1){
                drawCurrentPage(canvas);
                pageState=PAGE_STAY;
            }else{
                if (touchStyle==TOUCH_RIGHT){
                    drawCurrentPage(canvas);
                    drawPriviousPage(canvas);
                }else{
                    drawNextPage(canvas);
                    drawCurrentPage(canvas);
                }
            }
        }
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=ViewUtils.measureSize(defaultWidth,widthMeasureSpec);
        int height= ViewUtils.measureSize(defaultHeight,heightMeasureSpec);
        setMeasuredDimension(width,height);
        viewWidth=width;
        viewHeight=height;
        priviousPage=Bitmap.createBitmap(viewWidth,viewHeight,Bitmap.Config.RGB_565);
        currentPage=Bitmap.createBitmap(viewWidth,viewHeight,Bitmap.Config.RGB_565);
        nextPage=Bitmap.createBitmap(viewWidth,viewHeight,Bitmap.Config.RGB_565);

    }
    private void drawPriviousPage(Canvas canvas){
        canvas.drawBitmap(priviousPage,scrollPagerLeft,0,null);
    }
    private void drawCurrentPage(Canvas canvas){
        if (touchStyle==TOUCH_RIGHT){
            canvas.drawBitmap(currentPage,0,0,null);
        }else if (touchStyle==TOUCH_LEFT){
            canvas.drawBitmap(currentPage,scrollPagerLeft,0,null);
        }
    }
    private void drawNextPage(Canvas canvas){
        canvas.drawBitmap(nextPage,0,0,null);
    }
    private void scrollPage(float x,float y){
        touchPoint.x=x;
        touchPoint.y=y;

        if (touchStyle==TOUCH_RIGHT){
            scrollPagerLeft=touchPoint.x-xDown;
        }else if(touchStyle==TOUCH_LEFT){
            scrollPagerLeft=touchPoint.x-xDown-viewWidth;

        }
        if (scrollPagerLeft>0){
            scrollPagerLeft=0;
        }
        postInvalidate();

    }


    public void autoScrollToPriviousPage(){
        pageState=PAGE_PRIVAOUS;

        int dx,dy;
        dx= (int) -scrollPagerLeft;
        dy= (int) touchPoint.y;
        int time= (int) ((1+scrollPagerLeft/viewWidth)*scrollTime);
        scroller.startScroll((int)(viewWidth+scrollPagerLeft), (int) touchPoint.y,dx,dy,time);
    }
    public void autoScrollToNextPage(){
        pageState=PAGE_NEXT;

        int dx,dy;
        dx= (int) -(viewWidth+scrollPagerLeft);
        dy= (int) touchPoint.y;
        int time= (int) ((-scrollPagerLeft/viewWidth)*scrollTime);
        scroller.startScroll((int)(viewWidth+scrollPagerLeft), (int) touchPoint.y,dx,dy,time);
    }
    private void startCancalAnim(){
        int dx,dy;
        dx= (int) (viewWidth-1-touchPoint.x);
        dy= (int) touchPoint.y;
        scroller.startScroll((int)touchPoint.x, (int) touchPoint.y,dx,dy,scrollTime);
    }
private void resetView(){
        scrollPagerLeft=0;
        touchPoint.x=-1;
        touchPoint.y=-1;
}

    @Override
    public void computeScroll() {
//        super.computeScroll();
        if(scroller.computeScrollOffset()){
            float x=scroller.getCurrX();
            float y=scroller.getCurrY();
            scrollPagerLeft=0-(viewWidth-x);
            if (scroller.getFinalX()==x&&scroller.getFinalY()==y){
                if (touchStyle==TOUCH_RIGHT){
                    pageNum++;
                }else if (touchStyle==TOUCH_LEFT){
                    pageNum--;
                }
                resetView();
            }
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
         super.onTouchEvent(event);
         float x=event.getX();
         float y=event.getY();
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    xDown=x;
//                    if (x<=viewWidth/3){
//                        touchStyle=TOUCH_LEFT;
//                    if (pageNum<pageFactory.pageTotal){
//                        pageNum++;
//                        pageFactory.drawPreviousBitmap(priviousPage,pageNum);
//                        pageFactory.drawCurrentBitmap(currentPage,pageNum);
//                        pageNum--;
//                    }
//                    if (pageNum>1){
//                            pageNum--;
//                            pageFactory.drawCurrentBitmap(currentPage,pageNum);
//                            pageFactory.drawNextBitmap(nextPage,pageNum);
//
//                        }
//                    }else if (x>viewWidth*2/3){
//                        touchStyle=TOUCH_RIGHT;
//                    }else if (x>viewWidth/3&&x<viewWidth*2/3){
//                        touchStyle=TOUCH_MIDDLE;
//                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    testDown=event.getX();
                    if (x<=viewWidth/3){
                        touchStyle=TOUCH_LEFT;
                        if (pageNum<pageFactory.pageTotal){
                            pageNum++;
                            pageFactory.drawPreviousBitmap(priviousPage,pageNum);
                            pageFactory.drawCurrentBitmap(currentPage,pageNum);
                            pageNum--;
                        }
                        if (pageNum>1){
                            pageNum--;
                            pageFactory.drawCurrentBitmap(currentPage,pageNum);
                            pageFactory.drawNextBitmap(nextPage,pageNum);
            
                        }
                    }else if (x>viewWidth*2/3){
                        touchStyle=TOUCH_RIGHT;
                    }else if (x>viewWidth/3&&x<viewWidth*2/3){
                        touchStyle=TOUCH_MIDDLE;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    autoScroll();
                    break;
            }
        

         return true;
    }
    public void autoScroll(){
        switch (touchStyle){
            case TOUCH_LEFT:
                if (pageNum>1){
                    autoScrollToPriviousPage();
                }
                break;
            case TOUCH_RIGHT:
                if (pageNum<pageFactory.pageTotal){
                    autoScrollToNextPage();
                }
        }
    }
}
