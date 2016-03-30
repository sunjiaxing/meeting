package com.sb.meeting.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.common.ImageOption;
import com.sb.meeting.common.Utils;
import com.sb.meeting.ui.vo.CompanyVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业列表适配器
 * Created by sun on 2016/3/30.
 */
public class CompanyListAdapter extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private List<CompanyVO> list;

    public CompanyListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<CompanyVO> data) {
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
        return list.get(position).getCompanyId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_item_company, null);
            viewHolder = new ViewHolder();
            viewHolder.ivLogo = (ImageView) convertView.findViewById(R.id.iv_logo);
            viewHolder.tvCompanyName = (TextView) convertView.findViewById(R.id.tv_company_name);
            viewHolder.tvProductDesc = (TextView) convertView.findViewById(R.id.tv_product_desc);
            viewHolder.tvCatIds = (TextView) convertView.findViewById(R.id.tv_cat_ids);
            viewHolder.tvPattern = (TextView) convertView.findViewById(R.id.tv_pattern);
            viewHolder.tvCompanyType = (TextView) convertView.findViewById(R.id.tv_company_type);
            viewHolder.tvArea = (TextView) convertView.findViewById(R.id.tv_area);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CompanyVO vo = list.get(position);
        Utils.displayImage(vo.getLogo(), viewHolder.ivLogo, ImageOption.createNomalOption());
        viewHolder.tvCompanyName.setText(vo.getCompanyName());
        viewHolder.tvProductDesc.setText("主营产品介绍：" + vo.getProductDesc());
        viewHolder.tvCatIds.setText("主营行业：" + vo.getCatIds());
        viewHolder.tvPattern.setText("经营模式：" + vo.getPattern());
        viewHolder.tvCompanyType.setText("公司类型：" + vo.getCompanyType());
        viewHolder.tvArea.setText("地址：" + vo.getArea());

        return convertView;
    }

    class ViewHolder {
        ImageView ivLogo;
        TextView tvCompanyName;
        TextView tvProductDesc;
        TextView tvCatIds;
        TextView tvPattern;
        TextView tvCompanyType;
        TextView tvArea;
    }
}
