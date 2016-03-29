package com.sb.meeting.remote.dto;

import com.sb.meeting.remote.IParam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 企业dto
 * Created by sun on 2016/3/29.
 */
public class CompanyDto {

    /**
     * companyId : 1
     * companyName : 易朵云
     * logo : http://xxxxxxx.jpg
     * productDesc : 主营产品
     * catIds : 教育，互联网
     * pattern : 服务型，贸易型
     * companyType : 个体经营
     * isVIP : 0
     * area : 呼和浩特市
     */

    private int companyId;
    private String companyName;
    private String logo;
    private String productDesc;
    private String catIds;
    private String pattern;
    private String companyType;
    private int isVIP;
    private String area;

    public int getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getLogo() {
        return logo;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public String getCatIds() {
        return catIds;
    }

    public String getPattern() {
        return pattern;
    }

    public String getCompanyType() {
        return companyType;
    }

    public int getIsVIP() {
        return isVIP;
    }

    public String getArea() {
        return area;
    }

    /**
     * 解析json
     *
     * @param json
     * @throws JSONException
     */
    public void parseJson(JSONObject json) throws JSONException {
        if (json == null) {
            return;
        }
        if (json.has(IParam.COMPANY_ID)) {
            this.companyId = json.getInt(IParam.COMPANY_ID);
        }
        if (json.has(IParam.COMPANY_NAME)) {
            this.companyName = json.getString(IParam.COMPANY_NAME);
        }
        if (json.has(IParam.LOGO)) {
            this.logo = json.getString(IParam.LOGO);
        }
        if (json.has(IParam.PRODUCT_DESC)) {
            this.productDesc = json.getString(IParam.PRODUCT_DESC);
        }
        if (json.has(IParam.CAT_IDS)) {
            this.catIds = json.getString(IParam.CAT_IDS);
        }
        if (json.has(IParam.PATTERN)) {
            this.pattern = json.getString(IParam.PATTERN);
        }
        if (json.has(IParam.COMPANY_TYPE)) {
            this.companyType = json.getString(IParam.COMPANY_TYPE);
        }
        if (json.has(IParam.IS_VIP)) {
            this.isVIP = json.getInt(IParam.IS_VIP);
        }
        if (json.has(IParam.AREA)) {
            this.area = json.getString(IParam.AREA);
        }
    }
}
