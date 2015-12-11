package com.zhengshang.meeting.remote.dto;

import com.zhengshang.meeting.remote.IParam;

import java.util.Iterator;
import java.util.LinkedHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author sun
 * 
 */
public class NewsDetailDto {
	private String id;
	private String forwardId;
	private String title;// 标题
	private String summary;// 简介
	private String content;
	private String subTitle;
	private String imgUrl;
	private long cTime;
	private String cFrom;
	private String wapUrl;
	private String shortUrl;
	/** 推广信息Id */
	private String adId;
	/** 推广信息的显示内容 */
	private String adTitle;
	/** 推广信息的url */
	private String adUrl;
	/** 推广信息icon图标url */
	private String adIconUrl;

	/** 本条新闻能否评论 1为true */
	private int canComment;

	private String rootId;
	private int commentNum;
	private LinkedHashMap<String, String> imgs;
	/** 分享用的列表图 */
	private String iconUrl;

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public LinkedHashMap<String, String> getImgs() {
		return imgs;
	}

	public void setImgs(LinkedHashMap<String, String> imgs) {
		this.imgs = imgs;
	}

	public String getRootId() {
		return rootId;
	}

	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public long getcTime() {
		return cTime;
	}

	public void setcTime(long cTime) {
		this.cTime = cTime;
	}

	public String getcFrom() {
		return cFrom;
	}

	public void setcFrom(String cFrom) {
		this.cFrom = cFrom;
	}

	public String getWapUrl() {
		return wapUrl;
	}

	public void setWapUrl(String wapUrl) {
		this.wapUrl = wapUrl;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public String getAdId() {
		return adId;
	}

	public void setAdId(String adId) {
		this.adId = adId;
	}

	public String getAdTitle() {
		return adTitle;
	}

	public void setAdTitle(String adTitle) {
		this.adTitle = adTitle;
	}

	public String getAdUrl() {
		return adUrl;
	}

	public void setAdUrl(String adUrl) {
		this.adUrl = adUrl;
	}

	public String getAdIconUrl() {
		return adIconUrl;
	}

	public void setAdIconUrl(String adIconUrl) {
		this.adIconUrl = adIconUrl;
	}

	public int getCanComment() {
		return canComment;
	}

	public void setCanComment(int canComment) {
		this.canComment = canComment;
	}

	public String getForwardId() {
		return forwardId;
	}

	public void setForwardId(String forwardId) {
		this.forwardId = forwardId;
	}

	/**
	 * json解析
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public void paserJson(JSONObject json) throws JSONException {
		if (json.has(IParam.CONTENT)) {
			this.content = json.getString(IParam.CONTENT);
		}
		if (json.has(IParam.SUMMARY)) {
			this.summary = json.getString(IParam.SUMMARY);
		}
		if (json.has(IParam.SUBTITLE)) {
			this.subTitle = json.getString(IParam.SUBTITLE);
		}
		if (json.has(IParam.TITLE)) {
			this.title = json.getString(IParam.TITLE);
		}
		if (json.has(IParam.IMG_URL)) {
			this.imgUrl = json.getString(IParam.IMG_URL);
		}
		if (json.has(IParam.FROM)) {
			this.cFrom = json.getString(IParam.FROM);
		}
		if (json.has(IParam.C_TIME)) {
			this.cTime = json.getLong(IParam.C_TIME);
		}
		if (json.has(IParam.WAP_URL)) {
			this.wapUrl = json.getString(IParam.WAP_URL);
		}
		if (json.has(IParam.AD_ID)) {
			this.adId = json.getString(IParam.AD_ID);
		}
		if (json.has(IParam.AD_TITLE)) {
			this.adTitle = json.getString(IParam.AD_TITLE);
		}
		if (json.has(IParam.AD_URL)) {
			this.adUrl = json.getString(IParam.AD_URL);
		}
		if (json.has(IParam.SHORT_URL)) {
			this.shortUrl = json.getString(IParam.SHORT_URL);
		}
		if (json.has(IParam.ARTICLE)) {
			this.canComment = json.getInt(IParam.ARTICLE);
		}
		if (json.has(IParam.AD_IMG_URL)) {
			this.adIconUrl = json.getString(IParam.AD_IMG_URL);
		}
		if (json.has(IParam.FORWARD_ID)) {
			this.forwardId = json.getString(IParam.FORWARD_ID);
		}
		if (json.has(IParam.ROOT_ID)) {
			this.rootId = json.getString(IParam.ROOT_ID);
		}
		if (json.has(IParam.COMMENT_NUM)) {
			this.commentNum = json.getInt(IParam.COMMENT_NUM);
		}
		if (json.has(IParam.ICON_URL)) {
			this.iconUrl = json.getString(IParam.ICON_URL);
		}
		if (json.has(IParam.IMGS)) {
			this.imgs = new LinkedHashMap<>();
			JSONArray jsonArray = json.getJSONArray(IParam.IMGS);
			String key;
			String value;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Iterator<String> keys = jsonObject.keys();
				while (keys.hasNext()) {
					key = keys.next();
					value = jsonObject.getString(key);
					imgs.put(key, value);
				}
			}
		}
	}
}
