package com.zhengshang.meeting.ui.activity;

import android.graphics.drawable.AnimationDrawable;
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
import com.zhengshang.meeting.service.NewsService;
import com.zhengshang.meeting.ui.component.CustomerWebview;
import com.zhengshang.meeting.ui.vo.NewsDetailVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.InputStream;

import cz.msebera.android.httpclient.util.EncodingUtils;

/**
 * Created by sun on 2015/12/16.
 */
@EActivity(R.layout.layout_news_detail)
public class NewsDetailActivity extends BaseActivity {
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
    @ViewById(R.id.webview_detail)
    CustomerWebview webview;
    private String catId;
    private String newsId;
    private String title;
    private AnimationDrawable anim;
    private NewsService newsService;
    private NewsDetailVO detailVO;
    private String html;

    @AfterViews
    void init() {
        catId = getIntent().getStringExtra(IParam.CAT_ID);
        newsId = getIntent().getStringExtra(IParam.NEWS_ID);
        title = getIntent().getStringExtra(IParam.TITLE);
        anim = (AnimationDrawable) findViewById(R.id.iv_loading_in)
                .getBackground();
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
        initWebView();
        newsService = new NewsService(this);
        getNewsDetail();
    }

    /**
     * 初始化webview和加载新闻模板
     */
    private void initWebView() {
        // todo 给webview 注入图片点击 js

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
     * 获取新闻详情
     */
    private void getNewsDetail() {
        startLoadingSelf();
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_NEWS_DETAIL) {
            @Override
            protected void doBackground() throws Exception {
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
            webview.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        } else {
            webview.loadUrl(detailVO.getContentUrl());
        }
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
}
