package com.zhengshang.meeting.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.remote.IParam;
import com.zhengshang.meeting.ui.adapter.SortListAdapter;
import com.zhengshang.meeting.ui.component.SortListView;
import com.zhengshang.meeting.ui.vo.GoodsVO;
import com.zhengshang.meeting.ui.vo.ImageVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

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

    @AfterViews
    void init() {
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
    }

    /**
     * 初始时 header
     */
    private void initHeader() {
        View header = LayoutInflater.from(this).inflate(R.layout.layout_header_input_goods, null);
        ivCover = (ImageView) header.findViewById(R.id.iv_cover);
        ivCover.setOnClickListener(this);
        // 设置封面图 高度
        int coverHeight = Utils.getScreenHeight(this) / 3;
        ivCover.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, coverHeight));
        tvGoodsName = (TextView) header.findViewById(R.id.tv_goods_name);
        tvGoodsName.setOnClickListener(this);
        tvSelectCategory = (TextView) header.findViewById(R.id.tv_select_category);
        tvSelectCategory.setOnClickListener(this);
        tvInputPrice = (TextView) header.findViewById(R.id.tv_input_price);
        tvInputPrice.setOnClickListener(this);
        tvSelectValidTime = (TextView) header.findViewById(R.id.tv_select_valid_time);
        tvSelectValidTime.setOnClickListener(this);
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
                ChooseImageActivity_.intent(InputOtherGoodsInfoActivity.this).extra(IParam.LAST_NUM, 20).startForResult(0);
            }
        });
        sortListView.addFooterView(footerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            List<String> imageList = (List<String>) data.getSerializableExtra(IParam.CONTENT);
            List<ImageVO> imageVOList = buildImageVO(imageList);
            goodsVO.appendImageList(imageVOList);
            if (adapter == null) {
                adapter = new SortListAdapter(this);
                adapter.setData(goodsVO.getImageList());
                sortListView.setAdapter(adapter);
            } else {
                adapter.setData(goodsVO.getImageList());
                adapter.notifyDataSetChanged();
            }
        }
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

    }

    @Override
    public void onClick(View v) {

    }
}
