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
	/** 类别id */
	private String typeId;
	/** 类别名称 */
	private String name;
	/** 是否锁定 */
	private int isLock;
	/** 栏目所在位置 */
	private int position;

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

	/**
	 * 解析json
	 * 
	 * @param json
	 * @return
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
	}
}
