package com.concisesoftware.myanimationsapplication.animapp.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.concisesoftware.myanimationsapplication.animapp.R;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by concisecomp on 2014-05-13.
 */
public class MyYTLikeLayout extends RelativeLayout {
    public MyYTLikeLayout(Context context) {
        super(context);
        init(context);
    }

    public MyYTLikeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyYTLikeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.my_ytlike_layout, this, false);
        this.addView(rl);
        mTopView = (FrameLayout) rl.findViewById(R.id.yt_top);
        mBottomView = (FrameLayout) rl.findViewById(R.id.yt_bottom);

        mTopView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getState()==State.SMALL) {
                    setState(State.BIG, false);
                }
            }
        });
        mBottomView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //just to capture clicks inside this view
            }
        });
        final int mMinDistanceBeforeScroll = ViewConfiguration.get(context).getScaledPagingTouchSlop();
        mTopView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    isStartedInTopView = true;
                    mGestureDetector.onTouchEvent(event);
                }
                return false;
//                if (event.getActionMasked() == MotionEvent.ACTION_UP && mIsScrolling) {
//                    mIsScrolling = false;
//                    animateToNearestState();
//                }
////                Log.d("YTlike", "event for gesture detector: "+event.toString());
//                // Y property of event is relative to View
//                return mGestureDetector.onTouchEvent(event);
            }
        });
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent downEvent, MotionEvent currentEvent, float distanceFromLastEventX, float distanceFromLastEventY) {
                float currentState = getState();

                if (currentState >= 0 && currentState <= 1) {
                    //between OUT and SMALL
                    if (!mIsScrolling && Math.abs(downEvent.getX() - currentEvent.getX()) > mMinDistanceBeforeScroll) {
                        mIsScrolling = true;
                    }
                    //TODO
                }
                //TODO for only currentState == 1
                if (currentState >= 1 && currentState <= 2) {
                    //between SMALL and BIG
                    if (!mIsScrolling && Math.abs(downEvent.getY() - currentEvent.getY()) > mMinDistanceBeforeScroll) {
                        mIsScrolling = true;
                    }

                    if (mIsScrolling) {

                        //TODO set state depending on currentEvent.Y and touch location inside top view
                        float touchY = currentEvent.getY();
//                      float touchYRelativeToTopView = touchY - mTopView.getY();
//                      float touchYRelativeRecommended = topViewNormalHeight*mTopView.getScaleY()*mStartLocationInsideTopViewY;
//                      float touchEndYRelative = topViewNormalHeight*smallTopViewScale*mStartLocationInsideTopViewY;
                        float touchEndY = smallTopViewY;// +  touchEndYRelative;

                        float endValue = 2 - (touchY / touchEndY);
                        if(endValue>2) { endValue = 2; }
                        if(endValue<1) { endValue = 1; }
                        setState(endValue, true);
                    }
                }

                return mIsScrolling;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float currentState = getState();
                Log.d("YTlike ee ", "onFling - state:" + currentState + " vX:" + velocityX + " vY:" + velocityY);
                if (currentState == (int) currentState) {
                    return false;
                }

                if (currentState > 0 && currentState < 1) {
                    //only fling left/right
                    setState(velocityX < 0 ? 1 : 0, false);
                    return true;
                } else if (currentState > 1 && currentState < 2) {
                    //fling up/down
                    setState(velocityY < 0 ? 2 : 1, false);
                    return true;
                }
                return false;
            }
        });
        mAnimator = ObjectAnimator.ofFloat(this, "state", State.BIG);
