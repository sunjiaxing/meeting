package com.sb.meeting.remote.dto;

import com.sb.meeting.remote.IParam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 服务器端返回的配置信息
 * Created by sun on 2016/4/5.
 */
public class ConfigDto {
    private String qiniuToken;

    public String getQiniuToken() {
        return qiniuToken;
    }

    /**
     * 解析json
     *
     * @param json json
     * @throws JSONException
     */
    public void parseJson(JSONObject json) throws JSONException {
        if (json == null) {
            return;
        }
        if (json.has(IParam.QINIU_TOKEN)) {
            this.qiniuToken = json.getString(IParam.QINIU_TOKEN);
        }
    }
}
