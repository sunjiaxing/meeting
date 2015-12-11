package com.zhengshang.meeting.remote.dto;

import com.zhengshang.meeting.remote.IParam;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * news dto
 * 
 * @author sun
 */
public class NewsDto {

	private String id;
	private String catId;
	private String title;
	private String iconPath;
	private String summary;
	private int top;
	private int order;
	private int subject;
	private long createTime;
	private long updateTime;
	private int commentCount;
	private String iconAdUrl;
	private List<NewsDto> topNews;

	private int isRead;

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getSubject() {
		return subject;
	}

	public void setSubject(int subject) {
		this.subject = subject;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public String getIconAdUrl() {
		return iconAdUrl;
	}

	public void setIconAdUrl(String iconAdUrl) {
		this.iconAdUrl = iconAdUrl;
	}

	public List<NewsDto> getTopNews() {
		return topNews;
	}

	public void setTopNews(List<NewsDto> topNews) {
		this.topNews = topNews;
	}

	public void parseJson(JSONObject json) throws JSONException {
		if (json == null) {
			return;
		}
		if (json.has(IParam.ID)) {
			this.id = json.getString(IParam.ID);
		}
		if (json.has(IParam.ICON_URL)) {
			this.iconPath = json.getString(IParam.ICON_URL);
		}
		if (json.has(IParam.TITLE)) {
			this.title = json.getString(IParam.TITLE);
		}
		if (json.has(IParam.SUMMARY)) {
			this.summary = json.getString(IParam.SUMMARY);
		}
		if (json.has(IParam.TOP)) {
			this.top = json.getInt(IParam.TOP);
			if (top == 1) {
				// 顶部滚动新闻 图片地址重新获取
				if (json.has(IParam.IMG_URL)) {
					this.iconPath = json.getString(IParam.IMG_URL);
				}
				if (json.has(IParam.IS_AD) && json.getInt(IParam.IS_AD) == 1) {
					// 是广告 特殊处理
					if (json.has(IParam.AD_TITLE)) {
						this.title = json.getString(IParam.AD_TITLE);
					}
					if (json.has(IParam.AD_IMG_URL)) {
						this.iconPath = json.getString(IParam.AD_IMG_URL);
					}
					if (json.has(IParam.AD_URL)) {
						this.iconAdUrl = json.getString(IParam.AD_URL);
					}
				}
			}
		}
		if (json.has(IParam.SUBJECT)) {
			this.subject = json.getInt(IParam.SUBJECT);
		}
		if (json.has(IParam.CREATE_TIME)) {
			this.createTime = json.getLong(IParam.CREATE_TIME);
		}
		if (json.has(IParam.COUNT_COMMENT)) {
			this.commentCount = json.getInt(IParam.COUNT_COMMENT);
		}
		if (json.has(IParam.ICON_AD_URL)) {
			this.iconAdUrl = json.getString(IParam.ICON_AD_URL);
		}
		if (json.has(IParam.UPDATE_TIME)) {
			this.updateTime = json.getLong(IParam.UPDATE_TIME);
		}
	}

}
