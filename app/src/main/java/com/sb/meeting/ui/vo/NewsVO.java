package com.sb.meeting.ui.vo;

import java.io.Serializable;
import java.util.List;

/**
 * news VO
 */
public class NewsVO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String id;
    private String iconPath;
    private String title;
    private boolean isRead;
    private boolean subject;
    private int subjectId;
    private String summary;
    private List<NewsVO> topNews;// 头条列表
    private boolean isOpenBlank;// 是否外部浏览器打开
    private String iconAdUrl;
    private String catId;
    private long createTime;
    private boolean isTop;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean isSubject() {
        return subject;
    }

    public void setSubject(boolean subject) {
        this.subject = subject;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<NewsVO> getTopNews() {
        return topNews;
    }

    public void setTopNews(List<NewsVO> topNews) {
        this.topNews = topNews;
    }


    public String getIconAdUrl() {
        return iconAdUrl;
    }

    public void setIconAdUrl(String iconAdUrl) {
        this.iconAdUrl = iconAdUrl;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean isTop) {
        this.isTop = isTop;
    }

    public boolean isOpenBlank() {
        return isOpenBlank;
    }

    public void setIsOpenBlank(boolean isOpenBlank) {
        this.isOpenBlank = isOpenBlank;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
}
