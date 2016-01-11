package com.zhengshang.meeting.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 登录 activity
 * Created by sun on 2016/1/11.
 */
@EActivity(R.layout.layout_login)
public class LoginActivity extends BaseActivity {

    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.edit_user_name)
    EditText editUserName;
    @ViewById(R.id.edit_password)
    EditText editPassword;
    @ViewById(R.id.btn_login)
    Button btnLogin;
    @ViewById(R.id.tv_find_pwd)
    TextView tvFindPwd;
    @ViewById(R.id.tv_register)
    TextView tvRegister;

    @AfterViews
    void init() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("用户登录");
    }

    @Click(R.id.btn_login)
    void login() {
        String userName = editUserName.getText().toString();
        String password = editPassword.getText().toString();
        if (Utils.isEmpty(userName)) {
            editUserName.requestFocus();
            return;
        }
        if (Utils.isEmpty(password)){
            editPassword.requestFocus();
            return;
        }

    }

    @Override
    protected void onTaskSuccess(int action, Object data) {

    }
}
