package com.zhengshang.meeting.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.taskmanager.LogUtils;
import com.taskmanager.Task;
import com.taskmanager.TaskManager;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.BonConstants;
import com.zhengshang.meeting.common.TaskAction;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.remote.IParam;
import com.zhengshang.meeting.service.GoodsService;
import com.zhengshang.meeting.service.UserService;
import com.zhengshang.meeting.ui.activity.GoodsDetailAndPreviewActivity;
import com.zhengshang.meeting.ui.activity.GoodsDetailAndPreviewActivity_;
import com.zhengshang.meeting.ui.activity.InputGoodsNameActivity_;
import com.zhengshang.meeting.ui.activity.InputOtherGoodsInfoActivity_;
import com.zhengshang.meeting.ui.activity.LoginActivity_;
import com.zhengshang.meeting.ui.adapter.GoodsListAdapter;
import com.zhengshang.meeting.ui.component.RefreshListView;
import com.zhengshang.meeting.ui.vo.GoodsVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ItemLongClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * 易物 列表
 * Created by sun on 2016/2/19.
 */
@EFragment(R.layout.layout_refresh_listview)
public class TabGoodsListFragment extends BaseFragment implements RefreshListView.OnRefreshLoadMoreListener {

    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.btn_right)
    Button btnRight;
    @ViewById(R.id.layout_loading_refresh)
    View layoutLoading;
    @ViewById(R.id.layout_error)
    View layoutError;
    @ViewById(R.id.tv_description)
    TextView tvErrorMsg;
    @ViewById(R.id.btn_refresh)
    Button btnErrorRefresh;
    @ViewById(R.id.lv_drag)
    RefreshListView listView;
    @ViewById(R.id.iv_loading_in)
    ImageView ivLoading;

    private AnimationDrawable anim;
    private List<GoodsVO> goodsList = new ArrayList<>();
    private GoodsService goodsService;
    private UserService userService;
    private int pageIndex;
    private GoodsListAdapter adapter;

    private static final int REQUEST_CODE_INPUT_GOODS_NAME = 0;
    private static final int REQUEST_CODE_LOGIN = 1;
    private static final int REQUEST_CODE_PUBLISH = 2;

    @AfterViews
    void init() {
        anim = (AnimationDrawable) ivLoading.getBackground();
        ivBack.setVisibility(View.GONE);
        tvTitle.setText("商品列表");
        listView.setOnRefreshListener(this);
        listView.setLastUpdateTimeRelateObject(this);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setBackgroundColor(Color.TRANSPARENT);
        btnRight.setText("发布物品");
        goodsService = new GoodsService(getActivity());
        userService = new UserService(getActivity());
    }

    /**
     * 刷新 view
     */
    public void refreshView() {
        if (Utils.isEmpty(goodsList)) {
            startLoadingSelf();
            getGoodsFromDB();
        }
    }

    /**
     * 从数据库缓存中获取数据
     */
    private void getGoodsFromDB() {
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_GOODS_LIST_FROM_DB) {
            @Override
            protected void doBackground() throws Exception {
                setReturnData(goodsService.getGoodsListFromDB());
            }
        }, getActivity());
    }

    /**
     * 获取物品列表
     */
    private void getGoodsList() {
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_GOODS_LIST) {
            @Override
            protected void doBackground() throws Exception {
                pageIndex = 0;
                setReturnData(goodsService.getGoodsList(pageIndex));
            }
        }, getActivity());
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
        if (!Utils.isEmpty(goodsList)) {
            showToast(msg);
        } else {
            layoutError.setVisibility(View.VISIBLE);
            btnErrorRefresh.setText("刷新");
            tvErrorMsg.setText(msg);
        }
    }

    @Click(R.id.btn_right)
    void clickToInputGoodsName() {
        if (userService.checkLoginState()) {
            InputGoodsNameActivity_.intent(this).startForResult(REQUEST_CODE_INPUT_GOODS_NAME);
        } else {
            // 跳转登录
            LoginActivity_.intent(this).startForResult(REQUEST_CODE_LOGIN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_INPUT_GOODS_NAME && resultCode == Activity.RESULT_OK) {
            String goodsName = data.getStringExtra(IParam.CONTENT);
            InputOtherGoodsInfoActivity_.intent(this).extra(IParam.GOODS_NAME, goodsName).startForResult(REQUEST_CODE_PUBLISH);
        } else if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            clickToInputGoodsName();
        } else if (requestCode == REQUEST_CODE_PUBLISH && resultCode == Activity.RESULT_OK) {
            listView.autoRefresh();
        }
    }

    public void onTaskSuccess(int action, Object data) {
        switch (action) {
            case TaskAction.ACTION_GET_GOODS_LIST:// 刷新 和 初始化
                stopLoadingSelf();
                listView.onRefreshComplete();
                goodsList = (List<GoodsVO>) data;
                if (!Utils.isEmpty(goodsList)) {
                    if (goodsList.size() < BonConstants.LIMIT_GET_GOODS) {
                        listView.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_REMOVE);
                    } else {
                        listView.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_NORMAL);
                    }
                    refreshUI();
                } else {
                    showErrorMsg("暂无数据");
                }
                break;
            case TaskAction.ACTION_GET_GOODS_LIST_LOAD_MORE:// 加载更多
                List<GoodsVO> loadmoreData = (List<GoodsVO>) data;
                if (!Utils.isEmpty(loadmoreData)) {
                    if (loadmoreData.size() < BonConstants.LIMIT_GET_GOODS) {
                        listView.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_OVER);
                    } else {
                        listView.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_NORMAL);
                    }
                    goodsList.addAll(loadmoreData);
                    refreshUI();
                } else {
                    listView.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_OVER);
                }
                break;
            case TaskAction.ACTION_GET_GOODS_LIST_FROM_DB:// 获取缓存数据
                goodsList = (List<GoodsVO>) data;
                if (!Utils.isEmpty(goodsList)) {
                    stopLoadingSelf();
                    if (goodsList.size() < BonConstants.LIMIT_GET_GOODS) {
                        listView.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_REMOVE);
                    } else {
                        listView.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_NORMAL);
                    }
                    refreshUI();
                    listView.autoRefresh();
                } else {
                    getGoodsList();
                }
                break;
        }
    }

    /**
     * 刷新界面
     */
    private void refreshUI() {
        if (adapter == null) {
            adapter = new GoodsListAdapter(getActivity());
            adapter.setData(goodsList);
            listView.setAdapter(adapter);
        } else {
            adapter.setData(goodsList);
            adapter.notifyDataSetChanged();
        }
    }

    public void onTaskFail(int action, String errorMessage) {
        stopLoadingSelf();
        listView.onRefreshComplete();
        listView.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_NETWORK_DISABLE);
        showErrorMsg(errorMessage);
        LogUtils.e("action:" + action);
    }

    @Override
    public void onRefresh() {
        getGoodsList();
    }

    @Override
    public void onLoadMore() {
        pageIndex++;
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_GOODS_LIST_LOAD_MORE) {
            @Override
            protected void doBackground() throws Exception {
                setReturnData(goodsService.getGoodsList(pageIndex));
            }
        }, getActivity());
    }

    @Click(R.id.btn_refresh)
    void clickRefresh() {
        startLoadingSelf();
        getGoodsList();
    }

    @ItemClick(R.id.lv_drag)
    void onItemClick(int position) {
        GoodsVO vo = goodsList.get(position);
        GoodsDetailAndPreviewActivity_.intent(this)
                .extra(IParam.GOODS_ID, vo.getId())
                .extra(IParam.TYPE, GoodsDetailAndPreviewActivity.Type.DETAIL)
                .start();
    }
}
