package com.sb.meeting.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.common.TaskAction;
import com.sb.meeting.common.Utils;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.service.NewsService;
import com.sb.meeting.ui.adapter.ListViewPagerAdapter;
import com.sb.meeting.ui.component.ChannelGallery;
import com.sb.meeting.ui.fragment.BaseFragment;
import com.sb.meeting.ui.fragment.NewsPagerItemFragment;
import com.sb.meeting.ui.fragment.NewsPagerItemFragment_;
import com.sb.meeting.ui.vo.NewsChannelVO;
import com.taskmanager.Task;
import com.taskmanager.TaskManager;

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
    @ViewById(R.id.vp_news_list)
    ViewPager mPager;
    @ViewById(R.id.right_handle_layout)
    RelativeLayout rightHandleLayout;
    @ViewById(R.id.iv_red_point)
    ImageView ivRedPoint;
    @ViewById(R.id.channel_gallery)
    ChannelGallery channelGallery;
    @ViewById(R.id.tv_single_channel)
    TextView tvSingleChannel;

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
                // 获取用户栏目
                List<NewsChannelVO> userNewsTypes = newsService.getUserNewsTypes();
                if (Utils.isEmpty(userNewsTypes)) {
                    // 获取全部栏目
                    newsService.updateNewsType();
                    userNewsTypes = newsService.getUserNewsTypes();
                }
                setReturnData(userNewsTypes);
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
//        notifyNewsChannelUpdate(newsService.getNewsChannelUpdate());
        // 加载新闻
        ListViewPagerAdapter listViewPagerAdapter = new ListViewPagerAdapter(
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
     * @param data
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
        NewsChannelActivity_.intent(this).startForResult(0);
//        Intent intent = new Intent(this, TestActivity_.class);
//        intent.putExtra(IParam.URL,"from news");
//        startActivityForResult(intent, 0);
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

    void back() {
        finish();
    }

    @Override
    protected void onTaskSuccess(int action, Object data) {
        Object[] dataArray;
        int fragmentId;
        switch (action) {
            case TaskAction.ACTION_GET_NEWS_TYPE:// 获取新闻分类成功
                if (data != null) {
                    newsTypes = (List<NewsChannelVO>) data;
                    refreshUI(saveInstance);
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

    @Override
    protected void onTaskFail(int action, String errorMessage) {
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
