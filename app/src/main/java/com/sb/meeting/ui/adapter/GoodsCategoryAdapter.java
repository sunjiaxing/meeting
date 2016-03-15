package com.sb.meeting.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.common.Utils;
import com.sb.meeting.ui.vo.GoodsCategoryVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 物品 分类 适配器
 * Created by sun on 2016/2/23.
 */
public class GoodsCategoryAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<GoodsCategoryVO> list;

    public GoodsCategoryAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<GoodsCategoryVO> data) {
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
            convertView = layoutInflater.inflate(R.layout.layout_item_select_option, null);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GoodsCategoryVO vo = list.get(position);
        viewHolder.tvContent.setText(vo.getName());
        return convertView;
    }

    class ViewHolder {
        TextView tvContent;
    }
}
