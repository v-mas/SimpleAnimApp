package com.concisesoftware.myanimationsapplication.animapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Map<String, Class<?>> entries = new HashMap<String, Class<?>>();
        entries.put("Foldable Layout", FoldableActivity.class);
        entries.put("YouTube like draging", YouTubeLikeActivity.class);
        entries.put("Sliding from right", SlideFromRightActivity.class);
        entries.put("Menu form bottom", SlidingFromBottomMenuActivity.class);


        ListView listView = ((ListView)this.findViewById(R.id.main_list));
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
