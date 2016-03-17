package com.sb.meeting.ui.fragment;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.taskmanager.Task;
import com.taskmanager.TaskManager;
import com.sb.meeting.R;
import com.sb.meeting.common.BonConstants;
import com.sb.meeting.common.TaskAction;
import com.sb.meeting.common.Utils;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.service.NewsService;
import com.sb.meeting.ui.activity.NewsDetailActivity_;
import com.sb.meeting.ui.activity.NewsSubjectActivity_;
import com.sb.meeting.ui.activity.ShowUrlActivity;
import com.sb.meeting.ui.adapter.OnlineNewsAdapter;
import com.sb.meeting.ui.component.OnlineNewsFirstView;
import com.sb.meeting.ui.component.RefreshListView;
import com.sb.meeting.ui.vo.NewsChannelVO;
import com.sb.meeting.ui.vo.NewsVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

/**
 * 新闻每个栏目的fragment
 */
@EFragment(R.layout.listview_in_viewpager)
public class NewsPagerItemFragment extends BaseFragment implements
        OnlineNewsFirstView.OnClickFirstView,
        RefreshListView.OnRefreshLoadMoreListener {

    @ViewById(R.id.lv_drag)
    RefreshListView listview;
    @ViewById(R.id.layout_loading_news)
    View layoutLoading;
    @ViewById(R.id.layout_error_news)
    View layoutError;
    @ViewById(R.id.tv_description)
    TextView tvErrorMsg;
    @ViewById(R.id.btn_refresh)
    Button btnErrorRefresh;
    @ViewById(R.id.iv_loading_in)
    ImageView ivLoading;

    private NewsChannelVO newsType = null;
    private List<NewsVO> news;
    private OnlineNewsFirstView firstView;
    private OnlineNewsAdapter adapter;
    private int position;
    private NewsService newsService;
    private long minTime = 0;
    private boolean hasTop = false;
    private AnimationDrawable anim;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsService = new NewsService(getActivity());
    }

    @AfterViews
    void init() {
        anim = (AnimationDrawable) ivLoading.getBackground();
        listview.setVisibility(View.GONE);
        listview.setOnRefreshListener(this);
        // 创建firstview
        firstView = new OnlineNewsFirstView(getActivity(),
                R.layout.online_news_item_first, this);
        listview.addHeaderView(firstView, null, true);
        listview.disableWhenHorizontalMove(true);
    }

    /**
     * 设置数据
     *
     * @param type 新闻栏目
     * @param pos  位置
     */
    public void refreshCurrentView(NewsChannelVO type, int pos) {
        this.newsType = type;
        this.position = pos;
        listview.setLastUpdateTimeKey("channel" + type.getTypeId());
        if (Utils.isEmpty(news)) {
            startLoadingSelf();
            // 获取数据
            getDataFromDB();
        } else if (newsService.needRefresh(type.getChildId())
                && listview != null) {
            listview.autoRefresh();
        }
    }

    /**
     * 开始滚动
     */
    private void startScroll() {
        if (firstView != null && !Utils.isEmpty(news)
                && !Utils.isEmpty(news.get(0).getTopNews())
                && news.get(0).getTopNews().size() > 1) {
            firstView.startScroll(position);
        }
    }

    /**
     * 停止滚动
     */
    private void stopScroll() {
        if (firstView != null) {
            firstView.stopScroll(position);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        startScroll();
    }

    @Override
    public void onResume() {
        super.onResume();
//		StatService.onResume(this);
        startScroll();
    }

    /**
     * 开启loading
     */
    private void startLoadingSelf() {
        if (layoutLoading != null && layoutError != null && anim != null) {
            layoutLoading.setVisibility(View.VISIBLE);
            anim.start();
            layoutError.setVisibility(View.GONE);
        }
    }

    /**
     * 关闭loading
     */
    private void stopLoadingSelf() {
        if (layoutLoading != null && layoutError != null && anim != null) {
            layoutLoading.setVisibility(View.GONE);
            anim.stop();
            layoutError.setVisibility(View.GONE);
        }
    }

    /**
     * 显示错误信息
     *
     * @param msg 消息
     */
    private void showErrorMsg(String msg) {
        if (!isAdded()) {
            return;
        }
        if (Utils.isEmpty(msg)) {
            msg = getString(R.string.netconnecterror);
        }
        if (!Utils.isEmpty(news)) {
            showToast(msg);
        } else {
            layoutError.setVisibility(View.VISIBLE);
            btnErrorRefresh.setText("刷新");
            tvErrorMsg.setText(msg);
        }
    }

    /**
     * 获取数据库缓存数据
     */
    private void getDataFromDB() {
        if (newsType != null) {
            TaskManager.pushTask(new Task(TaskAction.ACTION_GET_NEWS_FROM_DB) {
                @Override
                protected void doBackground() throws Exception {
                    // 获取本地数据
                    if (newsService == null) {
                        newsService = new NewsService(getActivity());
                    }
                    setReturnData(new Object[]{NewsPagerItemFragment.this.hashCode(), newsService.getNewsFromDB(newsType.getChildId())});
                }
            }, getActivity());
        }
    }

    public void onTaskSuccess(int action, Object data) {
        switch (action) {
            case TaskAction.ACTION_GET_NEWS_FROM_DB:// 从本地缓存获取数据
                stopLoadingSelf();
                news = (List<NewsVO>) data;
                if (!Utils.isEmpty(news)) {
                    if (listview == null) {
                        return;
                    }
                    // 刷新界面
                    refreshUI();
                    // 设置加载更多状态
                    setLoadMoreState();
                    // 判断是否刷新
                    if (newsService.needRefresh(newsType.getChildId())
                            && listview != null) {
                        listview.autoRefresh();
                    }
                } else {
                    // 获取数据
                    startLoadingSelf();
                    refresh();
                }
                break;
            case TaskAction.ACTION_REFRESH_NEWS:// 刷新
                stopLoadingSelf();
                listview.onRefreshComplete();
                news = (List<NewsVO>) data;
                // 刷新界面
                refreshUI();
                // 设置加载更多状态
                setLoadMoreState();
                break;
            case TaskAction.ACTION_LOAD_MORE_NEWS:// 加载更多
                List<NewsVO> loadMoreData = (List<NewsVO>) data;
                if (!Utils.isEmpty(loadMoreData)) {
                    news.addAll(loadMoreData);
                }
                if (!Utils.isEmpty(loadMoreData)) {
                    refreshUI();
                }
                if (Utils.isEmpty(loadMoreData)
                        || loadMoreData.size()
                        % BonConstants.LIMIT_GET_NEWS != 0) {
                    listview.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_OVER);
                } else {
                    listview.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_NORMAL);
                }
                break;
        }
    }

    public void onTaskFail(int action, String message) {
        listview.onRefreshComplete();
        listview.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_NETWORK_DISABLE);
        switch (action) {
            case TaskAction.ACTION_GET_NEWS_FROM_DB:
                stopLoadingSelf();
                showErrorMsg(message);
                break;
            case TaskAction.ACTION_REFRESH_NEWS:
                stopLoadingSelf();
                listview.onRefreshComplete();
                showErrorMsg(message);
                break;
            default:
                showErrorMsg(message);
                break;
        }
    }

    /**
     * 刷新数据
     */
    private void refresh() {
        TaskManager.pushTask(new Task(TaskAction.ACTION_REFRESH_NEWS) {
            @Override
            protected void doBackground() throws Exception {
                minTime = 0;
                setReturnData(new Object[]{
                        NewsPagerItemFragment.this.hashCode(),
                        newsService.getNewsListFromWeb(newsType.getChildId(), newsType.getModelName(), minTime)
                });
            }
        }, getActivity());
    }

    /**
     * 设置加载更多状态
     */
    private void setLoadMoreState() {
        if (!Utils.isEmpty(news)) {
            int totalNewsCount;
            if (!Utils.isEmpty(news.get(0).getTopNews())) {
                totalNewsCount = news.size() + news.get(0).getTopNews().size()
                        - 1;
            } else {
                totalNewsCount = news.size();
            }
            if (totalNewsCount % BonConstants.LIMIT_GET_NEWS != 0) {
                listview.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_OVER);
            } else {
                listview.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_NORMAL);
            }
        }
    }

    @Override
    public void onLoadMore() {
        // 获取当前集合中的数据的时间的最小值---从集合最后一条数据
        TaskManager.pushTask(new Task(TaskAction.ACTION_LOAD_MORE_NEWS) {
            @Override
            protected void doBackground() throws Exception {
                minTime = news.get(news.size() - 1).getCreateTime();
                setReturnData(new Object[]{
                        NewsPagerItemFragment.this.hashCode(),
                        newsService.getNewsListFromWeb(newsType.getChildId(), newsType.getModelName(), minTime)
                });
            }
        }, getActivity());
    }

    /**
     * 更新界面数据
     */
    private void refreshUI() {
        if (listview == null) {
            return;
        }
        if (!Utils.isEmpty(news)) {
            // 显示listview
            listview.setVisibility(View.VISIBLE);
            // 结束刷新
            listview.onRefreshComplete();
            if (!Utils.isEmpty(news)) {
                // 初始化数据
                firstView.initData(news.get(0));
                if (!Utils.isEmpty(news.get(0).getTopNews())) {
                    hasTop = true;
                }
            }
            if (adapter == null) {
                adapter = new OnlineNewsAdapter(getActivity());
                adapter.setData(news, hasTop);
                listview.setAdapter(adapter);
            } else {
                adapter.setData(news, hasTop);
                // 防止空指针
                adapter.notifyDataSetChanged();
            }
            startScroll();
        } else {
            showErrorMsg("暂时无数据！");
        }
    }

    @Override
    public void onClickView(NewsVO model) {
        if (!Utils.isEmpty(model.getIconAdUrl())) {
            Intent intent = new Intent(getActivity(), ShowUrlActivity.class);
            intent.putExtra(IParam.URL, model.getIconAdUrl());
            intent.putExtra(IParam.TITLE, model.getTitle());
            startActivity(intent);
        } else {
            toNewsDetail(model);
        }
    }

    @ItemClick(R.id.lv_drag)
    void onItemClick(int position) {
        // 获取选中的news
        final NewsVO model = news.get(hasTop ? position : position - 1);
        if (model == null) {
            return;
        }
        // 设置阅读状态
        model.setRead(true);
        TaskManager.pushTask(new Task(TaskAction.ACTION_SET_NEWS_READ_STATE) {
            @Override
            protected void doBackground() throws Exception {
                setNeedCallBack(false);
                newsService.setReadState(model.getId(), newsType.getChildId(), 1);
            }
        }, getActivity());
        adapter.setData(news, hasTop);
        // 刷新适配器
        adapter.notifyDataSetChanged();
        if (!model.isSubject()) {
            // 不是专题--按以前的逻辑
            // 判断iconAdUrl
            if (!Utils.isEmpty(model.getIconAdUrl())) {
                Intent intent = new Intent(getActivity(), ShowUrlActivity.class);
                intent.putExtra(IParam.URL, model.getIconAdUrl());
                intent.putExtra(IParam.TITLE, model.getTitle());
                startActivity(intent);
            } else {
                // 界面跳转
                toNewsDetail(model);
            }
        } else {
            // 是专题--特殊处理
            NewsSubjectActivity_.intent(this).
                    extra(IParam.SPECIAL_ID, model.getSubjectId()).
                    extra(IParam.TITLE, model.getTitle()).start();
        }
    }

    /**
     * 跳转到新闻详情
     *
     * @param model
     */
    private void toNewsDetail(NewsVO model) {
        NewsDetailActivity_.intent(this)
                .extra(IParam.NEWS_ID, model.getId())
                .extra(IParam.TITLE, model.getTitle())
                .startForResult(0);
    }

    @Override
    public void onRefresh() {
        stopScroll();
        refresh();
    }

    /**
     * 无网络刷新
     */
    @Click(R.id.btn_refresh)
    void clickRefresh() {
        startLoadingSelf();
        refresh();
    }

    @Override
    public void onPause() {
        super.onPause();
//		StatService.onPause(this);
        stopScroll();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopScroll();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                getDataFromDB();
                break;
        }
    }
}
