package com.sb.meeting.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.taskmanager.Task;
import com.taskmanager.TaskManager;
import com.sb.meeting.R;
import com.sb.meeting.common.TaskAction;
import com.sb.meeting.common.Utils;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.service.GoodsService;
import com.sb.meeting.service.UserService;
import com.sb.meeting.ui.adapter.GoodsCategoryAdapter;
import com.sb.meeting.ui.vo.GoodsCategoryVO;
import com.sb.meeting.ui.vo.GoodsVO;
import com.sb.meeting.ui.vo.UserVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 选择期望物品分类和 输入联系方式页面
 * Created by sun on 2016/2/24.
 */
@EActivity(R.layout.layout_input_need_contact)
public class InputNeedAndContactActivity extends BaseActivity {

    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.btn_right)
    Button btnRight;
    @ViewById(R.id.tv_select_need)
    TextView tvSelectNeed;
    @ViewById(R.id.edit_phone)
    EditText editPhone;

    private GoodsVO goodsVO;
    private List<GoodsCategoryVO> categories;
    private GoodsService goodsService;

    @AfterViews
    void init() {
        ivBack.setVisibility(View.VISIBLE);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setBackgroundColor(Color.TRANSPARENT);
        btnRight.setText("完成");
        goodsVO = (GoodsVO) getIntent().getSerializableExtra(IParam.GOODS);
        categories = (List<GoodsCategoryVO>) getIntent().getSerializableExtra(IParam.CATEGORIES);
        UserService userService = new UserService(this);
        UserVO userInfo = userService.getLoginUserInfo();
        goodsService = new GoodsService(this);
        editPhone.setText(userInfo.getMobile());
        editPhone.setSelection(editPhone.getText().length());
        tvTitle.setText("期望");
    }

    @Click(R.id.tv_select_need)
    void selectCategory() {
        GoodsCategoryAdapter categoryAdapter = new GoodsCategoryAdapter(this);
        categoryAdapter.setData(categories);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择品类");
        builder.setAdapter(categoryAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goodsVO.setNeedCategory(categories.get(which));
                tvSelectNeed.setText(goodsVO.getNeedCategory().getName());
            }
        });
        builder.show();
    }

    @Click(R.id.btn_right)
    void complete() {
        if (Utils.isEmpty(editPhone.getText().toString())) {
            editPhone.requestFocus();
            showToast("请输入联系方式");
            return;
        }

        if (!Utils.checkPhoneValid(editPhone.getText().toString())) {
            editPhone.requestFocus();
            showToast("手机号码格式不正确");
            return;
        }
        startLoading("发布中...");
        TaskManager.pushTask(new Task(TaskAction.ACTION_PUBLISH_GOODS) {
            @Override
            protected void doBackground() throws Exception {
                goodsService.publishGoods(goodsVO, editPhone.getText().toString());
            }
        }, this);
    }


    @Override
    protected void onTaskSuccess(int action, Object data) {
        switch (action) {
            case TaskAction.ACTION_PUBLISH_GOODS:
                stopLoading();
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    protected void onTaskFail(int action, String errorMessage) {
        stopLoading();
        super.onTaskFail(action, errorMessage);
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
}
