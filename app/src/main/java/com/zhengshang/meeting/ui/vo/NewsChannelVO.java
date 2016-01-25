package com.zhengshang.meeting.ui.vo;

import java.io.Serializable;

/**
 * 新闻栏目VO
 *
 * @author sun
 */
public class NewsChannelVO implements Serializable {
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
    private boolean isLock;
    /**
     * 栏目所在位置
     */
    private int position;

    private String childId;

    private String modelName;

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

    public boolean isLock() {
        return isLock;
    }

    public void setIsLock(boolean isLock) {
        this.isLock = isLock;
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

    public NewsChannelVO(String typeId, String name, boolean lock, int position) {
        this.typeId = typeId;
        this.name = name;
        this.isLock = lock;
        this.position = position;
    }

    public NewsChannelVO(String typeId, String name, boolean isLock, int position, String childId, String modelName) {
        this.typeId = typeId;
        this.name = name;
        this.isLock = isLock;
        this.position = position;
        this.childId = childId;
        this.modelName = modelName;
    }
}
