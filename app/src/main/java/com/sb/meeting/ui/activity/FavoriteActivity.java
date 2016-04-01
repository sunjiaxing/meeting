package com.sb.meeting.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.taskmanager.Task;
import com.taskmanager.TaskManager;
import com.sb.meeting.R;
import com.sb.meeting.common.TaskAction;
import com.sb.meeting.common.Utils;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.service.UserService;
import com.sb.meeting.ui.adapter.FavoriteAdapter;
import com.sb.meeting.ui.component.RefreshListView;
import com.sb.meeting.ui.vo.FavoriteVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ItemLongClick;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 收藏 activity
 * Created by sun on 2016/1/21.
 */
@EActivity(R.layout.layout_news_subject)
public class FavoriteActivity extends BaseActivity implements RefreshListView.OnRefreshLoadMoreListener {

    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.layout_loading_subject)
    View layoutLoading;
    @ViewById(R.id.layout_error_subject)
    View layoutError;
    @ViewById(R.id.tv_description)
    TextView tvErrorMsg;
    @ViewById(R.id.btn_refresh)
    Button btnErrorRefresh;
    @ViewById(R.id.lv_drag)
    RefreshListView listView;

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
        listView.setOnRefreshListener(this);
        listView.setLastUpdateTimeRelateObject(this);
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
                } else {
                    showErrorMsg("暂无收藏信息");
                }
                break;
            case TaskAction.ACTION_DELETE_ALL_FAVORITE:
            case TaskAction.ACTION_DELETE_FAVORITE:
                stopLoading();
                showToast("删除成功");
                getFavoriteList();
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
        listView.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_REMOVE);
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
            case TaskAction.ACTION_DELETE_ALL_FAVORITE:
            case TaskAction.ACTION_DELETE_FAVORITE:
                stopLoading();
                showToast(errorMessage);
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
        FavoriteVO favoriteVO = favoriteList.get(position);
        if (favoriteVO.getFavoriteType() == 1) {
            NewsDetailActivity_.intent(this)
                    .extra(IParam.NEWS_ID, favoriteVO.getNewsId())
                    .extra(IParam.TITLE, favoriteVO.getTitle())
                    .startForResult(0);
        }
    }

    @ItemLongClick(R.id.lv_drag)
    void onItemLongClick(int position) {
        final FavoriteVO favoriteVO = favoriteList.get(position);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setItems(new String[]{"删除选中数据", "删除全部数据"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:// 删除当前
                        deleteFavorite(favoriteVO.getFavoriteId());
                        break;
                    case 1:// 删除全部
                        deleteFavorite(null);
                        break;
                }
            }
        }).show();


    }

    /**
     * 删除收藏
     *
     * @param id 收藏id
     */
    private void deleteFavorite(final String id) {
        startLoading("删除中...");
        if (id == null) {
            // 删除全部
            TaskManager.pushTask(new Task(TaskAction.ACTION_DELETE_ALL_FAVORITE) {
                @Override
                protected void doBackground() throws Exception {
                    userService.deleteAllFavorite();
                }
            }, this);
        } else {
            // 删除选中
            TaskManager.pushTask(new Task(TaskAction.ACTION_DELETE_FAVORITE) {
                @Override
                protected void doBackground() throws Exception {
                    userService.deleteFavoriteById(id);
                }
            }, this);
        }
    }
}
