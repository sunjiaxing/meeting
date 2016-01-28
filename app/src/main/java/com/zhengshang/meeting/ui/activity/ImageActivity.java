package com.zhengshang.meeting.ui.activity;

import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.ImageOption;
import com.zhengshang.meeting.common.Utils;
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
    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @AfterViews
    void init() {
        String url = getIntent().getStringExtra(IParam.URL);
        ImageLoader.getInstance().displayImage(url, ivImage, ImageOption.createNomalOption(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressBar.setVisibility(View.GONE);
                ivImage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
                // 重新计算图片宽和高  screenWidth    imageWidth
                //                   xxxx           imageHeight
                int screenWidth = Utils.getScreenWidth(ImageActivity.this);
                int ivHeight = screenWidth * loadedImage.getHeight() / loadedImage.getWidth();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ivHeight);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                ivImage.setLayoutParams(params);
                ivImage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressBar.setVisibility(View.GONE);
            }
        });
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
