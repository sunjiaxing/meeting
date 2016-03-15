package com.sb.meeting.remote.dto;

import com.sb.meeting.remote.IParam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 回复 dto 新增 回复对象
 * Created by sun on 2016/1/11.
 */
public class ReplyDto extends CommentDto {
    private String replyToUserId;
    private String replyToUserName;

    public String getReplyToUserId() {
        return replyToUserId;
    }

    public String getReplyToUserName() {
        return replyToUserName;
    }

    @Override
    public void parseJson(JSONObject json) throws JSONException {
        super.parseJson(json);
        if (json.has(IParam.REPLY_TO_USER_ID)) {
            this.replyToUserId = json.getString(IParam.REPLY_TO_USER_ID);
        }
        if (json.has(IParam.REPLY_TO_USER_NAME)) {
            this.replyToUserName = json.getString(IParam.REPLY_TO_USER_NAME);
        }
    }
}
