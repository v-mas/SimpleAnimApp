package com.concisesoftware.myanimationsapplication.animapp.animations;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by concisecomp on 2014-05-21.
 */
public class SlideInFromRightAnimation extends Animation {

    private final View mView;
    private final float mEndX;

    private float mDiffX;
    private AnimationListener mListener;

    private AnimationListener animationListener = new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            if(mListener!=null) {
                mListener.onAnimationStart(animation);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mView.setX(0.0f);
            if(mListener!=null) {
                mListener.onAnimationEnd(animation);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            if(mListener!=null) {
                mListener.onAnimationRepeat(animation);
            }
        }
    };

    public SlideInFromRightAnimation(View v, int duration, float finalX) {
        mView = v;
        mEndX = finalX;
        this.setDuration(duration);
        this.setInterpolator(new AccelerateDecelerateInterpolator());
        this.setAnimationListener(animationListener);

        DisplayMetrics metrics = v.getContext().getResources().getDisplayMetrics();
        mDiffX = metrics.widthPixels;

        mView.setAnimation(this);
    }

    public SlideInFromRightAnimation(View v, int duration, float finalX, int startOffset) {
        this(v, duration, finalX);
        this.setStartOffset(startOffset);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        mView.setX((1.0f-interpolatedTime)*mDiffX+mEndX);

    }

    @Override
    public void setAnimationListener(AnimationListener listener) {
        mListener = listener;
    }

    @Override
    public boolean willChangeBounds() {
        return false;
    }

    @Override
    public boolean willChangeTransformationMatrix() {
        return false;
    }

}
