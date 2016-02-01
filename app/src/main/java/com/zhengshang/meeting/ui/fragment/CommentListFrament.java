package com.zhengshang.meeting.ui.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.ui.activity.NewsDetailActivity;
import com.zhengshang.meeting.ui.adapter.CommentExpandableAdapter;
import com.zhengshang.meeting.ui.vo.CommentVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 评论列表 fragment
 * Created by sun on 2016/1/8.
 */
@EFragment(R.layout.layout_listview)
public class CommentListFrament extends BaseFragment implements CommentExpandableAdapter.CommentListener {
    @ViewById(R.id.listview)
    ExpandableListView listView;
    private List<CommentVO> list;
    private CommentExpandableAdapter adapter;

    @AfterViews
    void init() {
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2, long arg3) {
                return true;
            }
        });
    }

    public void setData(List<CommentVO> data) {
        if (!Utils.isEmpty(data)) {
            this.list = data;
            if (adapter == null) {
                adapter = new CommentExpandableAdapter(getActivity());
                adapter.setOnCommentListener(this);
                adapter.setData(list);
                listView.setAdapter(adapter);
            } else {
                adapter.setData(list);
                adapter.notifyDataSetChanged();
            }
            for (int i = 0; i < list.size(); i++) {
                listView.expandGroup(i);
            }
            listView.setGroupIndicator(null);
        } else {
            // 显示无数据提示

        }
    }

    /**
     * 刷新界面
     *
     * @param data 评论数据
     */
    public void refreshUI(List<CommentVO> data) {
        this.list = data;
        if (adapter != null) {
            adapter.setData(list);
            adapter.notifyDataSetChanged();
        } else {
            adapter = new CommentExpandableAdapter(getActivity());
            adapter.setData(list);
            listView.setAdapter(adapter);
        }
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
        ((NewsDetailActivity) getActivity()).clickToReply(groupPos, childPos);
    }

    @Override
    public void onLongClickReply(int groupPos, int childPos) {
        Log.e("============", "onLongClickReply: " + groupPos + "   " + childPos);
    }
}
