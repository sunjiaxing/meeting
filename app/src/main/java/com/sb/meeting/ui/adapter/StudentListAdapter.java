package com.sb.meeting.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.common.ImageOption;
import com.sb.meeting.common.Utils;
import com.sb.meeting.ui.component.CircleImageView;
import com.sb.meeting.ui.vo.StudentVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 学员列表适配器
 * Created by sun on 2016/3/31.
 */
public abstract class StudentListAdapter extends BaseAdapter implements View.OnClickListener{

    private final LayoutInflater layoutInflater;
    private List<StudentVO> list;

    public StudentListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<StudentVO> data) {
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
        return list.get(position).getStudentId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.layout_item_student, null);
            viewHolder.tvCompanyName = (TextView) convertView.findViewById(R.id.tv_company_name);
            viewHolder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.iv_user_avatar);
            viewHolder.tvStudentName = (TextView) convertView.findViewById(R.id.tv_student_name);
            viewHolder.tvPosition = (TextView) convertView.findViewById(R.id.tv_position);
            viewHolder.tvPhone = (TextView) convertView.findViewById(R.id.tv_phone);
            viewHolder.tvClassName = (TextView) convertView.findViewById(R.id.tv_class_name);
            viewHolder.tvClassPosition = (TextView) convertView.findViewById(R.id.tv_class_position);
            viewHolder.tvArea = (TextView) convertView.findViewById(R.id.tv_area);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        StudentVO vo = list.get(position);
        viewHolder.tvCompanyName.setText(vo.getCompanyName());
        viewHolder.tvCompanyName.setTag(position);
        viewHolder.tvCompanyName.setOnClickListener(this);
        Utils.displayImage(vo.getAvatarUrl(), viewHolder.ivAvatar, ImageOption.createNomalOption());
        viewHolder.tvStudentName.setText(vo.getStudentName());
        viewHolder.tvPosition.setText(vo.getPosition());
        viewHolder.tvPhone.setText("电话 " + vo.getPhone());
        viewHolder.tvClassName.setText(vo.getClassName());
        viewHolder.tvClassPosition.setText(vo.getClassPosition());
        viewHolder.tvArea.setText("地区 " + vo.getArea());
        return convertView;
    }

    class ViewHolder {
        TextView tvCompanyName;
        CircleImageView ivAvatar;
        TextView tvStudentName;
        TextView tvPosition;
        TextView tvPhone;
        TextView tvClassName;
        TextView tvClassPosition;
        TextView tvArea;
    }
}
