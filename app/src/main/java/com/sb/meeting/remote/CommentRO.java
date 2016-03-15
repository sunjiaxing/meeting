package com.sb.meeting.remote;

import android.content.Context;

import com.sb.meeting.common.BonConstants;
import com.sb.meeting.exeception.AppException;
import com.sb.meeting.remote.dto.CommentDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论 相关 网络请求
 * Created by sun on 2016/1/8.
 */
public class CommentRO extends BaseRO {
    public CommentRO(Context context) {
        super(context);
    }

    public enum RemoteCommentURL implements IBaseURL {
        GET_COMMENT_LIST(IParam.LIST), ADD_COMMENT(IParam.ADD_COMMENT), ADD_REPLY(IParam.ADD_REPLY);
        private static final String NAMESPACE = IParam.COMMENT;
        private String url;

        RemoteCommentURL(String mapping) {
            url = NAMESPACE + BonConstants.SLASH + mapping;
        }

        @Override
        public String getURL() {

            return url;
        }
    }

    /**
     * 获取评论列表
     *
     * @param newsId 新闻id
     * @return 评论 集合
     * @throws JSONException
     */
    public List<CommentDto> getCommentList(String newsId) throws JSONException {
        String url = getServerUrl() + RemoteCommentURL.GET_COMMENT_LIST.getURL()
                + IParam.WENHAO + IParam.NEWS_ID + IParam.EQUALS_STRING + newsId;
        String result = httpGetRequest(url, null);
        JSONObject json = new JSONObject(result);
        if (json.getInt(IParam.STATUS) == 1) {
            List<CommentDto> list = new ArrayList<>();
            JSONArray array = json.getJSONArray(IParam.LIST);
            CommentDto comment;
            for (int i = 0; i < array.length(); i++) {
                comment = new CommentDto();
                comment.parseJson(array.getJSONObject(i));
                list.add(comment);
            }
            return list;
        } else {
            throw new AppException(json.getInt(IParam.ERROR_CODE));
        }
    }

    /**
     * 发表评论
     *
     * @param newsId  新闻id
     * @param userId  用户id
     * @param content 评论内容
     * @return boolean
     * @throws JSONException
     */
    public boolean addComment(String newsId, String userId, String content) throws JSONException {
        String url = getServerUrl() + RemoteCommentURL.ADD_COMMENT.getURL();
        Map<String, Object> params = new HashMap<>();
        params.put(IParam.NEWS_ID, newsId);
        params.put(IParam.USER_ID, userId);
        params.put(IParam.CONTENT, content);
        String result = httpPostRequest(url, null, params);
        JSONObject json = new JSONObject(result);
        if (json.getInt(IParam.STATUS) == 1) {
            return true;
        } else {
            throw new AppException(json.getInt(IParam.ERROR_CODE));
        }
    }

    /**
     * 发表回复
     *
     * @param newsId   新闻id
     * @param userId   用户id
     * @param parentId 评论的id
     * @param content  回复内容
     * @return Boolean
     * @throws JSONException
     */
    public boolean addReply(String newsId, String userId, int parentId, String content) throws JSONException {
        String url = getServerUrl() + RemoteCommentURL.ADD_REPLY.getURL();
        Map<String, Object> params = new HashMap<>();
        params.put(IParam.NEWS_ID, newsId);
        params.put(IParam.USER_ID, userId);
        params.put(IParam.PARENT_ID, parentId);
        params.put(IParam.CONTENT, content);
        String result = httpPostRequest(url, null, params);
        JSONObject json = new JSONObject(result);
        if (json.getInt(IParam.STATUS) == 1) {
            return true;
        } else {
            throw new AppException(json.getInt(IParam.ERROR_CODE));
        }
    }
}
