package com.sb.meeting.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.common.BonConstants;
import com.sb.meeting.common.ImageOption;
import com.sb.meeting.common.Utils;
import com.sb.meeting.dao.ConfigDao;
import com.sb.meeting.ui.vo.CommentVO;
import com.sb.meeting.ui.vo.ReplyVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论列表适配器
 * TODO getView中 待优化
 */
public class CommentExpandableAdapter extends BaseExpandableListAdapter {
    private LayoutInflater inflater;
    private List<CommentVO> commentList;
    private CommentListener listener;
    private String userId;

    public CommentExpandableAdapter(Context context) {
        super();
        inflater = LayoutInflater.from(context);
        userId = ConfigDao.getInstance(context).getUserId();
    }

    public void setData(List<CommentVO> parentList) {
        this.commentList = new ArrayList<>(parentList);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return commentList.get(groupPosition).getReplies()
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean arg2, View arg3, ViewGroup arg4) {
        ReplyVO replyVO = commentList.get(groupPosition)
                .getReplies().get(childPosition);

        View childView = inflater.inflate(R.layout.layout_comment_reply, null);
        TextView text = (TextView) childView
                .findViewById(R.id.tv_child_comment_content);
        text.setTag(new int[]{groupPosition, childPosition});
        text.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    int[] data = (int[]) v.getTag();
                    listener.onLongClickReply(data[0], data[1]);
                }
                return true;
            }
        });
        text.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int[] data = (int[]) v.getTag();
                    listener.onClickReply(data[0], data[1]);
                }
            }
        });
        String content = "<font color=#5d7eab>" + replyVO.getUserName()
                + "</font>回复<font color=#5d7eab>"
                + replyVO.getReplyToUserName() + "</font>:"
                + replyVO.getContent();
        text.setText(Html.fromHtml(content));
        return childView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return commentList.get(groupPosition).getReplies().size();
    }

    @Override
    public Object getGroup(int pos) {
        return commentList.get(pos);
    }

    @Override
    public int getGroupCount() {
        return commentList.size();
    }

    @Override
    public long getGroupId(int pos) {
        return pos;
    }

    @Override
    public View getGroupView(int position, boolean arg1, View convertView,
                             ViewGroup arg3) {
        CommentVO commentVO = commentList.get(position);
        View view = inflater.inflate(R.layout.layout_comment_root, null);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_user_avatar);
        TextView tvContent = (TextView) view
                .findViewById(R.id.tv_comment_content);
        TextView tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
        TextView tvTime = (TextView) view.findViewById(R.id.tv_time);

        tvContent.setTag(new int[]{position, -1});
        iv.setTag(position);
        iv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickUserAvatar((Integer) v.getTag());
                }
            }
        });
        tvContent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int[] data = (int[]) v.getTag();
                    CommentVO vo = commentList.get(data[0]);
                    if (!vo.getUserId().equals(userId)) {
                        listener.onClickReply(data[0], data[1]);
                    }
                }
            }
        });
        tvContent.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    int[] data = (int[]) v.getTag();
                    listener.onLongClickComment(data[0]);
                }
                return true;
            }
        });
        Utils.displayImage(commentVO.getUserAvatar(), iv, ImageOption.createNomalOption());
        tvUserName.setText(commentVO.getUserName());
        String content = commentVO.getContent();
        // 判断评论 长度
        if (content != null && content.length() > BonConstants.LIMIT_COMMENT_LENGTH) {
            content = content.substring(0, BonConstants.LIMIT_COMMENT_LENGTH);
        }
        tvContent.setText(content);
        tvTime.setText(commentVO.getCreateTime());
        return view;
    }

    public void setOnCommentListener(CommentListener commentListener) {
        this.listener = commentListener;
    }

    public interface CommentListener {
        void onClickUserAvatar(int groupPos);

        void onLongClickComment(int groupPos);

        void onClickReply(int groupPos, int childPos);

        void onLongClickReply(int groupPos, int childPos);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

}
