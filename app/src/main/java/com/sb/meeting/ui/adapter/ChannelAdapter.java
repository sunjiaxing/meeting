package com.sb.meeting.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.sb.meeting.common.Utils;
import com.sb.meeting.ui.component.DragGridView;
import com.sb.meeting.ui.vo.NewsChannelVO;

import java.util.List;

/**
 * Created by sun on 2016/1/4.
 */
public class ChannelAdapter extends BaseAdapter implements DragGridView.DragGridViewAdapter {

    private LayoutInflater inflater;
    private List<NewsChannelVO> data;
    private int hidePosition = AdapterView.INVALID_POSITION;

    public ChannelAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<NewsChannelVO> list) {
        this.data = list;
    }

    @Override
    public int getCount() {
        return Utils.isEmpty(data) ? 0 : data.size();
    }

    @Override
    public NewsChannelVO getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //hide时隐藏Text
        if (position != hidePosition) {
            convertView.setVisibility(View.VISIBLE);
//            view.setText(strList.get(position));
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

    @Override
    public void swapView(int draggedPos, int destPos) {
        //从前向后拖动，其他item依次前移
        if (draggedPos < destPos) {
            data.add(destPos + 1, getItem(draggedPos));
            data.remove(draggedPos);
        }
        //从后向前拖动，其他item依次后移
        else if (draggedPos > destPos) {
            data.add(destPos, getItem(draggedPos));
            data.remove(draggedPos + 1);
        }
        hidePosition = destPos;
        notifyDataSetChanged();
    }
}
