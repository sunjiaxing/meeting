package com.zhengshang.meeting.remote.dto;

import com.zhengshang.meeting.remote.IParam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 物品详情 dto
 * Created by sun on 2016/3/1.
 */
public class GoodsDetailDto extends GoodsDto {
    private String validTime;
    private String needCategory;
    private String imgJson;

    public String getValidTime() {
        return validTime;
    }

    public String getNeedCategory() {
        return needCategory;
    }

    public String getImgJson() {
        return imgJson;
    }

    public void parseJson(JSONObject json) throws JSONException {
        if (json == null) {
            return;
        }
        super.parseJson(json);
        if (json.has(IParam.VALID_TIME)) {
            this.validTime = json.getString(IParam.VALID_TIME);
        }
        if (json.has(IParam.NEED_CATEGORY)) {
            this.needCategory = json.getString(IParam.NEED_CATEGORY);
        }
        if (json.has(IParam.IMAGES)) {
            this.imgJson = json.getString(IParam.IMAGES);
        }
    }
}
