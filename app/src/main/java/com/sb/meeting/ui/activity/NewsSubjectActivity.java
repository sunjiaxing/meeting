package com.sb.meeting.ui.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taskmanager.Task;
import com.taskmanager.TaskManager;
import com.sb.meeting.R;
import com.sb.meeting.common.ImageOption;
import com.sb.meeting.common.TaskAction;
import com.sb.meeting.common.Utils;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.service.NewsService;
import com.sb.meeting.ui.adapter.OnlineNewsAdapter;
import com.sb.meeting.ui.component.RefreshListView;
import com.sb.meeting.ui.vo.NewsSubjectVO;
import com.sb.meeting.ui.vo.NewsVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

/**
 * 新闻专题列表 activity
 * Created by sun on 2016/1/20.
 */
@EActivity(R.layout.layout_news_subject)
public class NewsSubjectActivity extends BaseActivity implements RefreshListView.OnRefreshLoadMoreListener {

    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.layout_loading)
    View layoutLoading;
    @ViewById(R.id.layout_error)
    View layoutError;
    @ViewById(R.id.tv_description)
    TextView tvErrorMsg;
    @ViewById(R.id.btn_refresh)
    Button btnErrorRefresh;
    @ViewById(R.id.lv_drag)
    RefreshListView listView;

    private AnimationDrawable anim;
    private int specialId;
    private NewsSubjectVO subjectVO;
    private NewsService newsService;
    private ImageView ivBanner;
    private TextView tvDesc;
    private OnlineNewsAdapter adapter;

    @AfterViews
    void init() {
        specialId = getIntent().getIntExtra(IParam.SPECIAL_ID, 0);
        String title = getIntent().getStringExtra(IParam.TITLE);
        title = "专题新闻";
        anim = (AnimationDrawable) findViewById(R.id.iv_loading_in)
                .getBackground();
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
        listView.setOnRefreshListener(this);
        listView.setLastUpdateTimeRelateObject(this);
        listView.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_REMOVE);
        initHeader();
        newsService = new NewsService(this);
        startLoadingSelf();
        getNewsSubject();
    }

    /**
     * 初始化header
     */
    void initHeader() {
        View header = getLayoutInflater().inflate(R.layout.layout_subject_header, null);
        ivBanner = (ImageView) header.findViewById(R.id.iv_banner);
        tvDesc = (TextView) header.findViewById(R.id.tv_description);
        listView.addHeaderView(header);
    }

    /**
     * 获取新闻专题
     */
    private void getNewsSubject() {
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_NEWS_SUBJECT) {
            @Override
            protected void doBackground() throws Exception {
                setReturnData(newsService.getNewsSubject(specialId));
            }
        }, this);
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
     * @param msg 错误信息
     */
    private void showErrorMsg(String msg) {
        if (Utils.isEmpty(msg)) {
            msg = getString(R.string.netconnecterror);
        }
        if (subjectVO != null) {
            showToast(msg);
        } else {
            layoutError.setVisibility(View.VISIBLE);
            btnErrorRefresh.setText("刷新");
            tvErrorMsg.setText(msg);
        }
    }

    @Override
    protected void onTaskSuccess(int action, Object data) {
        switch (action) {
            case TaskAction.ACTION_GET_NEWS_SUBJECT:
                stopLoadingSelf();
                listView.onRefreshComplete();
                if (data != null) {
                    subjectVO = (NewsSubjectVO) data;
                    refreshUI();
                }
                break;
        }
    }

    /**
     * 刷新界面
     */
    private void refreshUI() {
        ImageLoader.getInstance().displayImage(subjectVO.getBanner(), ivBanner, ImageOption.createNomalOption());
        tvDesc.setText(subjectVO.getDescription());
        if (adapter == null) {
            adapter = new OnlineNewsAdapter(this);
            adapter.setData(subjectVO.getNewsVOList(), false);
            listView.setAdapter(adapter);
        } else {
            adapter.setData(subjectVO.getNewsVOList(), false);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onTaskFail(int action, String errorMessage) {
        switch (action) {
            case TaskAction.ACTION_GET_NEWS_SUBJECT:
                stopLoadingSelf();
                listView.onRefreshComplete();
                showErrorMsg(errorMessage);
                break;
            default:
                super.onTaskFail(action, errorMessage);
                break;
        }
    }

    @Click(R.id.iv_back)
    void back() {
        finish();
    }


    @ItemClick(R.id.lv_drag)
    void onItemClick(int position) {
        // 获取选中的news
        NewsVO model = subjectVO.getNewsVOList().get(position - 1);
        if (model != null && !model.isSubject()) {
            // 不是专题--按以前的逻辑
            // 设置阅读状态
            model.setRead(true);
            adapter.setData(subjectVO.getNewsVOList(), false);
            // 刷新适配器
            adapter.notifyDataSetChanged();
            // 判断iconAdUrl
            if (!Utils.isEmpty(model.getIconAdUrl())) {
                Intent intent = new Intent(this, ShowUrlActivity.class);
                intent.putExtra(IParam.URL, model.getIconAdUrl());
                intent.putExtra(IParam.TITLE, model.getTitle());
                startActivity(intent);
            } else {
                // 界面跳转
                Intent intent = new Intent(this, NewsDetailActivity_.class);
                intent.putExtra(IParam.NEWS_ID, model.getId());
                intent.putExtra(IParam.CAT_ID, model.getCatId());
                intent.putExtra(IParam.TITLE, model.getTitle());
                startActivityForResult(intent, 0);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Click(R.id.btn_refresh)
    void clickRefresh() {
        startLoadingSelf();
        getNewsSubject();
    }

    @Override
    public void onRefresh() {
        getNewsSubject();
    }

    @Override
    public void onLoadMore() {

    }
}
