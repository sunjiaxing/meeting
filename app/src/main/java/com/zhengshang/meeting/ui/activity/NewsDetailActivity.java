package com.zhengshang.meeting.ui.activity;

import android.graphics.drawable.AnimationDrawable;
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
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

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

    @AfterViews
    void init() {
        catId = getIntent().getStringExtra(IParam.CATID);
        newsId = getIntent().getStringExtra(IParam.NEWS_ID);
        title = getIntent().getStringExtra(IParam.TITLE);
        anim = (AnimationDrawable) findViewById(R.id.iv_loading_in)
                .getBackground();
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
        newsService = new NewsService(this);
        getNewsDetail();
    }

    private void getNewsDetail() {
        startLoadingSelf();
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_NEWS_DETAIL, getLocalClassName()) {
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
     * @param msg
     * @author
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

    }
}
