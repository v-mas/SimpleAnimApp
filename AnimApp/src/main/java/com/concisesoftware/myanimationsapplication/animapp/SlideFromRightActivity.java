package com.concisesoftware.myanimationsapplication.animapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.widget.GridLayout;

import com.concisesoftware.myanimationsapplication.animapp.animations.SlideInFromRightAnimation;

import java.util.Random;


public class SlideFromRightActivity extends Activity {

    private static final String TAG = "SlideAnimActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_from_right);

        final GridLayout layout = (GridLayout) this.findViewById(R.id.main_layout);
        layout.setColumnCount(5);

        final Random random = new Random(System.currentTimeMillis());

        final DisplayMetrics metrics = getResources().getDisplayMetrics();

        layout.post(new Runnable() {
            @Override
            public void run() {
                int height = (int) (layout.getHeight()/11.0f);
                int width = (int) (layout.getWidth()/5.0f);
                for (int i=0; i<55; i++) {
                    final View v = new View(SlideFromRightActivity.this);
//                    v.setVisibility(View.INVISIBLE);
                    int color = random.nextInt() | 0xFF000000 ; //full visible
                    v.setBackgroundColor(color);
                    GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                    lp.height = height;
                    lp.width = width;
                    v.setLayoutParams(lp);
                    layout.addView(v);

                    final int offset = 70*(i%layout.getColumnCount())+50*(i/layout.getColumnCount());
                    Animation a = new SlideInFromRightAnimation(SlideFromRightActivity.this, 1000, offset);
                    v.startAnimation(a);
//                    v.setVisibility(View.VISIBLE);
                }
            }
        });

    }

}
