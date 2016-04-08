package com.sb.meeting.ui.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sb.meeting.common.Utils;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.ui.activity.ShowWebActivity_;

/**
 * 自定义webview 功能包括图片点击和电话点击
 */
public class CustomWebView extends WebView {

    public CustomWebView(Context context) {
        super(context);
        init();
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface", "SetJavaScriptEnabled"})
    private void init() {
        getSettings().setJavaScriptEnabled(true);// js可用
        getSettings().setLoadsImagesAutomatically(true);// 设置自动加载图片
        getSettings().setDomStorageEnabled(true);
        getSettings().setDefaultTextEncodingName("UTF-8");
        getSettings().setTextSize(TextSize.NORMAL);
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains(".apk")) {
                    getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } else if (url.contains(".3gp")
                        || url.contains(".mp4")
                        || url.contains(".flv")
                        || url.contains(".wmv")
                        || url.contains(".mov")) {
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
                } else if (url.contains(".apk")) {
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

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

            }
        });

        addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void onClick(String adUrl, String adTitle) {
                if (!Utils.isEmpty(adUrl)) {
                    ShowWebActivity_.intent(getContext())
                            .extra(IParam.URL, adUrl)
                            .extra(IParam.TITLE, adTitle)
                            .start();
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
