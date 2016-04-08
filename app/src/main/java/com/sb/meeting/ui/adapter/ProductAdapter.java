package com.sb.meeting.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.common.ImageOption;
import com.sb.meeting.common.Utils;
import com.sb.meeting.ui.vo.ProductVO;

import java.util.List;

/**
 * 产品列表适配器
 * Created by sun on 2016/4/1.
 */
public class ProductAdapter extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private List<ProductVO> list;
    private final LinearLayout.LayoutParams layoutParams;

    public ProductAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        int w = (Utils.getScreenWidth(context) - Utils.dip2px(context, 15)) / 2;
        layoutParams = new LinearLayout.LayoutParams(w, w);
    }

    public void setData(List<ProductVO> data) {
        this.list = data;
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
        return list.get(position).getProductId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.layout_item_product, null);
            viewHolder.ivThumb = (ImageView) convertView.findViewById(R.id.iv_thumb);
            viewHolder.ivThumb.setLayoutParams(layoutParams);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ProductVO vo = list.get(position);
        Utils.displayImage(vo.getThumb(), viewHolder.ivThumb, ImageOption.createNomalOption());
        viewHolder.tvName.setText(vo.getProductName());
        return convertView;
    }

    class ViewHolder {
        ImageView ivThumb;
        TextView tvName;
    }
}
