package com.sb.meeting.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.common.Utils;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.ui.component.CustomerWebview;

/**
 * 显示Url信息的activity
 */
public class ShowUrlActivity extends BaseActivity implements OnClickListener {

    private String title;
    private String url;
    private ImageView btnBack;
    private TextView tvTitle;
    private CustomerWebview webview;
    /**
     * 菜单布局
     **/
    private View menuBgLayout;
    private LinearLayout btnShouYe, btnShare;
    private ImageView btnRight;
    private boolean canShare;
    private boolean needParam;
    private String shareUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_url);
        // 获取title和Url
        title = getIntent().getStringExtra(IParam.TITLE);
        url = getIntent().getStringExtra(IParam.URL);
        shareUrl = getIntent().getStringExtra(IParam.SHARE_URL);
        canShare = getIntent().getBooleanExtra(IParam.CAN_SHARE, false);
        needParam = getIntent().getBooleanExtra(IParam.NEED_PARAM, false);
        // 标题栏
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(title);
        // 返回键
        btnBack = (ImageView) findViewById(R.id.iv_back);
        // 设置返回键显示
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);
        // btnRefresh=(Button) findViewById(R.id.btn_right);
        // btnRefresh.setVisibility(View.VISIBLE);
        // btnRefresh.setBackgroundResource(R.drawable.btn_share_bg);
        // btnRefresh.setOnClickListener(this);
        // 右侧按钮
        btnRight = (ImageView) findViewById(R.id.iv_right);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setBackgroundResource(R.drawable.btn_more);
        btnRight.setOnClickListener(this);
        // 菜单
        menuBgLayout = (View) findViewById(R.id.menuLayout);
        menuBgLayout.setOnClickListener(this);
        // 返回首页
        btnShouYe = (LinearLayout) findViewById(R.id.btn_back_main);
        btnShouYe.setOnClickListener(this);

        btnShare = (LinearLayout) findViewById(R.id.btn_share);
        btnShare.setOnClickListener(this);

        TextView tvShareLine = (TextView) findViewById(R.id.tv_share_line);
        if (canShare) {
            tvShareLine.setVisibility(View.VISIBLE);
            btnShare.setVisibility(View.VISIBLE);
        } else {
            tvShareLine.setVisibility(View.GONE);
            btnShare.setVisibility(View.GONE);
        }
        webview = (CustomerWebview) findViewById(R.id.webview_ad);
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                try {
                    if (newProgress > 70) {
//                        stopLoading();
                    } else {
//                        startLoading();
                    }
                    if (newProgress == 100 && url.equals("file:///android_asset/about.htm")) {
//                        String version = Utils.getVersionName(ShowUrlActivity.this);
//                        view.loadUrl("javascript:showVersion('" + version + "');");
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!Utils.isEmpty(title)) {
                    tvTitle.setText(title);
                } else {
                    tvTitle.setText("链接内容");
                }
            }
        });
        webview.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:// 返回
                webview.clearHistory();
                webview.clearCache(true);
                this.finish();
                break;
        /*
         * case R.id.iv_right:// 刷新 // webview.reload(); showShare(true, null);
		 * break;
		 */
            case R.id.iv_right:// 右侧菜单
                controlMenuBg(true);
                break;
            case R.id.menuLayout:// 菜单布局
                controlMenuBg(false);
                break;
            case R.id.btn_back_main: // 返回主页
//                returnToMain();
                break;
            case R.id.btn_share:// 分享
                controlMenuBg(false);
//                ShareVO shareVO = new ShareVO();
//                shareVO.setTitle(title);
//                shareVO.setSummary("");
//                if (needParam) {
//                    shareUrl += "?idenCode=" + MasterDao.getInstance(this).getIDenCode();
//                }
//                shareVO.setLongUrl(shareUrl);
//                Utils.shareForNew(false, null, this, shareVO, null);
                break;
        }
    }

    /**
     * 控制菜单
     *
     * @param isOpen true 表示打开 false表示关闭
     */
    private void controlMenuBg(boolean isOpen) {
        if (isOpen && menuBgLayout != null && !menuBgLayout.isShown()) {
            menuBgLayout.setVisibility(View.VISIBLE);
        } else {
            menuBgLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controlMenuBg(false);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webview.onResume();
//        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        webview.reload();
        webview.onPause();
//        StatService.onPause(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webview.canGoBack()) {
                webview.goBack();
                return true;
            } else {
                webview.clearHistory();
                webview.clearCache(true);
                this.finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onTaskSuccess(int action, Object data) {

    }
}
