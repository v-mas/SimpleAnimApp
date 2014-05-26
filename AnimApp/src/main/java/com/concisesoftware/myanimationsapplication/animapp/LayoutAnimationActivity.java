package com.concisesoftware.myanimationsapplication.animapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;


public class LayoutAnimationActivity extends Activity {

    private LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_animation);
        root = (LinearLayout) findViewById(R.id.main_layout);
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim);
        root.setLayoutAnimation(animationController);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.layout_animation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add) {
            TextView tv = new TextView(this);
            tv.setText("nowy tekst");
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            root.addView(tv, 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
