package com.sb.meeting.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 展示地区的GridView
 * Created by sun on 2016/4/5.
 */
public class GridViewWithScroll extends GridView {
    public GridViewWithScroll(Context context) {
        super(context);
    }

    public GridViewWithScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewWithScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
