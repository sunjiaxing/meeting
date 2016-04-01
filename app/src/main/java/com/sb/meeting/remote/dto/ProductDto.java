package com.sb.meeting.remote.dto;

import com.sb.meeting.remote.IParam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 产品 dto
 * Created by sun on 2016/3/31.
 */
public class ProductDto {

    /**
     * productId : 10
     * productName : 产品1
     * thumb : http://xxxxxxx.jpg
     */

    private int productId;
    private String productName;
    private String thumb;

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getThumb() {
        return thumb;
    }

    public void parseJson(JSONObject json) throws JSONException {
        if (json == null) {
            return;
        }
        if (json.has(IParam.PRODUCT_ID)) {
            this.productId = json.getInt(IParam.PRODUCT_ID);
        }
        if (json.has(IParam.PRODUCT_NAME)) {
            this.productName = json.getString(IParam.PRODUCT_NAME);
        }
        if (json.has(IParam.THUMB)) {
            this.thumb = json.getString(IParam.THUMB);
        }
    }
}
