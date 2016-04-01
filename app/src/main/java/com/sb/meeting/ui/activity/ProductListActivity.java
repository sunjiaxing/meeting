package com.sb.meeting.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.ui.vo.ProductVO;

import org.androidannotations.annotations.EActivity;
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

    private List<ProductVO> productList;

    @Override
    protected void onTaskSuccess(int action, Object data) {

    }
}
