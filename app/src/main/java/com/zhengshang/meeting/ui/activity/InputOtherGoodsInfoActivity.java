package com.zhengshang.meeting.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.taskmanager.LogUtils;
import com.taskmanager.Task;
import com.taskmanager.TaskManager;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.ImageOption;
import com.zhengshang.meeting.common.TaskAction;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.remote.IParam;
import com.zhengshang.meeting.service.GoodsService;
import com.zhengshang.meeting.ui.adapter.GoodsCategoryAdapter;
import com.zhengshang.meeting.ui.adapter.SortListAdapter;
import com.zhengshang.meeting.ui.adapter.ValidTimeAdapter;
import com.zhengshang.meeting.ui.component.SortListView;
import com.zhengshang.meeting.ui.vo.GoodsCategoryVO;
import com.zhengshang.meeting.ui.vo.GoodsVO;
import com.zhengshang.meeting.ui.vo.ImageVO;
import com.zhengshang.meeting.ui.vo.ValidTimeVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 输入其他商品信息页面
 * Created by sun on 2016/2/20.
 */
@EActivity(R.layout.layout_input_other_goods_info)
public class InputOtherGoodsInfoActivity extends BaseActivity implements View.OnClickListener {

    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.btn_right)
    Button btnRight;
    @ViewById(R.id.btn_right_two)
    Button btnRightTwo;
    @ViewById(R.id.lv_sort)
    SortListView sortListView;
    @Extra(IParam.GOODS_NAME)
    String goodsName;

    private TextView tvGoodsName;
    private ImageView ivCover;
    private TextView tvSelectCategory;
    private TextView tvInputPrice;
    private TextView tvSelectValidTime;
    private GoodsVO goodsVO;
    private SortListAdapter adapter;
    private List<String> imagePathList = new ArrayList<>();
    private GoodsService goodsService;
    private List<GoodsCategoryVO> categories;
    private List<ValidTimeVO> validTime;
    private AlertDialog inputImageDescDialog;

    private static final int REQUEST_CODE_SELECT_IMAGE = 0;
    private static final int REQUEST_CODE_CHANGE_COVER = 1;
    private static final int REQUEST_CODE_CHANGE_GOODS_NAME = 2;
    private static final int REQUEST_CODE_SEND_RESULT = 3;

    @AfterViews
    void init() {
        goodsService = new GoodsService(this);
        ivBack.setVisibility(View.VISIBLE);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setBackgroundColor(Color.TRANSPARENT);
        btnRight.setText("预览");

        btnRightTwo.setVisibility(View.VISIBLE);
        btnRightTwo.setBackgroundColor(Color.TRANSPARENT);
        btnRightTwo.setText("下一步");

        initHeader();
        tvGoodsName.setText(goodsName);
        initFooter();

        sortListView.setAdapter(null);

        goodsVO = new GoodsVO();
        goodsVO.setName(goodsName);

        if (Utils.isEmpty(categories)) {
            getGoodsCategory();
        }
        if (Utils.isEmpty(validTime)) {
            getValidTime();
        }
    }

    /**
     * 初始时 header
     */
    private void initHeader() {
        View header = LayoutInflater.from(this).inflate(R.layout.layout_header_input_goods, null);
        header.setClickable(false);
        ivCover = (ImageView) header.findViewById(R.id.iv_cover);
        ivCover.setOnClickListener(this);
        // 设置封面图 高度
        int coverHeight = Utils.getScreenHeight(this) / 3;
        ivCover.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, coverHeight));
        tvGoodsName = (TextView) header.findViewById(R.id.tv_goods_name);
        tvGoodsName.setOnClickListener(this);
        tvSelectCategory = (TextView) header.findViewById(R.id.tv_select_category);
        header.findViewById(R.id.layout_select_category).setOnClickListener(this);
        tvInputPrice = (TextView) header.findViewById(R.id.tv_input_price);
        header.findViewById(R.id.layout_input_price).setOnClickListener(this);
        tvSelectValidTime = (TextView) header.findViewById(R.id.tv_select_valid_time);
        header.findViewById(R.id.layout_select_valid_time).setOnClickListener(this);
        sortListView.addHeaderView(header);
    }

    /**
     * 初始化 footer
     */
    private void initFooter() {
        TextView footerView = new TextView(this);
        footerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        footerView.setTextColor(Color.GRAY);
        footerView.setTextSize(16);
        footerView.setGravity(Gravity.CENTER);
        int dp_10 = Utils.dip2px(this, 10);
        footerView.setPadding(0, dp_10, 0, dp_10);
        footerView.setText("点击选择图片");
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImageActivity_.intent(InputOtherGoodsInfoActivity.this).extra(IParam.LAST_NUM, 20 - imagePathList.size()).startForResult(REQUEST_CODE_SELECT_IMAGE);
            }
        });
        sortListView.addFooterView(footerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i("onActivityResult");
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            List<String> imageList = (List<String>) data.getSerializableExtra(IParam.CONTENT);
            imagePathList.addAll(imageList);
            // 构建 vo
            List<ImageVO> imageVOList = buildImageVO(imageList);
            goodsVO.appendImageList(imageVOList);
            if (adapter == null) {
                adapter = new SortListAdapter(this) {
                    @Override
                    public void onClick(View v) {
                        inputImageDesc((int) v.getTag());
                    }
                };
                adapter.setData(goodsVO.getImageList());
                sortListView.setAdapter(adapter);
            } else {
                adapter.setData(goodsVO.getImageList());
                adapter.notifyDataSetChanged();
            }
            if (Utils.isEmpty(goodsVO.getCoverUrl())) {
                setCover(imagePathList.get(0));
            }
        } else if (requestCode == REQUEST_CODE_CHANGE_COVER && resultCode == RESULT_OK) {
            setCover(data.getStringExtra(IParam.CONTENT));
        } else if (requestCode == REQUEST_CODE_CHANGE_GOODS_NAME && resultCode == RESULT_OK) {
            goodsName = data.getStringExtra(IParam.CONTENT);
            tvGoodsName.setText(goodsName);
            goodsVO.setName(goodsName);
        } else if (requestCode == REQUEST_CODE_SEND_RESULT && resultCode == RESULT_OK) {
            showToast("发布成功");
            setResult(RESULT_OK);
            finish();
        }
    }

    /**
     * 输入图片描述
     *
     * @param postion 图片位置
     */
    private void inputImageDesc(final int postion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.layout_input_image_desc, null);
        final EditText editContent = (EditText) view.findViewById(R.id.edit_content);
        editContent.setText(goodsVO.getImageList().get(postion).getDesc());
        final TextView tvLast = (TextView) view.findViewById(R.id.tv_last);
        editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvLast.setText((200 - s.length()) + "个字");
            }
        });

        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputImageDescDialog != null) {
                    inputImageDescDialog.dismiss();
                }
            }
        });
        view.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isEmpty(editContent.getText().toString())) {
                    editContent.requestFocus();
                    return;
                }
                if (inputImageDescDialog != null) {
                    inputImageDescDialog.dismiss();
                }
                goodsVO.getImageList().get(postion).setDesc(editContent.getText().toString());
                adapter.setData(goodsVO.getImageList());
                adapter.notifyDataSetChanged();
            }
        });
        builder.setView(view);
        inputImageDescDialog = builder.show();
    }

    /**
     * 设置封面
     *
     * @param path 封面图 path
     */
    private void setCover(String path) {
        // 设置封面
        ImageLoader.getInstance().displayImage(
                ImageDownloader.Scheme.FILE.wrap(path),
                ivCover,
                ImageOption.createNomalOption());
        goodsVO.setCoverUrl(path);
    }

    /**
     * 构建imagevo集合
     *
     * @param files 图片路径集合
     * @return
     */
    private List<ImageVO> buildImageVO(List<String> files) {
        if (!Utils.isEmpty(files)) {
            List<ImageVO> voList = new ArrayList<>();
            for (String path : files) {
                ImageVO vo = new ImageVO();
                vo.setFilePath(path);
                vo.setDesc(null);
                voList.add(vo);
            }
            return voList;
        }
        return null;
    }

    @Override
    protected void onTaskSuccess(int action, Object data) {
        switch (action) {
            case TaskAction.ACTION_GET_GOODS_CATEGORY:
                if (data != null) {
                    this.categories = (List<GoodsCategoryVO>) data;
                }
                break;
            case TaskAction.ACTION_GET_VALID_TIME:
                if (data != null) {
                    this.validTime = (List<ValidTimeVO>) data;
                }
                break;
        }
    }

    @Override
    protected void onTaskFail(int action, String errorMessage) {
        super.onTaskFail(action, errorMessage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_goods_name:// 编辑物品名称
                InputGoodsNameActivity_.intent(this).extra(IParam.GOODS_NAME, goodsVO.getName()).startForResult(REQUEST_CODE_CHANGE_GOODS_NAME);
                break;
            case R.id.iv_cover:// 设置封面
                if (goodsVO != null && !Utils.isEmpty(goodsVO.getCoverUrl())) {
                    // 切换封面
                    ChangeCoverActivity_.intent(this).extra(IParam.CONTENT, (Serializable) imagePathList).startForResult(REQUEST_CODE_CHANGE_COVER);
                } else {
                    showToast("请先选择图片");
                }
                break;
            case R.id.layout_select_category:// 选择品类
                selectCategory();
                break;
            case R.id.layout_input_price:// 输入市场价/兑换价
                inputPrice();
                break;
            case R.id.layout_select_valid_time:// 选择有效时间
                selectValidTime();
                break;
        }
    }

    /**
     * 选择有效时间
     */
    private void selectValidTime() {
        if (!Utils.isEmpty(validTime)) {
            ValidTimeAdapter validTimeAdapter = new ValidTimeAdapter(this);
            validTimeAdapter.setData(validTime);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("选择有效时间");
            builder.setAdapter(validTimeAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    goodsVO.setValidTime(validTime.get(which));
                    tvSelectValidTime.setText(goodsVO.getValidTime().getName());
                }
            });
            builder.show();
        } else {
            getValidTime();
        }
    }

    /**
     * 输入价格
     */
    private void inputPrice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("输入市场价/兑换价");
        View inputView = getLayoutInflater().inflate(R.layout.layout_input_price, null);
        final EditText editMarketPrice = (EditText) inputView.findViewById(R.id.edit_market_price);
        final EditText editExchangePrice = (EditText) inputView.findViewById(R.id.edit_exchange_price);
        if (goodsVO.getMarketPrice() > 0) {
            editMarketPrice.setText(String.valueOf(goodsVO.getMarketPrice()));
            editMarketPrice.setSelection(String.valueOf(goodsVO.getMarketPrice()).length());
        }
        if (goodsVO.getExchangePrice() > 0) {
            editExchangePrice.setText(String.valueOf(goodsVO.getExchangePrice()));
            editExchangePrice.setSelection(String.valueOf(goodsVO.getExchangePrice()).length());
        }
        builder.setView(inputView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String market = editMarketPrice.getText().toString();
                String exchange = editExchangePrice.getText().toString();
                if (Utils.isEmpty(market)) {
                    showToast("请输入市场价");
                    return;
                }
                if (Utils.isEmpty(exchange)) {
                    showToast("请输入兑换价");
                    return;
                }
                goodsVO.setMarketPrice(Double.parseDouble(market));
                goodsVO.setExchangePrice(Double.parseDouble(exchange));
                tvInputPrice.setText(market + "/" + exchange);
                dialog.dismiss();
            }
        }).setNegativeButton("取消", null).show();
    }

    /**
     * 选择品类
     */
    private void selectCategory() {
        if (!Utils.isEmpty(categories)) {
            GoodsCategoryAdapter categoryAdapter = new GoodsCategoryAdapter(this);
            categoryAdapter.setData(categories);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("选择品类");
            builder.setAdapter(categoryAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    goodsVO.setCategory(categories.get(which));
                    tvSelectCategory.setText(goodsVO.getCategory().getName());
                }
            });
            builder.show();
        } else {
            getGoodsCategory();
        }
    }

    /**
     * 获取物品分类数据
     */
    private void getGoodsCategory() {
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_GOODS_CATEGORY) {
            @Override
            protected void doBackground() throws Exception {

                setReturnData(goodsService.getGoodsCategories());
            }
        }, this);
    }

    /**
     * 获取有效时间
     */
    private void getValidTime() {
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_VALID_TIME) {
            @Override
            protected void doBackground() throws Exception {

                setReturnData(goodsService.getValidTime());
            }
        }, this);
    }

    /**
     * 下一步
     */
    @Click(R.id.btn_right_two)
    void next() {
        // 数据验证
        if (goodsVO.getCategory() == null) {
            showToast("请选择分类");
            return;
        }
        if (!(goodsVO.getMarketPrice() > 0 && goodsVO.getExchangePrice() > 0)) {
            showToast("请输入市场价和兑换价");
            return;
        }
        if (goodsVO.getValidTime() == null) {
            showToast("请选择有效时间");
            return;
        }
        if (Utils.isEmpty(goodsVO.getImageList())) {
            showToast("请添加图片");
            return;
        }
        InputNeedAndContactActivity_.intent(this)
                .extra(IParam.GOODS, goodsVO)
                .extra(IParam.CATEGORIES, (Serializable) categories)
                .startForResult(REQUEST_CODE_SEND_RESULT);
    }

    /**
     * 预览
     */
    @Click(R.id.btn_right)
    void preview() {
        GoodsDetailAndPreviewActivity_.intent(this)
                .extra(IParam.TYPE, GoodsDetailAndPreviewActivity.Type.PREVIEW)
                .extra(IParam.GOODS, goodsVO)
                .start();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtils.i("onSaveInstanceState");
        if (goodsVO != null) {
            outState.putSerializable(IParam.GOODS, goodsVO);
        }
        if (imagePathList != null) {
            outState.putSerializable(IParam.LIST, (Serializable) imagePathList);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtils.i("onRestoreInstanceState");
        goodsVO = (GoodsVO) savedInstanceState.getSerializable(IParam.GOODS);
        imagePathList = (List<String>) savedInstanceState.getSerializable(IParam.LIST);
        if (goodsVO != null) {
            if (goodsVO.getCategory() != null) {
                tvSelectCategory.setText(goodsVO.getCategory().getName());
            }
            if (goodsVO.getValidTime() != null) {
                tvSelectValidTime.setText(goodsVO.getValidTime().getName());
            }
            if (goodsVO.getMarketPrice() > 0 && goodsVO.getExchangePrice() > 0) {
                tvInputPrice.setText(goodsVO.getMarketPrice() + "/" + goodsVO.getExchangePrice());
            }
        }
    }
}
