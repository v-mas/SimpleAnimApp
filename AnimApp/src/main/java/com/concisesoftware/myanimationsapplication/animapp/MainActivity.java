package com.concisesoftware.myanimationsapplication.animapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity {

    public static Animation flash(final View v, final int color, int singleDuration) {
        final Drawable oldVDraw = v.getBackground();
        final Drawable colorDraw = new ColorDrawable(color);
        final Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                colorDraw.setAlpha((int)(255*(1-interpolatedTime)));
            }
            @Override
            public boolean willChangeBounds() {
                return false;
            }
            @Override
            public boolean willChangeTransformationMatrix() {
                return false;
            }
        };
        a.setDuration(singleDuration);
        a.setRepeatMode(Animation.RESTART);
        a.setAnimationListener(new Animation.AnimationListener() {
            final int l = v.getPaddingLeft();
            final int t = v.getPaddingTop();
            final int r = v.getPaddingRight();
            final int b = v.getPaddingBottom();
            @Override
            public void onAnimationStart(Animation animation) {
                LayerDrawable ld = new LayerDrawable(new Drawable[] {
                        oldVDraw,
                        colorDraw
                });
                v.setBackgroundDrawable(ld);
                v.setPadding(l, t, r, b);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                colorDraw.setAlpha(0);
                v.setBackgroundDrawable(oldVDraw);
                v.setPadding(l, t, r, b);
            }
        });
        Animation old = v.getAnimation();
        if(old!=null) {
            old.cancel();
        }
        v.startAnimation(a);
        return a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Map<String, Class<?>> entries = new HashMap<String, Class<?>>();
        entries.put("Foldable Layout", FoldableActivity.class);
        entries.put("YouTube like draging", YouTubeLikeActivity.class);
        entries.put("Sliding from right", SlideFromRightActivity.class);
        entries.put("Menu form bottom", SlidingFromBottomMenuActivity.class);
        entries.put("Layout animation", LayoutAnimationActivity.class);


        final ListView listView = ((ListView)this.findViewById(R.id.main_list));
        listView.setAdapter(new BaseAdapter() {
            private final ArrayList mData = new ArrayList();

            {
                mData.addAll(entries.entrySet());
            }

            @Override
            public int getCount() {
                return mData.size();
            }

            @Override
            public Map.Entry<String, ?> getItem(int position) {
                return (Map.Entry) mData.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final View result;

                if (convertView == null) {
                    result = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_selectable_list_item, parent, false);
                } else {
                    result = convertView;
                }

                Map.Entry<String, ?> item = getItem(position);

                ((TextView) result.findViewById(android.R.id.text1)).setText(item.getKey());
                result.setTag(item.getKey());

                result.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.flash(result, Color.rgb(83,172,16), 500);
                        result.postDelayed(this, 500*getCount());
                    }
                }, position*500);

                return result;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent out = new Intent(getApplicationContext(),entries.get(view.getTag()));
                startActivity(out);
            }
        });

    }

}
