package com.sb.meeting.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sb.meeting.common.ImageOption;
import com.sb.meeting.ui.vo.ImageVO;

import java.util.List;

/**
 * 企业详情 滚动图片适配器
 * Created by sun on 2016/4/8.
 */
public class CompanyScrollImageAdapter extends PagerAdapter {
    private List<ImageVO> adItem;
    private Context context;
    private OnClickImageListener listener;
    private LinearLayout.LayoutParams params;

    public CompanyScrollImageAdapter(Context context) {
        this.context = context;
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void setData(List<ImageVO> adItem) {
        this.adItem = adItem;
    }

    @Override
    public int getCount() {
        return adItem.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imagesView = new ImageView(context);
        imagesView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imagesView.setLayoutParams(params);
        imagesView.setTag(position);
        ImageLoader.getInstance().displayImage(adItem.get(position).getUrl(),
                imagesView, ImageOption.createNomalOption());
        imagesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickView(v);
                }

            }
        });
        container.addView(imagesView, position);
        return imagesView;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    public void setOnImageClickListener(OnClickImageListener listener) {
        this.listener = listener;
    }

    public interface OnClickImageListener {
        void onClickView(View v);
    }
}
