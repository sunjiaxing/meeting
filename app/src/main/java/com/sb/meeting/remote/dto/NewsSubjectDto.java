package com.sb.meeting.remote.dto;

import com.sb.meeting.remote.IParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻专题 dto
 * Created by sun on 2016/1/20.
 */
public class NewsSubjectDto {
    private int id;
    private String title;
    private String banner;
    private String description;
    private long createTime;
    private List<NewsDto> newsDtoList = new ArrayList<>();

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBanner() {
        return banner;
    }

    public String getDescription() {
        return description;
    }

    public long getCreateTime() {
        return createTime;
    }

    public List<NewsDto> getNewsDtoList() {
        return newsDtoList;
    }

    /**
     * 解析 json
     * @param json
     * @throws JSONException
     */
    public void parseJson(JSONObject json) throws JSONException {
        if (json == null) {
            return;
        }
        if (json.has(IParam.SPECIAL_ID)) {
            this.id = json.getInt(IParam.SPECIAL_ID);
        }
        if (json.has(IParam.TITLE)) {
            this.title = json.getString(IParam.TITLE);
        }
        if (json.has(IParam.DESCRIPTION)) {
            this.description = json.getString(IParam.DESCRIPTION);
        }
        if (json.has(IParam.IMG_URL)) {
            this.banner = json.getString(IParam.IMG_URL);
        }
        if (json.has(IParam.CREATE_TIME)) {
            this.createTime = json.getLong(IParam.CREATE_TIME);
        }
        if (json.has(IParam.CONTAINS)) {
            JSONArray array = json.getJSONArray(IParam.CONTAINS);
            for (int i = 0; i < array.length(); i++) {
                NewsDto dto = new NewsDto();
                dto.parseJson(array.getJSONObject(i));
                newsDtoList.add(dto);
            }
        }
    }
}