//        mAnimator.setupEndValues();
//        mAnimator.setupStartValues();
//        mAnimator.addListener();

        this.post(new Runnable() {
            @Override
            public void run() {
                topViewNormalWidth = mTopView.getWidth();
//                topViewNormalHeight = mTopView.getHeight();
                topViewNormalHeight = topViewNormalWidth*9/16;
                int smallTopViewWidth = (int)(topViewNormalWidth*smallTopViewScale);
                int smallTopViewHeight = (int)(topViewNormalHeight*smallTopViewScale);
                smallTopViewX = rl.getWidth() - smallTopViewWidth - smallTopViewMargins;
                smallTopViewY = rl.getHeight() - smallTopViewHeight - smallTopViewMargins;
                setState(State.BIG);
            }
        });
    }

    //TODO onStateChangeListener

    protected int smallTopViewMargins = 20;

    protected int animationDurationPerState = 300;
    protected float smallTopViewScale = 0.5f;

    private int smallTopViewX;
    private int smallTopViewY;
    private int topViewNormalHeight;
    private int topViewNormalWidth;

    private ObjectAnimator mAnimator;
    private GestureDetector mGestureDetector;

    private float mState = State.OUT;
    /**
     * Possible states of {@link com.concisesoftware.myanimationsapplication.animapp.widgets.MyYTLikeLayout}
     */
    public interface State {
        /**
         * Big - on whole screen
         */
        int BIG = 2;
        /**
         * visible only top part, on the bottom of screen
         */
        int SMALL = 1;
        /**
         * invisible
         */
        int OUT = 0;
    }

    private FrameLayout mTopView;
    private FrameLayout mBottomView;

    /**
     * Set the vie to show in top part of layout
     * @param topView
     */
    public void setTopView(View topView) {
        mTopView.removeAllViews();
        mTopView.addView(topView);
    }
    /**
     * Set the vie to show in bottom part of layout
     * @param bottomView
     */
    public void setBottomView(View bottomView) {
        mBottomView.removeAllViews();
        mBottomView.addView(bottomView);
    }

    /**
     * Restores full screen state
     */
    public void backToFullScreen() {
        float currentState = getState();
        if(currentState == 1) {
            setState(State.BIG, false);
        } else if(currentState == 0) {
            setState(State.BIG, true);
        }
    }

    public void setTopViewNormalHeight(int height) {
        topViewNormalHeight = height;
    }

    /**
     * changes the layout appearance
     * @param state {@link com.concisesoftware.myanimationsapplication.animapp.widgets.MyYTLikeLayout.State}
     */
    private void setState(float state, boolean isFromUser) {
//        Log.d("YTlike", "setState: "+state +" from user: "+isFromUser);
        if(isFromUser) {
            mAnimator.cancel();
        }
        if(state > State.BIG) { state = State.BIG; }
        if(state < State.OUT) { state = State.OUT; }

        if(state >=0 && state <=1) {
            mBottomView.setAlpha(0);
            mTopView.setAlpha(state);

//            mTopView.setX(smallTopViewX*state);
//            mTopView.setY(smallTopViewY);
            mBottomView.setY(smallTopViewY+topViewNormalHeight);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    (int) (topViewNormalWidth*smallTopViewScale),
                    (int) (topViewNormalHeight*smallTopViewScale)
            );
            lp.setMargins(smallTopViewX, smallTopViewY, 0, 0);
            Log.d("YTlike setState", "state: "+state+"; new layoutParams: [width:"+lp.width+"][height:"+lp.height
                    +"][marginTop:"+lp.topMargin+"][marginLeft:"+lp.leftMargin+"]");
            mTopView.setLayoutParams(lp);
            mTopView.postInvalidate();
        } else if(state>1 && state<=2) {
            mBottomView.setAlpha(state-1);
            mTopView.setAlpha(1);

            int newTopViewY = (int)(smallTopViewY*(2.0f - state));

//            mTopView.setX(smallTopViewX*(2 - state));
//            mTopView.setY(newTopViewY);

            mBottomView.setY(newTopViewY+topViewNormalHeight);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    (int) (topViewNormalWidth*((state-1)*(1-smallTopViewScale)+smallTopViewScale)),
                    (int) (topViewNormalHeight*((state-1)*(1-smallTopViewScale)+smallTopViewScale))
            );
            lp.setMargins((int) (smallTopViewX*(2 - state)), newTopViewY, 0, 0);
            Log.d("YTlike setState", "state: "+state+"; new layoutParams: [width:"+lp.width+"][height:"+lp.height
                    +"][marginTop:"+lp.topMargin+"][marginLeft:"+lp.leftMargin+"]");
            mTopView.setLayoutParams(lp);
            mTopView.postInvalidate();
        }

        mState = state;
    }

    public void setState(int state, boolean immediately) {
        if(immediately){
            mAnimator.cancel();
            setState((float)state, false);
        } else {
            float diff = Math.abs(state - mState);
            int duration = (int)(diff * animationDurationPerState);
            mAnimator.cancel();
            mAnimator.setFloatValues(mState, state);
            mAnimator.setDuration(duration).start();
        }
    }

    /**
     * method for animator to run
     * @param state
     */
    protected void setState(float state) {
        setState(state, false);
    }

    private void animateToNearestState() {
        float current = getState();
        int scrollTo = Math.round(current);
        setState(scrollTo, false);
    }

    public float getState() {
        return mState;
    }

    //---------- touch events and gestures ----------//

    private long mLastEventTime;
    private boolean mLastEventResult;

    /**
     * is user dragging a view
     */
    private boolean mIsScrolling;

    private boolean isStartedInTopView;
//
//    protected int mMinDistanceBeforeScroll;
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        return super.dispatchTouchEvent(ev);
//        //TODO true if inside one of views (so all view under can get touch events if they are visible)
//        //TODO protected onTouchSmallTopView (witch listener ?)
//        return true;
//    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
//        if(event.getActionMasked() == MotionEvent.ACTION_DOWN) {
//            Rect topViewRect = new Rect();
//            mTopView.getHitRect(topViewRect);
//            if(topViewRect.contains((int)event.getX(), (int)event.getY())) {
////                mStartLocationInsideTopViewX = (event.getX() - mTopView.getLeft())/mTopView.getWidth();
////                mStartLocationInsideTopViewY = (event.getY() - mTopView.getTop())/mTopView.getHeight();
//
//                isStartedInTopView = true;
//            }
//        }

        if(isStartedInTopView) {
            return processTouch(event);
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        processTouch(event);
        if(isStartedInTopView) {
            return processTouch(event);
        } else {
            return super.onTouchEvent(event);
        }
    }
//
    private boolean processTouch(MotionEvent event) {
        // Checking if that event was already processed (by onInterceptTouchEvent prior to onTouchEvent)
        long eventTime = event.getEventTime();
        if (mLastEventTime == eventTime) return mLastEventResult;
        mLastEventTime = eventTime;

        if (event.getActionMasked() == MotionEvent.ACTION_UP && mIsScrolling) {
            mIsScrolling = false;
            isStartedInTopView = false;
            animateToNearestState();
        }

        if(isStartedInTopView) {
            mLastEventResult = mGestureDetector.onTouchEvent(event);
        } else {
            mLastEventResult = super.onTouchEvent(event);
        }

        return mLastEventResult;
    }
}
