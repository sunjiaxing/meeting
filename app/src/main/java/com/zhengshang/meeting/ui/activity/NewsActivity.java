package com.zhengshang.meeting.ui.activity;


import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taskmanager.Task;
import com.taskmanager.TaskManager;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.TaskAction;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.remote.IParam;
import com.zhengshang.meeting.service.NewsService;
import com.zhengshang.meeting.service.UserService;
import com.zhengshang.meeting.ui.adapter.ListViewPagerAdapter;
import com.zhengshang.meeting.ui.component.ChannelGallery;
import com.zhengshang.meeting.ui.fragment.BaseFragment;
import com.zhengshang.meeting.ui.fragment.NewsPagerItemFragment;
import com.zhengshang.meeting.ui.fragment.NewsPagerItemFragment_;
import com.zhengshang.meeting.ui.vo.NewsChannelVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻资讯fragment
 */
@EActivity(R.layout.online_news)
public class NewsActivity extends BaseActivity implements
        ViewPager.OnPageChangeListener {
    // 以下注入组件
    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.iv_right)
    ImageView ivRight;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.vp_news_list)
    ViewPager mPager;
    @ViewById(R.id.right_handle_layout)
    RelativeLayout rightHandleLayout;
    @ViewById(R.id.iv_red_point)
    ImageView ivRedPoint;
    @ViewById(R.id.tv_handle_news_type_open)
    TextView tvHandleNewsTypeOpen;
    @ViewById(R.id.menuLayout)
    View menuBgLayout;
    @ViewById(R.id.btn_back_main)
    LinearLayout btnShouYe;
    @ViewById(R.id.channel_gallery)
    ChannelGallery channelGallery;

    private ListViewPagerAdapter listViewPagerAdapter;
    private List<NewsChannelVO> newsTypes = new ArrayList<>();
    private NewsService newsService;
    private List<BaseFragment> fragmentList;
    private Bundle saveInstance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.saveInstance = savedInstanceState;
        newsService = new NewsService(this);
    }

    @AfterViews
    void init() {
        // 设置点击事件
        channelGallery.setOnItemClickListener(new ChannelGallery.ItemClickListener() {

            @Override
            public void onItemClick(int position) {
                if (mPager != null) {
                    mPager.setCurrentItem(position);
                }
            }
        });
        rightHandleLayout.setVisibility(View.GONE);
        if (newsService.getNewsChannelUpdate()) {
            ivRedPoint.setVisibility(View.VISIBLE);
        } else {
            ivRedPoint.setVisibility(View.GONE);
        }
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.news));
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setBackgroundResource(R.drawable.btn_more);

        mPager.addOnPageChangeListener(this);
        if (Utils.isEmpty(newsTypes)) {
            getNewsType();
            updateNewsChannel();
        } else {
            refreshUI(saveInstance);
        }
    }

    /**
     * 联网获取新闻类型
     */
    private void getNewsType() {
        TaskManager.pushTaskWithQueue(new Task(TaskAction.ACTION_GET_NEWS_TYPE) {
            @Override
            protected void doBackground() throws Exception {
                newsService.getAllNewsTypes();
                setReturnData(newsService.getUserNewsTypes());
            }
        }, this);
    }

    /**
     * 更新栏目
     */
    private void updateNewsChannel() {
        TaskManager.pushTaskWithQueue(new Task(TaskAction.ACTION_UPDATE_NEWS_CHANNEL) {
            @Override
            protected void doBackground() throws Exception {
                setNeedCallBack(false);
                newsService.updateNewsType();
            }
        }, this);
    }

    /**
     * 刷新界面
     *
     * @param savedInstanceState
     */
    private void refreshUI(Bundle savedInstanceState) {
        // 加载栏目
        updateGallery(newsTypes);
        // 判断栏目更新
        notifyNewsChannelUpdate(newsService.getNewsChannelUpdate());
        // 加载新闻
        listViewPagerAdapter = new ListViewPagerAdapter(
                getSupportFragmentManager());
        listViewPagerAdapter.setData(createFragment2Show(newsTypes.size(),
                savedInstanceState));
        mPager.setAdapter(listViewPagerAdapter);
        mPager.setOffscreenPageLimit(newsTypes.size());
        if (mPager.getCurrentItem() != 0) {
            mPager.setCurrentItem(0);
        } else {
            ((NewsPagerItemFragment) fragmentList.get(0)).refreshCurrentView(newsTypes.get(0), 0);
        }
    }

    /**
     * 构建要显示的 空 fragment
     *
     * @param count
     * @param savedInstanceState
     * @return
     */
    private List<BaseFragment> createFragment2Show(int count,
                                                   Bundle savedInstanceState) {
        fragmentList = new ArrayList<>();
        if (savedInstanceState == null) {
            for (int i = 0; i < count; i++) {
                fragmentList.add(new NewsPagerItemFragment_());
            }
        } else {
            for (int i = 0; i < count; i++) {
                NewsPagerItemFragment itemFragment = (NewsPagerItemFragment) getSupportFragmentManager()
                        .findFragmentByTag(getFragmentTag(i));
                fragmentList.add(itemFragment);
            }
        }
        return fragmentList;
    }

    /**
     * 获取fragment内存缓存
     *
     * @param position
     * @return
     * @author sun 下午5:33:51
     */
    private String getFragmentTag(int position) {
        return "android:switcher:" + R.id.vp_news_list + ":" + position;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controlMenuBg(false);
        return super.onTouchEvent(event);
    }

    /***
     * 通知新闻栏目更新
     */
    public void notifyNewsChannelUpdate(boolean show) {
        if (show) {
            // 更新了
            ivRedPoint.setVisibility(View.VISIBLE);
        } else {
            // 未更新
            ivRedPoint.setVisibility(View.GONE);
        }
    }

    /**
     * 更新新闻分类
     *
     * @param data
     */
    public void updateGallery(List<NewsChannelVO> data) {
        if (data != null && data.size() > 0) {
            // 有栏目数据时显示操作按钮
            channelGallery.setData(data);
            rightHandleLayout.setVisibility(View.VISIBLE);
            if (mPager != null) {
                mPager.setCurrentItem(0);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int position) {

    }

    @Override
    public void onPageScrolled(int position, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        if (channelGallery != null) {
            channelGallery.setSelected(position);
        }
        if (!Utils.isEmpty(fragmentList)) {
            NewsPagerItemFragment itemFragment = (NewsPagerItemFragment) fragmentList.get(position);
            itemFragment.refreshCurrentView(newsTypes.get(position), position);
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

    /**
     * 右侧按钮点击事件
     */
    @Click(R.id.iv_right)
    void clickRightButton() {
        controlMenuBg(true);
    }

    /**
     * 菜单点击事件
     */
    @Click(R.id.menuLayout)
    void clickMenuLayout() {
        controlMenuBg(false);
    }

    /**
     * 返回主页 点击事件
     */
    @Click(R.id.btn_back_main)
    void clickBack2Main() {
        // TODO returnToMain();
        controlMenuBg(false);
        // 暂定退出登录功能
        new UserService(this).logout();
        showToast("退出成功");
    }

    /**
     * 分类展开 按钮
     */
    @Click(R.id.tv_handle_news_type_open)
    void clickTypeOpen() {
        startActivityForResult(new Intent(this, NewsChannelActivity_.class), 0);
//        startActivityForResult(new Intent(this, TestActivity_.class), 0);
    }


    void afterSaveNewsChannel() {
        // 清除原fragment在内存中的缓存
        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        for (BaseFragment item : fragmentList) {
            ft.remove(item);
        }
        ft.commit();
        // 刷新界面
        refreshUI(null);
    }

    @Override
    public void onResume() {
        super.onResume();
//		TODO StatService.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//		TODO StatService.onPause(this);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            // 操作栏目
            newsTypes = (List<NewsChannelVO>) data.getSerializableExtra(IParam.LIST);
            afterSaveNewsChannel();
        }
    }

    @Click(R.id.iv_back)
    void back() {
        finish();
    }

    @Override
    protected void onTaskSuccess(int action, Object data) {
        Object[] dataArray;
        int fragmentId;
        switch (action) {
            case TaskAction.ACTION_GET_NEWS_TYPE:// 获取新闻分类成功
                newsTypes = (List<NewsChannelVO>) data;
                refreshUI(saveInstance);
                break;
            case TaskAction.ACTION_GET_NEWS_FROM_DB:// 获取缓存数据
            case TaskAction.ACTION_REFRESH_NEWS:// 刷新
            case TaskAction.ACTION_LOAD_MORE_NEWS:// 加载更多
                dataArray = (Object[]) data;
                fragmentId = Integer.parseInt(String.valueOf(dataArray[0]));
                NewsPagerItemFragment fra;
                for (BaseFragment base : fragmentList) {
                    fra = (NewsPagerItemFragment) base;
                    Log.d("=-==============", "id:[" + fra.getId() + "],hashCode:[" + fra.hashCode() + "]");
                    if (fra.hashCode() == fragmentId) {
                        fra.onTaskSuccess(action, dataArray[1]);
                    }
                }
                break;
        }
    }

    @Override
    protected void onTaskFail(int action, String errorMessage) {
        NewsPagerItemFragment fra;
        for (BaseFragment base : fragmentList) {
            fra = (NewsPagerItemFragment) base;
            fra.onTaskFail(action, errorMessage);
        }
    }
}
