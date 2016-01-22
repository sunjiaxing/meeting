package com.zhengshang.meeting.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.dao.ConfigDao;
import com.zhengshang.meeting.ui.adapter.QuickFlingAdapter;

/***
 * 自定义拖拉ListView
 *
 * @author sun
 */
public class DragListView extends ListView implements OnScrollListener,
        OnClickListener {
    /**
     * 拖拉ListView枚举所有状态
     */
    private enum RefreshState {
        /**
         * 普通状态
         */
        LV_NORMAL,
        /**
         * 下拉状态（为超过mHeadViewHeight）
         */
        LV_PULL_REFRESH,
        /**
         * 松开可刷新状态（超过mHeadViewHeight）
         */
        LV_RELEASE_REFRESH,
        /**
         * 加载状态
         */
        LV_REFRESHING;
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

    /**
     * listview功能
     */
    public enum ListViewPullType {
        /**
         * 能刷新和加载更多
         */
        LV_ALL,
        /**
         * 只能刷新
         */
        LV_ONLY_REFRESH,
        /**
         * 只能加载更多
         */
        LV_ONLY_LOAD_MORE,
        /**
         * 不能刷新不能加载更多
         */
        LV_DISABLE
    }

    private View headView, footView, loadingView;
    /**
     * 刷新msg
     */
    private TextView tvRefreshTip, tvLastRefreshTime, tvLoadMore;
    /**
     * 下拉图标（mHeadView）
     */
    private ImageView ivArrow;
    /**
     * 刷新进度体（mHeadView）
     */
    private ProgressBar progressBar;
    /**
     * headView的高
     */
    private int headViewHeight;
    /**
     * 旋转动画，旋转动画之后旋转动画.
     */
    private Animation animation, reverseAnimation;
    /**
     * 当前视图能看到的第一个项的索引
     */
    private int firstItemIndex = -1;
    /**
     * 用于保证startY的值在一个完整的touch事件中只被记录一次
     */
    private boolean isRecord = false;
    /**
     * 按下是的y坐标,move时的y坐标
     */
    private int startY;
    private RefreshState refreshState = RefreshState.LV_NORMAL;// 拖拉状态.(自定义枚举)

    private LoadMoreState loadMoreState = LoadMoreState.LV_NORMAL;// 加载更多默认状态.
    /**
     * headView是否返回.
     */
    private boolean isBack = false;
    /**
     * 下拉刷新接口（自定义）
     */
    public OnRefreshLoadMoreListener onRefreshLoadMoreListener;
    private QuickFlingAdapter adapter;
    private ListViewPullType pullType;
    private int scrollPosition;
    private ConfigDao configDao;
    private OnScrollListener listener;

    public int getScrollPosition() {
        return scrollPosition;
    }

    public void setScrollPosition(int scrollPosition) {
        this.scrollPosition = scrollPosition;
    }

    public DragListView(Context context) {
        super(context, null);
        initDragListView(context);
    }

    public DragListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDragListView(context);
    }

    public DragListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initDragListView(context);
    }

    /**
     * 设置下拉刷新接口
     */
    public void setOnRefreshListener(
            OnRefreshLoadMoreListener onRefreshLoadMoreListener) {
        this.onRefreshLoadMoreListener = onRefreshLoadMoreListener;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof QuickFlingAdapter) {
            this.adapter = (QuickFlingAdapter) adapter;
        }
    }

    /**
     * 设置拉动类型
     *
     * @param type
     */
    public void setPullType(ListViewPullType type) {
        this.pullType = type;
        switch (type) {
            case LV_ALL:
                if (headView != null && footView != null) {
                    addHeaderView(headView, null, false);
                    addFooterView(footView);
                }
                break;
            case LV_ONLY_REFRESH:
                if (headView != null) {
                    addHeaderView(headView, null, false);
                }
                break;
            case LV_ONLY_LOAD_MORE:
                if (footView != null) {
                    addFooterView(footView);
                }
                break;
            case LV_DISABLE:
                if (headView != null && footView != null) {
                    removeHeaderView(headView);
                    removeFooterView(footView);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置上次刷新时间
     *
     * @param time
     */
    public void setLastRefreshTime(String time) {
        if (tvLastRefreshTime != null) {
            tvLastRefreshTime.setVisibility(VISIBLE);
            tvLastRefreshTime.setText(time);
        }
    }

    /**
     * 隐藏刷新时间
     */
    public void hideUpdateTimeTip() {
        if (tvLastRefreshTime != null) {
            tvLastRefreshTime.setText("");
        }
    }

    /***
     * 初始化ListView
     */
    public void initDragListView(Context context) {
        configDao = ConfigDao.getInstance(context);

        String time = "";// 更新时间

        initHeadView(context, time);// 初始化该head.

        initLoadMoreView(context);// 初始化footer

        setOnScrollListener(this);// ListView滚动监听
    }

    /***
     * 初始话头部HeadView
     *
     * @param context 上下文
     * @param time    上次更新时间
     */
    public void initHeadView(Context context, String time) {
        headView = LayoutInflater.from(context).inflate(
                R.layout.listview_header, null);
        ivArrow = (ImageView) headView.findViewById(R.id.iv_arrow);
        ivArrow.setMinimumWidth(50);
        ivArrow.setMinimumHeight(50);
        progressBar = (ProgressBar) headView.findViewById(R.id.pb_loading);
        tvRefreshTip = (TextView) headView.findViewById(R.id.tv_refresh_tip);
        tvLastRefreshTime = (TextView) headView
                .findViewById(R.id.tv_last_refresh_time);
        measureView(headView);

        headViewHeight = headView.getMeasuredHeight();

        headView.setPadding(0, -1 * headViewHeight, 0, 0);
        headView.invalidate();
        initAnimation();// 初始化动画
    }

    /***
     * 初始化底部加载更多控件
     */
    private void initLoadMoreView(Context context) {
        footView = LayoutInflater.from(context).inflate(
                R.layout.listview_footer, null);

        tvLoadMore = (TextView) footView.findViewById(R.id.tv_load_more);

        loadingView = footView.findViewById(R.id.layout_loading);
        tvLoadMore.setOnClickListener(this);
    }

    /***
     * 初始化动画
     */
    private void initAnimation() {
        // 旋转动画
        animation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());// 匀速
        animation.setDuration(250);
        animation.setFillAfter(true);// 停留在最后状态.
        // 反向旋转动画
        reverseAnimation = new RotateAnimation(-180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(250);
        reverseAnimation.setFillAfter(true);
    }

    /***
     * 作用：测量 headView的宽和高.
     *
     * @param child
     */
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    /***
     * touch 事件监听
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (pullType == ListViewPullType.LV_ALL
                || pullType == ListViewPullType.LV_ONLY_REFRESH) {
            switch (ev.getAction()) {
                // 按下
                case MotionEvent.ACTION_DOWN:
                    doActionDown(ev);
                    break;
                // 移动
                case MotionEvent.ACTION_MOVE:
                    doActionMove(ev);
                    break;
                // 抬起
                case MotionEvent.ACTION_UP:
                    doActionUp(ev);
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    /***
     * 摁下操作
     * <p/>
     * 作用：获取摁下是的y坐标
     *
     * @param event
     */
    void doActionDown(MotionEvent event) {
        if (firstItemIndex == 0 && !isRecord) {
            startY = (int) event.getY();
            isRecord = true;
        }
    }

    /***
     * 拖拽移动操作
     *
     * @param event
     */
    void doActionMove(MotionEvent event) {
        int tempY = (int) event.getY();
        if (!isRecord && firstItemIndex == 0) {
            isRecord = true;
            startY = tempY;
        }
        if (refreshState != RefreshState.LV_REFRESHING && isRecord) {
            // 可以松开刷新了
            if (refreshState == RefreshState.LV_RELEASE_REFRESH) {
                // 往上推，推到屏幕足够掩盖head的程度，但还没有全部掩盖
                if ((tempY - startY < headViewHeight) && (tempY - startY) > 0) {
                    refreshState = RefreshState.LV_PULL_REFRESH;
                    switchViewState(refreshState);
                }
                // 一下子推到顶
                else if (tempY - startY <= 0) {
                    refreshState = RefreshState.LV_NORMAL;
                    switchViewState(refreshState);
                }
                // 往下拉，或者还没有上推到屏幕顶部掩盖head
                else {
                    // 不用进行特别的操作，只用更新paddingTop的值就行了
                }
            }
            // 还没有到达显示松开刷新的时候,NOMAL或者是PULL_REFRESH状态
            if (refreshState == RefreshState.LV_PULL_REFRESH) {
                // 下拉到可以进入RELEASE_TO_REFRESH的状态
                if (tempY - startY >= headViewHeight * 2) {
                    refreshState = RefreshState.LV_RELEASE_REFRESH;
                    isBack = true;
                    switchViewState(refreshState);
                }
                // 上推到顶了
                else if (tempY - startY <= 0) {
                    refreshState = RefreshState.LV_NORMAL;
                    switchViewState(refreshState);
                }
            }

            // done状态下
            if (refreshState == RefreshState.LV_NORMAL) {
                if (tempY - startY > 0) {
                    refreshState = RefreshState.LV_PULL_REFRESH;
                    switchViewState(refreshState);
                }
            }
            // 更新headView的size
            if (refreshState == RefreshState.LV_PULL_REFRESH) {
                headView.setPadding(0, -1 * headViewHeight + (tempY - startY),
                        0, 0);
                headView.invalidate();
            }

            // 更新headView的paddingTop
            if (refreshState == RefreshState.LV_RELEASE_REFRESH) {
                headView.setPadding(0, tempY - startY - headViewHeight, 0, 0);
                headView.invalidate();
            }
        }
    }

    /***
     * 手势抬起操作
     *
     * @param event
     */
    public void doActionUp(MotionEvent event) {
        if (refreshState != RefreshState.LV_REFRESHING) {
            if (refreshState == RefreshState.LV_NORMAL) {
            }
            if (refreshState == RefreshState.LV_PULL_REFRESH) {
                refreshState = RefreshState.LV_NORMAL;
                switchViewState(refreshState);
            }
            if (refreshState == RefreshState.LV_RELEASE_REFRESH) {
                refreshState = RefreshState.LV_REFRESHING;
                switchViewState(refreshState);
            }
        }
        isRecord = false;
        isBack = false;
    }

    /**
     * 切换headview视图
     */
    private void switchViewState(RefreshState state) {
        switch (state) {
            // 普通状态
            case LV_NORMAL:
                headView.setPadding(0, -1 * headViewHeight, 0, 0);
                headView.invalidate();
                progressBar.setVisibility(View.GONE);
                ivArrow.clearAnimation();
                // 此处更换图标
                ivArrow.setImageResource(R.mipmap.arrow);
                tvRefreshTip.setText(getContext().getString(R.string.drop_down));
                tvLastRefreshTime.setVisibility(View.VISIBLE);

                break;
            // 下拉状态
            case LV_PULL_REFRESH:
                progressBar.setVisibility(View.GONE);
                tvRefreshTip.setVisibility(View.VISIBLE);
                tvLastRefreshTime.setVisibility(View.VISIBLE);
                ivArrow.clearAnimation();
                ivArrow.setVisibility(View.VISIBLE);
                if (isBack) {
                    isBack = false;
                    ivArrow.clearAnimation();
                    ivArrow.startAnimation(reverseAnimation);
                    tvRefreshTip
                            .setText(getContext().getString(R.string.drop_down));
                } else {
                    tvRefreshTip
                            .setText(getContext().getString(R.string.drop_down));
                }

                break;
            // 松开刷新状态
            case LV_RELEASE_REFRESH:
                ivArrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tvRefreshTip.setVisibility(View.VISIBLE);
                tvLastRefreshTime.setVisibility(View.VISIBLE);
                ivArrow.clearAnimation();
                ivArrow.startAnimation(animation);
                tvRefreshTip.setText(getContext()
                        .getString(R.string.release_update));
                break;
            // 加载状态
            case LV_REFRESHING:
                headView.setPadding(0, 0, 0, 0);
                headView.invalidate();
                progressBar.setVisibility(View.VISIBLE);
                ivArrow.clearAnimation();
                ivArrow.setVisibility(View.GONE);
                tvRefreshTip.setText(getContext().getString(R.string.loading));
                tvLastRefreshTime.setVisibility(View.GONE);
                onRefresh();
                break;
        }
    }

    /***
     * 下拉刷新
     */
    private void onRefresh() {
        if (onRefreshLoadMoreListener != null) {
            onRefreshLoadMoreListener.onRefresh();
        }
    }

    /***
     * 下拉刷新完毕
     */
    public void onRefreshComplete() {
        refreshState = RefreshState.LV_NORMAL;
        switchViewState(refreshState);
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

    /**
     * ListView 滑动监听
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        firstItemIndex = firstVisibleItem;
        if (listener != null) {
            listener.onScroll(view, firstVisibleItem, visibleItemCount,
                    totalItemCount);
        }
    }

    public void addOnScrollListener(OnScrollListener l) {
        this.listener = l;
    }

    public interface OnScrollListener {
        void onScroll(AbsListView view, int firstVisibleItem,
                      int visibleItemCount, int totalItemCount);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (pullType == ListViewPullType.LV_ALL
                || pullType == ListViewPullType.LV_ONLY_LOAD_MORE) {
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
                        adapter.setQuickState(true);
                    }
                    break;
                case SCROLL_STATE_TOUCH_SCROLL:// 手动滑动状态
                    if (adapter != null && adapter instanceof QuickFlingAdapter) {
                        // 设置快速滑动状态
                        adapter.setQuickState(true);
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
                        adapter.setQuickState(false);
                        // 通知适配器更新
                        adapter.notifyDataSetChanged();
                    }
                    this.scrollPosition = view.getFirstVisiblePosition();
                    break;
                default:
                    if (adapter != null && adapter instanceof QuickFlingAdapter) {
                        // 设置快速滑动状态
                        adapter.setQuickState(false);
                        // 通知适配器更新
                        // adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    /***
     * 底部点击事件
     */
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
     * 刷新
     */
    public void isRefreshing() {
        refreshState = RefreshState.LV_REFRESHING;
        switchViewState(refreshState);
    }

    public void updateRefreshTimeTip(long time) {
        // 设置listview上的更新时间
        setLastRefreshTime("上次刷新 : "
                + Utils.formateTime(time, false));
    }

}
