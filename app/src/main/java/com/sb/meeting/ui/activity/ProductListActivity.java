package com.sb.meeting.ui.activity;

import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.common.TaskAction;
import com.sb.meeting.common.Utils;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.service.YellowPageService;
import com.sb.meeting.ui.adapter.ProductAdapter;
import com.sb.meeting.ui.vo.ProductVO;
import com.taskmanager.Task;
import com.taskmanager.TaskManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 产品列表 页面
 * Created by sun on 2016/4/1.
 */
@EActivity(R.layout.layout_product_list)
public class ProductListActivity extends BaseActivity {

    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.layout_loading_product)
    View layoutLoading;
    @ViewById(R.id.layout_error_product)
    View layoutError;
    @ViewById(R.id.tv_description)
    TextView tvErrorMsg;
    @ViewById(R.id.btn_refresh)
    Button btnErrorRefresh;
    @ViewById(R.id.iv_loading_in)
    ImageView ivLoading;
    @ViewById(R.id.gv_product_list)
    GridView gvProduct;
    @Extra(IParam.COMPANY_ID)
    int companyId;

    private List<ProductVO> productList;
    private AnimationDrawable anim;
    private YellowPageService yellowPageService;
    private int pageIndex;
    private ProductAdapter productAdapter;

    @AfterViews
    void init() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("产品列表");
        anim = (AnimationDrawable) ivLoading.getBackground();
        yellowPageService = new YellowPageService(this);
        getProductList();
    }

    /**
     * 获取产品列表
     */
    @Click(R.id.btn_refresh)
    void getProductList() {
        startLoadingSelf();
        pageIndex = 0;
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_PRODUCT_LIST) {
            @Override
            protected void doBackground() throws Exception {
                setReturnData(yellowPageService.getProductList(companyId, pageIndex));
            }
        }, this);
    }

    @Override
    protected void onTaskSuccess(int action, Object data) {
        if (action == TaskAction.ACTION_GET_PRODUCT_LIST) {
            stopLoadingSelf();
            if (data != null) {
                productList = (List<ProductVO>) data;
                refreshUI();
            } else {
                showErrorMsg("暂无数据");
            }
        }
    }

    /**
     * 刷新界面
     */
    private void refreshUI() {
        if (productAdapter == null) {
            productAdapter = new ProductAdapter(this);
            productAdapter.setData(productList);
            gvProduct.setAdapter(productAdapter);
        } else {
            productAdapter.setData(productList);
            productAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onTaskFail(int action, String errorMessage) {
        if (action == TaskAction.ACTION_GET_PRODUCT_LIST) {
            stopLoadingSelf();
            showErrorMsg(errorMessage);
        } else {
            super.onTaskFail(action, errorMessage);
        }
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
        if (!Utils.isEmpty(productList)) {
            showToast(msg);
        } else {
            layoutError.setVisibility(View.VISIBLE);
            btnErrorRefresh.setText("刷新");
            tvErrorMsg.setText(msg);
        }
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

    @ItemClick(R.id.gv_product_list)
    void onItemClick(int position) {
        ProductDetailActivity_.intent(this)
                .extra(IParam.PRODUCT_ID, productList.get(position).getProductId())
                .start();
    }
}
