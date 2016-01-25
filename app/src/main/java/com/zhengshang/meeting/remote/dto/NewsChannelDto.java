package com.zhengshang.meeting.remote.dto;

import com.zhengshang.meeting.remote.IParam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * news channel dto
 *
 * @author sun
 */
public class NewsChannelDto {
    /**
     * 类别id
     */
    private String typeId;
    /**
     * 类别名称
     */
    private String name;
    /**
     * 是否锁定
     */
    private int isLock;
    /**
     * 栏目所在位置
     */
    private int position;

    private String childId;
    private String modelName;

    public String getTypeId() {
        return typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsLock() {
        return isLock;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getChildId() {
        return childId;
    }

    public String getModelName() {
        return modelName;
    }

    /**
     * 解析json
     *
     * @param json
     * @throws JSONException
     */
    public void parseJson(JSONObject json) throws JSONException {
        if (json.has(IParam.TYPE_ID)) {
            this.typeId = json.getString(IParam.TYPE_ID);
        }
        if (json.has(IParam.NAME)) {
            this.name = json.getString(IParam.NAME);
        }
        if (json.has(IParam.LOCK)) {
            this.isLock = json.getInt(IParam.LOCK);
        }
        if (json.has(IParam.POSITION)) {
            this.position = json.getInt(IParam.POSITION);
        }
        if (json.has(IParam.CHILD_ID)) {
            this.childId = json.getString(IParam.CHILD_ID);
        }
        if (json.has(IParam.MODEL_NAME)) {
            this.modelName = json.getString(IParam.MODEL_NAME);
        }
    }
}
