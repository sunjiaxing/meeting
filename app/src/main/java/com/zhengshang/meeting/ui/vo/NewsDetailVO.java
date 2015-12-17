package com.zhengshang.meeting.ui.vo;

import java.io.Serializable;

/**
 * newsDetail VO
 */
public class NewsDetailVO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String shortUrl;// 用于分享
    private String longUrl;// 用于分享
    private String summary;// 用于分享
    private String iconUrl;// 用于分享

    // 主体内容
    private String id;
    private String title;
    private String cFrom;
    private String cTime;
    private String content;
    private String contentUrl;

    // 广告相关内容
    private String adId;
    private String adUrl;
    private String adTitle;
    private String adIconUrl;


    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getcFrom() {
        return cFrom;
    }

    public void setcFrom(String cFrom) {
        this.cFrom = cFrom;
    }

    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAdIconUrl() {
        return adIconUrl;
    }

    public void setAdIconUrl(String adIconUrl) {
        this.adIconUrl = adIconUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
}
