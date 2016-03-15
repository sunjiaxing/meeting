package com.sb.meeting.remote.dto;

import com.sb.meeting.remote.IParam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * name dto
 * Created by sun on 2016/2/23.
 */
public class NameAndValueDto {
    private String name;
    private int value;

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public void parseJson(JSONObject json) throws JSONException {
        if (json == null) {
            return;
        }
        if (json.has(IParam.NAME)) {
            this.name = json.getString(IParam.NAME);
        }
        if (json.has(IParam.VALUE)) {
            this.value = json.getInt(IParam.VALUE);
        }
    }
}
