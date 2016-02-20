package com.zhengshang.meeting.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.ui.activity.BaseActivity;
import com.zhengshang.meeting.ui.activity.InputGoodsNameActivity_;
import com.zhengshang.meeting.ui.component.DragListView;
import com.zhengshang.meeting.ui.vo.GoodsVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 易物 列表
 * Created by sun on 2016/2/19.
 */
@EFragment(R.layout.layout_news_subject)
public class TabGoodsListFragment extends BaseFragment implements DragListView.OnRefreshLoadMoreListener {

    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.btn_right)
    Button btnRight;
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
    @ViewById(R.id.iv_loading_in)
    ImageView ivLoading;

    private AnimationDrawable anim;
    private List<GoodsVO> goodsList;

    @AfterViews
    void init() {
        anim = (AnimationDrawable) ivLoading.getBackground();
        ivBack.setVisibility(View.GONE);
        tvTitle.setText("商品列表");
        listView.setPullType(DragListView.ListViewPullType.LV_ALL);
        listView.setDividerHeight(0);
        listView.setFastScrollEnabled(true);
        listView.setOnRefreshListener(this);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setBackgroundColor(Color.TRANSPARENT);
        btnRight.setText("发布物品");
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
    void clickToEdit() {
        InputGoodsNameActivity_.intent(this).startForResult(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {

        }
    }

    public void onTaskSuccess(int action, Object data) {

    }
    public void onTaskFail(int action, String errorMessage) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
