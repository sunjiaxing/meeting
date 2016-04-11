package com.sb.meeting.ui.activity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.common.TaskAction;
import com.sb.meeting.common.Utils;
import com.sb.meeting.service.UserService;
import com.sb.meeting.ui.component.TlcyDialog;
import com.taskmanager.Task;
import com.taskmanager.TaskManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 设置页面 Activity
 * Created by sun on 2016/4/9.
 */
@EActivity(R.layout.layout_setting)
public class SettingActivity extends BaseActivity {

    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.tv_title)
    TextView tvTitle;

    @ViewById(R.id.tv_logout)
    TextView tvLogout;
    private UserService userService;

    @AfterViews
    void init() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("设置");
        userService = new UserService(this);
        if (userService.checkLoginState()) {
            tvLogout.setVisibility(View.VISIBLE);
        } else {
            tvLogout.setVisibility(View.GONE);
        }
    }

    /**
     * 清除缓存
     */
    @Click(R.id.tv_clear_cache)
    void clearCache() {
        showAlert("缓存能帮助您节省流量，但占用系统空间，确定要清除缓存吗？", new TlcyDialog.TlcyDialogListener() {
            @Override
            public void onClick() {
                startLoading("清除中...");
                TaskManager.pushTask(new Task(TaskAction.ACTION_CLEAR_CACHE) {
                    @Override
                    protected void doBackground() throws Exception {
                        userService.clearCache();
                    }
                }, SettingActivity.this);
            }
        }, null);
    }

    /**
     * 给我们评价
     */
    @Click(R.id.tv_point)
    void giveScore() {
        Utils.toMarketGrade(this);
    }

    /**
     * 检查更新
     */
    @Click(R.id.tv_check_update)
    void checkUpdate() {
        startLoading("检查中...");
        TaskManager.pushTask(new Task(TaskAction.ACTION_CHECK_UPDATE) {
            @Override
            protected void doBackground() throws Exception {
                setReturnData(userService.updateVersion());
            }
        }, this);
    }

    /**
     * 退出登录
     */
    @Click(R.id.tv_logout)
    void logout() {
        showAlert("确定要退出登录吗？", new TlcyDialog.TlcyDialogListener() {
            @Override
            public void onClick() {
                userService.logout();
                setResult(RESULT_OK);
                finish();
            }
        }, null);
    }

    /**
     * 返回
     */
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

    @Override
    protected void onTaskSuccess(int action, Object data) {
        if (action == TaskAction.ACTION_CLEAR_CACHE) {
            stopLoading();
            showToast("清理结束");
        } else if (action == TaskAction.ACTION_CHECK_UPDATE) {
            stopLoading();

        }
    }

    @Override
    protected void onTaskFail(int action, String errorMessage) {
        stopLoading();
        showToast(errorMessage);
    }
}
