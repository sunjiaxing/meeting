package com.sb.meeting.ui.fragment;

import android.annotation.SuppressLint;
import android.webkit.JavascriptInterface;

import com.sb.meeting.R;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.ui.activity.ImageActivity_;
import com.sb.meeting.ui.activity.NewsDetailActivity;
import com.sb.meeting.ui.component.CustomWebView;
import com.sb.meeting.ui.vo.ImageVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 新闻详情 fragment
 * Created by sun on 2016/1/8.
 */
@EFragment(R.layout.layout_news_webview)
public class NewsDetailFragment extends BaseFragment {

    @ViewById(R.id.webview_detail)
    CustomWebView webview;

    @AfterViews
    @SuppressLint("JavascriptInterface")
    void init() {
        // 给webview 注入图片点击 事件
        webview.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void onClick(String url) {
                List<ImageVO> list = new ArrayList<>();
                ImageVO vo = new ImageVO();
                vo.setUrl(url);
                list.add(vo);
                ImageActivity_.intent(getActivity()).extra(IParam.IMAGES, (Serializable) list).start();
            }
        }, "image");

        webview.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void onClick(int position) {
                ((NewsDetailActivity) getActivity()).toDetail(position);
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

    /**
     * 判断是否 可返回
     *
     * @return
     */
    public boolean canBack() {
        return webview == null || webview.canGoBack();
    }

    /**
     * webview后退
     */
    public void back() {
        webview.canGoBack();
    }
}
