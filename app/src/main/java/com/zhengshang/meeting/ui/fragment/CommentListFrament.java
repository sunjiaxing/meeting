package com.zhengshang.meeting.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.zhengshang.meeting.R;
import com.zhengshang.meeting.ui.adapter.CommentExpandableAdapter;
import com.zhengshang.meeting.ui.vo.CommentVO;

import java.util.List;

/**
 * 评论列表 fragment
 * Created by sun on 2016/1/8.
 */
public class CommentListFrament extends BaseFragment implements CommentExpandableAdapter.CommentListener {
    private ExpandableListView listView;
    private List<CommentVO> list;
    private CommentExpandableAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_listview, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ExpandableListView) view.findViewById(R.id.listview);
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2, long arg3) {
                return true;
            }
        });
    }

    public void setData(List<CommentVO> data) {
        this.list = data;
        if (adapter == null) {
            adapter = new CommentExpandableAdapter(getActivity());
            adapter.setData(list);
            listView.setAdapter(adapter);
        } else {
            adapter.setData(list);
            adapter.notifyDataSetChanged();
        }
        adapter.setOnCommentListener(this);
        for (int i = 0; i < list.size(); i++) {
            listView.expandGroup(i);
        }
        listView.setGroupIndicator(null);
    }

    @Override
    public void onClickUserAvatar(int groupPos) {
        Log.e("============", "onClickUserAvatar: " + groupPos);
    }

    @Override
    public void onLongClickComment(int groupPos) {
        Log.e("============", "onLongClickComment: " + groupPos);
    }

    @Override
    public void onClickReply(int groupPos, int childPos) {
        Log.e("============", "onClickReply: " + groupPos + "   " + childPos);
    }

    @Override
    public void onLongClickReply(int groupPos, int childPos) {
        Log.e("============", "onLongClickReply: " + groupPos + "   " + childPos);
    }
}
