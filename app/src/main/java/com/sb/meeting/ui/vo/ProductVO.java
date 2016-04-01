package com.sb.meeting.ui.vo;

import java.io.Serializable;

/**
 * 产品 vo
 * Created by sun on 2016/3/31.
 */
public class ProductVO implements Serializable {
    private int productId;
    private String productName;
    private String thumb;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
