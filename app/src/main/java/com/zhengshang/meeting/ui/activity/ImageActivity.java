package com.zhengshang.meeting.ui.activity;

import android.view.KeyEvent;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.ImageOption;
import com.zhengshang.meeting.remote.IParam;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 图片 展示 activity
 * Created by sun on 2016/1/26.
 */
@EActivity(R.layout.layout_image)
public class ImageActivity extends BaseActivity {

    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.iv_image)
    ImageView ivImage;

    @AfterViews
    void init() {
        String url = getIntent().getStringExtra(IParam.URL);
        ImageLoader.getInstance().displayImage(url, ivImage, ImageOption.createNomalOption());
    }

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

    }
}
