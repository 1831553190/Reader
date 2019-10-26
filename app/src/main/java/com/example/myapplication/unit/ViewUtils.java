package com.example.myapplication.unit;

import android.view.View;

public class ViewUtils {
    public static  int measureSize(int defaultSize,int measureSpec){
        int specMode= View.MeasureSpec.getMode(measureSpec);
        int specSize= View.MeasureSpec.getSize(measureSpec);

        if (specMode==View.MeasureSpec.EXACTLY){
            return specSize;
        }else if(specMode==View.MeasureSpec.AT_MOST){
            return Math.min(defaultSize,specSize);
        }
        return defaultSize;
    }
}
