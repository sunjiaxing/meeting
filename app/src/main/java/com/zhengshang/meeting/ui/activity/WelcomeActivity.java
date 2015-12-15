package com.zhengshang.meeting.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.zhengshang.meeting.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by sun on 2015/12/14.
 */
@EActivity(R.layout.layout_main)
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
                    NewsActivity_.intent(WelcomeActivity.this).start();
                    finish();
                }
            }
        };
        handler.sendEmptyMessageDelayed(1, 3000);
    }


    @Override
    protected void onTaskSuccess(int action, Object data) {

    }
}
