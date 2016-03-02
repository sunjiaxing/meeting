package com.zhengshang.meeting.ui.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.taskmanager.Task;
import com.taskmanager.TaskManager;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.ImageOption;
import com.zhengshang.meeting.common.TaskAction;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.remote.IParam;
import com.zhengshang.meeting.service.GoodsService;
import com.zhengshang.meeting.service.UserService;
import com.zhengshang.meeting.ui.adapter.GoodsImageAdapter;
import com.zhengshang.meeting.ui.vo.GoodsImageVO;
import com.zhengshang.meeting.ui.vo.GoodsVO;
import com.zhengshang.meeting.ui.vo.ImageVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 物品 详情和预览公用页面
 * Created by sun on 2016/2/24.
 */
@EActivity(R.layout.layout_goods_detail_and_preview)
public class GoodsDetailAndPreviewActivity extends BaseActivity {

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
    @ViewById(R.id.lv_detail)
    ListView listView;
    @ViewById(R.id.iv_loading_in)
    ImageView ivLoading;
    @ViewById(R.id.tv_attention)
    TextView tvAttention;

    @Extra(IParam.GOODS_ID)
    int goodsId;
    @Extra(IParam.TYPE)
    Type viewType;

    private AnimationDrawable anim;
    private GoodsVO goodsVO;
    private GoodsService goodsService;
    private UserService userService;
    private TextView tvGoodsName;
    private ImageView ivCover;
    private TextView tvScanNum;
    private TextView tvAttentionNum;
    private TextView tvImageNum;
    private TextView tvPublishTime;
    private GoodsImageAdapter adapter;

    public enum Type {
        DETAIL, PREVIEW
    }

    @AfterViews
    void init() {
        ivBack.setVisibility(View.VISIBLE);
        anim = (AnimationDrawable) ivLoading.getBackground();
        goodsVO = (GoodsVO) getIntent().getSerializableExtra(IParam.GOODS);
        listView.setClickable(false);
        goodsService = new GoodsService(this);
        userService = new UserService(this);
        initHeader();
        if (viewType == Type.DETAIL) {
            tvTitle.setText("详情");
            startLoadingSelf();
            getGoodsDetail();
        } else if (viewType == Type.PREVIEW) {
            tvTitle.setText("预览");
            refreshUI();
        }
    }

