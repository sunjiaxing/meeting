package com.zhengshang.meeting.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.ImageOption;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.ui.vo.GoodsVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 物品列表 适配器
 * Created by sun on 2016/2/25.
 */
public class GoodsListAdapter extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private List<GoodsVO> list;

    public GoodsListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<GoodsVO> data) {
        this.list = new ArrayList<>(data);
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
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.layout_item_goods_list, null);
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.iv_image);
            viewHolder.tvGoodsName = (TextView) convertView.findViewById(R.id.tv_goods_name);
            viewHolder.tvScanNum = (TextView) convertView.findViewById(R.id.tv_scan_num);
            viewHolder.tvAttentionNum = (TextView) convertView.findViewById(R.id.tv_attention_num);
            viewHolder.tvPublishTime = (TextView) convertView.findViewById(R.id.tv_publish_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GoodsVO vo = list.get(position);
        ImageLoader.getInstance().displayImage(vo.getCoverUrl(), viewHolder.ivImage, ImageOption.createNomalOption());
        viewHolder.tvGoodsName.setText(vo.getName());
        viewHolder.tvScanNum.setText("浏览量：" + vo.getScanNum());
        viewHolder.tvAttentionNum.setText(vo.getAttentionNum() + "人关注");
        viewHolder.tvPublishTime.setText("发布时间：" + vo.getPublishTime());
        return convertView;
    }

    class ViewHolder {
        ImageView ivImage;
        TextView tvGoodsName;
        TextView tvScanNum;
        TextView tvAttentionNum;
        TextView tvPublishTime;
    }
}
