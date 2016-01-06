package com.zhengshang.meeting.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhengshang.meeting.R;
import com.zhengshang.meeting.ui.component.DragGridView;
import com.zhengshang.meeting.ui.vo.NewsChannelVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的栏目适配器
 */
public abstract class DragChannelAdapter extends QuickFlingAdapter implements DragGridView.DragGridViewAdapter {

    protected List<NewsChannelVO> myChannelList = null;
    protected int hidePosition = AdapterView.INVALID_POSITION;

    public DragChannelAdapter(Context context) {
        super(context);
    }

    public void setData(List<NewsChannelVO> list) {
        this.myChannelList = new ArrayList<>(list);
    }

    @Override
    public int getCount() {

        return myChannelList != null ? myChannelList.size() : 0;
    }

    @Override
    public NewsChannelVO getItem(int position) {

        return myChannelList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.news_type_gridview_item,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.tvChannelName = (TextView) convertView
                    .findViewById(R.id.tv_name);
            viewHolder.ivHandleLogo = (ImageView) convertView
                    .findViewById(R.id.iv_logo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 获取数据
        NewsChannelVO newsType = myChannelList.get(position);
        viewHolder.tvChannelName.setText(newsType.getName());
        if (newsType.getIsLock() == 1) {
            // 锁定状态不显示图标
            viewHolder.ivHandleLogo.setVisibility(View.GONE);
        } else {
            // 未锁定--显示
            viewHolder.ivHandleLogo.setVisibility(View.VISIBLE);
            viewHolder.ivHandleLogo
                    .setImageResource(R.mipmap.news_type_delete);
        }
        //hide时隐藏Text
        if (position != hidePosition) {
            convertView.setVisibility(View.VISIBLE);
        } else {
            convertView.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    @Override
    public void hideView(int pos) {
        hidePosition = pos;
        notifyDataSetChanged();
    }

    @Override
    public void showHideView() {
        hidePosition = AdapterView.INVALID_POSITION;
        notifyDataSetChanged();
    }

//    @Override
//    public void swapView(int draggedPos, int destPos) {
//        //从前向后拖动，其他item依次前移
//        if (draggedPos < destPos) {
//            myChannelList.add(destPos + 1, getItem(draggedPos));
//            myChannelList.remove(draggedPos);
//        }
//        //从后向前拖动，其他item依次后移
//        else if (draggedPos > destPos) {
//            myChannelList.add(destPos, getItem(draggedPos));
//            myChannelList.remove(draggedPos + 1);
//        }
//        hidePosition = destPos;
//        notifyDataSetChanged();
//    }

    public class ViewHolder {
        TextView tvChannelName;
        ImageView ivHandleLogo;
    }

}
