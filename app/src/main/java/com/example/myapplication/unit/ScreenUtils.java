package com.example.myapplication.unit;

import android.content.Context;
import android.util.TypedValue;
import android.view.WindowManager;

public class ScreenUtils {
    
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
    
    public static int sp2px(Context context, float spVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }
    
    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }
    
    
    
    public static int  getScreenWidth(Context context){
        int width;
        WindowManager windowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width=windowManager.getDefaultDisplay().getWidth();
        return width;
    }
    public static int  getScreenHeight(Context context){
        int height;
        WindowManager windowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        height=windowManager.getDefaultDisplay().getWidth();
        return height;
    }
}