    /**
     * 刷新 界面
     */
    private void refreshUI() {
        if (goodsVO != null) {
            switch (viewType) {
                case DETAIL:
                    ImageLoader.getInstance().displayImage(goodsVO.getCoverUrl(), ivCover, ImageOption.createNomalOption());
                    tvPublishTime.setText(goodsVO.getPublishTime());
                    tvAttention.setVisibility(View.VISIBLE);
                    if (goodsVO.isAttention()) {
                        tvAttention.setText("取消关注");
                    } else {
                        tvAttention.setText("关注");
                    }
                    break;
                case PREVIEW:
                    ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(goodsVO.getCoverUrl()), ivCover, ImageOption.createNomalOption());
                    tvPublishTime.setText(Utils.formateTime(System.currentTimeMillis(), "yyyy-MM-dd"));
                    break;
            }
            tvGoodsName.setText(goodsVO.getName());
            tvScanNum.setText("浏览：" + goodsVO.getScanNum());
            tvAttentionNum.setText("关注：" + goodsVO.getAttentionNum());
            tvImageNum.setText("图片：" + (!Utils.isEmpty(goodsVO.getImageList()) ? goodsVO.getImageList().size() : 0));
            List<GoodsImageVO> goodsImageVOList = formateImage(goodsVO.getImageList());
            if (!Utils.isEmpty(goodsImageVOList)) {
                if (adapter == null) {
                    adapter = new GoodsImageAdapter(this) {
                        @Override
                        public void onClick(View v) {
                            String url = (String) v.getTag();
                            int index = 0;
                            // 计算索引
                            for (int i = 0; i < goodsVO.getImageList().size(); i++) {
                                if (url.equals(goodsVO.getImageList().get(i).getUrl())) {
                                    index = i;
                                    break;
                                }
                            }
                            ImageActivity_.intent(GoodsDetailAndPreviewActivity.this)
                                    .extra(IParam.IMAGES, (Serializable) goodsVO.getImageList())
                                    .extra(IParam.INDEX, index)
                                    .start();
                        }
                    };
                    adapter.setData(goodsImageVOList, viewType);
                    listView.setAdapter(adapter);
                } else {
                    adapter.setData(goodsImageVOList, viewType);
                    adapter.notifyDataSetChanged();
                }
            } else {
                listView.setAdapter(null);
            }
        }
    }

    /**
     * 转换数据格式
     *
     * @param data
     * @return
     */
    private List<GoodsImageVO> formateImage(List<ImageVO> data) {
        List<GoodsImageVO> list = new ArrayList<>();
        if (!Utils.isEmpty(data)) {
            GoodsImageVO goodsImgVO = new GoodsImageVO();
            ImageVO vo = data.get(0);
            goodsImgVO.setUrl1(viewType == Type.PREVIEW ? vo.getFilePath() : vo.getUrl());
            goodsImgVO.setDesc(vo.getDesc());
            list.add(goodsImgVO);
            for (int i = 1; i < data.size(); i++) {
                vo = data.get(i);
                if (Utils.isEmpty(list.get(list.size() - 1).getDesc())
                        && Utils.isEmpty(vo.getDesc())
                        && Utils.isEmpty(list.get(list.size() - 1).getUrl2())) {
                    // 上一组数据 无 描述信息  && 当前本身 无 描述信息 && 上一组数据 只有一个url
                    // 添加到上一项
                    list.get(list.size() - 1).setUrl2(viewType == Type.PREVIEW ? vo.getFilePath() : vo.getUrl());
                } else {
                    // 直接添加 新一项
                    goodsImgVO = new GoodsImageVO();
                    goodsImgVO.setUrl1(viewType == Type.PREVIEW ? vo.getFilePath() : vo.getUrl());
                    goodsImgVO.setDesc(vo.getDesc());
                    list.add(goodsImgVO);
                }
            }
        }
        return list;
    }

    /**
     * 初始化header
     */
    private void initHeader() {
        View header = LayoutInflater.from(this).inflate(R.layout.layout_header_goods_detail, null);
        header.setClickable(false);
        ivCover = (ImageView) header.findViewById(R.id.iv_cover);
        // 设置封面图 高度
        int coverHeight = Utils.getScreenHeight(this) / 3;
        ivCover.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, coverHeight));
        tvGoodsName = (TextView) header.findViewById(R.id.tv_goods_name);
        tvScanNum = (TextView) header.findViewById(R.id.tv_scan_num);
        tvAttentionNum = (TextView) header.findViewById(R.id.tv_attention_num);
        tvImageNum = (TextView) header.findViewById(R.id.tv_image_num);
        tvPublishTime = (TextView) header.findViewById(R.id.tv_publish_time);
        listView.addHeaderView(header);
    }


    @Override
    protected void onTaskSuccess(int action, Object data) {
        switch (action) {
            case TaskAction.ACTION_GET_GOODS_DETAIL:
                stopLoadingSelf();
                if (data != null) {
                    goodsVO = (GoodsVO) data;
                    refreshUI();
                }
                break;
            case TaskAction.ACTION_GOODS_ATTENTION:
                showToast("操作成功");
                goodsVO.setIsAttention(!goodsVO.isAttention());
                if (goodsVO.isAttention()) {
                    tvAttention.setText("取消关注");
                } else {
                    tvAttention.setText("关注");
                }
                break;
        }
    }

    @Override
    protected void onTaskFail(int action, String errorMessage) {
        stopLoadingSelf();
        showErrorMsg(errorMessage);
    }

    /**
     * 获取物品详情
     */
    private void getGoodsDetail() {
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_GOODS_DETAIL) {
            @Override
            protected void doBackground() throws Exception {
                setReturnData(goodsService.getGoodsDetail(goodsId));
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
        if (goodsVO != null) {
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
    protected void onDestroy() {
        super.onDestroy();
        ImageLoader.getInstance().clearMemoryCache();
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
     * 关注 /  取消关注
     */
    @Click(R.id.tv_attention)
    void attention() {
        if (userService.checkLoginState()) {
            TaskManager.pushTask(new Task(TaskAction.ACTION_GOODS_ATTENTION) {
                @Override
                protected void doBackground() throws Exception {
                    if (goodsVO.isAttention()) {
                        goodsService.cancelAttention(goodsId);
                    } else {
                        goodsService.attention(goodsId);
                    }
                }
            }, this);
        } else {
            LoginActivity_.intent(this).startForResult(0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            attention();
        }
    }
}
