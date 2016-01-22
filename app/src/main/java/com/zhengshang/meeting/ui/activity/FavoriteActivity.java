package com.zhengshang.meeting.ui.activity;

import android.graphics.drawable.AnimationDrawable;
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
import com.zhengshang.meeting.service.UserService;
import com.zhengshang.meeting.ui.adapter.FavoriteAdapter;
import com.zhengshang.meeting.ui.component.DragListView;
import com.zhengshang.meeting.ui.vo.FavoriteVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 收藏 activity
 * Created by sun on 2016/1/21.
 */
@EActivity(R.layout.layout_news_subject)
public class FavoriteActivity extends BaseActivity implements DragListView.OnRefreshLoadMoreListener {

    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.layout_loading)
    View layoutLoading;
    @ViewById(R.id.layout_error)
    View layoutError;
    @ViewById(R.id.tv_description)
    TextView tvErrorMsg;
    @ViewById(R.id.btn_refresh)
    Button btnErrorRefresh;
    @ViewById(R.id.lv_drag)
    DragListView listView;

    private AnimationDrawable anim;
    private List<FavoriteVO> favoriteList;
    private UserService userService;
    private FavoriteAdapter adapter;

    @AfterViews
    void init() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("我的收藏");
        anim = (AnimationDrawable) findViewById(R.id.iv_loading_in)
                .getBackground();
        listView.setPullType(DragListView.ListViewPullType.LV_ONLY_REFRESH);
        listView.setDividerHeight(0);
        listView.setFastScrollEnabled(true);
        listView.setOnRefreshListener(this);
        userService = new UserService(this);
        startLoadingSelf();
        getFavoriteList();
    }

    /**
     * 获取收藏列表
     */
    private void getFavoriteList() {
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_FAVORITE_LIST) {
            @Override
            protected void doBackground() throws Exception {
                setReturnData(userService.getFavoriteList());
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
        if (!Utils.isEmpty(favoriteList)) {
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
            case TaskAction.ACTION_GET_FAVORITE_LIST:// 获取收藏列表
                stopLoadingSelf();
                listView.onRefreshComplete();
                if (data != null) {
                    favoriteList = (List<FavoriteVO>) data;
                    refreshUI();
                }
                break;

        }
    }

    /**
     * 刷新界面显示
     */
    private void refreshUI() {
        if (adapter == null) {
            adapter = new FavoriteAdapter(this);
            adapter.setData(favoriteList);
            listView.setAdapter(adapter);
        } else {
            adapter.setData(favoriteList);
            adapter.notifyDataSetChanged();
        }
        listView.setLastRefreshTime(Utils.formateTime(System.currentTimeMillis(), false));
    }

    @Click(R.id.btn_refresh)
    void clickRefresh() {
        startLoadingSelf();
        getFavoriteList();
    }

    @Override
    protected void onTaskFail(int action, String errorMessage) {
        switch (action) {
            case TaskAction.ACTION_GET_FAVORITE_LIST:
                stopLoadingSelf();
                listView.onRefreshComplete();
                showErrorMsg(errorMessage);
                break;
            default:
                super.onTaskFail(action, errorMessage);
                break;
        }
    }

    @Override
    public void onRefresh() {
        getFavoriteList();
    }

    @Override
    public void onLoadMore() {

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

    @ItemClick(R.id.lv_drag)
    void onItemClick(int position) {
        FavoriteVO favoriteVO = favoriteList.get(position - 1);
        if (favoriteVO.getFavoriteType() == 1) {
            NewsSubjectActivity_.intent(this)
                    .extra(IParam.NEWS_ID, favoriteVO.getId())
                    .extra(IParam.TITLE, favoriteVO.getTitle())
                    .startForResult(0);
        }
    }
}
