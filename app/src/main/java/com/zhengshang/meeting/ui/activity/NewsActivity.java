package com.zhengshang.meeting.ui.activity;


import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taskmanager.Task;
import com.taskmanager.TaskManager;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.TaskAction;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.service.NewsService;
import com.zhengshang.meeting.ui.adapter.ListViewPagerAdapter;
import com.zhengshang.meeting.ui.adapter.MoreChannelAdapter;
import com.zhengshang.meeting.ui.adapter.MyChannelAdapter;
import com.zhengshang.meeting.ui.component.MyGallery;
import com.zhengshang.meeting.ui.fragment.NewsPagerItemFragment;
import com.zhengshang.meeting.ui.fragment.NewsPagerItemFragment_;
import com.zhengshang.meeting.ui.vo.NewsChannelVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
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
    @ViewById(R.id.gallery1)
    MyGallery myGallery;
    @ViewById(R.id.right_handle_layout)
    RelativeLayout rightHandleLayout;
    @ViewById(R.id.iv_red_point)
    ImageView ivRedPoint;
    @ViewById(R.id.tv_handle_news_type_open)
    TextView tvHandleNewsTypeOpen;
    @ViewById(R.id.tv_handle_news_type_close)
    TextView tvHandleNewsTypeClose;
    @ViewById(R.id.layout_handle_news_type)
    View handleNewsTypeView;
    @ViewById(R.id.gv_my_channel)
    GridView gvMyChannel;
    @ViewById(R.id.gv_more_channel)
    GridView gvMoreChannel;
    @ViewById(R.id.menuLayout)
    View menuBgLayout;
    @ViewById(R.id.btn_back_main)
    LinearLayout btnShouYe;

    private ListViewPagerAdapter listViewPagerAdapter;
    private List<NewsChannelVO> newsTypes = new ArrayList<>();
    /**
     * 系统全部新闻栏目信息
     */
    private List<NewsChannelVO> allNewsTypes = new ArrayList<>();
    /**
     * 我的栏目适配器
     */
    private MyChannelAdapter myChannelAdapter;
    /**
     * 更多栏目适配器
     */
    private MoreChannelAdapter moreChannelAdapter;
    private NewsService newsService;
    private boolean isChange = false;
    private List<NewsPagerItemFragment> fragmentList;
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
        myGallery.setOnItemClickListener(new MyGallery.TlcyGalleryListener() {

            @Override
            public void onState(int state) {
            }

            @Override
            public boolean onItemClick(int position) {
                if (mPager != null) {
                    mPager.setCurrentItem(position);
                }
                return !Utils.isEmpty(newsTypes);
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
        if (Utils.isEmpty(allNewsTypes) || Utils.isEmpty(newsTypes)) {
            getNewsType();
            newsService.updateNewsType();
        } else {
            refreshUI(saveInstance);
        }
    }

    /**
     * 联网获取新闻类型
     */
    private void getNewsType() {
        TaskManager.pushTaskWithQueue(new Task(TaskAction.ACTION_GET_NEWS_TYPE, getLocalClassName()) {
            @Override
            protected void doBackground() throws Exception {
                setReturnData(new Object[]{newsService.getAllNewsTypes(), newsService.getUserNewsTypes()});
            }
        }, this);
    }

    /***
     * 刷新界面
     *
     * @param savedInstanceState
     */
    private void refreshUI(Bundle savedInstanceState) {
        // 加载栏目
        updateGallery(newsTypes, 0);
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
            fragmentList.get(0).refreshCurrentView(newsTypes.get(0), 0);
        }
    }

    /**
     * 构建要显示的 空 fragment
     *
     * @param count
     * @param savedInstanceState
     * @return
     */
    private List<NewsPagerItemFragment> createFragment2Show(int count,
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
        closeChannel();
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
     * @param index
     */
    public void updateGallery(List<NewsChannelVO> data, int index) {
        if (data != null && data.size() > 0) {
            // 有栏目数据时显示操作按钮
            rightHandleLayout.setVisibility(View.VISIBLE);
            myGallery.setAdapter(R.layout.item, R.mipmap.menu_selected, data);
            myGallery.setSelected(index);
            if (mPager != null) {
                mPager.setCurrentItem(index);
            }
            myGallery.scrollToSelected();
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
        if (myGallery != null) {
            myGallery.setSelected(position);
            myGallery.scrollToSelected();
        }
        if (!Utils.isEmpty(fragmentList)) {
            NewsPagerItemFragment itemFragment = fragmentList.get(position);
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
    }

    /**
     * 分类展开 按钮
     */
    @Click(R.id.tv_handle_news_type_open)
    void clickTypeOpen() {
        // 构建显示数据
        // 创建适配器 并设置
        myChannelAdapter = new MyChannelAdapter(this);
        myChannelAdapter.setData(newsTypes);
        moreChannelAdapter = new MoreChannelAdapter(this);
        moreChannelAdapter.setData(getMoreChannelList());
        gvMyChannel.setAdapter(myChannelAdapter);
        gvMoreChannel.setAdapter(moreChannelAdapter);
        // 显示编辑区域
        handleNewsTypeView.setVisibility(View.VISIBLE);
        newsService.setNewsChannelUpdate(false);
    }

    /**
     * 获取更多栏目列表
     *
     * @return
     */
    public List<NewsChannelVO> getMoreChannelList() {
        ArrayList<NewsChannelVO> moreChannelList = null;
        if (!Utils.isEmpty(allNewsTypes) && newsTypes != null) {
            moreChannelList = new ArrayList<>();
            moreChannelList.addAll(allNewsTypes);
            for (NewsChannelVO my : newsTypes) {
                for (NewsChannelVO more : moreChannelList) {
                    if (more.getTypeId().equals(my.getTypeId())) {
                        moreChannelList.remove(more);
                        break;
                    }
                }
            }
        }
        return moreChannelList;
    }

    /***
     * 关闭栏目编辑菜单
     */
    @Click({R.id.tv_handle_news_type_close, R.id.layout_handle_news_type})
    void closeChannel() {
        // 判断栏目编辑区状态
        if (handleNewsTypeView.getVisibility() != View.GONE) {
            // 隐藏红色圆点提示
            notifyNewsChannelUpdate(newsService.getNewsChannelUpdate());
            // 隐藏编辑区域
            handleNewsTypeView.setVisibility(View.GONE);
            if (isChange) {
                // 整理栏目索引
                tidyNewsTypeIndex(newsTypes);
                // 清除原fragment在内存中的缓存
                FragmentTransaction ft = getSupportFragmentManager()
                        .beginTransaction();
                for (NewsPagerItemFragment item : fragmentList) {
                    ft.remove(item);
                }
                ft.commit();
                // 刷新界面
                refreshUI(null);
                // 整理数据库中的栏目数据
                TaskManager.pushTask(new Task(TaskAction.ACTION_SAVE_NEWS_TYPE, this.getLocalClassName()) {
                    @Override
                    protected void doBackground() {
                        // 不需要返回处理
                        setNeedCallBack(false);
                        // 执行操作
                        newsService.saveNewsType(newsTypes);
                    }
                }, this);
                isChange = false;
            }
        }
    }

    /**
     * 整理我的栏目索引
     *
     * @param types
     */
    private void tidyNewsTypeIndex(List<NewsChannelVO> types) {
        if (!Utils.isEmpty(types)) {
            for (int i = 0; i < types.size(); i++) {
                types.get(i).setPosition(i);
            }
        }
    }

    /**
     * 移除已选栏目
     *
     * @param item
     */
    @ItemClick(R.id.gv_my_channel)
    void removeChannel(NewsChannelVO item) {
        // 点击我的栏目
        // 判断是否锁定
        if (item.getIsLock() == 1) {
            // 锁定
            return;
        }
        // 将选中项从我的栏目中移除并更新数据
        newsTypes.remove(item);
        myChannelAdapter.setData(newsTypes);
        moreChannelAdapter.setData(getMoreChannelList());
        myChannelAdapter.notifyDataSetChanged();
        moreChannelAdapter.notifyDataSetChanged();
        isChange = true;
    }

    /**
     * 添加新栏目
     *
     * @param item
     */
    @ItemClick(R.id.gv_more_channel)
    void addNewChannel(NewsChannelVO item) {
        // 点击更多栏目
        // 判断是否锁定
        if (item.getIsLock() == 1) {
            // 锁定
            return;
        }
        // 将选中项添加到我的栏目中
        newsTypes.add(item);
        myChannelAdapter.setData(newsTypes);
        moreChannelAdapter.setData(getMoreChannelList());
        myChannelAdapter.notifyDataSetChanged();
        moreChannelAdapter.notifyDataSetChanged();
        isChange = true;
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
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Click(R.id.iv_back)
    void back() {
        finish();
    }

    @Override
    protected void onTaskSuccess(int action, Object data) {
        Object[] dataArray = null;
        int fragmentId;
        switch (action) {
            case TaskAction.ACTION_GET_NEWS_TYPE:// 获取新闻分类成功
                dataArray = (Object[]) data;
                allNewsTypes = (List<NewsChannelVO>) dataArray[0];
                newsTypes = (List<NewsChannelVO>) dataArray[1];
                refreshUI(saveInstance);
                break;
            case TaskAction.ACTION_GET_NEWS_FROM_DB:// 获取缓存数据
            case TaskAction.ACTION_REFRESH_NEWS:// 刷新
            case TaskAction.ACTION_LOAD_MORE_NEWS:// 加载更多
                dataArray = (Object[]) data;
                fragmentId = Integer.parseInt(String.valueOf(dataArray[0]));
                for (NewsPagerItemFragment fra : fragmentList) {
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
        switch (action) {
            case TaskAction.ACTION_GET_NEWS_FROM_DB:
                for (NewsPagerItemFragment fra : fragmentList) {
                    fra.onTaskFail(action, errorMessage);
                }
                break;
            default:
                super.onTaskFail(action, errorMessage);
                break;
        }
    }
}
