package com.zhengshang.meeting.ui.activity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taskmanager.Task;
import com.taskmanager.TaskManager;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.BonConstants;
import com.zhengshang.meeting.common.ImageOption;
import com.zhengshang.meeting.common.TaskAction;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.service.UserService;
import com.zhengshang.meeting.ui.vo.UserVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 用户中心页面
 * Created by sun on 2016/1/18.
 */
@EActivity(R.layout.layout_user_center)
public class UserCenterActivity extends BaseActivity {
    // 以下注入组件
    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.iv_right)
    ImageView ivRight;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.iv_user_avatar)
    ImageView ivUserAvatar;
    @ViewById(R.id.tv_user_name)
    TextView tvUserName;
    @ViewById(R.id.tv_logout)
    TextView tvLogout;
    private UserService userService;
    private UserVO userVO;

    @AfterViews
    void init() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("用户中心");
        userService = new UserService(this);
        getUserInfo();
    }

    @Click(R.id.tv_logout)
    void logout() {
        userService.logout();
        showToast("退出成功");
        finish();
    }

    @Click(R.id.iv_back)
    void back() {
        finish();
    }

    /**
     * 获取用户信息
     */
    void getUserInfo() {
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_USER_INFO) {
            @Override
            protected void doBackground() throws Exception {
                setReturnData(userService.getLoginUserInfo());
            }
        }, this);
    }

    @Override
    protected void onTaskSuccess(int action, Object data) {
        switch (action) {
            case TaskAction.ACTION_GET_USER_INFO:
                if (data != null) {
                    userVO = (UserVO) data;
                    refreshUI();
                }
                break;
        }
    }

    /**
     * 刷新界面
     */
    private void refreshUI() {
        if (userVO != null) {
            tvLogout.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(
                    Utils.getUserAvatar(userVO.getUserId(), BonConstants.UserAvatarType.TYPE_180x180),
                    ivUserAvatar,
                    ImageOption.createRoundOption(50));
            tvUserName.setText(userVO.getNickName());
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
}
