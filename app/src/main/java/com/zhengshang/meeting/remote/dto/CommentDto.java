package com.zhengshang.meeting.remote.dto;

import com.zhengshang.meeting.remote.IParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论 dto
 * Created by sun on 2016/1/8.
 */
public class CommentDto {

    private int id;
    private String userId;
    private String userName;
    private long createTime;
    private String content;
    private int parentId;
    private int groupId;
    private List<ReplyDto> replies;

    public List<ReplyDto> getReplies() {
        return replies;
    }

    public int getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public String getContent() {
        return content;
    }

    public int getParentId() {
        return parentId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void parseJson(JSONObject json) throws JSONException {
        if (json == null) {
            return;
        }
        if (json.has(IParam.ID)) {
            this.id = json.getInt(IParam.ID);
        }
        if (json.has(IParam.USER_ID)) {
            this.userId = json.getString(IParam.USER_ID);
        }
        if (json.has(IParam.USER_NAME)) {
            this.userName = json.getString(IParam.USER_NAME);
        }
        if (json.has(IParam.CREATE_TIME)) {
            this.createTime = json.getLong(IParam.CREATE_TIME);
        }
        if (json.has(IParam.CONTENT)) {
            this.content = json.getString(IParam.CONTENT);
        }
        if (json.has(IParam.PARENT_ID)) {
            this.parentId = json.getInt(IParam.PARENT_ID);
        }
        if (json.has(IParam.GROUP_ID)) {
            this.groupId = json.getInt(IParam.GROUP_ID);
        }
        if (json.has(IParam.REPLIES)) {
            JSONArray array = json.getJSONArray(IParam.REPLIES);
            replies = new ArrayList<>();
            ReplyDto replyDto;
            for (int i = 0; i < array.length(); i++) {
                replyDto = new ReplyDto();
                replyDto.parseJson(array.getJSONObject(i));
                replies.add(replyDto);
            }
        }
    }
}
