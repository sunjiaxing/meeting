package com.sb.meeting.remote.dto;

import com.sb.meeting.remote.IParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业详情 dto
 * Created by sun on 2016/3/31.
 */
public class CompanyDetailDto extends CompanyDto {

    private String companyIntroduce;
    private String webUrl;
    private String contact;
    private String phone;
    private String email;
    private String qq;
    private String companyAddress;
    private String companyMap;

    private List<String> imageList;
    private List<CertificateDto> certificateList;
    private List<ProductDto> productList;

    public int getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public int getIsVIP() {
        return isVIP;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public String getCompanyIntroduce() {
        return companyIntroduce;
    }

    public String getLogo() {
        return logo;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getContact() {
        return contact;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getQQ() {
        return qq;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public String getCompanyMap() {
        return companyMap;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public List<CertificateDto> getCertificateList() {
        return certificateList;
    }

    public List<ProductDto> getProductList() {
        return productList;
    }

    public void parseJson(JSONObject json) throws JSONException {
        super.parseJson(json);
        if (json == null) {
            return;
        }
        if (json.has(IParam.COMPANY_INTRODUCE)) {
            this.companyIntroduce = json.getString(IParam.COMPANY_INTRODUCE);
        }
        if (json.has(IParam.WEB_URL)){
            this.webUrl = json.getString(IParam.WEB_URL);
        }
        if (json.has(IParam.CONTACT)){
            this.contact = json.getString(IParam.CONTACT);
        }
        if (json.has(IParam.PHONE)){
            this.phone = json.getString(IParam.PHONE);
        }
        if (json.has(IParam.QQ)){
            this.qq = json.getString(IParam.QQ);
        }
        if (json.has(IParam.EMAIL)){
            this.email = json.getString(IParam.EMAIL);
        }
        if (json.has(IParam.COMPANY_ADDRESS)){
            this.companyAddress = json.getString(IParam.COMPANY_ADDRESS);
        }
        if (json.has(IParam.COMPANY_MAP)){
            this.companyMap = json.getString(IParam.COMPANY_MAP);
        }
        if (json.has(IParam.CERTIFICATE)){
            JSONArray array = json.getJSONArray(IParam.CERTIFICATE);
            this.certificateList = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                CertificateDto dto = new CertificateDto();
                dto.parseJson(array.getJSONObject(i));
                certificateList.add(dto);
            }
        }
        if (json.has(IParam.PRODUCT_LIST)){
            JSONArray array = json.getJSONArray(IParam.PRODUCT_LIST);
            this.productList = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                ProductDto dto = new ProductDto();
                dto.parseJson(array.getJSONObject(i));
                productList.add(dto);
            }
        }




    }
}
