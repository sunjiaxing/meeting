package com.zhengshang.meeting.ui.vo;

import com.zhengshang.meeting.remote.IParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 图片 vo
 * Created by sun on 2016/2/22.
 */
public class ImageVO implements Serializable {
    private String filePath;
    private String url;
    private String desc;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void parseJson(JSONObject json) throws JSONException {
        if (json == null) {
            return;
        }
        if (json.has(IParam.URL)) {
            setUrl(json.getString(IParam.URL));
        }
        if (json.has(IParam.DESC)) {
            setDesc(json.getString(IParam.DESC));
        }
    }
}
