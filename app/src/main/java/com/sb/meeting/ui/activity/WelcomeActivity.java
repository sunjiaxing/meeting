package com.sb.meeting.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.sb.meeting.R;
import com.sb.meeting.common.TaskAction;
import com.sb.meeting.service.YellowPageService;
import com.taskmanager.Task;
import com.taskmanager.TaskManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


/**
 * 欢迎页面
 * Created by sun on 2015/12/14.
 */
@EActivity(R.layout.layout_welcome)
public class WelcomeActivity extends BaseActivity {

    @ViewById(R.id.iv_init)
    ImageView ivInit;

    @AfterViews
    void init() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    MainActivity_.intent(WelcomeActivity.this).start();
                    finish();
                }
            }
        };
        handler.sendEmptyMessageDelayed(1, 3000);
        TaskManager.pushTask(new Task(TaskAction.ACTION_INIT_AREA) {
            @Override
            protected void doBackground() throws Exception {
                YellowPageService yellowPageService = new YellowPageService(WelcomeActivity.this);
                yellowPageService.initArea();
                yellowPageService.getConfigInfoFromWeb();
                setNeedCallBack(false);
            }
        }, this);
    }


    @Override
    protected void onTaskSuccess(int action, Object data) {

    }
}
