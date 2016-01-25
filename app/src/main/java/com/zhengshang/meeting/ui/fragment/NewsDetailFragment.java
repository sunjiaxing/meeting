package com.zhengshang.meeting.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;

import com.taskmanager.LogUtils;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.ui.component.CustomerWebview;

/**
 * 新闻详情 fragment
 * Created by sun on 2016/1/8.
 */
public class NewsDetailFragment extends BaseFragment {

    private CustomerWebview webview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_news_webview, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webview = (CustomerWebview) view.findViewById(R.id.webview_detail);
        // 给webview 注入图片点击 事件
        webview.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void onClick(String url) {
                LogUtils.e(url);
            }
        }, "image");

        webview.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void onClick(int position) {
                LogUtils.e("position：" + position);
            }
        }, "news");
    }

    /**
     * 设置 HTML
     *
     * @param html 新闻详情 HTML
     */
    public void setHtml(String html) {
        if (webview != null) {
            webview.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        }
    }

    /**
     * 设置 url
     *
     * @param url 新闻详情 url
     */
    public void setUrl(String url) {
        webview.loadUrl(url);
    }
}
