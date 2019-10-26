package com.example.myapplication.test;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.bookopen.BookFactory;
import com.example.myapplication.unit.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class ContentSwitchView extends FrameLayout {
    float startX=-1;
    TestFameView testFameView,nextPage,prePage,changeView;
    List<TestFameView> contentList;
	private int animDuration=300;
    private boolean isMoving=false;
    private float scrollX;
    BookFactory bookFactory;
    private static final int PREPAGE=1;
    private static final int NONE=-1;
    private static final int PREANDNEXT=0;
    private static final int NEXTPAGE=2;
    static int state;
    static String preContent;
    static String currentContent;
    static String nextContent;
    private boolean end=false;
    static boolean turnBack=false;
    
    
    public ContentSwitchView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ContentSwitchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void updateData(String path){
        bookFactory.openBook(path);
        prePage=new TestFameView(getContext());

        preContent=bookFactory.prePage();
        currentContent=bookFactory.nextPage();
        nextContent=bookFactory.nextContent();

        prePage.updateData(preContent);
        testFameView.updateData(currentContent);
        nextPage=new TestFameView(getContext());

        nextPage.updateData(nextContent);
        contentList.add(prePage);
        contentList.add(testFameView);
        contentList.add(nextPage);
        addView(contentList.get(2),0);
        addView(contentList.get(1));
//        System.out.println("preContent:\n\n\n"+preContent+
//                "\n\n\ncurrentContent:\n"+currentContent+
//                "\n\n\nnextContent:\n"+nextContent);
    }


    private void init(Context context){
        testFameView=new TestFameView(context);
        scrollX= ScreenUtils.dp2px(getContext(),30f);
        bookFactory=new BookFactory(context,testFameView.getTextContent());
        contentList=new ArrayList<>();
    }
//
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        if (contentList.size() > 0) {
////            if (state == NONE && contentList.size() >= 1) {
////                contentList.get(0).layout(0, top, getWidth(), bottom);
////            } else if (state == PREANDNEXT && contentList.size() >= 3) {
//
////            } else if (state == PREPAGE && contentList.size() >= 2) {
////                contentList.get(0).layout(-getWidth(), top, 0, bottom);
////                contentList.get(1).layout(0, top, getWidth(), bottom);
////            } else if (contentList.size() >= 2) {
////                contentList.get(0).layout(0, top, getWidth(), bottom);
////                contentList.get(1).layout(0, top, getWidth(), bottom);
////            }
////        } else {
////            super.onLayout(changed, left, top, right, bottom);
////        }
//    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (!isMoving){
            int durWidth = 0;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
//                if (startX == -1)
//                    startX = event.getX();
                    //处理分辨率过大，移动冗余值,当横向滑动值超过冗余值则开始滑动
                    int durX = (int) (event.getX() - startX);
                    if (durX > durWidth) {
                        durX = durX - durWidth;
                    } else if (durX < -durWidth) {
                        durX = durX + durWidth;
                    } else {
                        durX = 0;
                    }
            
                    if (durX > 0) {
                        int tempX = durX - getWidth();
                        if (tempX < -getWidth())
                            tempX = -getWidth();
                        else if (tempX > 0)
                            tempX = 0;
//                        contentList.get(0).layout(tempX, contentList.get(0).getTop(), tempX + getWidth(), contentList.get(0).getBottom());
                    } else if (durX < 0) {
                        int tempX = durX;
                        if (tempX > 0)
                            tempX = 0;
                        else if (tempX < -getWidth())
                            tempX = -getWidth();
                        if (!end)
                        contentList.get(1).layout(tempX, contentList.get(1).getTop(), tempX + getWidth(), contentList.get(1).getBottom());
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
//                moveSuccessAnim(testFameView, -getWidth());
//	            if (startX == -1)
//		            startX = event.getX();
                    if (event.getX() - startX > durWidth) {
                        state = PREPAGE;
                        //注意冗余值
                        if (event.getX() - startX + durWidth > scrollX) {
                            //向前翻页成功
                            moveSuccessAnim(contentList.get(1), 0);
                        } else {
                            moveFailAnim(contentList.get(1), -getWidth());
                        }
//		            }
//	            else {
//			            //没有上一页
//			            noPre();
//		            }
                    } else if (event.getX() - startX < -durWidth) {
                        state=NEXTPAGE;
//			            int tempIndex = (state == PREANDNEXT ? 1 : 0);
                        //注意冗余值
                        if (startX - event.getX() - durWidth > scrollX) {
                            //向后翻页成功
                            moveSuccessAnim(contentList.get(1), -getWidth());
                        } else {
                            moveFailAnim(contentList.get(1), 0);
                        }
                    }
//		            else {
//			            //没有下一页
//			            noNext();
//		            }


//	            }

//	            else {
//		            //点击事件
//		            if (readBookControl.getCanClickTurn() && event.getX() <= getWidth() / 3) {
//			            //点击向前翻页
//			            if (state == PREANDNEXT || state == ONLYPRE) {
//				            initMoveSuccessAnim(viewContents.get(0), 0);
//			            } else {
//				            noPre();
//			            }
//		            } else if (readBookControl.getCanClickTurn() && event.getX() >= getWidth() / 3 * 2) {
//			            //点击向后翻页
//			            if (state == PREANDNEXT || state == ONLYNEXT) {
//				            int tempIndex = (state == PREANDNEXT ? 1 : 0);
//				            initMoveSuccessAnim(viewContents.get(tempIndex), -getWidth());
//			            } else {
//				            noNext();
//			            }
//		            } else {
//			            //点击中间部位
//			            if (loadDataListener != null)
//				            loadDataListener.showMenu();
//		            }
//	            }
//	            startX = -1;

//        }
            }
        }
        return true;
    }
    
    
    private void moveSuccessAnim(final View view , int orderX){
	    if(null!=view){
	        long temp = Math.abs(view.getLeft() - orderX) / (getWidth() / animDuration);
	        ValueAnimator tempAnim=ValueAnimator.ofInt(view.getLeft(),orderX).setDuration(temp);
	        tempAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    view.layout(value, view.getTop(), value + getWidth(), view.getBottom());
                }
            });
	        tempAnim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isMoving = true;
                }
        
                @Override
                public void onAnimationEnd(Animator animation) {
                    isMoving = false;
                    if (state==PREPAGE){

                        currentContent=bookFactory.prePage();
                        preContent=bookFactory.preContent();
                        nextContent=bookFactory.nextContent();
                        if(currentContent.equals("没有上一页")){
                            Toast.makeText(getContext(), "没有上一页", Toast.LENGTH_SHORT).show();
                        }else{
                            contentList.get(1).updateData(currentContent);
                            contentList.get(2).updateData(nextContent);
                        }
//                        System.out.println("currentPage:\n"+currentContent);
                        end=false;
//                        currentContent=preContent;
//                        preContent=bookFactory.prePage();
//                        changeView=contentList.get(0);
//                        removeView(view);
//                        contentList.remove(1);
//                        changeView.updateData(preContent);
//                        contentList.add(0,changeView);
//                        addView(contentList.get(0));
//                        contentList.get(0).layout(-getWidth(), view.getTop(), 0, view.getBottom());
                    }else{
                        currentContent=bookFactory.nextPage();
                        preContent=bookFactory.preContent();
                        nextContent=bookFactory.nextContent();
                        if(currentContent.equals("没有下一页")){
                            Toast.makeText(getContext(), "没有下一页", Toast.LENGTH_SHORT).show();
                            end=true;
                        }else{
                            changeView=contentList.get(1);
                            removeView(view);
                            contentList.remove(1);
                            changeView.updateData(nextContent);
                            contentList.add(changeView);
                            addView(contentList.get(2),0);
                        }
                        
                    }

//
//                    System.out.println("preContent:\n\n\n"+preContent+
//                            "\n\n\ncurrentContent:\n\n\n"+currentContent+
//                            "\n\n\nnextContent:\n"+nextContent);
//                    if (contentList.size()>=3){
////                        contentList.get(0).layout(-getWidth(), view.getTop(), 0, view.getBottom());
////                        contentList.get(1).layout(0, view.getTop(), getWidth(), view.getBottom());
////                        contentList.get(2).layout(0, view.getTop(), getWidth(), view.getBottom());
//
//                    }
                  
                }
        
                @Override
                public void onAnimationCancel(Animator animation) {
            
                }
        
                @Override
                public void onAnimationRepeat(Animator animation) {
            
                }
            });
	        tempAnim.start();
        }
    }
	
