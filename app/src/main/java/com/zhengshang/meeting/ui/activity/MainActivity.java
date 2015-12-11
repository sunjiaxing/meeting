package com.zhengshang.meeting.ui.activity;


import com.zhengshang.meeting.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.layout_main)
public class MainActivity extends BaseActivity {
    @AfterViews
    void init() {
        NewsActivity_.intent(this).start();
        finish();
    }


    @Override
    protected void onTaskSuccess(int action, Object data) {

    }
}
