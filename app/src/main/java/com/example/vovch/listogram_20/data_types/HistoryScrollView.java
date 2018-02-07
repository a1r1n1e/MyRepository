package com.example.vovch.listogram_20.data_types;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by vovch on 30.01.2018.
 */

public class HistoryScrollView extends ScrollView {
    OnTopReachedListener topReachedListener;
    public HistoryScrollView(Context context, AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);
    }

    public HistoryScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HistoryScrollView(Context context) {
        super(context);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //View view = getChildAt(getChildCount() - 1);
        //int diff = (view.getBottom() - (getHeight() + getScrollY())) -  view.getPaddingBottom();

        if (t == 0 && topReachedListener != null) {
            topReachedListener.onTopReached();
        }
    }

    public OnTopReachedListener getOnTopReachedListener() {
        return topReachedListener;
    }

    public void setOnTopReachedListener(
            OnTopReachedListener newOnTopReachedListener) {
        topReachedListener = newOnTopReachedListener;
    }

    public interface OnTopReachedListener {
        public void onTopReached();
    }
}
