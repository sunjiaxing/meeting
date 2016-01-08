package com.zhengshang.meeting.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zhengshang.meeting.R;
import com.zhengshang.meeting.ui.vo.CommentVO;

import java.util.List;

/**
 * 评论列表 fragment
 * Created by sun on 2016/1/8.
 */
public class CommentListFrament extends BaseFragment {
    private ListView listView;
    private List<CommentVO> list;

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
        listView = (ListView) view.findViewById(R.id.listview);
    }

    public void setData(List<CommentVO> data) {
        this.list = data;
    }
}
