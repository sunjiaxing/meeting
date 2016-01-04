package com.zhengshang.meeting.ui.activity;

import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.taskmanager.Task;
import com.taskmanager.TaskManager;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.TaskAction;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.service.NewsService;
import com.zhengshang.meeting.ui.adapter.DragChannelAdapter;
import com.zhengshang.meeting.ui.adapter.MoreChannelAdapter;
import com.zhengshang.meeting.ui.adapter.MyChannelAdapter;
import com.zhengshang.meeting.ui.component.DragGridView;
import com.zhengshang.meeting.ui.fragment.NewsPagerItemFragment;
import com.zhengshang.meeting.ui.vo.NewsChannelVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sun on 2016/1/4.
 */
@EActivity(R.layout.layout_news_channel)
public class NewsChannelActivity extends BaseActivity {

    // 以下注入组件
    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.iv_right)
    ImageView ivRight;
    @ViewById(R.id.btn_right)
    Button btnRight;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.gv_my_channel)
    DragGridView gvMyChannel;
    @ViewById(R.id.gv_more_channel)
    GridView gvMoreChannel;
    private NewsService newsService;

    private List<NewsChannelVO> allNewsTypes = new ArrayList<>();
    private List<NewsChannelVO> newsTypes = new ArrayList<>();

    private DragChannelAdapter myChannelAdapter;
    private MoreChannelAdapter moreChannelAdapter;
    private boolean isChange = false;

    @AfterViews
    void init() {
        newsService = new NewsService(this);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("栏目管理");
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setBackground(null);
        btnRight.setText("完成");
        getNewsType();
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

    @Override
    protected void onTaskSuccess(int action, Object data) {
        Object[] dataArray;
        switch (action) {
            case TaskAction.ACTION_GET_NEWS_TYPE:
                dataArray = (Object[]) data;
                allNewsTypes = (List<NewsChannelVO>) dataArray[0];
                newsTypes = (List<NewsChannelVO>) dataArray[1];
                refreshUI();
                break;
            case TaskAction.ACTION_SAVE_NEWS_TYPE:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    private void refreshUI() {
        myChannelAdapter = new DragChannelAdapter(this);
        myChannelAdapter.setData(newsTypes);
        moreChannelAdapter = new MoreChannelAdapter(this);
        moreChannelAdapter.setData(getMoreChannelList());
        gvMyChannel.setAdapter(myChannelAdapter);
        gvMoreChannel.setAdapter(moreChannelAdapter);
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

    @ItemClick(R.id.gv_my_channel)
    void deleteChannel(NewsChannelVO item) {
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

    @Click(R.id.btn_right)
    void complete() {
        if (isChange) {
            // 整理栏目索引
            tidyNewsTypeIndex(newsTypes);
            // 整理数据库中的栏目数据
            TaskManager.pushTask(new Task(TaskAction.ACTION_SAVE_NEWS_TYPE, this.getLocalClassName()) {
                @Override
                protected void doBackground() {
                    // 不需要返回处理
//                    setNeedCallBack(false);
                    // 执行操作
                    newsService.saveNewsType(newsTypes);
                }
            }, this);
            isChange = false;
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
}
