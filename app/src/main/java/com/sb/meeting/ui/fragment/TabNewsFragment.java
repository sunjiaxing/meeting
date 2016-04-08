package com.sb.meeting.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taskmanager.Task;
import com.taskmanager.TaskManager;
import com.sb.meeting.R;
import com.sb.meeting.common.TaskAction;
import com.sb.meeting.common.Utils;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.service.NewsService;
import com.sb.meeting.service.UserService;
import com.sb.meeting.ui.activity.LoginActivity_;
import com.sb.meeting.ui.activity.NewsChannelActivity_;
import com.sb.meeting.ui.activity.UserCenterActivity_;
import com.sb.meeting.ui.adapter.ListViewPagerAdapter;
import com.sb.meeting.ui.component.ChannelGallery;
import com.sb.meeting.ui.vo.NewsChannelVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻资讯fragment
 */
@EFragment(R.layout.online_news)
public class TabNewsFragment extends BaseFragment implements
        ViewPager.OnPageChangeListener {
    // 以下注入组件
    @ViewById(R.id.vp_news_list)
    ViewPager mPager;
    @ViewById(R.id.right_handle_layout)
    RelativeLayout rightHandleLayout;
    @ViewById(R.id.iv_red_point)
    ImageView ivRedPoint;
    @ViewById(R.id.tv_handle_news_type_open)
    TextView tvHandleNewsTypeOpen;
    @ViewById(R.id.channel_gallery)
    ChannelGallery channelGallery;
    @ViewById(R.id.iv_user_info)
    ImageView ivUserCenter;
    @ViewById(R.id.tv_single_channel)
    TextView tvSingleChannel;

    private List<NewsChannelVO> newsTypes = new ArrayList<>();
    private NewsService newsService;
    private List<BaseFragment> fragmentList;
    private UserService userService;

    private static final int REQUEST_EDIT_NEWS_CHANNEL = 0x3221;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsService = new NewsService(getActivity());
        userService = new UserService(getActivity());
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
//        if (newsService.getNewsChannelUpdate()) {
//            ivRedPoint.setVisibility(View.VISIBLE);
//        } else {
//            ivRedPoint.setVisibility(View.GONE);
//        }
        mPager.addOnPageChangeListener(this);

    }

    /**
     * 刷新view
     */
    public void refreshView() {
        if (Utils.isEmpty(newsTypes)) {
            getNewsType();
            updateNewsChannel();
        }
    }

    @Click(R.id.iv_user_info)
    void toUserCenter() {
        if (userService.checkLoginState()) {
            UserCenterActivity_.intent(this).start();
        } else {
            LoginActivity_.intent(this).startForResult(1);
        }
    }

    /**
     * 联网获取新闻类型
     */
    private void getNewsType() {
        TaskManager.pushTaskWithQueue(new Task(TaskAction.ACTION_GET_NEWS_TYPE) {
            @Override
            protected void doBackground() throws Exception {
                // 获取用户栏目
                List<NewsChannelVO> userNewsTypes = newsService.getUserNewsTypes();
                if (Utils.isEmpty(userNewsTypes)) {
                    // 获取全部栏目
                    newsService.updateNewsType();
                    userNewsTypes = newsService.getUserNewsTypes();
                }
                setReturnData(userNewsTypes);
            }
        }, getActivity());
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
        }, getActivity());
    }

    /**
     * 刷新界面
     */
    private void refreshUI() {
        // 加载栏目
        updateGallery(newsTypes);
        // 判断栏目更新
//        notifyNewsChannelUpdate(newsService.getNewsChannelUpdate());
        // 加载新闻
        ListViewPagerAdapter listViewPagerAdapter = new ListViewPagerAdapter(
                getChildFragmentManager());
        listViewPagerAdapter.setData(createFragment2Show(newsTypes.size()));
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
     * @param count fragment数量
     * @return fragmentList
     */
    private List<BaseFragment> createFragment2Show(int count) {
        fragmentList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            NewsPagerItemFragment itemFragment = (NewsPagerItemFragment) getChildFragmentManager()
                    .findFragmentByTag(getFragmentTag(i));
            if (itemFragment != null) {
                fragmentList.add(itemFragment);
            } else {
                fragmentList.add(new NewsPagerItemFragment_());
            }
        }
        return fragmentList;
    }

    /**
     * 构建fragmentTag
     *
     * @param position 位置
     * @return tag
     */
    private String getFragmentTag(int position) {
        return "android:switcher:" + R.id.vp_news_list + ":" + position;
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
     * @param data voList
     */
    public void updateGallery(List<NewsChannelVO> data) {
        if (data != null && data.size() > 0) {
            if (data.size() == 1) {
                channelGallery.setVisibility(View.GONE);
                tvSingleChannel.setVisibility(View.VISIBLE);
                tvSingleChannel.setText(data.get(0).getName());
            } else {
                // 有栏目数据时显示操作按钮
                channelGallery.setVisibility(View.VISIBLE);
                tvSingleChannel.setVisibility(View.GONE);
                channelGallery.setData(data);
            }
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
     * 分类展开 按钮
     */
    @Click(R.id.tv_handle_news_type_open)
    void clickTypeOpen() {
        NewsChannelActivity_.intent(this).startForResult(REQUEST_EDIT_NEWS_CHANNEL);
//        Intent intent = new Intent(this, TestActivity_.class);
//        intent.putExtra(IParam.URL,"from news");
//        startActivityForResult(intent, 0);
    }


    void afterSaveNewsChannel() {
        // 清除原fragment在内存中的缓存
        FragmentTransaction ft = getChildFragmentManager()
                .beginTransaction();
        for (BaseFragment item : fragmentList) {
            ft.remove(item);
        }
        ft.commit();
        // 刷新界面
        refreshUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_NEWS_CHANNEL && resultCode == Activity.RESULT_OK) {
            // 操作栏目
            newsTypes = (List<NewsChannelVO>) data.getSerializableExtra(IParam.LIST);
            afterSaveNewsChannel();
        } else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            toUserCenter();
        }
    }

    public void onTaskSuccess(int action, Object data) {
        Object[] dataArray;
        int fragmentId;
        switch (action) {
            case TaskAction.ACTION_GET_NEWS_TYPE:// 获取新闻分类成功
                if (data != null) {
                    newsTypes = (List<NewsChannelVO>) data;
                    refreshUI();
                }
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

    public void onTaskFail(int action, String errorMessage) {
        if (action == TaskAction.ACTION_GET_NEWS_TYPE) {
            showToast(errorMessage);
            return;
        }
        NewsPagerItemFragment fra;
        for (BaseFragment base : fragmentList) {
            fra = (NewsPagerItemFragment) base;
            fra.onTaskFail(action, errorMessage);
        }
    }
}
