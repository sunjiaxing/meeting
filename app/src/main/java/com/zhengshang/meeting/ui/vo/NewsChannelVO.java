package com.zhengshang.meeting.ui.vo;

import java.io.Serializable;

/**
 * 新闻栏目VO
 * 
 * @author sun
 * 
 */
public class NewsChannelVO implements Serializable{
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

	public void setTypeId(String typeId) {
		this.typeId = typeId;
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

	public void setIsLock(int isLock) {
		this.isLock = isLock;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public NewsChannelVO(String typeId, String name, int isLock, int position) {
		super();
		this.typeId = typeId;
		this.name = name;
		this.isLock = isLock;
		this.position = position;
	}

	public NewsChannelVO() {
		super();
	}
}