//	private void initMoveSuccessAnim(final View view, final int orderX) {
//		if (null != view) {
//			long temp = Math.abs(view.getLeft() - orderX) / (getWidth() / animDuration);
//			ValueAnimator tempAnim = ValueAnimator.ofInt(view.getLeft(), orderX).setDuration(temp);
//			tempAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//				@Override
//				public void onAnimationUpdate(ValueAnimator animation) {
//					if (null != view) {
//						int value = (int) animation.getAnimatedValue();
//						view.layout(value, view.getTop(), value + getWidth(), view.getBottom());
//					}
//				}
//			});
//			tempAnim.addListener(new Animator.AnimatorListener() {
//				@Override
//				public void onAnimationStart(Animator animation) {
//					isMoving = true;
//				}
//
//				@Override
//				public void onAnimationEnd(Animator animation) {
//					isMoving = false;
//					if (orderX == 0) {
//						//翻向前一页
//						durPageView = viewContents.get(0);
//						if (state == PREANDNEXT) {
//							ContentSwitchView.this.removeView(viewContents.get(viewContents.size() - 1));
//							viewContents.remove(viewContents.size() - 1);
//						}
//						state = ONLYNEXT;
//						if (durPageView.getDurChapterIndex() - 1 >= 0 || durPageView.getDurPageIndex() - 1 >= 0) {
//							addPrePage(durPageView.getDurChapterIndex(), durPageView.getChapterAll(), durPageView.getDurPageIndex(), durPageView.getPageAll());
//							if (state == NONE)
//								state = ONLYPRE;
//							else state = PREANDNEXT;
//						}
//					} else {
//						//翻向后一夜
//						if (state == ONLYNEXT) {
//							durPageView = viewContents.get(1);
//						} else {
//							durPageView = viewContents.get(2);
//							ContentSwitchView.this.removeView(viewContents.get(0));
//							viewContents.remove(0);
//						}
//						state = ONLYPRE;
//						if (durPageView.getDurChapterIndex() + 1 <= durPageView.getChapterAll() - 1 || durPageView.getDurPageIndex() + 1 <= durPageView.getPageAll() - 1) {
//							addNextPage(durPageView.getDurChapterIndex(), durPageView.getChapterAll(), durPageView.getDurPageIndex(), durPageView.getPageAll());
//							if (state == NONE)
//								state = ONLYNEXT;
//							else state = PREANDNEXT;
//						}
//					}
//					if (loadDataListener != null)
//						loadDataListener.updateProgress(durPageView.getDurChapterIndex(), durPageView.getDurPageIndex());
//				}
//
//				@Override
//				public void onAnimationCancel(Animator animation) {
//
//				}
//
//				@Override
//				public void onAnimationRepeat(Animator animation) {
//
//				}
//			});
//			tempAnim.start();
//		}
//	}
//
	private void moveFailAnim(final View view, int orderX) {
		if (null != view) {
			long temp = Math.abs(view.getLeft() - orderX) / (getWidth() / animDuration);
			ValueAnimator tempAnim = ValueAnimator.ofInt(view.getLeft(), orderX).setDuration(temp);
			tempAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					if (null != view) {
						int value = (int) animation.getAnimatedValue();
						view.layout(value, view.getTop(), value + getWidth(), view.getBottom());
					}
				}
			});
			tempAnim.start();
		}
	}
    
}
