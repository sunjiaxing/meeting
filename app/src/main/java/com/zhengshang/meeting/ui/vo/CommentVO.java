package com.zhengshang.meeting.ui.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 评论 VO
 * Created by sun on 2016/1/8.
 */
public class CommentVO implements Serializable{
    private int id;
    private String userId;
    private String userName;
    private String userAvatar;
    private String createTime;
    private String content;
    private List<ReplyVO> replies;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ReplyVO> getReplies() {
        if (replies == null) {
            replies = new ArrayList<>();
        }
        return replies;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }
}
