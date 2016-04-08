package com.sb.meeting.ui.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.common.ImageOption;
import com.sb.meeting.common.TaskAction;
import com.sb.meeting.common.Utils;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.service.YellowPageService;
import com.sb.meeting.ui.adapter.CompanyScrollImageAdapter;
import com.sb.meeting.ui.component.ChildViewPager;
import com.sb.meeting.ui.component.CircleImageView;
import com.sb.meeting.ui.vo.CompanyDetailVO;
import com.sb.meeting.ui.vo.ProductVO;
import com.taskmanager.Task;
import com.taskmanager.TaskManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;

/**
 * 企业详情 Activity
 * Created by sun on 2016/3/31.
 */
@EActivity(R.layout.layout_company_vip_detail)
public class CompanyVIPDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @ViewById(R.id.layout_loading_company_detail)
    View layoutLoading;
    @ViewById(R.id.layout_error_company_detail)
    View layoutError;
    @ViewById(R.id.tv_description)
    TextView tvErrorMsg;
    @ViewById(R.id.btn_refresh)
    Button btnErrorRefresh;
    @ViewById(R.id.iv_loading_in)
    ImageView ivLoadingIn;
    @Extra(IParam.COMPANY_ID)
    int companyId;
    @ViewById(R.id.vp_image_list)
    ChildViewPager vpImageList;
    @ViewById(R.id.layout_vip_detail_point)
    LinearLayout layoutImagePoint;
    @ViewById(R.id.tv_company_name)
    TextView tvCompanyName;
    @ViewById(R.id.tv_product_desc)
    TextView tvProductDesc;
    @ViewById(R.id.tv_company_introduce)
    TextView tvCompanyIntroduce;
    @ViewById(R.id.tv_contact)
    TextView tvContact;
    @ViewById(R.id.tv_phone)
    TextView tvPhone;
    @ViewById(R.id.tv_qq)
    TextView tvQQ;
    @ViewById(R.id.tv_email)
    TextView tvEmail;
    @ViewById(R.id.iv_logo)
    CircleImageView ivLogo;
    @ViewById(R.id.tv_company_address)
    TextView tvCompanyAddress;

    @ViewById(R.id.layout_product1)
    LinearLayout layoutProduct1;
    @ViewById(R.id.iv_product1)
    ImageView ivProduct1;
    @ViewById(R.id.tv_product1)
    TextView tvProduct1;
    @ViewById(R.id.layout_product2)
    LinearLayout layoutProduct2;
    @ViewById(R.id.iv_product2)
    ImageView ivProduct2;
    @ViewById(R.id.tv_product2)
    TextView tvProduct2;
    @ViewById(R.id.layout_product3)
    LinearLayout layoutProduct3;
    @ViewById(R.id.iv_product3)
    ImageView ivProduct3;
    @ViewById(R.id.tv_product3)
    TextView tvProduct3;

    private AnimationDrawable anim;
    private CompanyDetailVO detailVO;
    private YellowPageService yellowPageService;
    private int index;
    private static final int TIME_SCROLL = 2000;
    private static final int HANDLER_WHAT = 0x15032701;
    private Handler imgHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HANDLER_WHAT:
                    // 显示当前位置
                    vpImageList.setCurrentItem(index, true);
                    index = (index + 1 >= detailVO.getImageList().size() ? 0
                            : index + 1);
                    imgHandler.sendEmptyMessageDelayed(HANDLER_WHAT, TIME_SCROLL);
                    break;
            }
        }
    };

    @AfterViews
    void init() {
        anim = (AnimationDrawable) ivLoadingIn.getBackground();
        ivLogo.setBorderOverlay(true);
        yellowPageService = new YellowPageService(this);
        int w = Utils.getScreenWidth(this) / 3;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(w, w);
        ivProduct1.setLayoutParams(layoutParams);
        ivProduct2.setLayoutParams(layoutParams);
        ivProduct3.setLayoutParams(layoutParams);
        vpImageList.addOnPageChangeListener(this);
        getDetail();
    }

    @Override
    protected void onTaskSuccess(int action, Object data) {
        switch (action) {
            case TaskAction.ACTION_GET_COMPANY_DETAIL:
                stopLoadingSelf();
                if (data != null) {
                    detailVO = (CompanyDetailVO) data;
                    refreshUI();
                } else {
                    showErrorMsg("暂无数据");
                }
                break;
        }
    }

    @Override
    protected void onTaskFail(int action, String errorMessage) {
        switch (action) {
            case TaskAction.ACTION_GET_COMPANY_DETAIL:
                stopLoadingSelf();
                showErrorMsg(errorMessage);
                break;
            default:
                super.onTaskFail(action, errorMessage);
                break;
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

    /**
     * 刷新界面
     */
    private void refreshUI() {
        if (detailVO != null) {
            // 加载滚动图
            if (!Utils.isEmpty(detailVO.getImageList())) {
                vpImageList.setOffscreenPageLimit(detailVO.getImageList().size());
                CompanyScrollImageAdapter scrollImageAdapter = new CompanyScrollImageAdapter(this);
                scrollImageAdapter.setData(detailVO.getImageList());
                vpImageList.setAdapter(scrollImageAdapter);
                if (detailVO.getImageList().size() > 1) {
                    // 加载 point
                    for (int i = 0; i < detailVO.getImageList().size(); i++) {
                        ImageView img = new ImageView(this);
                        img.setBackgroundResource(R.mipmap.dian_bg);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                Utils.dip2px(this, 5), Utils.dip2px(this, 5));
                        params.rightMargin = 10;
                        layoutImagePoint.addView(img, params);
                    }
                    layoutImagePoint.getChildAt(0).setBackgroundResource(
                            R.mipmap.dian);
                    startScroll();
                }
            }
            tvCompanyName.setText(detailVO.getCompanyName());
            tvProductDesc.setText("主营产品描述：" + detailVO.getProductDesc());
            tvContact.setText("姓名 " + detailVO.getContact());
            tvPhone.setText("电话 " + detailVO.getPhone());
            tvQQ.setText("QQ " + detailVO.getQQ());
            tvEmail.setText("邮箱 " + detailVO.getEmail());
            Utils.displayImage(detailVO.getLogo(), ivLogo, ImageOption.createNomalOption());
            tvCompanyAddress.setText(detailVO.getCompanyAddress());
            // 加载产品信息
            if (!Utils.isEmpty(detailVO.getProductList())) {
                if (detailVO.getProductList().size() >= 1) {
                    layoutProduct1.setVisibility(View.VISIBLE);
                    Utils.displayImage(detailVO.getProductList().get(0).getThumb(), ivProduct1, ImageOption.createNomalOption());
                    tvProduct1.setText(detailVO.getProductList().get(0).getProductName());
                }
                if (detailVO.getProductList().size() >= 2) {
                    layoutProduct2.setVisibility(View.VISIBLE);
                    Utils.displayImage(detailVO.getProductList().get(1).getThumb(), ivProduct2, ImageOption.createNomalOption());
                    tvProduct2.setText(detailVO.getProductList().get(1).getProductName());
                }
                if (detailVO.getProductList().size() >= 3) {
                    layoutProduct3.setVisibility(View.VISIBLE);
                    Utils.displayImage(detailVO.getProductList().get(2).getThumb(), ivProduct3, ImageOption.createNomalOption());
                    tvProduct3.setText(detailVO.getProductList().get(2).getProductName());
                }
            }
        }
    }

    /**
     * 开始滚动
     */
    private void startScroll() {
        if (detailVO != null && !Utils.isEmpty(detailVO.getImageList())
                && detailVO.getImageList().size() > 1) {
            index = 0;
            imgHandler.sendEmptyMessageDelayed(HANDLER_WHAT, TIME_SCROLL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        imgHandler.removeMessages(HANDLER_WHAT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imgHandler.removeMessages(HANDLER_WHAT);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startScroll();
    }

    /**
     * 跳转到企业微官网
     */
    @Click(R.id.tv_web_url)
    void toCompanyWeb() {
        if (detailVO != null && !Utils.isEmpty(detailVO.getWebUrl())) {
            ShowWebActivity_.intent(this)
                    .extra(IParam.URL, detailVO.getWebUrl())
                    .extra(IParam.TITLE, detailVO.getCompanyName())
                    .start();
        }
    }

    /**
     * 跳转到企业地图页面
     */
    @Click(R.id.tv_company_address)
    void toMap() {

    }

    /**
     * 跳转到荣誉证书界面
     */
    @Click(R.id.layout_certificate)
    void toCertificate() {
        if (!Utils.isEmpty(detailVO.getCertificateList())) {
            CompanyCertificateActivity_.intent(this)
                    .extra(IParam.CONTENT, (Serializable) detailVO.getCertificateList())
                    .start();
        }
    }

    /**
     * 跳转到企业简介页面
     */
    @Click(R.id.tv_company_introduce)
    void toCompanyIntroduce() {
        ShowWebActivity_.intent(this)
                .extra(IParam.CONTENT, detailVO.getCompanyIntroduce())
                .extra(IParam.TITLE, detailVO.getCompanyName())
                .start();
    }

    /**
     * 跳转到产品列表
     */
    @Click(R.id.layout_to_product)
    void lookMoreProduct() {
        ProductListActivity_.intent(this).extra(IParam.COMPANY_ID, companyId).start();
    }

    /**
     * 跳转到产品详情
     *
     * @param v
     */
    @Click({R.id.layout_product1, R.id.layout_product2, R.id.layout_product3})
    void clickProduct(View v) {
        ProductVO productVO = null;
        switch (v.getId()) {
            case R.id.layout_product1:
                productVO = detailVO.getProductList().get(0);
                break;
            case R.id.layout_product2:
                productVO = detailVO.getProductList().get(1);
                break;
            case R.id.layout_product3:
                productVO = detailVO.getProductList().get(2);
                break;
        }
        if (productVO != null) {
            ProductDetailActivity_.intent(this)
                    .extra(IParam.PRODUCT_ID, productVO.getProductId())
                    .start();
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
        if (detailVO != null) {
            showToast(msg);
        } else {
            layoutError.setVisibility(View.VISIBLE);
            btnErrorRefresh.setText("刷新");
            tvErrorMsg.setText(msg);
        }
    }

    /**
     * 获取 详情
     */
    @Click(R.id.btn_refresh)
    public void getDetail() {
        startLoadingSelf();
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_COMPANY_DETAIL) {
            @Override
            protected void doBackground() throws Exception {
                setReturnData(yellowPageService.getCompanyDetail(companyId));
            }
        }, this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        // 清除所有圆点
        for (int i = 0; i < detailVO.getImageList().size(); i++) {
            layoutImagePoint.getChildAt(i).setBackgroundResource(
                    R.mipmap.dian_bg);
        }
        // 当前点变色
        layoutImagePoint.getChildAt(position).setBackgroundResource(
                R.mipmap.dian);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
