package com.sb.meeting.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.common.BonConstants;
import com.sb.meeting.common.TaskAction;
import com.sb.meeting.common.Utils;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.service.GoodsService;
import com.sb.meeting.ui.adapter.PublishedGoodsAdapter;
import com.sb.meeting.ui.component.RefreshListView;
import com.sb.meeting.ui.component.TlcyDialog;
import com.sb.meeting.ui.vo.GoodsVO;
import com.taskmanager.Task;
import com.taskmanager.TaskManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 我发布的 物品 页面
 * Created by sun on 2016/3/22.
 */
@EActivity(R.layout.layout_published_goods)
public class PublishedGoodsActivity extends BaseActivity implements RefreshListView.OnRefreshLoadMoreListener {

    public static final int REQUESTCODE_EDIT_GOODS = 0x3251;

    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.layout_loading_published)
    View layoutLoading;
    @ViewById(R.id.layout_error_published)
    View layoutError;
    @ViewById(R.id.tv_description)
    TextView tvErrorMsg;
    @ViewById(R.id.btn_refresh)
    Button btnErrorRefresh;
    @ViewById(R.id.lv_drag)
    RefreshListView listView;
    @ViewById(R.id.iv_loading_in)
    ImageView ivLoadingIn;

    private AnimationDrawable anim;
    private List<GoodsVO> publishedGoods;
    private GoodsService goodsService;
    private int pageIndex;
    private PublishedGoodsAdapter adapter;
    private GoodsVO goodsVO;
    private String contact;

    @AfterViews
    void init() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("我发布的");
        anim = (AnimationDrawable) ivLoadingIn.getBackground();
        goodsService = new GoodsService(this);
        listView.setLastUpdateTimeRelateObject(this);
        listView.setOnRefreshListener(this);
        startLoadingSelf();
        pageIndex = 0;
        getPublishedGoods();
    }

    /**
     * 获取发布的物品
     */
    private void getPublishedGoods() {
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_PUBLISHED_GOODS) {
            @Override
            protected void doBackground() throws Exception {
                setReturnData(goodsService.getPublishedGoods(pageIndex));
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
        if (!Utils.isEmpty(publishedGoods)) {
            showToast(msg);
        } else {
            layoutError.setVisibility(View.VISIBLE);
            btnErrorRefresh.setText("刷新");
            tvErrorMsg.setText(msg);
        }
    }

    @Override
    protected void onTaskSuccess(int action, Object data) {
        if (action == TaskAction.ACTION_GET_PUBLISHED_GOODS) {
            if (data != null) {
                publishedGoods = (List<GoodsVO>) data;
                refreshUI();
                if (!Utils.isEmpty(publishedGoods) && publishedGoods.size() == BonConstants.LIMIT_GET_PUBLISHED_GOODS) {
                    listView.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_NORMAL);
                } else {
                    listView.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_OVER);
                }
            }
        } else if (action == TaskAction.ACTION_GET_PUBLISHED_GOODS_MORE) {
            if (data != null) {
                List<GoodsVO> more = (List<GoodsVO>) data;
                publishedGoods.addAll(more);
                refreshUI();
                if (!Utils.isEmpty(more) && more.size() == BonConstants.LIMIT_GET_PUBLISHED_GOODS) {
                    listView.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_NORMAL);
                } else {
                    listView.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_OVER);
                }
            }
        } else if (action == TaskAction.ACTION_EDIT_GOODS_INFO) {
            stopLoading();
            showToast("操作成功");
            listView.autoRefresh();
        }
    }

    @Override
    protected void onTaskFail(int action, String errorMessage) {
        switch (action) {
            case TaskAction.ACTION_GET_PUBLISHED_GOODS:
                stopLoadingSelf();
                listView.onRefreshComplete();
                showErrorMsg(errorMessage);
                break;
            case TaskAction.ACTION_GET_PUBLISHED_GOODS_MORE:
                listView.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_NETWORK_DISABLE);
                showErrorMsg(errorMessage);
                break;
            case TaskAction.ACTION_EDIT_GOODS_INFO:
                stopLoading();
                showAlert("发布失败，确定要重新发布吗？", new TlcyDialog.TlcyDialogListener() {
                    @Override
                    public void onClick() {
                        rePublish();
                    }
                }, null);
                break;
            default:
                super.onTaskFail(action, errorMessage);
                break;
        }
    }

    /**
     * 刷新界面
     */
    private void refreshUI() {
        stopLoadingSelf();
        listView.onRefreshComplete();
        if (adapter == null) {
            adapter = new PublishedGoodsAdapter(this);
            adapter.setData(publishedGoods);
            listView.setAdapter(adapter);
        } else {
            adapter.setData(publishedGoods);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        pageIndex = 0;
        getPublishedGoods();
    }

    @Override
    public void onLoadMore() {
        pageIndex++;
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_PUBLISHED_GOODS_MORE) {
            @Override
            protected void doBackground() throws Exception {
                setReturnData(goodsService.getPublishedGoods(pageIndex));
            }
        }, this);
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
    void onItemClick(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(new String[]{"编辑", "删除"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    GoodsVO vo = publishedGoods.get(position);
                    UpdateGoodsInfoActivity_.intent(PublishedGoodsActivity.this)
                            .extra(IParam.GOODS_ID, vo.getId())
                            .startForResult(REQUESTCODE_EDIT_GOODS);
                } else if (which == 1) {
                    showToast("删除功能暂未开放");
                }
            }
        }).show();
    }

    @Click(R.id.btn_refresh)
    void clickRefresh() {
        startLoadingSelf();
        pageIndex = 0;
        getPublishedGoods();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE_EDIT_GOODS && resultCode == RESULT_OK) {
            goodsVO = (GoodsVO) data.getSerializableExtra(IParam.GOODS);
            contact = data.getStringExtra(IParam.USER_CONTACT);
            showAlert("编辑之后需要重新审核，确定要修改吗？", new TlcyDialog.TlcyDialogListener() {
                @Override
                public void onClick() {
                    rePublish();
                }
            }, null);
        }
    }

    /**
     * 重新发布
     */
    private void rePublish() {
        startLoading("重新发布中...");
        TaskManager.pushTask(new Task(TaskAction.ACTION_EDIT_GOODS_INFO) {
            @Override
            protected void doBackground() throws Exception {
                goodsService.updateGoodsInfo(goodsVO, contact);
            }
        }, this);
    }
}
