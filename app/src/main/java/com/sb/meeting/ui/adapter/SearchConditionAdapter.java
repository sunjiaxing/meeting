package com.sb.meeting.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.common.Utils;
import com.sb.meeting.dao.entity.Area;
import com.sb.meeting.ui.vo.ClassVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询条件 适配器 （地区、班级）
 * Created by sun on 2016/4/5.
 */
public class SearchConditionAdapter extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<Area> areaList;
    private List<ClassVO> classList;
    private boolean isClass;
    private int selectAreaId;
    private int selectClassId;

    public SearchConditionAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setAreaData(List<Area> area) {
        this.areaList = new ArrayList<>(area);
        isClass = false;
    }

    public void setSelectArea(int areaId) {
        this.selectAreaId = areaId;
    }

    public void setClassData(List<ClassVO> classData) {
        this.classList = new ArrayList<>(classData);
        isClass = true;
    }

    public void setSelectClass(int classId) {
        this.selectClassId = classId;
    }

    @Override
    public int getCount() {
        return !isClass ?
                (!Utils.isEmpty(areaList) ? areaList.size() : 0) :
                (!Utils.isEmpty(classList) ? classList.size() : 0);
    }

    @Override
    public Object getItem(int position) {
        return position;
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
            convertView = layoutInflater.inflate(R.layout.layout_item_search_condition, null);
            viewHolder.tvCondition = (TextView) convertView.findViewById(R.id.tv_condition);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (!isClass) {
            Area area = areaList.get(position);
            viewHolder.tvCondition.setText(area.getName());
            if (area.getAreaId() == selectAreaId) {
                viewHolder.tvCondition.setBackgroundResource(R.drawable.price_tip_bg_shape);
                viewHolder.tvCondition.setTextColor(Color.WHITE);
            } else {
                viewHolder.tvCondition.setBackgroundResource(R.drawable.search_condition_bg_shape);
                viewHolder.tvCondition.setTextColor(context.getResources().getColor(R.color.c_454545));
            }
        } else {
            ClassVO classVO = classList.get(position);
            viewHolder.tvCondition.setText(classVO.getClassName());
            if (classVO.getClassId() == selectClassId) {
                viewHolder.tvCondition.setBackgroundResource(R.drawable.price_tip_bg_shape);
                viewHolder.tvCondition.setTextColor(Color.WHITE);
            } else {
                viewHolder.tvCondition.setBackgroundResource(R.drawable.search_condition_bg_shape);
                viewHolder.tvCondition.setTextColor(context.getResources().getColor(R.color.c_454545));
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvCondition;
    }

}
