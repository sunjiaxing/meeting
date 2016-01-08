package com.zhengshang.meeting.remote;

import android.content.Context;

import com.zhengshang.meeting.common.BonConstants;
import com.zhengshang.meeting.exeception.AppException;
import com.zhengshang.meeting.remote.dto.CommentDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论 相关 网络请求
 * Created by sun on 2016/1/8.
 */
public class CommentRO extends BaseRO {
    public CommentRO(Context context) {
        super(context);
    }

    public enum RemoteCommentURL implements IBaseURL {
        GET_COMMENT_LIST(IParam.LIST);
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
     * @param newsId
     * @param catId
     * @return
     * @throws JSONException
     */
    public List<CommentDto> getCommentList(String newsId, String catId) throws JSONException {
        String url = getServerUrl() + RemoteCommentURL.GET_COMMENT_LIST.getURL()
                + IParam.WENHAO + IParam.NEWS_ID + IParam.EQUALS_STRING + newsId
                + IParam.AND + IParam.CAT_ID + IParam.EQUALS_STRING + catId;
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
}
