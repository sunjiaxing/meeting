package com.sb.meeting.ui.component;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.common.Utils;
import com.sb.meeting.ui.vo.NewsChannelVO;

import java.util.List;

/**
 * 用于展示新闻栏目的自定义组件
 * Created by sun on 2016/1/6.
 */
public class ChannelGallery extends HorizontalScrollView implements View.OnClickListener {

    private LinearLayout linearLayout;
    private int selected;
    private boolean scrollCenter = true;
    private ItemClickListener listener;
    private int itemPadding;
    private int itemMargin;

    @Override
    public void onClick(View v) {
        try {
            int pos = Integer.parseInt(v.getTag().toString());
            if (pos == selected) {
                return;
            }
            if (listener != null) {
                listener.onItemClick(pos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface ItemClickListener {
        void onItemClick(int pos);
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public void setScrollCenter(boolean scrollCenter) {
        this.scrollCenter = scrollCenter;
    }

    public ChannelGallery(Context context) {
        super(context);
        init();
    }

    public ChannelGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChannelGallery(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    void init() {
        itemPadding = Utils.dip2px(getContext(), 10);
        itemMargin = Utils.dip2px(getContext(), 5);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        setScrollbarFadingEnabled(false);
        setFillViewport(false);
        linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        addView(linearLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 设置数据
     *
     * @param data
     */
    public void setData(List<NewsChannelVO> data) {
        if (data != null) {
            linearLayout.removeAllViews();
            NewsChannelVO vo;
            TextView tv;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = itemMargin;
            params.rightMargin = itemMargin;
            for (int i = 0; i < data.size(); i++) {
                vo = data.get(i);
                tv = new TextView(getContext());
                tv.setTag(i);
                tv.setText(vo.getName());
                tv.setTextSize(18);
                tv.setTextColor(getResources().getColor(R.color.news_content_color));
                tv.setBackgroundColor(Color.TRANSPARENT);
                tv.setPadding(itemPadding, 3, itemPadding, 3);
                tv.setMinWidth(70);
                tv.setGravity(Gravity.CENTER);
                tv.setOnClickListener(this);
                linearLayout.addView(tv, params);
            }
            setSelected(0);
        }
    }

    /**
     * 设置选中状态
     *
     * @param pos
     */
    public void setSelected(int pos) {
        this.selected = pos;
        TextView tv;
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            tv = (TextView) linearLayout.getChildAt(i);
            if (i == selected) {
                // 设置选中
                tv.setTextColor(Color.WHITE);
                tv.setBackgroundResource(R.drawable.news_category_shape);
            } else {
                // 恢复默认
                tv.setTextColor(getResources().getColor(R.color.news_content_color));
                tv.setBackgroundColor(Color.TRANSPARENT);
            }
        }
        scrollToSelected();
    }

    /**
     * 滚动到 选中位置
     */
    public void scrollToSelected() {
        post(new Runnable() {
            @Override
            public void run() {
                TextView tv = (TextView) linearLayout.getChildAt(selected);
                int scrollX;
                if (scrollCenter) {
                    scrollX = tv.getLeft() - (getWidth() - tv.getWidth()) / 2;
                } else {
                    scrollX = tv.getLeft() - getWidth() + tv.getWidth();
                }
                smoothScrollTo(scrollX, 0);
            }
        });
    }
}
