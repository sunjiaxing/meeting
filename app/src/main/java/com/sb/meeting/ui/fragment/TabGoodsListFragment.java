package com.sb.meeting.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.common.BonConstants;
import com.sb.meeting.common.TaskAction;
import com.sb.meeting.common.Utils;
import com.sb.meeting.dao.entity.CheckingGoods;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.service.GoodsService;
import com.sb.meeting.service.UserService;
import com.sb.meeting.ui.activity.GoodsDetailAndPreviewActivity;
import com.sb.meeting.ui.activity.GoodsDetailAndPreviewActivity_;
import com.sb.meeting.ui.activity.InputGoodsNameActivity_;
import com.sb.meeting.ui.activity.InputOtherGoodsInfoActivity_;
import com.sb.meeting.ui.activity.LoginActivity_;
import com.sb.meeting.ui.activity.PublishedGoodsActivity_;
import com.sb.meeting.ui.adapter.GoodsListAdapter;
import com.sb.meeting.ui.component.RefreshListView;
import com.sb.meeting.ui.vo.GoodsVO;
import com.sb.meeting.ui.vo.ImageVO;
import com.taskmanager.LogUtils;
import com.taskmanager.Task;
import com.taskmanager.TaskManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    private static final int REQUEST_CODE_INPUT_GOODS_NAME = 0x3231;
    private static final int REQUEST_CODE_LOGIN = 0x3232;
    private static final int REQUEST_CODE_PUBLISH = 0x3233;
    private static final int REQUEST_CODE_ATTENTION = 0x3234;
    private CheckingGoods checkingGoods;
    private GoodsVO vo;
    private String contact;

    @AfterViews
    void init() {
        anim = (AnimationDrawable) ivLoading.getBackground();
        ivBack.setVisibility(View.GONE);
        tvTitle.setText("易物");
        tvTitle.setTextColor(getResources().getColor(R.color.c_ff946e));
        tvTitle.setTypeface(null, Typeface.BOLD);
        listView.setOnRefreshListener(this);
        listView.setLastUpdateTimeRelateObject(this);
        listView.setFastScrollEnabled(false);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setBackgroundColor(Color.TRANSPARENT);
        btnRight.setText("发布");
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
            getCheckingDataFromDB();
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
        }, this);
    }

    /**
     * 获取审核数据
     */
    private void getCheckingDataFromDB() {
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_CHECKING_DATA) {
            @Override
            protected void doBackground() throws Exception {
                setReturnData(goodsService.getCheckingData());
            }
        }, this);
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
            vo = (GoodsVO) data.getSerializableExtra(IParam.GOODS);
            contact = data.getStringExtra(IParam.USER_CONTACT);
            publishGoods();
        } else if (requestCode == REQUEST_CODE_ATTENTION && resultCode == Activity.RESULT_OK) {
            listView.autoRefresh();
        }
    }

    /**
     * 发布物品
     */
    private void publishGoods() {
        // 构建临时 存储
        checkingGoods = new CheckingGoods();
        checkingGoods.setUUID(UUID.randomUUID().toString());
        checkingGoods.setGoodsName(vo.getName());
        checkingGoods.setCoverUrl(vo.getCoverUrl());
        checkingGoods.setExchangePrice(vo.getExchangePrice());
        checkingGoods.setMarketPrice(vo.getMarketPrice());
        checkingGoods.setValidTime(vo.getValidTime().getId() + "-" + vo.getValidTime().getName());
        checkingGoods.setCount(vo.getCount());
        checkingGoods.setCategory(vo.getCategory().getId() + "-" + vo.getCategory().getName());
        checkingGoods.setNeedCategory(vo.getNeedCategory().getId() + "-" + vo.getNeedCategory().getName());
        checkingGoods.setContact(contact);
        try {
            JSONArray array = new JSONArray();
            for (ImageVO imageVO : vo.getImageList()) {
                JSONObject json = new JSONObject();
                json.put(IParam.URL, imageVO.getUrl());
                json.put(IParam.DESC, imageVO.getDesc());
                array.put(json);
            }
            checkingGoods.setImages(array.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        checkingGoods.setPublishTime(System.currentTimeMillis());
        refreshUI();
        TaskManager.pushTask(new Task(TaskAction.ACTION_PUBLISH_GOODS) {
            @Override
            protected void doBackground() throws Exception {
                goodsService.saveCheckingData(checkingGoods);
                goodsService.publishGoods(vo, contact, checkingGoods.getUUID());
            }
        }, this);
    }

    /**
     * 重新发布
     */
    private void rePublish() {
        if (checkingGoods != null) {
            checkingGoods.setSendSuccess(0);
            refreshUI();
            TaskManager.pushTask(new Task(TaskAction.ACTION_PUBLISH_GOODS) {
                @Override
                protected void doBackground() throws Exception {
                    goodsService.publishGoods(vo, contact, checkingGoods.getUUID());
                }
            }, this);
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
            case TaskAction.ACTION_GOODS_ATTENTION:
                stopLoading();
                adapter.notifyDataSetChanged();
                break;
            case TaskAction.ACTION_PUBLISH_GOODS:
                checkingGoods.setSendSuccess(1);
                refreshUI();
                break;
            case TaskAction.ACTION_GET_CHECKING_DATA:
                if (data != null) {
                    checkingGoods = (CheckingGoods) data;
                    refreshUI();
                }
                break;
        }
    }

    /**
     * 刷新界面
     */
    private void refreshUI() {
        if (adapter == null) {
            adapter = new GoodsListAdapter(getActivity()) {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.tv_attention:
                        case R.id.iv_attention_tip:
                            int pos = (int) v.getTag();
                            if (checkingGoods != null && pos == 0) {
                                // 判断 状态，发送失败 才能重发
                                if (checkingGoods.getSendSuccess() == -1) {
                                    rePublish();
                                }
                            } else {
                                startLoading();
                                attention(checkingGoods != null ? pos - 1 : pos);
                            }
                            break;
                    }
                }
            };
            adapter.setData(goodsList);
            adapter.setTempData(checkingGoods);
            listView.setAdapter(adapter);
        } else {
            adapter.setData(goodsList);
            adapter.setTempData(checkingGoods);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 关注
     *
     * @param pos
     */
    private void attention(int pos) {
        final GoodsVO goodsVO = goodsList.get(pos);
        if (userService.checkLoginState()) {
            TaskManager.pushTask(new Task(TaskAction.ACTION_GOODS_ATTENTION) {
                @Override
                protected void doBackground() throws Exception {
                    if (!goodsVO.isAttention()) {
                        goodsService.attention(goodsVO.getId());
                        goodsVO.setIsAttention(!goodsVO.isAttention());
                    }
                }
            }, getActivity());
        } else {
            stopLoading();
            LoginActivity_.intent(this).startForResult(REQUEST_CODE_ATTENTION);
        }
    }

    public void onTaskFail(int action, String errorMessage) {
        switch (action) {
            case TaskAction.ACTION_GOODS_ATTENTION:
                stopLoading();
                showToast(errorMessage);
                break;
            case TaskAction.ACTION_PUBLISH_GOODS:
                showToast("发送失败");
                checkingGoods.setSendSuccess(-1);
                adapter.setTempData(checkingGoods);
                adapter.notifyDataSetChanged();
                break;
            default:
                stopLoadingSelf();
                listView.onRefreshComplete();
                listView.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_NETWORK_DISABLE);
                showErrorMsg(errorMessage);
                break;
        }
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
        if (checkingGoods != null && position == 0) {
            if (checkingGoods.getSendSuccess() == 1) {
                PublishedGoodsActivity_.intent(this).start();
            }
        } else {
            GoodsVO vo = goodsList.get(checkingGoods != null ? position - 1 : position);
            GoodsDetailAndPreviewActivity_.intent(this)
                    .extra(IParam.GOODS_ID, vo.getId())
                    .extra(IParam.TYPE, GoodsDetailAndPreviewActivity.Type.DETAIL)
                    .start();
        }
    }
}
