package com.sb.meeting.ui.component;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.taskmanager.LogUtils;
import com.sb.meeting.common.Utils;
import com.sb.meeting.ui.activity.ShowUrlActivity;

/**
 * 自定义webview 功能包括图片点击和电话点击
 */
public class CustomerWebview extends WebView {

    public CustomerWebview(Context context) {
        super(context);
        init();
    }

    public CustomerWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomerWebview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        getSettings().setJavaScriptEnabled(true);// js可用
        getSettings().setLoadsImagesAutomatically(true);// 设置自动加载图片
        getSettings().setDomStorageEnabled(true);
        getSettings().setDefaultTextEncodingName("UTF-8");
        getSettings().setTextSize(TextSize.NORMAL);
        // getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.indexOf(".apk") != -1) {
                    getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } else if (url.indexOf(".3gp") != -1
                        || url.indexOf(".mp4") != -1
                        || url.indexOf(".flv") != -1
                        || url.indexOf(".wmv") != -1
                        || url.indexOf(".mov") != -1) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String strend = "";
                    if (url.toLowerCase().endsWith(".mp4")) {
                        strend = "mp4";
                    } else if (url.toLowerCase().endsWith(".3gp")) {
                        strend = "3gp";
                    } else if (url.toLowerCase().endsWith(".mov")) {
                        strend = "mov";
                    } else if (url.toLowerCase().endsWith(".flv")) {
                        strend = "flv";
                    } else if (url.toLowerCase().endsWith(".wmv")) {
                        strend = "wmv";
                    }

                    intent.setDataAndType(Uri.parse(url), "video/" + strend);
                    getContext().startActivity(intent);
                } else if (url.indexOf(".apk") != -1) {
                    getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } else if (url.startsWith("mailto:") || url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    getContext().startActivity(intent);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.getSettings().setJavaScriptEnabled(true);
                super.onPageFinished(view, url);
                if (url != null && !url.startsWith("http") && !url.startsWith("https")) {
                    addImageClickFunction();
                }
            }
        });

        addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void onClick(String adUrl, String adTitle) {
                if (!Utils.isEmpty(adUrl)) {
                    Intent intent = new Intent(getContext(),
                            ShowUrlActivity.class);
                    intent.putExtra("url", adUrl);
                    intent.putExtra("title", adTitle);
                    getContext().startActivity(intent);
                }
            }
        }, "ad");
    }

    /**
     * 添加 图片点击 js
     */
    private void addImageClickFunction() {
        loadUrl(
                "javascript:(function(){" +
                        "var objs = document.getElementsByTagName(\"img\"); " +
                        "for(var i=0;i<objs.length;i++){"
                        + "    objs[i].onclick=function() {  "
                        + "        window.image.onClick(this.src);  " +
                        "      }  " +
                        "}"
                        + "})()");
    }
}
