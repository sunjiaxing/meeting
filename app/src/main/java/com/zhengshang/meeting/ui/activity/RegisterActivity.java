package com.zhengshang.meeting.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
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
 * 用户注册 Activity
 * Created by sun on 2016/3/2.
 */
@EActivity(R.layout.layout_register)
public class RegisterActivity extends BaseActivity {

    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.edit_phone)
    EditText editPhone;
    @ViewById(R.id.edit_code)
    EditText editCode;
    @ViewById(R.id.edit_password)
    EditText editPassword;
    @ViewById(R.id.tv_show_password)
    TextView tvShowPassword;
    @ViewById(R.id.btn_get_code)
    Button btnGetCode;
    @ViewById(R.id.btn_register)
    Button btnRegister;
    private UserService userService;
    private int lastSecond = 120;
    private boolean isDisplay;
    private Handler handler;
    private static final int WHAT_TIMER = 1;

    @AfterViews
    void init() {
        tvTitle.setText("用户注册");
        ivBack.setVisibility(View.VISIBLE);
        String phoneNumber = Utils.getPhoneNumber(this);
        if (!Utils.isEmpty(phoneNumber)) {
            editPhone.setText(phoneNumber);
            editPhone.setSelection(phoneNumber.length());
            btnGetCode.setEnabled(true);
        } else {
            btnGetCode.setEnabled(false);
        }
        editPhone.addTextChangedListener(textWatcher);
        userService = new UserService(this);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == WHAT_TIMER) {
                    lastSecond--;
                    if (lastSecond > 0) {
                        btnGetCode.setText(lastSecond + "秒后重新获取");
                        handler.sendEmptyMessageDelayed(WHAT_TIMER, 1000);
                    } else {
                        btnGetCode.setEnabled(true);
                        btnGetCode.setText("获取验证码");
                    }
                }
            }
        };
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (Utils.checkPhoneValid(s.toString())) {
                handler.removeMessages(WHAT_TIMER);
                btnGetCode.setEnabled(true);
            } else {
                btnGetCode.setEnabled(false);
            }
        }
    };

    /**
     * 获取验证码
     */
    @Click(R.id.btn_get_code)
    void getCode() {
        final String phone = editPhone.getText().toString();
        if (!Utils.isEmpty(phone) && Utils.checkPhoneValid(phone)) {
            btnGetCode.setEnabled(false);
            startLoading();
            TaskManager.pushTask(new Task(TaskAction.ACTION_GET_CODE) {
                @Override
                protected void doBackground() throws Exception {
                    userService.getRegisterCode(phone);
                }
            }, this);
        }
    }

    @Override
    protected void onTaskSuccess(int action, Object data) {
        switch (action) {
            case TaskAction.ACTION_GET_CODE:// 获取验证码
                stopLoading();
                showToast("获取成功，请耐心等待短信通知");
                btnGetCode.setText(lastSecond + "秒后重新获取");
                handler.sendEmptyMessageDelayed(WHAT_TIMER, 1000);
                break;
            case TaskAction.ACTION_REGISTER:// 注册
                stopLoading();
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    /**
     * 注册
     */
    @Click(R.id.btn_register)
    void register() {
        final String phone = editPhone.getText().toString();
        final String code = editCode.getText().toString();
        final String password = editPassword.getText().toString();
        if (Utils.isEmpty(phone)) {
            editPhone.requestFocus();
        } else if (!Utils.checkPhoneValid(phone)) {
            editPhone.requestFocus();
            showToast("手机号码不正确");
        } else if (Utils.isEmpty(code)) {
            editCode.requestFocus();
        } else if (Utils.isEmpty(password)) {
            editPassword.requestFocus();
        } else {
            btnRegister.setEnabled(false);
            startLoading("注册中...");
            TaskManager.pushTask(new Task(TaskAction.ACTION_REGISTER) {
                @Override
                protected void doBackground() throws Exception {
                    userService.register(phone, code, password);
                }
            }, this);
        }
    }

    @Click(R.id.iv_back)
    void back() {
        if (handler != null) {
            handler.removeMessages(WHAT_TIMER);
        }
        finish();
    }

    @Click(R.id.tv_show_password)
    void showPassword() {
        if (isDisplay) {
            // 改为隐藏
            // 设置密码为隐藏的
            editPassword
                    .setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
            tvShowPassword.setText("显示密码");
            isDisplay = false;
        } else {
            // 改为显示
            // 设置EditText的密码为可见的
            editPassword
                    .setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
            tvShowPassword.setText("隐藏密码");
            isDisplay = true;
        }
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
    protected void onTaskFail(int action, String errorMessage) {
        switch (action) {
            case TaskAction.ACTION_GET_CODE:
                stopLoading();
                btnGetCode.setEnabled(true);
                showToast(errorMessage);
                break;
            case TaskAction.ACTION_REGISTER:
                stopLoading();
                showToast(errorMessage);
                btnRegister.setEnabled(true);
                break;
            default:
                stopLoading();
                super.onTaskFail(action, errorMessage);
                break;
        }
    }
}
