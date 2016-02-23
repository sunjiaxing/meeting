package com.zhengshang.meeting.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.ui.vo.GoodsCategoryVO;
import com.zhengshang.meeting.ui.vo.ValidTimeVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 有效时间 适配器
 * Created by sun on 2016/2/23.
 */
public class ValidTimeAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<ValidTimeVO> list;

    public ValidTimeAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<ValidTimeVO> data) {
        this.list = new ArrayList<ValidTimeVO>(data);
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
        ValidTimeVO vo = list.get(position);
        viewHolder.tvContent.setText(vo.getName());
        return convertView;
    }

    class ViewHolder {
        TextView tvContent;
    }
}
