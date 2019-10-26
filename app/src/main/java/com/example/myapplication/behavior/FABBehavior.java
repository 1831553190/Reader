package com.example.myapplication.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FABBehavior extends FloatingActionButton.Behavior {
	float down=0;
	
	
	
	public FABBehavior(Context context, AttributeSet attrs) {
		super();
	}
	
//	@Override
//	public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull FloatingActionButton child, @NonNull View dependency) {
//
//		if (dependency instanceof RecyclerView){
//			return true;
//		}
//		return false;
//	}
	
//	@Override
//	public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
//		return true;//axes==ViewCompat.SCROLL_AXIS_VERTICAL||super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
//	}
	
	
	//
//	@Override
//	public boolean onTouchEvent(@NonNull CoordinatorLayout parent, @NonNull FloatingActionButton child, @NonNull MotionEvent ev) {
//		super.onTouchEvent(parent, child, ev);
//		switch (ev.getAction()){
//			case MotionEvent.ACTION_DOWN:
//				down=ev.getY();
//				break;
//			case MotionEvent.ACTION_UP:
//				if (ev.getY()-down>0){
//					child.show();
//				}else{
//					child.hide();
//				}
//				break;
//		}
//		return true;
//	}
	
	@Override
	public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
		super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
		if (dyConsumed>0&&child.getVisibility()==View.VISIBLE){
			child.hide();
		}else if(dyConsumed<0&&child.getVisibility()!=View.VISIBLE){
			child.show();
		}
		
		
//		if (dyConsumed>0){
//			child.animate().translationY(child.getHeight()+((CoordinatorLayout.LayoutParams)child.getLayoutParams()).bottomMargin).setInterpolator(new AccelerateInterpolator(3));
//			ViewCompat.animate(child).scaleX(0f).scaleY(0f).start();
//		}else {
//			child.animate().translationY(0).setInterpolator(new DecelerateInterpolator(3));
//			ViewCompat.animate(child).scaleX(1f).scaleY(1f).start();
//		}
	}
}
