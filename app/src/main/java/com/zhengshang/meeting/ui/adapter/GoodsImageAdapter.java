package com.zhengshang.meeting.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.ImageOption;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.ui.activity.GoodsDetailAndPreviewActivity;
import com.zhengshang.meeting.ui.vo.GoodsImageVO;

import java.util.List;

/**
 * 物品 详情/预览  图片列表
 * Created by sun on 2016/3/1.
 */
public abstract class GoodsImageAdapter extends BaseAdapter implements View.OnClickListener {

    private final LayoutInflater layoutInflater;
    private List<GoodsImageVO> list;
    private final LinearLayout.LayoutParams layoutParamsLeft;
    private final LinearLayout.LayoutParams layoutParamsRight;
    private GoodsDetailAndPreviewActivity.Type viewType;
    private final int screenW;

    public GoodsImageAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        screenW = Utils.getScreenWidth(context);
        int imgH = (screenW - Utils.dip2px(context, 26)) / 2;
        int dp_3 = Utils.dip2px(context, 3);
        layoutParamsLeft = new LinearLayout.LayoutParams(imgH, imgH);
        layoutParamsLeft.rightMargin = dp_3;
        layoutParamsRight = new LinearLayout.LayoutParams(imgH, imgH);
        layoutParamsRight.leftMargin = dp_3;
    }

    public void setData(List<GoodsImageVO> data, GoodsDetailAndPreviewActivity.Type type) {
        this.list = data;
        this.viewType = type;
    }

    @Override
    public int getCount() {
        return !Utils.isEmpty(list) ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.layout_item_goods_image, null);
            viewHolder.layoutSingle = convertView.findViewById(R.id.layout_single);
            viewHolder.layoutMore = convertView.findViewById(R.id.layout_more);
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.iv_image);
            viewHolder.ivImage1 = (ImageView) convertView.findViewById(R.id.iv_image1);
            viewHolder.ivImage2 = (ImageView) convertView.findViewById(R.id.iv_image2);
            viewHolder.tvText = (TextView) convertView.findViewById(R.id.tv_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GoodsImageVO vo = list.get(position);
        if (Utils.isEmpty(vo.getDesc()) && !Utils.isEmpty(vo.getUrl2())) {
            // 多张
            viewHolder.layoutSingle.setVisibility(View.GONE);
            viewHolder.layoutMore.setVisibility(View.VISIBLE);
            viewHolder.ivImage1.setLayoutParams(layoutParamsLeft);
            viewHolder.ivImage2.setLayoutParams(layoutParamsRight);
            if (viewType == GoodsDetailAndPreviewActivity.Type.PREVIEW) {
                ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(vo.getUrl1()), viewHolder.ivImage1, ImageOption.createNomalOption());
                ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(vo.getUrl2()), viewHolder.ivImage2, ImageOption.createNomalOption());
            } else {
                ImageLoader.getInstance().displayImage(vo.getUrl1(), viewHolder.ivImage1, ImageOption.createNomalOption());
                ImageLoader.getInstance().displayImage(vo.getUrl2(), viewHolder.ivImage2, ImageOption.createNomalOption());
                viewHolder.ivImage1.setTag(vo.getUrl1());
                viewHolder.ivImage2.setTag(vo.getUrl2());
                viewHolder.ivImage1.setOnClickListener(this);
                viewHolder.ivImage2.setOnClickListener(this);
            }
        } else {
            // 单张
            viewHolder.layoutSingle.setVisibility(View.VISIBLE);
            viewHolder.layoutMore.setVisibility(View.GONE);
            viewHolder.tvText.setText(vo.getDesc());
            if (viewType == GoodsDetailAndPreviewActivity.Type.PREVIEW) {
                ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(vo.getUrl1()), viewHolder.ivImage, ImageOption.createNomalOption(), loadingListener);
            } else {
                ImageLoader.getInstance().displayImage(vo.getUrl1(), viewHolder.ivImage, ImageOption.createNomalOption(), loadingListener);
                viewHolder.ivImage.setTag(vo.getUrl1());
                viewHolder.ivImage.setOnClickListener(this);
            }
        }
        return convertView;
    }

    class ViewHolder {
        View layoutSingle;
        View layoutMore;
        ImageView ivImage;
        TextView tvText;
        ImageView ivImage1;
        ImageView ivImage2;
    }
    ImageLoadingListener loadingListener = new SimpleImageLoadingListener() {
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            //  screenW   imgW
            //    ?       imgH
            int needH = screenW * loadedImage.getHeight() / loadedImage.getWidth();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenW, needH);
            if (view != null) {
                ImageView iv = (ImageView) view;
                iv.setLayoutParams(layoutParams);
                iv.setImageBitmap(loadedImage);
            }
        }
    };

}
