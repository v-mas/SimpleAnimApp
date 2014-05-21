package com.concisesoftware.myanimationsapplication.animapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alexvasilkov.foldablelayout.FoldableListLayout;
import com.concisesoftware.myanimationsapplication.animapp.widgets.MyFoldableListLayout;


public class FoldableActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foldable_activity);

        final int[] colors = new int[] {
                Color.rgb(255,255,255),
                Color.rgb(127,127,127),
                Color.rgb(0  ,0  ,0  ),
                Color.rgb(255,0  ,0  ),
                Color.rgb(0  ,255,0  ),
                Color.rgb(0  ,0  ,255)
        };

        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        final MyFoldableListLayout foldableListLayout = (MyFoldableListLayout) this.findViewById(R.id.foldablelatyout);
        final BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return colors.length;
            }

            @Override
            public Object getItem(int position) {
                return colors[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = inflater.inflate(R.layout.foldable_frame, parent, false);

                View v = new View(getApplicationContext());
                v.setBackgroundColor(colors[position]);
                v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                ((FrameLayout)convertView).addView(v);
                return convertView;
            }
        };
        foldableListLayout.setAdapter(adapter);

        final View firstView = inflater.inflate(R.layout.first_view, null, false);
        foldableListLayout.setFirstView(firstView);

        foldableListLayout.setOnFoldRotationListener(new FoldableListLayout.OnFoldRotationListener() {
            private int lastState = 0;
            @Override
            public void onFoldRotation(float rotation, boolean isFromUser) {
                int newState = lastState;
                if(rotation<110) {
                    newState=1;
                } else if(rotation>=110 && rotation <120) {
                    newState=2;
                } else if(rotation>=120 && rotation <140) {
                    newState=3;
                } else {
                    newState=0;
                }

                if(newState != lastState) {
                    switch(newState) {
                        case 1:
                            ((TextView) firstView.findViewById(android.R.id.text1)).setText("WHOA, not that hard!");
                            break;
                        case 2:
                            ((TextView) firstView.findViewById(android.R.id.text1)).setText("PULL! PULL!");
                            break;
                        case 3:
                            ((TextView) firstView.findViewById(android.R.id.text1)).setText("PULL!");
                            break;
                        case 0:
                        default:
                            ((TextView) firstView.findViewById(android.R.id.text1)).setText("pierwszy");
                            break;
                    }

                    Log.d("FoldableLayout", "rotation "+rotation+" isFromUser "+isFromUser+" lastState "+newState);
                    lastState=newState;
                    foldableListLayout.clearCache();
                }
            }
        });


    }

}
