package com.zhengshang.meeting.ui.vo;

import java.io.Serializable;

/**
 * 回复 VO
 * Created by sun on 2016/1/11.
 */
public class ReplyVO extends CommentVO implements Serializable{
    private String replyToUserId;
    private String replyToUserName;

    public String getReplyToUserId() {
        return replyToUserId;
    }

    public void setReplyToUserId(String replyToUserId) {
        this.replyToUserId = replyToUserId;
    }

    public String getReplyToUserName() {
        return replyToUserName;
    }

    public void setReplyToUserName(String replyToUserName) {
        this.replyToUserName = replyToUserName;
    }
}
