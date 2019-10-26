package com.example.myapplication.behavior;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.unit.ViewUtils;

public class EditTextBehavior extends CoordinatorLayout.Behavior<View> {
    // 界面整体向上滑动，达到列表可滑动的临界点
    private boolean upReach;
    // 列表向上滑动后，再向下滑动，达到界面整体可滑动的临界点
    private boolean downReach;
    // 列表上一个全部可见的item位置
    private int lastPosition = -1;
    boolean isHidden=false;
//    Scroller scroller;
    boolean tag=false;
    static float evy = 0;

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private static final Interpolator ACCELERATE = new AccelerateInterpolator();
    private static final Interpolator DECELERATE = new DecelerateInterpolator();

    float height=100;
    private boolean isAnimate;
    private float viewY;
    
    

    public EditTextBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    


    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downReach = false;
                upReach = false;
                evy=ev.getY();
                break;
                case MotionEvent.ACTION_UP:
                    System.out.println(child.getY());
                    if (child.getY()<height/2){
//                    if (evy-ev.getY()>=0){
                        hide(child);
                    }else{
                        show(child);
                    }
                    return false;
                    
        }
        return false;
    }

    private void show(final View view){
        ViewPropertyAnimator animator = view.animate().translationY(0).setInterpolator(ACCELERATE).setDuration(100);
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isAnimate=true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isAnimate=false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                hide(view);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animator.start();

    }
    private void hide(final View view){
        ViewPropertyAnimator animator = view.animate().translationY(height).setInterpolator(DECELERATE).setDuration(100);
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isAnimate=true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
//                view.setVisibility(View.GONE);
                isAnimate=false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                show(view);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animator.start();
    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        viewY=coordinatorLayout.getHeight()-child.getY();
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }
    



    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);

        if (target instanceof RecyclerView) {
            RecyclerView list = (RecyclerView) target;
            // 列表第一个全部可见Item的位置
            int pos = ((LinearLayoutManager) list.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (pos == 0 && pos < lastPosition) {
                downReach = true;
            }
            // 整体可以滑动，否则RecyclerView消费滑动事件
            if (canScroll(child, dy) && pos == 0) {
                float finalY = child.getTranslationY() - dy;
                if (finalY < -child.getHeight()) {
                    finalY = -child.getHeight();
                    upReach = true;
                } else if (finalY > 0) {
                    finalY = 0;
                }
                child.setTranslationY(finalY);
                // 让CoordinatorLayout消费滑动事件
                consumed[1] = dy;
            }
            lastPosition = pos;
        }
    }

    private boolean canScroll(View child, float scrollY) {
        if (scrollY > 0 && child.getTranslationY() == -child.getHeight() && !upReach) {
            return false;
        }
        if (downReach) {
            return false;
        }
        return true;
    }

    
    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull View child, int layoutDirection) {
        if(!isHidden){
            float y = child.getHeight() + child.getTranslationY()+child.getMeasuredHeight();
            y=-y;
            height=y;
            child.setY(y);
            isHidden=true;
        }
        return super.onLayoutChild(parent, child, layoutDirection);
    }
}


