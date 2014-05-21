package com.concisesoftware.myanimationsapplication.animapp.animations;

import android.content.Context;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * Created by concisecomp on 2014-05-21.
 */
public class SlideInFromRightAnimation extends TranslateAnimation {

    public SlideInFromRightAnimation(Context context, int duration) {
        super(context.getResources().getDisplayMetrics().widthPixels, 0,
              0, 0);
        this.setDuration(duration);
        this.setInterpolator(new AccelerateDecelerateInterpolator());

    }

    public SlideInFromRightAnimation(Context context, int duration, int startOffset) {
        this(context, duration);
        this.setStartOffset(startOffset);
    }
}
