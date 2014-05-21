package com.concisesoftware.myanimationsapplication.animapp.widgets;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.alexvasilkov.foldablelayout.FoldableListLayout;
import com.concisesoftware.myanimationsapplication.animapp.R;

/**
 * Created by concisecomp on 2014-05-12.
 */
public class MyFoldableListLayout extends FoldableListLayout {
    private MyWrapperAdapter mAdapterWrapper;

    public MyFoldableListLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public MyFoldableListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyFoldableListLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mAdapterWrapper = new MyWrapperAdapter(context);
    }

    @Override
    public void setAdapter(BaseAdapter adapter) {
        if(adapter != null) {
            mAdapterWrapper.setWrappedAdapter(adapter);
            super.setAdapter(mAdapterWrapper);
        } else {
            super.setAdapter(null);
        }
    }

    public void clearCache() {
        //dirty trick to clear all layout cache
        super.setAdapter(mAdapterWrapper);
    }

    public void setFirstView(View firstView) {
        if(!mAdapterWrapper.hasFirstView()) {
            setFoldRotation(getFoldRotation()+180, false);
        }
        mAdapterWrapper.setFirstView(firstView);
    }

    @Override
    protected void setFoldRotation(float rotation, boolean isFromUser) {
        if(mAdapterWrapper.hasFirstView()) {
            if(rotation<180) {
                if (rotation < 100) rotation = 100;
            }
        }
        super.setFoldRotation(rotation, isFromUser);
    }

    @Override
    public void scrollToPosition(int index) {
        if(mAdapterWrapper.hasFirstView() && index==0) { index = 1; }
        super.scrollToPosition(index);
    }

    private class MyWrapperAdapter extends BaseAdapter {

        MyWrapperAdapter(Context context) {
            this(context, null);
        }
        MyWrapperAdapter(Context context, BaseAdapter adapter) {
            this(context, null, null);
        }
        MyWrapperAdapter(Context context, BaseAdapter adapter, View firstView) {
            mContext = context;
            mAdapter = adapter;
            before = firstView;
        }

        View before;

        Context mContext;
        BaseAdapter mAdapter;

        public void setFirstView(View firstView) {
            before = firstView;
            notifyDataSetChanged();
        }

        public boolean hasFirstView() {
            return before!=null;
        }

        public void setWrappedAdapter(BaseAdapter adapter) {
            mAdapter = adapter;
            notifyDataSetChanged();
        }

        public BaseAdapter getWrappedAdapter() {
            return mAdapter;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return mAdapter!=null ? mAdapter.areAllItemsEnabled() : (before!=null ? true : false);
        }

        @Override
        public boolean isEnabled(int position) {
            if(before != null) {
                if (position == 0) {
                    return true;
                }
                return mAdapter == null || mAdapter.isEnabled(position - 1);
            } else {
                return mAdapter == null || mAdapter.isEnabled(position);
            }
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            super.registerDataSetObserver(observer);
            if(mAdapter != null) { mAdapter.registerDataSetObserver(observer); }
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            super.unregisterDataSetObserver(observer);
            if(mAdapter != null) { mAdapter.unregisterDataSetObserver(observer); }
        }

        @Override
        public int getCount() {
            if(before != null) {
                return mAdapter!=null ? mAdapter.getCount()+1 : 1;
            } else {
                return mAdapter!=null ? mAdapter.getCount() : 0;
            }
        }

        @Override
        public Object getItem(int position) {
            if(before != null) {
                if(position == 0) { return null; }
                else { return mAdapter!=null ? mAdapter.getItem(position-1) : null; }
            } else {
                return mAdapter!=null ? mAdapter.getItem(position) : null;
            }
        }

        @Override
        public long getItemId(int position) {
            if(before != null) {
                if (position == 0) { return -1; }
                else { return mAdapter!=null ? mAdapter.getItemId(position - 1) : 0; }
            } else {
                return mAdapter!=null ? mAdapter.getItemId(position) : 0;
            }

        }

        @Override
        public boolean hasStableIds() {
            return mAdapter!=null ? mAdapter.hasStableIds() : true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(before != null) {
                if (position == 0) {
                    return before;
                } else {
                    if (mAdapter != null) {
                        return mAdapter.getView(position - 1, convertView, parent);
                    } else {
                        return LayoutInflater.from(mContext).inflate(R.layout.fold_empty, parent, false);
                    }
                }
            } else {
                if (mAdapter != null) {
                    return mAdapter.getView(position, convertView, parent);
                } else {
                    return null;
                }
            }
        }

        @Override
        public void notifyDataSetChanged() {
//            super.notifyDataSetChanged();
            if(mAdapter!=null) { mAdapter.notifyDataSetChanged(); }
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return before==null && (mAdapter==null || mAdapter.isEmpty());
        }
    }

}
