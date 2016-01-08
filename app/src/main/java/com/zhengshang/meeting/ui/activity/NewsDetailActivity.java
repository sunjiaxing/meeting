package com.zhengshang.meeting.ui.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.taskmanager.Task;
import com.taskmanager.TaskManager;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.TaskAction;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.remote.IParam;
import com.zhengshang.meeting.service.CommentService;
import com.zhengshang.meeting.service.NewsService;
import com.zhengshang.meeting.ui.adapter.ListViewPagerAdapter;
import com.zhengshang.meeting.ui.fragment.BaseFragment;
import com.zhengshang.meeting.ui.fragment.CommentListFrament;
import com.zhengshang.meeting.ui.fragment.NewsDetailFragment;
import com.zhengshang.meeting.ui.vo.NewsDetailVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.util.EncodingUtils;

/**
 * 新闻详情 Activity
 * Created by sun on 2016/1/8.
 */
@EActivity(R.layout.layout_news_detail_main)
public class NewsDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.vp_news_detail)
    ViewPager vpMain;
    @ViewById(R.id.layout_loading)
    View layoutLoading;
    @ViewById(R.id.layout_error)
    View layoutError;
    @ViewById(R.id.tv_description)
    TextView tvErrorMsg;
    @ViewById(R.id.btn_refresh)
    Button btnErrorRefresh;

    private String catId;
    private String newsId;
    private AnimationDrawable anim;
    private NewsService newsService;
    private NewsDetailVO detailVO;
    private String html;
    private NewsDetailFragment newsDetailFragment;
    private CommentListFrament commentListFrament;
    private List<BaseFragment> fragmentList;
    private Bundle saveInstance;
    private CommentService commentService;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        this.saveInstance = savedInstanceState;
    }

    @AfterViews
    void init() {
        catId = getIntent().getStringExtra(IParam.CAT_ID);
        newsId = getIntent().getStringExtra(IParam.NEWS_ID);
        String title = getIntent().getStringExtra(IParam.TITLE);
        anim = (AnimationDrawable) findViewById(R.id.iv_loading_in)
                .getBackground();
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
        vpMain.addOnPageChangeListener(this);
        initNewsTemplate();
        initFragment();
        newsService = new NewsService(this);
        commentService = new CommentService(this);
        getNewsDetailAndComment();
    }

    /**
     * 获取fragmentmanager中的缓存
     *
     * @param position
     * @return
     */
    private String getFragmentTag(int position) {
        return "android:switcher:" + R.id.vp_news_detail + ":" + position;
    }

    /**
     * 初始化加载新闻模板
     */
    private void initNewsTemplate() {
        try {
            InputStream is = getAssets().open(
                    "news_detail_template.txt");
            int length = is.available();
            byte[] buffer = new byte[length];
            is.read(buffer);
            is.close();
            html = EncodingUtils.getString(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化 fragment
     */
    private void initFragment() {
        fragmentList = new ArrayList<>();
        if (saveInstance == null) {
            newsDetailFragment = new NewsDetailFragment();
            commentListFrament = new CommentListFrament();
        } else {
            newsDetailFragment = (NewsDetailFragment) getSupportFragmentManager()
                    .findFragmentByTag(getFragmentTag(0));
            commentListFrament = (CommentListFrament) getSupportFragmentManager()
                    .findFragmentByTag(getFragmentTag(1));
        }
        fragmentList.add(newsDetailFragment);
        fragmentList.add(commentListFrament);
        ListViewPagerAdapter adapter = new ListViewPagerAdapter(getSupportFragmentManager());
        adapter.setData(fragmentList);
        vpMain.setAdapter(adapter);
        vpMain.setCurrentItem(0);
    }

    /**
     * 获取新闻详情和评论列表
     */
    private void getNewsDetailAndComment() {
        startLoadingSelf();
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_NEWS_DETAIL) {
            @Override
            protected void doBackground() throws Exception {
                commentService.getCommentList(newsId, catId);
                setReturnData(newsService.getNewsDetailFromWeb(newsId, catId));
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
        if (detailVO != null) {
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
            case TaskAction.ACTION_GET_NEWS_DETAIL:// 获取新闻详情
                stopLoadingSelf();
                detailVO = (NewsDetailVO) data;
                refreshUI();
                break;
        }
    }

    /**
     * 刷新界面
     */
    private void refreshUI() {
        // 展示新闻详情
        if (!Utils.isEmpty(html) && Utils.isEmpty(detailVO.getContentUrl())) {
            html = html
                    .replace("@title", detailVO.getTitle())
                    .replace(
                            "@comeAndTime",
                            "来源:" + detailVO.getcFrom() + "  "
                                    + detailVO.getcTime())
                    .replace("@content", detailVO.getContent());
            if (!Utils.isEmpty(detailVO.getAdIconUrl())
                    && !Utils.isEmpty(detailVO.getAdTitle())) {
                try {
                    InputStream is = getAssets().open(
                            "news_detail_bottom_ad.txt");
                    int length = is.available();
                    byte[] buffer = new byte[length];
                    is.read(buffer);
                    is.close();
                    String bottomAd = EncodingUtils.getString(buffer, "UTF-8");
                    html = html.replace("@ad", bottomAd);
                    if (!Utils.isEmpty(detailVO.getAdIconUrl())) {
                        html = html.replace("@adIconUrl",
                                detailVO.getAdIconUrl());
                        html = html.replace("@adUrl", detailVO.getAdUrl());
                    } else {
                        html = html.replace("@adIconUrl", "");
                        html = html.replace("@adUrl", "");
                    }
                    if (!Utils.isEmpty(detailVO.getAdTitle())) {
                        html = html.replace("@adTitle",
                                detailVO.getAdTitle());
                    } else {
                        html = html.replace("@adTitle", "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                html = html.replace("@ad", "");
            }
            // 传递给 fragment
            newsDetailFragment.setHtml(html);
        } else {
            // 传递给 fragment
            newsDetailFragment.setUrl(detailVO.getContentUrl());
        }

        // TODO 添加评论列表数据


    }


    @Override
    protected void onTaskFail(int action, String errorMessage) {
        switch (action) {
            case TaskAction.ACTION_GET_NEWS_DETAIL:
                stopLoadingSelf();
                showErrorMsg(errorMessage);
                break;
        }
    }

    @Click(R.id.iv_back)
    void back() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
