package com.zhengshang.meeting.ui.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 新闻专题 VO
 * Created by sun on 2016/1/20.
 */
public class NewsSubjectVO implements Serializable{
    private int id;
    private String title;
    private String banner;
    private String description;

    private List<NewsVO> newsVOList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<NewsVO> getNewsVOList() {
        if (newsVOList == null) {
            newsVOList = new ArrayList<>();
        }
        return newsVOList;
    }

    public void setNewsVOList(List<NewsVO> newsVOList) {
        this.newsVOList = newsVOList;
    }
}
