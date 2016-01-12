package com.zhengshang.meeting.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.taskmanager.Task;
import com.taskmanager.TaskManager;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.TaskAction;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.service.UserService;

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

    private UserService userService;

    @AfterViews
    void init() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("用户登录");
        userService = new UserService(this);
    }

    @Click(R.id.btn_login)
    void login() {
        final String userName = editUserName.getText().toString();
        final String password = editPassword.getText().toString();
        if (Utils.isEmpty(userName)) {
            editUserName.requestFocus();
            return;
        }
        if (Utils.isEmpty(password)) {
            editPassword.requestFocus();
            return;
        }
        startLoading("登录中...", true);
        TaskManager.pushTask(new Task(TaskAction.ACTION_LOGIN) {
            @Override
            protected void doBackground() throws Exception {
                userService.login(userName, password);
            }
        }, this);
    }

    @Override
    protected void onTaskSuccess(int action, Object data) {
        switch (action) {
            case TaskAction.ACTION_LOGIN:// 登录
                stopLoading();
                showToastLongTime("登录成功");
                break;
        }
    }

    @Override
    protected void onTaskFail(int action, String errorMessage) {
        switch (action) {
            case TaskAction.ACTION_LOGIN:// 登录
                stopLoading();
                showToastLongTime(errorMessage);
                break;
        }
    }
}
