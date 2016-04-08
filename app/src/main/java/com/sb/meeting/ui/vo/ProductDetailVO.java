package com.sb.meeting.ui.vo;

import java.io.Serializable;

/**
 * 产品详情 VO
 * Created by sun on 2016/4/1.
 */
public class ProductDetailVO extends ProductVO implements Serializable {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
