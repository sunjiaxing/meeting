package com.zhengshang.meeting.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import com.taskmanager.LogUtils;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.remote.IParam;
import com.zhengshang.meeting.ui.vo.UserVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

/**
 * Created by sun on 2016/1/8.
 */
@EActivity(R.layout.layout_test)
public class TestActivity extends BaseActivity {

    private String url;
    private String test;
    private UserVO userVO;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_test);
//        Button btn = (Button) findViewById(R.id.btn);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//    }
    @AfterViews
    void init(){
        url = getIntent().getStringExtra(IParam.URL);
        LogUtils.e(url);
        test = url;
        userVO = new UserVO();
        if (url.equals("你好啊")){
            userVO.setNickName(url);
        } else {
            userVO.setUserName(url);
        }
    }
    @Click(R.id.btn)
    void click(){
        /*Intent intent = new Intent(TestActivity.this, TestActivity_.class);
        intent.putExtra(IParam.URL, "你好啊");
        startActivity(intent);*/

        TestActivity_.intent(this).extra(IParam.URL,"你好啊").start();
    }

    @Override
    protected void onTaskSuccess(int action, Object data) {

    }
}
