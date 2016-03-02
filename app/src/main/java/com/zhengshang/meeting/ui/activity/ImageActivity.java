package com.zhengshang.meeting.ui.activity;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.ImageOption;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.remote.IParam;
import com.zhengshang.meeting.ui.component.ChildViewPager;
import com.zhengshang.meeting.ui.vo.ImageVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片 展示 activity
 * Created by sun on 2016/1/26.
 */
@EActivity(R.layout.layout_image_main)
public class ImageActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.vp_image)
    ChildViewPager viewPager;
    @ViewById(R.id.layout_desc_and_position)
    LinearLayout layoutDescAndPos;
    @ViewById(R.id.tv_desc)
    TextView tvDesc;
    @ViewById(R.id.tv_position)
    TextView tvPosition;
    @Extra(IParam.INDEX)
    int index;
    private List<ImageVO> imageList;
    private DisplayImageOptions options;

    @AfterViews
    void init() {
        imageList = (List<ImageVO>) getIntent().getSerializableExtra(IParam.IMAGES);
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        // 计算 底部描述和索引记录的 高度
        int h = Utils.getScreenHeight(this) / 4;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutDescAndPos.setLayoutParams(layoutParams);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        if (index != 0) {
            viewPager.setCurrentItem(index);
        } else {
            ImageVO vo = imageList.get(0);
            if (!Utils.isEmpty(vo.getDesc())) {
                tvDesc.setVisibility(View.VISIBLE);
                tvDesc.setText(vo.getDesc());
            } else {
                tvDesc.setVisibility(View.INVISIBLE);
            }
            tvPosition.setText("1/1");
        }
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

    PagerAdapter adapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return !Utils.isEmpty(imageList) ? imageList.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageVO vo = imageList.get(position);
            View view = getLayoutInflater().inflate(R.layout.layout_image, null);
            final ImageView ivImage = (ImageView) view.findViewById(R.id.iv_image);
            ivImage.setTag(position);
            ivImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return false;
                }
            });
            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            ImageLoader.getInstance().displayImage(vo.getUrl(), ivImage, options, new ImageLoadingListener() {
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
            container.addView(view, 0);
            return view;
        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ImageVO vo = imageList.get(position);
        if (!Utils.isEmpty(vo.getDesc())) {
            tvDesc.setVisibility(View.VISIBLE);
            tvDesc.setText(vo.getDesc());
        } else {
            tvDesc.setVisibility(View.INVISIBLE);
        }
        tvPosition.setText((position + 1) + "/" + imageList.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
