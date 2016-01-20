package com.zhengshang.meeting.remote.dto;

import com.zhengshang.meeting.remote.IParam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 新闻列表 dto
 * Created by sun on 2015/12/14.
 */
public class NewsDto {
    private int isAd;
    private String id;
    private String title;
    private String imgUrl;
    private String iconUrl;
    private int top;
    private String summary;
    private long createTime;
    private long updateTime;
    private String adTitle;
    private String adUrl;
    private String adImgUrl;
    private int isOpenBlank;

    private int isSpecial;
    private int specialId;

    public int getIsAd() {
        return isAd;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public int getTop() {
        return top;
    }

    public String getSummary() {
        return summary;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public String getAdImgUrl() {
        return adImgUrl;
    }

    public int getIsOpenBlank() {
        return isOpenBlank;
    }

    public int getIsSpecial() {
        return isSpecial;
    }

    public int getSpecialId() {
        return specialId;
    }

    /**
     * 解析json
     *
     * @param json
     * @throws JSONException
     */
    public void parseJson(JSONObject json) throws JSONException {
        if (json.has(IParam.IS_AD) && !json.isNull(IParam.IS_AD)) {
            this.isAd = json.getInt(IParam.IS_AD);
        }
        if (json.has(IParam.ID)) {
            this.id = json.getString(IParam.ID);
        }
        if (json.has(IParam.TITLE)) {
            this.title = json.getString(IParam.TITLE);
        }
        if (json.has(IParam.IMG_URL)) {
            this.imgUrl = json.getString(IParam.IMG_URL);
        }
        if (json.has(IParam.ICON_URL)) {
            this.iconUrl = json.getString(IParam.ICON_URL);
        }
        if (json.has(IParam.TOP) && !json.isNull(IParam.TOP)) {
            this.top = json.getInt(IParam.TOP);
        }
        if (json.has(IParam.SUMMARY)) {
            this.summary = json.getString(IParam.SUMMARY);
        }
        if (json.has(IParam.CREATE_TIME)) {
            this.createTime = json.getLong(IParam.CREATE_TIME);
        }
        if (json.has(IParam.UPDATE_TIME)) {
            this.updateTime = json.getInt(IParam.UPDATE_TIME);
        }
        if (json.has(IParam.AD_TITLE)) {
            this.adTitle = json.getString(IParam.AD_TITLE);
        }
        if (json.has(IParam.AD_URL)) {
            this.adUrl = json.getString(IParam.AD_URL);
        }
        if (json.has(IParam.AD_IMG_URL)) {
            this.adImgUrl = json.getString(IParam.AD_IMG_URL);
        }
        if (json.has(IParam.IS_OPEN_BLANK) && !json.isNull(IParam.IS_OPEN_BLANK)) {
            this.isOpenBlank = json.getInt(IParam.IS_OPEN_BLANK);
        }
        if (json.has(IParam.IS_SPECIAL) && !json.isNull(IParam.IS_SPECIAL)) {
            this.isSpecial = json.getInt(IParam.IS_SPECIAL);
        }
        if (json.has(IParam.SPECIAL_ID) && !json.isNull(IParam.SPECIAL_ID)) {
            this.specialId = json.getInt(IParam.SPECIAL_ID);
        }
    }
}
