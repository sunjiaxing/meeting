package com.sb.meeting.ui.activity;

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

import com.sb.meeting.R;
import com.sb.meeting.common.BonConstants;
import com.sb.meeting.common.TaskAction;
import com.sb.meeting.common.Utils;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.service.UserService;
import com.taskmanager.LogUtils;
import com.taskmanager.Task;
import com.taskmanager.TaskManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

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
    private static final int WHAT_CODE_ERROR = 2;
    private static final int WHAT_GET_CODE_SUCCESS = 3;
    private static final int WHAT_CHECK_CODE_SUCCESS = 4;
    private EventHandler eventHandler;
    private String phone;
    private String code;
    private String password;

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
                switch (msg.what) {
                    case WHAT_TIMER:
                        lastSecond--;
                        if (lastSecond > 0) {
                            btnGetCode.setText(lastSecond + "秒后重新获取");
                            handler.sendEmptyMessageDelayed(WHAT_TIMER, 1000);
                        } else {
                            setGetCodeButtonEnable();
                        }
                        break;
                    case WHAT_CODE_ERROR:// 验证码SDK错误信息
                        try {
                            btnRegister.setEnabled(true);
                            setGetCodeButtonEnable();
                            stopLoading();
                            JSONObject json = new JSONObject(((Throwable) msg.obj).getMessage());
                            showToast(json.getString(IParam.DETAIL));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case WHAT_GET_CODE_SUCCESS:// 成功获取验证码
                        getCodeSuccess();
                        break;
                    case WHAT_CHECK_CODE_SUCCESS:// 验证码通过
                        commitRegister();
                        break;
                }
            }
        };
        initSMSSDK();
    }

    /**
     * 初始化短信验证码SDK
     */
    private void initSMSSDK() {
        SMSSDK.initSDK(this, BonConstants.KEY_SMS, BonConstants.SECRET_SMS);
        if (eventHandler == null) {
            eventHandler = new EventHandler() {
                @Override
                public void afterEvent(int event, int result, Object data) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //回调完成
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            //提交验证码成功
                            handler.sendEmptyMessage(WHAT_CHECK_CODE_SUCCESS);
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //获取验证码成功
                            handler.sendEmptyMessage(WHAT_GET_CODE_SUCCESS);
                        } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                            //返回支持发送验证码的国家列表
                            ArrayList<HashMap<String, Object>> c = (ArrayList<HashMap<String, Object>>) data;
                            for (HashMap<String, Object> map : c) {
                                for (String key : map.keySet()) {
                                    LogUtils.e("key:" + key + "--------value:" + map.get(key));
                                }
                            }
                        }
                    } else {
                        handler.sendMessage(handler.obtainMessage(WHAT_CODE_ERROR, data));
                    }
                }
            };
        }
        SMSSDK.registerEventHandler(eventHandler); //注册短信回调
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
                btnGetCode.setEnabled(true);
            } else {
                btnGetCode.setEnabled(false);
            }
        }
    };

    /**
     * 设置获取验证码按钮可用
     */
    private void setGetCodeButtonEnable() {
        handler.removeMessages(WHAT_TIMER);
        btnGetCode.setEnabled(true);
        btnGetCode.setText("获取验证码");
    }

    /**
     * 获取验证码
     */
    @Click(R.id.btn_get_code)
    void getCode() {
        final String phone = editPhone.getText().toString();
        if (!Utils.isEmpty(phone) && Utils.checkPhoneValid(phone)) {
            btnGetCode.setEnabled(false);
            editPhone.setEnabled(false);
            startLoading();
            SMSSDK.getVerificationCode("86", phone);
//
//            TaskManager.pushTask(new Task(TaskAction.ACTION_GET_CODE) {
//                @Override
//                protected void doBackground() throws Exception {
//                    userService.getRegisterCode(phone);
//                }
//            }, this);
        }
    }

    @Override
    protected void onTaskSuccess(int action, Object data) {
        switch (action) {
            case TaskAction.ACTION_GET_CODE:// 获取验证码
                getCodeSuccess();
                break;
            case TaskAction.ACTION_REGISTER:// 注册
                stopLoading();
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    /**
     * 成功获取验证码
     */
    private void getCodeSuccess() {
        stopLoading();
        showToast("获取成功，请耐心等待短信通知");
        btnGetCode.setText(lastSecond + "秒后重新获取");
        handler.sendEmptyMessageDelayed(WHAT_TIMER, 1000);
    }

    /**
     * 注册
     */
    @Click(R.id.btn_register)
    void register() {
        phone = editPhone.getText().toString();
        code = editCode.getText().toString();
        password = editPassword.getText().toString();
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
            SMSSDK.submitVerificationCode("86", phone, code);
//            TaskManager.pushTask(new Task(TaskAction.ACTION_REGISTER) {
//                @Override
//                protected void doBackground() throws Exception {
//                    userService.register(phone, code, password);
//                }
//            }, this);
        }
    }

    /**
     * 提交注册信息
     */
    private void commitRegister() {
        TaskManager.pushTask(new Task(TaskAction.ACTION_REGISTER) {
            @Override
            protected void doBackground() throws Exception {
                userService.register(phone, code, password);
            }
        }, this);
    }

    @Click(R.id.iv_back)
    void back() {
        if (handler != null) {
            handler.removeMessages(WHAT_TIMER);
        }
        SMSSDK.unregisterEventHandler(eventHandler);
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
