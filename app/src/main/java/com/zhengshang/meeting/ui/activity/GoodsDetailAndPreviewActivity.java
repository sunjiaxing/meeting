package com.zhengshang.meeting.ui.activity;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.taskmanager.Task;
import com.taskmanager.TaskManager;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.TaskAction;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.remote.IParam;
import com.zhengshang.meeting.service.GoodsService;
import com.zhengshang.meeting.service.UserService;
import com.zhengshang.meeting.ui.vo.GoodsVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

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

    @Extra(IParam.GOODS_ID)
    int goodsId;
    @Extra(IParam.TYPE)
    Type viewType;

    private AnimationDrawable anim;
    private GoodsVO goodsVO;
    private GoodsService goodsService;
    private UserService userService;

    public enum Type {
        DETAIL, PREVIEW
    }

    @AfterViews
    void init(){
        ivBack.setVisibility(View.VISIBLE);
        anim = (AnimationDrawable) ivLoading.getBackground();
        goodsVO = (GoodsVO) getIntent().getSerializableExtra(IParam.GOODS);
        goodsService = new GoodsService(this);
        userService = new UserService(this);
        if (viewType == Type.DETAIL) {
            tvTitle.setText("详情");
            startLoading();
            getGoodsDetail();
        } else if (viewType == Type.PREVIEW){
            tvTitle.setText("预览");
            refreshUI();
        }
    }



    private void refreshUI() {

    }


    @Override
    protected void onTaskSuccess(int action, Object data) {

    }

    private void getGoodsDetail() {
        String userId = userService.getLoginUserId();
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_GOODS_DETAIL) {
            @Override
            protected void doBackground() throws Exception {

            }
        },this);
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
}
