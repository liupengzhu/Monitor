package cn.com.larunda.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sddt on 18-3-28.
 */

public class HeaderAndFooterRecyclerView extends RecyclerView {

    protected HeaderAndFooterAdapter mAdapter;
    protected Adapter mRealAdapter;

    public HeaderAndFooterRecyclerView(Context context) {
        super(context);
    }

    public HeaderAndFooterRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderAndFooterRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mRealAdapter = adapter;
        if (adapter instanceof HeaderAndFooterAdapter) {
            mAdapter = (HeaderAndFooterAdapter) adapter;
        } else {
            mAdapter = new HeaderAndFooterAdapter(getContext(), adapter);
        }
        super.setAdapter(mAdapter);
    }

    public void addHeaderView(View view) {
        if (null == view) {
            throw new IllegalArgumentException("the view to add must not be null !");
        } else if (mAdapter == null) {
            throw new IllegalStateException("u must set a adapter first !");
        } else {
            mAdapter.addHeaderView(view);
        }
    }

    public void addFooterView(View view) {
        if (null == view) {
            throw new IllegalArgumentException("the view to add must not be null !");
        } else if (mAdapter == null) {
            throw new IllegalStateException("u must set a adapter first !");
        } else {
            mAdapter.addFooterView(view);
        }
    }

    public void removeHeaderView(View view) {
        if (null == view) {
            throw new IllegalArgumentException("the view to remove must not be null !");
        } else if (mAdapter == null) {
            throw new IllegalStateException("u must set a adapter first !");
        } else {
            mAdapter.removeHeaderView(view);
        }
    }

    public void removeFooterView(View view) {
        if (null == view) {
            throw new IllegalArgumentException("the view to remove must not be null !");
        } else if (mAdapter == null) {
            throw new IllegalStateException("u must set a adapter first !");
        } else {
            mAdapter.removeFooterView(view);
        }
    }
    /**获得真正的adapter*/

    public Adapter getRealAdapter() {
        return mRealAdapter;
    }
}
