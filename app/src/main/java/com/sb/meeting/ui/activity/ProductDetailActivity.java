package com.sb.meeting.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.common.TaskAction;
import com.sb.meeting.common.Utils;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.service.YellowPageService;
import com.sb.meeting.ui.component.CustomWebView;
import com.sb.meeting.ui.vo.ImageVO;
import com.sb.meeting.ui.vo.ProductDetailVO;
import com.taskmanager.Task;
import com.taskmanager.TaskManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品详情 Activity
 * Created by sun on 2016/4/1.
 */
@EActivity(R.layout.layout_product_detail)
public class ProductDetailActivity extends BaseActivity {
    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.layout_loading_product_detail)
    View layoutLoading;
    @ViewById(R.id.layout_error_product_detail)
    View layoutError;
    @ViewById(R.id.tv_description)
    TextView tvErrorMsg;
    @ViewById(R.id.btn_refresh)
    Button btnErrorRefresh;
    @ViewById(R.id.iv_loading_in)
    ImageView ivLoading;
    @ViewById(R.id.webview_detail)
    CustomWebView webview;
    @Extra(IParam.PRODUCT_ID)
    int productId;

    private ProductDetailVO detailVO;
    private AnimationDrawable anim;
    private YellowPageService yellowPageService;

    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    @AfterViews
    void init() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("产品详情");
        anim = (AnimationDrawable) ivLoading.getBackground();
        yellowPageService = new YellowPageService(this);
        webview.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void onClick(String url) {
                List<ImageVO> list = new ArrayList<>();
                ImageVO vo = new ImageVO();
                vo.setUrl(url);
                list.add(vo);
                ImageActivity_.intent(ProductDetailActivity.this).extra(IParam.IMAGES, (Serializable) list).start();
            }
        }, "image");
        getDetail();
    }

    @Click(R.id.btn_refresh)
    void getDetail() {
        startLoadingSelf();
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_PRODUCT_DETAIL) {
            @Override
            protected void doBackground() throws Exception {
                setReturnData(yellowPageService.getProductDetail(productId));
            }
        }, this);
    }

    @Override
    protected void onTaskSuccess(int action, Object data) {
        if (action == TaskAction.ACTION_GET_PRODUCT_DETAIL) {
            stopLoadingSelf();
            if (data != null) {
                detailVO = (ProductDetailVO) data;
                refreshUI();
            } else {
                showErrorMsg("暂无数据");
            }
        }
    }

    /**
     * 刷新界面
     */
    private void refreshUI() {
        if (detailVO != null) {
            webview.loadDataWithBaseURL(null, detailVO.getContent(), "text/html", "UTF-8", null);
        }
    }

    @Override
    protected void onTaskFail(int action, String errorMessage) {
        if (action == TaskAction.ACTION_GET_PRODUCT_DETAIL) {
            stopLoadingSelf();
            showErrorMsg(errorMessage);
        } else {
            super.onTaskFail(action, errorMessage);
        }
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
