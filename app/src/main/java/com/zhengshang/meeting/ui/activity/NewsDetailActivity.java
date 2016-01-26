package com.zhengshang.meeting.ui.activity;

import android.content.Intent;
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
import com.zhengshang.meeting.service.UserService;
import com.zhengshang.meeting.ui.adapter.ListViewPagerAdapter;
import com.zhengshang.meeting.ui.fragment.BaseFragment;
import com.zhengshang.meeting.ui.fragment.CommentListFrament;
import com.zhengshang.meeting.ui.fragment.NewsDetailFragment;
import com.zhengshang.meeting.ui.vo.CommentVO;
import com.zhengshang.meeting.ui.vo.NewsDetailVO;
import com.zhengshang.meeting.ui.vo.NewsVO;
import com.zhengshang.meeting.ui.vo.ReplyVO;
import com.zhengshang.meeting.ui.vo.UserVO;

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
    @ViewById(R.id.tv_comment_tip)
    TextView tvCommentTip;
    @ViewById(R.id.tv_switch)
    TextView tvSwitch;
    @ViewById(R.id.btn_favorite)
    Button btnFavorite;
    @ViewById(R.id.btn_share)
    Button btnShare;

    private String newsId;
    private AnimationDrawable anim;
    private NewsService newsService;
    private NewsDetailVO detailVO;
    private List<CommentVO> commentList;
    private String html;
    private NewsDetailFragment newsDetailFragment;
    private CommentListFrament commentListFrament;
    private List<BaseFragment> fragmentList;
    private Bundle saveInstance;
    private CommentService commentService;
    private boolean isNews;
    private UserService userService;
    private int parentId;
    private CommentVO replyTo;
    private int groupPos;
    private int childPos;
    private UserVO userVO;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        this.saveInstance = savedInstanceState;
    }

    @AfterViews
    void init() {
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
        userService = new UserService(this);
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
    @Click(R.id.btn_refresh)
    void getNewsDetailAndComment() {
        startLoadingSelf();
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_NEWS_DETAIL) {
            @Override
            protected void doBackground() throws Exception {
                setReturnData(new Object[]{newsService.getNewsDetailFromWeb(newsId), commentService.getCommentList(newsId)});
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
                Object[] dataArr = (Object[]) data;
                detailVO = (NewsDetailVO) dataArr[0];
                if (dataArr[1] != null) {
                    commentList = (List<CommentVO>) dataArr[1];
                }
                refreshUI();
                break;
            case TaskAction.ACTION_FAVORITE_NEWS:// 收藏
                showToast("收藏成功");
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
            // 处理相关新闻
            if (!Utils.isEmpty(detailVO.getRelations())) {
                String relationHtml = "<div class=\"comment\">" +
                        "  <div class=\"comment_top\"></div>" +
                        "  <div class=\"comment_title\">相关新闻</div>";

                for (int i = 0; i < detailVO.getRelations().size(); i++) {
                    NewsVO vo = detailVO.getRelations().get(i);
                    relationHtml += "<dl onclick=\"window.news.onClick(" + i + ")\">" +
                            "    <dt><img src=\"" + vo.getIconPath() + "\" width=\"62\" height=\"62\"></dt>" +
                            "    <dd>" +
                            "      <h3><a class=\"name\">" + vo.getTitle() + "</a></h3>" +
                            "      <h4>" + vo.getSummary() + "</h4>" +
                            "    </dd>" +
                            "  </dl>";
                }
                relationHtml += "  <div class=\"line_02\"></div></div>";
                html = html.replace("@relation", relationHtml);
            } else {
                html = html.replace("@relation", "");
            }
            // 处理广告
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
        // 添加评论列表数据
        commentListFrament.setData(commentList);
    }


    @Override
    protected void onTaskFail(int action, String errorMessage) {
        switch (action) {
            case TaskAction.ACTION_GET_NEWS_DETAIL:
                stopLoadingSelf();
                showErrorMsg(errorMessage);
                break;
            case TaskAction.ACTION_FAVORITE_NEWS:// 收藏
                btnFavorite.setEnabled(true);
                showToast(errorMessage);
                break;
            default:
                super.onTaskFail(action, errorMessage);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            clickToComment();
        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            String content = data.getStringExtra(IParam.CONTENT);
            sendComment(content);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            String content = data.getStringExtra(IParam.CONTENT);
            sendReply(content);
        } else if (requestCode == 3 && resultCode == RESULT_OK) {
            clickToReply(groupPos, childPos);
        } else if (requestCode == 4 && resultCode == RESULT_OK) {
            favorite();
        }
    }

    @Click(R.id.tv_comment_tip)
    void clickToComment() {
        if (userService.checkLoginState()) {
            getUserInfo();
            // 跳转评论输入框
            CommentInputActivity_.intent(this).startForResult(1);
        } else {
            // 跳转登录
            LoginActivity_.intent(this).startForResult(0);
        }
    }

    /**
     * 发表评论
     *
     * @param content 评论内容
     */
    private void sendComment(final String content) {
        // 添加数据到UI
        CommentVO vo = new CommentVO();
        vo.setContent(content);
        vo.setUserId(userVO.getUserId());
        vo.setUserName(userVO.getNickName());
        vo.setCreateTime(Utils.formateCommentTime(System.currentTimeMillis()));
        if (commentList == null) {
            commentList = new ArrayList<>();
        }
        commentList.add(0, vo);
        commentListFrament.refreshUI(commentList);

        // 发送数据到服务器
        TaskManager.pushTask(new Task(TaskAction.ACTION_SEND_COMMENT) {
            @Override
            protected void doBackground() throws Exception {
//                setNeedCallBack(false);  测试时注释此行代码 以便输出错误信息
                commentService.sendComment(newsId, content);
            }
        }, this);
    }

    /**
     * 获取登录用户信息
     */
    private void getUserInfo() {
        if (userVO == null) {
            userVO = userService.getLoginUserInfo();
        }
    }


    @Click(R.id.tv_switch)
    void switchDetailAndComment() {
        if (isNews) {
            // 显示 评论
            vpMain.setCurrentItem(0);
        } else {
            vpMain.setCurrentItem(1);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            tvSwitch.setText("评论");
            isNews = false;
        } else if (position == 1) {
            tvSwitch.setText("原文");
            isNews = true;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 点击回复
     *
     * @param groupPos 评论组 pos
     * @param childPos 组内 pos
     */
    public void clickToReply(int groupPos, int childPos) {
        if (childPos == -1) {
            replyTo = commentList.get(groupPos);
        } else {
            replyTo = commentList.get(groupPos).getReplies().get(childPos);
        }
        this.parentId = replyTo.getId();
        // 记录 position
        this.groupPos = groupPos;
        this.childPos = childPos;
        if (userService.checkLoginState()) {
            getUserInfo();
            if (replyTo.getUserId().equals(userVO.getUserId())) {
                showToast("不能回复自己哦");
                return;
            }
            // 跳转 输入框
            CommentInputActivity_.intent(this).extra(IParam.HINT, "回复：" + replyTo.getUserName()).startForResult(2);
        } else {
            // 跳转登录
            LoginActivity_.intent(this).startForResult(3);
        }
    }

    /**
     * 执行回复
     *
     * @param content 回复内容
     */
    private void sendReply(final String content) {
        // 构建 临时 回复对象
        ReplyVO reply = new ReplyVO();
        reply.setUserId(userVO.getUserId());
        reply.setUserName(userVO.getNickName());
        reply.setReplyToUserId(replyTo.getUserId());
        reply.setReplyToUserName(replyTo.getUserName());
        reply.setContent(content);
        reply.setCreateTime(Utils.formateCommentTime(System.currentTimeMillis()));
        commentList.get(groupPos).getReplies().add(reply);
        commentListFrament.refreshUI(commentList);

        // 服务器通信
        TaskManager.pushTask(new Task(TaskAction.ACTION_SEND_REPLY) {
            @Override
            protected void doBackground() throws Exception {
//                setNeedCallBack(false); 测试时注释此行代码 以便输出错误信息
                commentService.sendReply(newsId, parentId, content);
            }
        }, this);
    }

    /**
     * 收藏
     */
    @Click(R.id.btn_favorite)
    void favorite() {
        if (detailVO != null) {
            if (userService.checkLoginState()) {
                btnFavorite.setEnabled(false);
                TaskManager.pushTask(new Task(TaskAction.ACTION_FAVORITE_NEWS) {
                    @Override
                    protected void doBackground() throws Exception {
                        userService.favoriteNews(detailVO.getId());
                    }
                }, this);
            } else {
                LoginActivity_.intent(this).startForResult(4);
            }
        }
    }

    @Click(R.id.btn_share)
    void share() {
        showToast("功能尚未开发");
    }
}
