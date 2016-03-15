package com.sb.meeting.ui.vo;

import java.io.Serializable;

/**
 * 物品分类 vo
 * Created by sun on 2016/2/23.
 */
public class GoodsCategoryVO implements Serializable {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
