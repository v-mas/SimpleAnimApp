package com.concisesoftware.myanimationsapplication.animapp.widgets;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.concisesoftware.myanimationsapplication.animapp.R;

/**
 * Created by concisecomp on 2014-05-21.
 */
public class YTLikeSecond extends FrameLayout {
    private final String TAG = "YT2";

    //TODO żeby bottom się ruszał razem z top, i zeby top zmieniał swój rozmiar


    private final int MIN_FLING_VELOCITY = 400;

    private boolean mMaximized = true;

    private FrameLayout mTopView;
    private FrameLayout mBottomView;

    private ViewDragHelper mDragHelper;
    private int mDragRange;

    private float mInitialMotionX;
    private float mInitialMotionY;
    private boolean mDoCaptureEventUp;
    private boolean mIsUnableToDrag;
    private int mScrollTouchSlop;
    private float mSlideOffset;

    // 0 - maximized
    // 1 - minimized
    private class YTLikeDragHelperCalback extends ViewDragHelper.Callback {
        @Override
        public void onViewDragStateChanged(int state) {
            Log.d(TAG, "drag state changed state["+state+"]");
            if (mDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE) {
                if (mSlideOffset == 0) {
                    if (!isMaximized()) {
                        mMaximized = true;
                        dispatchOnMaximized();
                    }
                } else if (isMaximized()) {
                    dispatchOnMinimized();
                    mMaximized = false;
                }
            }
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            Log.d(TAG, "drag position changed t["+top+"] dy["+dy+"]");
            onChangePosition(top);
            invalidate();
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            Log.d(TAG, "drag view captured");
            if(!isMaximized()) {
                mBottomView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            Log.d(TAG, "drag view released");
            int top = getTop();

            if (yvel > 0 || (yvel == 0 && mSlideOffset > 0.5f)) {
                top += mDragRange;
            }

            mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
            invalidate();
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            boolean result = mTopView == child;
            Log.d(TAG, "drag view try capture: "+result);
            return result;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mDragRange;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final int topBound = getTop();
            final int bottomBound = topBound + mDragRange;


            return Math.min(Math.max(top, topBound), bottomBound);
        }
    }

    public YTLikeSecond(Context context) {
        super(context);
        init(context);
    }

    public YTLikeSecond(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public YTLikeSecond(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.my_ytlike_layout, this, false);
        this.addView(rl);

//        View extraView = new View(context);
//        extraView.setVisibility(View.INVISIBLE);
//        rl.addView(extraView, 0, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

        mTopView = (FrameLayout) rl.findViewById(R.id.yt_top);
        mBottomView = (FrameLayout) rl.findViewById(R.id.yt_bottom);

        mDragHelper = ViewDragHelper.create(rl, new YTLikeDragHelperCalback());
        mDragHelper.setMinVelocity(MIN_FLING_VELOCITY * context.getResources().getDisplayMetrics().density);

        ViewConfiguration vc = ViewConfiguration.get(context);
        mScrollTouchSlop = vc.getScaledTouchSlop();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mDragRange = bottom-top - mTopView.getMeasuredHeight();
    }

    private boolean isInTopView(int x, int y) {
        int[] viewLocation = new int[2];
        mTopView.getLocationOnScreen(viewLocation);
        int[] parentLocation = new int[2];
        this.getLocationOnScreen(parentLocation);
        int screenX = parentLocation[0] + x;
        int screenY = parentLocation[1] + y;
        return screenX >= viewLocation[0] && screenX < viewLocation[0] + mTopView.getWidth() &&
                screenY >= viewLocation[1] && screenY < viewLocation[1] + mTopView.getHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        final int action = event.getAction();

        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                mInitialMotionX = event.getX();
                mInitialMotionY = event.getY();
                boolean result = isMaximized() || (!isMaximized() && isInTopView((int)mInitialMotionX, (int)mInitialMotionY));
                mDoCaptureEventUp = result;
                return result;
            }

            case MotionEvent.ACTION_UP: {
                final float x = event.getX();
                final float y = event.getY();
                final float dx = x - mInitialMotionX;
                final float dy = y - mInitialMotionY;
                final int slop = mDragHelper.getTouchSlop();
                if (dx * dx + dy * dy < slop * slop && isInTopView((int) x, (int) y)) {
                    mTopView.playSoundEffect(SoundEffectConstants.CLICK);
                    if (!isMaximized()) {
                        maximize();
                    } else {
                        minimize();
                    }
                    return true;
                }
                return mDoCaptureEventUp;
            }
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);

        if (mIsUnableToDrag && action != MotionEvent.ACTION_DOWN) {
            mDragHelper.cancel();
            return super.onInterceptTouchEvent(event);
        }

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }

        final float x = event.getX();
        final float y = event.getY();
        boolean interceptTap = false;

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mIsUnableToDrag = false;
                mInitialMotionX = x;
                mInitialMotionY = y;
                boolean result = isMaximized() || (!isMaximized() && isInTopView((int)mInitialMotionX, (int)mInitialMotionY));
                mDoCaptureEventUp = result;
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final float adx = Math.abs(x - mInitialMotionX);
                final float ady = Math.abs(y - mInitialMotionY);
                final int dragSlop = mDragHelper.getTouchSlop();

                // Handle any horizontal scrolling on the drag view.
                if (adx > mScrollTouchSlop && ady < mScrollTouchSlop) {
                    return super.onInterceptTouchEvent(event);
                }
                // Intercept the touch if the drag view has any vertical scroll.
                // onTouchEvent will determine if the view should drag vertically.
                else if (ady > mScrollTouchSlop) {
                    interceptTap = isInTopView((int) x, (int) y);
                }

                if ((ady > dragSlop && adx > ady) || !isInTopView((int) x, (int) y)) {
                    mDragHelper.cancel();
                    mIsUnableToDrag = true;
                    return false;
                }
                break;
            }
        }

        final boolean interceptForDrag = mDragHelper.shouldInterceptTouchEvent(event);

        return interceptForDrag || interceptTap;
    }

    public boolean isMaximized() {
        return mMaximized;
    }

    public void setTopView(View topView) {
        mTopView.removeAllViews();
        mTopView.addView(topView);
    }

    public void setBottomView(View bottomView) {
        mBottomView.removeAllViews();
        mBottomView.addView(bottomView);
    }

    public void maximize() {
        if(!isMaximized()) {
            smoothSlideTo(0.0f);
        }
    }

    public void minimize() {
        if(isMaximized()) {
            smoothSlideTo(1.0f);
        }

    }

    boolean smoothSlideTo(float slideOffset) {
        int y = (int) (getTop() + slideOffset * mDragRange);
        Log.d(TAG, "smoothSlide slideOffset["+slideOffset+"] y["+y+"] range["+mDragRange+"]");

        if (mDragHelper.smoothSlideViewTo(mTopView, 0, y)) {
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }

    private void onChangePosition(int newTop) {
        final int topBound = getTop();
        mSlideOffset = (float) (newTop - topBound) / mDragRange;
        dispatchOnSlide();
    }


    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


    private void dispatchOnSlide() {
//        if (mPanelSlideListener != null) {
//            mPanelSlideListener.onPanelSlide(panel, mSlideOffset);
//        }
    }

    private void dispatchOnMaximized() {
        Log.d(TAG, "onMax");
//        if (mPanelSlideListener != null) {
//            mPanelSlideListener.onPanelExpanded(panel);
//        }
        sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
    }

    private void dispatchOnMinimized() {
        Log.d(TAG, "onMin");
//        if (mPanelSlideListener != null) {
//            mPanelSlideListener.onPanelCollapsed(panel);
//        }
        sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
    }
}
