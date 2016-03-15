package com.sb.meeting.ui.component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.dao.ConfigDao;
import com.sb.meeting.ui.adapter.QuickFlingAdapter;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * listview 刷新 加载更多  组件
 * Created by sun on 2016/2/17.
 */
public class RefreshListView extends PtrClassicFrameLayout implements AbsListView.OnScrollListener, View.OnClickListener, PtrHandler {

    private ListView listView;
    private LoadMoreState loadMoreState = LoadMoreState.LV_NORMAL;// 加载更多默认状态.
    /**
     * 下拉刷新接口（自定义）
     */
    public OnRefreshLoadMoreListener onRefreshLoadMoreListener;
    private View loadingView;
    private TextView tvLoadMore;
    private ConfigDao configDao;
    private BaseAdapter adapter;
    private boolean disallowInterceptTouchEvent = false;

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        if (onRefreshLoadMoreListener != null) {
            onRefreshLoadMoreListener.onRefresh();
        }

    }

    /**
     * 添加 headerview
     *
     * @param v
     * @param data
     * @param isSelectable
     */
    public void addHeaderView(View v, Object data, boolean isSelectable) {
        if (listView != null) {
            listView.addHeaderView(v, data, isSelectable);
        }
    }

    public void addHeaderView(View v) {
        addHeaderView(v, null, true);
    }


    /**
     * 点击加载更多枚举所有状态
     */
    public enum LoadMoreState {
        /**
         * 普通状态
         */
        LV_NORMAL,
        /**
         * 加载状态
         */
        LV_LOADING,
        /**
         * 结束状态
         */
        LV_OVER,
        /**
         * 网络不稳定
         */
        LV_NETWORK_DISABLE,
        /**
         * 移除状态
         */
        LV_REMOVE
    }

    /***
     * 自定义刷新和加载更多接口
     */
    public interface OnRefreshLoadMoreListener {
        /***
         * // 下拉刷新执行
         */
        void onRefresh();

        /***
         * 点击加载更多
         */
        void onLoadMore();
    }

    /**
     * 设置下拉刷新接口
     */
    public void setOnRefreshListener(
            OnRefreshLoadMoreListener onRefreshLoadMoreListener) {
        this.onRefreshLoadMoreListener = onRefreshLoadMoreListener;
    }

    public RefreshListView(Context context) {
        super(context);
        init();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        configDao = ConfigDao.getInstance(getContext());
        listView = new ListView(getContext());
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setHorizontalFadingEdgeEnabled(false);
        listView.setVerticalFadingEdgeEnabled(false);
        listView.setFastScrollEnabled(true);
        listView.setOnScrollListener(this);
        addView(listView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // 初始化底部加载更多控件
        View footView = LayoutInflater.from(getContext()).inflate(
                R.layout.listview_footer, null);

        tvLoadMore = (TextView) footView.findViewById(R.id.tv_load_more);

        loadingView = footView.findViewById(R.id.layout_loading);
        tvLoadMore.setOnClickListener(this);
        listView.addFooterView(footView);

        setPtrHandler(this);
    }


    public void setSelector(int resID) {
        if (listView != null) {
            listView.setSelector(resID);
        }
    }

    public void setSelector(Drawable sel) {
        if (listView != null) {
            listView.setSelector(sel);
        }
    }

    public void setAdapter(ListAdapter adapter) {
        if (listView == null) {
            return;
        }
        listView.setAdapter(adapter);
        if (adapter instanceof QuickFlingAdapter) {
            this.adapter = (QuickFlingAdapter) adapter;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int first = view.getFirstVisiblePosition();
        int count = view.getChildCount();
        if ((first + count) >= view.getCount()
                && configDao.getAutoScrollState()) {
            // 滑动到底部
            if (onRefreshLoadMoreListener != null) {
                if (loadMoreState == LoadMoreState.LV_NORMAL
                        || loadMoreState == LoadMoreState.LV_NETWORK_DISABLE) {
                    updateLoadMoreViewState(LoadMoreState.LV_LOADING);
                    onRefreshLoadMoreListener.onLoadMore();
                }
            }
        }
        switch (scrollState) {
            case SCROLL_STATE_FLING:// 快速滑动状态
                if (adapter != null && adapter instanceof QuickFlingAdapter) {
                    // 设置快速滑动状态
                    ((QuickFlingAdapter) adapter).setQuickState(true);
                }
                break;
            case SCROLL_STATE_TOUCH_SCROLL:// 手动滑动状态
                if (adapter != null && adapter instanceof QuickFlingAdapter) {
                    // 设置快速滑动状态
                    ((QuickFlingAdapter) adapter).setQuickState(true);
                    // 通知适配器更新
                    // adapter.notifyDataSetChanged();
                }
                break;
            case SCROLL_STATE_IDLE:// 停止滑动--空闲状态
                // if(getParent()!=null){
                // getParent().requestDisallowInterceptTouchEvent(false);
                // }
                if (adapter != null && adapter instanceof QuickFlingAdapter) {
                    // 设置快速滑动状态
                    ((QuickFlingAdapter) adapter).setQuickState(false);
                    // 通知适配器更新
                    adapter.notifyDataSetChanged();
                }
                break;
            default:
                if (adapter != null && adapter instanceof QuickFlingAdapter) {
                    // 设置快速滑动状态
                    ((QuickFlingAdapter) adapter).setQuickState(false);
                    // 通知适配器更新
                    // adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    /***
     * 下拉刷新完毕
     */
    public void onRefreshComplete() {
        refreshComplete();
    }

    /***
     * 点击加载更多
     *
     * @param flag 数据是否已全部加载完毕
     */
    public void onLoadMoreComplete(LoadMoreState flag) {
        updateLoadMoreViewState(flag);
    }

    /**
     * 更新Footview视图
     */
    private void updateLoadMoreViewState(LoadMoreState state) {
        switch (state) {
            // 普通状态
            case LV_NORMAL:
                loadingView.setVisibility(View.GONE);
                tvLoadMore.setVisibility(View.VISIBLE);
                tvLoadMore.setText(getContext().getString(R.string.get_more));
                break;
            // 加载中状态
            case LV_LOADING:
                loadingView.setVisibility(View.VISIBLE);
                tvLoadMore.setVisibility(View.GONE);
                break;
            // 加载完毕状态
            case LV_OVER:
                loadingView.setVisibility(View.GONE);
                tvLoadMore.setVisibility(View.VISIBLE);
                tvLoadMore.setText(getContext().getString(R.string.no_data_tip));
                break;
            case LV_NETWORK_DISABLE:// 网络不稳定
                loadingView.setVisibility(View.GONE);
                tvLoadMore.setVisibility(View.VISIBLE);
                tvLoadMore.setText(getContext().getString(R.string.netconnecterror));
                break;
            case LV_REMOVE:
                loadingView.setVisibility(View.GONE);
                tvLoadMore.setVisibility(View.GONE);
                break;
        }
        this.loadMoreState = state;
    }

    @Override
    public void onClick(View v) {
        if (onRefreshLoadMoreListener != null) {
            if (loadMoreState == LoadMoreState.LV_NORMAL
                    || loadMoreState == LoadMoreState.LV_NETWORK_DISABLE) {
                updateLoadMoreViewState(LoadMoreState.LV_LOADING);
                onRefreshLoadMoreListener.onLoadMore();
            }
        }

    }

    /**
     * 设置 item点击事件
     *
     * @param listener 监听
     */
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        if (listView != null) {
            listView.setOnItemClickListener(listener);
        }
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        if (listView != null) {
            listView.setOnItemLongClickListener(listener);
        }
    }

    /*@Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        disallowInterceptTouchEvent = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (disallowInterceptTouchEvent) {
            return dispatchTouchEventSupper(e);
        }
        return super.dispatchTouchEvent(e);
    }*/
}
