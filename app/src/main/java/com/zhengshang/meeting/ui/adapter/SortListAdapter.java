package com.zhengshang.meeting.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.ImageOption;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.ui.vo.ImageVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 输入 物品  用于排序的listview
 * Created by sun on 2016/2/22.
 */
public abstract class SortListAdapter extends BaseAdapter implements View.OnClickListener {
    private List<ImageVO> goodsVOList;
    private LayoutInflater layoutInflater;

    public SortListAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<ImageVO> data) {
        this.goodsVOList = new ArrayList<>(data);
    }

    @Override
    public int getCount() {
        return !Utils.isEmpty(goodsVOList) ? goodsVOList.size() : 0;
    }

    @Override
    public ImageVO getItem(int position) {
        return goodsVOList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.layout_item_input_goods, null);
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.iv_image);
            viewHolder.tvInputDesc = (TextView) convertView.findViewById(R.id.tv_input_desc);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageVO vo = goodsVOList.get(position);
        ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(vo.getFilePath()), viewHolder.ivImage, ImageOption.createNomalOption());
        if (!Utils.isEmpty(vo.getDesc())) {
            viewHolder.tvInputDesc.setText(vo.getDesc());
        } else {
            viewHolder.tvInputDesc.setText(R.string.input_image_desc_tip);
        }
        viewHolder.tvInputDesc.setTag(position);
        viewHolder.tvInputDesc.setOnClickListener(this);
        return convertView;
    }

    class ViewHolder {
        ImageView ivImage;
        TextView tvInputDesc;
    }

}
