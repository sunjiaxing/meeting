package com.sb.meeting.ui.activity;

import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.common.Utils;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.ui.component.CustomWebView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * 显示网页内容（包括文本类型和url类型）的Activity
 * Created by sun on 2016/4/1.
 */
@EActivity(R.layout.layout_show_web)
public class ShowWebActivity extends BaseActivity {

    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.layout_loading_web)
    View layoutLoading;
    @ViewById(R.id.layout_error_web)
    View layoutError;
    @ViewById(R.id.tv_description)
    TextView tvErrorMsg;
    @ViewById(R.id.btn_refresh)
    Button btnErrorRefresh;
    @ViewById(R.id.iv_loading_in)
    ImageView ivLoading;
    @ViewById(R.id.webView)
    CustomWebView webview;

    @Extra(IParam.URL)
    String url;
    @Extra(IParam.CONTENT)
    String content;
    @Extra(IParam.TITLE)
    String title;

    private AnimationDrawable anim;

    @AfterViews
    void init() {
        ivBack.setVisibility(View.VISIBLE);
        if (!Utils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        anim = (AnimationDrawable) ivLoading.getBackground();
        startLoadingSelf();
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!Utils.isEmpty(title)) {
                    tvTitle.setText(title);
                }
            }
        });
        if (!Utils.isEmpty(url)) {
            webview.loadUrl(url);
        } else if (!Utils.isEmpty(content)) {
            webview.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
        }
        stopLoadingSelf();
    }

    @Override
    protected void onTaskSuccess(int action, Object data) {

    }

    @Click(R.id.iv_back)
    void back() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            finish();
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
        if (!Utils.isEmpty(url) || !Utils.isEmpty(content)) {
            showToast(msg);
        } else {
            layoutError.setVisibility(View.VISIBLE);
            btnErrorRefresh.setText("刷新");
            tvErrorMsg.setText(msg);
        }
    }
}
