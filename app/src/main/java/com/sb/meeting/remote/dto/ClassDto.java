package com.sb.meeting.remote.dto;

import com.sb.meeting.remote.IParam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 班级 dto
 * Created by sun on 2016/4/6.
 */
public class ClassDto {

    /**
     * classId : 1
     * className : 班级1
     */

    private int classId;
    private String className;

    public int getClassId() {
        return classId;
    }

    public String getClassName() {
        return className;
    }

    /**
     * 解析 json
     *
     * @param json json
     * @throws JSONException
     */
    public void parseJson(JSONObject json) throws JSONException {
        if (json == null) {
            return;
        }
        if (json.has(IParam.CLASS_ID)) {
            this.classId = json.getInt(IParam.CLASS_ID);
        }
        if (json.has(IParam.CLASS_NAME)) {
            this.className = json.getString(IParam.CLASS_NAME);
        }
    }
}
