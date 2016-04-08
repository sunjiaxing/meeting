package com.sb.meeting.ui.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 企业详情 vo
 * Created by sun on 2016/3/31.
 */
public class CompanyDetailVO extends CompanyVO implements Serializable {
    private String companyIntroduce;
    private String webUrl;
    private String contact;
    private String phone;
    private String email;
    private String qq;
    private String companyAddress;
    private double longitude;
    private double latitude;

    private List<ImageVO> imageList;
    private List<CertificateVO> certificateList;
    private List<ProductVO> productList;

    public String getCompanyIntroduce() {
        return companyIntroduce;
    }

    public void setCompanyIntroduce(String companyIntroduce) {
        this.companyIntroduce = companyIntroduce;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQQ() {
        return qq;
    }

    public void setQQ(String qq) {
        this.qq = qq;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public List<ImageVO> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageVO> imageList) {
        this.imageList = imageList;
    }

    public List<CertificateVO> getCertificateList() {
        return certificateList;
    }

    public void setCertificateList(List<CertificateVO> certificateList) {
        this.certificateList = certificateList;
    }

    public List<ProductVO> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductVO> productList) {
        this.productList = productList;
    }
}
