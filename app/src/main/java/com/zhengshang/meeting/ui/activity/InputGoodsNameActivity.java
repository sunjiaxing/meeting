package com.zhengshang.meeting.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.remote.IParam;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * 输入商品名称页面
 * Created by sun on 2016/2/19.
 */
@EActivity(R.layout.layout_input_goods_name)
public class InputGoodsNameActivity extends BaseActivity {

    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.btn_right)
    Button btnRight;
    @ViewById(R.id.edit_goods_name)
    EditText editGoodsName;
    @Extra(IParam.GOODS_NAME)
    String goodsName;


    @AfterViews
    void init() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("商品名称");
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setBackgroundColor(Color.TRANSPARENT);
        btnRight.setText("下一步");
        btnRight.setEnabled(false);
        if (!Utils.isEmpty(goodsName)) {
            editGoodsName.setText(goodsName);
            editGoodsName.setSelection(goodsName.length());
        }
        editGoodsName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    btnRight.setEnabled(true);
                } else {
                    btnRight.setEnabled(false);
                }
            }
        });
    }

    @Click(R.id.iv_back)
    void back() {
        finish();
    }

    @Click(R.id.btn_right)
    void next() {
        Intent data = new Intent();
        data.putExtra(IParam.CONTENT, editGoodsName.getText().toString());
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    protected void onTaskSuccess(int action, Object data) {

    }
}
