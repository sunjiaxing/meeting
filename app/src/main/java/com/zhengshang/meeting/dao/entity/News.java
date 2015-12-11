package com.zhengshang.meeting.dao.entity;

import java.util.List;

/**
 * 新闻实体类
 * 
 * @author sun
 * 
 */
public class News {

	public static final String KEY_COLUMN_TABLE_ID = "table_id";
	public static final String KEY_COLUMN_ID = "id";
	public static final String KEY_COLUMN_TITLE = "title";
	public static final String KEY_COLUMN_ICON_PATH = "iconPath";
	public static final String KEY_COLUMN_SUMMARY = "summary";
	public static final String KEY_COLUMN_CAT_ID = "catId";
	public static final String KEY_COLUMN_TOP = "top";
	public static final String KEY_COLUMN_NEWS_ORDER = "newsOrder";
	public static final String KEY_COLUMN_CREATE_TIME = "createTime";
	public static final String KEY_COLUMN_IS_READ = "isRead";
	public static final String KEY_COLUMN_SUBJECT = "subject";
	public static final String KEY_COLUMN_COMMENT_COUNT = "commentCount";
	public static final String KEY_COLUMN_ICON_AD_URL = "iconAdUrl";
	public static final String KEY_COLUMN_REMARK = "remark";

	public static final String KEY_TABLE_NAME = "onlinenews";

	public static final String CREAT_TABLE = "CREATE TABLE " + KEY_TABLE_NAME
			+ " (" + KEY_COLUMN_TABLE_ID + " INTEGER PRIMARY KEY,"
			+ KEY_COLUMN_ID + " TEXT," + KEY_COLUMN_TITLE + " TEXT,"
			+ KEY_COLUMN_ICON_PATH + " TEXT," + KEY_COLUMN_SUMMARY + " TEXT,"
			+ KEY_COLUMN_CAT_ID + " TEXT," + KEY_COLUMN_TOP
			+ " INTEGER DEFAULT 0," + KEY_COLUMN_NEWS_ORDER + " INTEGER,"
			+ KEY_COLUMN_CREATE_TIME + " LONG," + KEY_COLUMN_IS_READ
			+ " INTEGER DEFAULT 0," + KEY_COLUMN_SUBJECT
			+ " INTEGER DEFAULT 0 ," + KEY_COLUMN_COMMENT_COUNT
			+ " INTEGER DEFAULT 0," + KEY_COLUMN_ICON_AD_URL + " TEXT,"
			+ KEY_COLUMN_REMARK + " TEXT " + ")";

	public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
			+ KEY_TABLE_NAME;
	public static final String DELETE_TABLE_DATA = "DELETE FROM "
			+ KEY_TABLE_NAME;

	private String newsId;
	private String title;
	private String iconPath;
	private String summary;
	private int top;
	private int order;
	private int isRead;// 新闻阅读状态
	private List<News> topNews;// 头条列表
	/** 是否是专题 */
	private int subject;
	/** 新闻创建时间 */
	private long createTime;
	/** 评论条数 */
	private int commentCount = 0;
	/** 分类Id */
	private String catId;
	private String iconAdUrl;
	private long updateTime;

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
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

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public List<News> getTopNews() {
		return topNews;
	}

	public void setTopNews(List<News> topNews) {
		this.topNews = topNews;
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

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getIconAdUrl() {
		return iconAdUrl;
	}

	public void setIconAdUrl(String iconAdUrl) {
		this.iconAdUrl = iconAdUrl;
	}

}
