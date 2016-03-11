package com.zhengshang.meeting.ui.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品 vo
 * Created by sun on 2016/2/19.
 */
public class GoodsVO implements Serializable{
    private int id;
    private String name;
    private GoodsCategoryVO category;
    private GoodsCategoryVO needCategory;
    private double marketPrice;
    private double exchangePrice;
    private String coverUrl;
    private int scanNum;
    private int attentionNum;
    private ValidTimeVO validTime;
    private String publishTime;
    private boolean isAttention;
    private List<ImageVO> imageList;

    private String validTimeStr;
    private String needCategoryStr;
    private int count;

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

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public double getExchangePrice() {
        return exchangePrice;
    }

    public void setExchangePrice(double exchangePrice) {
        this.exchangePrice = exchangePrice;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getScanNum() {
        return scanNum;
    }

    public void setScanNum(int scanNum) {
        this.scanNum = scanNum;
    }

    public int getAttentionNum() {
        return attentionNum;
    }

    public void setAttentionNum(int attentionNum) {
        this.attentionNum = attentionNum;
    }

    public GoodsCategoryVO getCategory() {
        return category;
    }

    public void setCategory(GoodsCategoryVO category) {
        this.category = category;
    }

    public ValidTimeVO getValidTime() {
        return validTime;
    }

    public void setValidTime(ValidTimeVO validTime) {
        this.validTime = validTime;
    }

    public List<ImageVO> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageVO> imageList) {
        this.imageList = imageList;
    }

    public void appendImageList(List<ImageVO> imageList) {
        if (this.imageList == null) {
            this.imageList = new ArrayList<>();
        }
        this.imageList.addAll(imageList);
    }

    public GoodsCategoryVO getNeedCategory() {
        return needCategory;
    }

    public void setNeedCategory(GoodsCategoryVO needCategory) {
        this.needCategory = needCategory;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public boolean isAttention() {
        return isAttention;
    }

    public void setIsAttention(boolean isAttention) {
        this.isAttention = isAttention;
    }

    public String getValidTimeStr() {
        return validTimeStr;
    }

    public void setValidTimeStr(String validTimeStr) {
        this.validTimeStr = validTimeStr;
    }

    public String getNeedCategoryStr() {
        return needCategoryStr;
    }

    public void setNeedCategoryStr(String needCategoryStr) {
        this.needCategoryStr = needCategoryStr;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
