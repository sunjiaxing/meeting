package com.sb.meeting.ui.activity;

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
import com.sb.meeting.ui.adapter.GoodsListAdapter;
import com.sb.meeting.ui.component.RefreshListView;
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
 * 我关注的 物品 页面
 * Created by sun on 2016/3/22.
 */
@EActivity(R.layout.layout_attention_goods)
public class AttentionGoodsActivity extends BaseActivity implements RefreshListView.OnRefreshLoadMoreListener {

    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.layout_loading_attention)
    View layoutLoading;
    @ViewById(R.id.layout_error_attention)
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
    private List<GoodsVO> attentionGoods;
    private GoodsService goodsService;
    private int pageIndex;
    private GoodsListAdapter adapter;

    @AfterViews
    void init() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("我关注的");
        anim = (AnimationDrawable) ivLoadingIn.getBackground();
        goodsService = new GoodsService(this);
        listView.setLastUpdateTimeRelateObject(this);
        listView.setOnRefreshListener(this);
        startLoadingSelf();
        pageIndex = 0;
        getAttentionGoods();
    }

    /**
     * 获取关注的物品
     */
    private void getAttentionGoods() {
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_ATTENTION_GOODS) {
            @Override
            protected void doBackground() throws Exception {
                setReturnData(goodsService.getAttentionGoods(pageIndex));
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
        if (!Utils.isEmpty(attentionGoods)) {
            showToast(msg);
        } else {
            layoutError.setVisibility(View.VISIBLE);
            btnErrorRefresh.setText("刷新");
            tvErrorMsg.setText(msg);
        }
    }

    @Override
    protected void onTaskSuccess(int action, Object data) {
        if (action == TaskAction.ACTION_GET_ATTENTION_GOODS) {
            if (data != null) {
                attentionGoods = (List<GoodsVO>) data;
                refreshUI();
                if (!Utils.isEmpty(attentionGoods) && attentionGoods.size() == BonConstants.LIMIT_GET_PUBLISHED_GOODS) {
                    listView.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_NORMAL);
                } else {
                    listView.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_OVER);
                }
            } else {
                stopLoadingSelf();
                showErrorMsg("暂无关注数据");
            }
        } else if (action == TaskAction.ACTION_GET_ATTENTION_GOODS_MORE) {
            if (data != null) {
                List<GoodsVO> more = (List<GoodsVO>) data;
                attentionGoods.addAll(more);
                refreshUI();
                if (!Utils.isEmpty(more) && more.size() == BonConstants.LIMIT_GET_ATTENTION_GOODS) {
                    listView.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_NORMAL);
                } else {
                    listView.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_OVER);
                }
            }
        }
    }

    @Override
    protected void onTaskFail(int action, String errorMessage) {
        switch (action) {
            case TaskAction.ACTION_GET_ATTENTION_GOODS:
                stopLoadingSelf();
                listView.onRefreshComplete();
                showErrorMsg(errorMessage);
                break;
            case TaskAction.ACTION_GET_ATTENTION_GOODS_MORE:
                listView.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_NETWORK_DISABLE);
                showErrorMsg(errorMessage);
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
            adapter = new GoodsListAdapter(this) {
                @Override
                public void onClick(View v) {

                }
            };
            adapter.setData(attentionGoods);
            listView.setAdapter(adapter);
        } else {
            adapter.setData(attentionGoods);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        pageIndex = 0;
        getAttentionGoods();
    }

    @Override
    public void onLoadMore() {
        pageIndex++;
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_ATTENTION_GOODS_MORE) {
            @Override
            protected void doBackground() throws Exception {
                setReturnData(goodsService.getAttentionGoods(pageIndex));
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
    void onItemClick(int position) {
        GoodsVO vo = attentionGoods.get(position);
        GoodsDetailAndPreviewActivity_.intent(this)
                .extra(IParam.GOODS_ID, vo.getId())
                .extra(IParam.TYPE, GoodsDetailAndPreviewActivity.Type.DETAIL)
                .start();
    }

    @Click(R.id.btn_refresh)
    void clickRefresh() {
        startLoadingSelf();
        pageIndex = 0;
        getAttentionGoods();
    }
}
