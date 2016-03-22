package com.sb.meeting.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sb.meeting.R;
import com.sb.meeting.common.ImageOption;
import com.sb.meeting.common.Utils;
import com.sb.meeting.ui.vo.GoodsVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 物品列表 适配器
 * Created by sun on 2016/2/25.
 */
public abstract class GoodsListAdapter extends BaseAdapter implements View.OnClickListener {

    private final LayoutInflater layoutInflater;
    private List<GoodsVO> list;
    private final LinearLayout.LayoutParams layoutParams;

    public GoodsListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        // 600 240
        int screenWidth = Utils.getScreenWidth(context) - 40;
        int h = screenWidth * 240 / 600;
        layoutParams = new LinearLayout.LayoutParams(screenWidth, h);
        int dp_10 = Utils.dip2px(context, 10);
        layoutParams.setMargins(dp_10, dp_10 / 2, dp_10, 0);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
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
            viewHolder.ivImage.setLayoutParams(layoutParams);
            viewHolder.tvGoodsName = (TextView) convertView.findViewById(R.id.tv_goods_name);
            viewHolder.tvAttention = (TextView) convertView.findViewById(R.id.tv_attention);
            viewHolder.ivAttentionTip = (ImageView) convertView.findViewById(R.id.iv_attention_tip);

            viewHolder.tvExchangePrice = (TextView) convertView.findViewById(R.id.tv_exchange_price);
            viewHolder.tvMarketPrice = (TextView) convertView.findViewById(R.id.tv_market_price);
            viewHolder.tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //删除线
            viewHolder.tvPublishTime = (TextView) convertView.findViewById(R.id.tv_publish_time);

            viewHolder.tvValidTime = (TextView) convertView.findViewById(R.id.tv_valid_time);
            viewHolder.tvScanNum = (TextView) convertView.findViewById(R.id.tv_scan_num);
            viewHolder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);
            viewHolder.tvAttentionNum = (TextView) convertView.findViewById(R.id.tv_attention_num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GoodsVO vo = list.get(position);
        viewHolder.tvGoodsName.setText(vo.getName());
        if (vo.isAttention()) {
            viewHolder.ivAttentionTip.setImageResource(R.mipmap.icon_attention_ok);
            viewHolder.tvAttention.setText("已关注");
        } else {
            viewHolder.ivAttentionTip.setImageResource(R.mipmap.icon_attention_plus);
            viewHolder.tvAttention.setText("关注");
        }
        viewHolder.tvAttention.setTag(position);
        viewHolder.ivAttentionTip.setTag(position);
        viewHolder.tvAttention.setOnClickListener(this);
        viewHolder.ivAttentionTip.setOnClickListener(this);

        viewHolder.tvExchangePrice.setText(Utils.parseDouble(vo.getExchangePrice()));
        viewHolder.tvMarketPrice.setText("¥" + Utils.parseDouble(vo.getMarketPrice()));
        viewHolder.tvPublishTime.setText(vo.getPublishTime());

        ImageLoader.getInstance().displayImage(vo.getCoverUrl(), viewHolder.ivImage, ImageOption.createNomalOption());

        viewHolder.tvValidTime.setText(vo.getValidTimeStr() + "内有效");
        viewHolder.tvScanNum.setText("浏览 " + vo.getScanNum());
        viewHolder.tvCount.setText("库存 " + vo.getCount());
        viewHolder.tvAttentionNum.setText("关注 " + vo.getAttentionNum());

        return convertView;
    }

    class ViewHolder {
        ImageView ivImage;
        ImageView ivAttentionTip;
        TextView tvGoodsName;
        TextView tvScanNum;
        TextView tvAttentionNum;
        TextView tvAttention;
        TextView tvPublishTime;
        TextView tvCount;
        TextView tvExchangePrice;
        TextView tvMarketPrice;
        TextView tvValidTime;

    }
}
