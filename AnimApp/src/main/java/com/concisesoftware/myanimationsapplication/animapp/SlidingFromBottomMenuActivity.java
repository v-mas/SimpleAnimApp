package com.concisesoftware.myanimationsapplication.animapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;


public class SlidingFromBottomMenuActivity extends Activity {

    private final static String TAG = "SlidingFromBottom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_from_bottom_menu);

        SlidingUpPanelLayout layout = (SlidingUpPanelLayout) this.findViewById(R.id.bottom_menu_container);

        final RelativeLayout menuLayout = (RelativeLayout) this.findViewById(R.id.bottom_menu_content);
        final RelativeLayout menuHeaderLayout = (RelativeLayout) this.findViewById(R.id.bottom_menu_header);

        final LinearLayout mainLayout = (LinearLayout) this.findViewById(R.id.main_layout);

        ScrollView scrollView = new ScrollView(this);
        final LinearLayout insideMenuLayout = new LinearLayout(this);
        insideMenuLayout.setOrientation(LinearLayout.VERTICAL);

        scrollView.addView(insideMenuLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

        layout.expandPane();
        layout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.d(TAG, "panel slide offset["+slideOffset+"]");
            }

            @Override
            public void onPanelCollapsed(View panel) {
                TextView tv = new TextView(SlidingFromBottomMenuActivity.this);
                tv.setText("panel collapsed");
                insideMenuLayout.addView(tv);
                Log.d(TAG, "panel collapse");
            }

            @Override
            public void onPanelExpanded(View panel) {
                TextView tv = new TextView(SlidingFromBottomMenuActivity.this);
                tv.setText("panel expanded");
                insideMenuLayout.addView(tv);
                Log.d(TAG, "panel expand");
            }

            @Override
            public void onPanelAnchored(View panel) {
                TextView tv = new TextView(SlidingFromBottomMenuActivity.this);
                tv.setText("panel anchored");
                insideMenuLayout.addView(tv);
                Log.d(TAG, "panel anchored");
            }
        });
    }

}
