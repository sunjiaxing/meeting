package com.sb.meeting.remote.dto;

import com.sb.meeting.remote.IParam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 产品 详情 dto
 * Created by sun on 2016/4/1.
 */
public class ProductDetailDto extends ProductDto {

    private String content;

    public String getContent() {
        return content;
    }

    public void parseJson(JSONObject json) throws JSONException {
        super.parseJson(json);
        if (json.has(IParam.DESCRIPTION)) {
            this.content = json.getString(IParam.DESCRIPTION);
        }
    }
}
