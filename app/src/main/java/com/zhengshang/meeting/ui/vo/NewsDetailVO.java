package com.zhengshang.meeting.ui.vo;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * newsDetail VO
 */
public class NewsDetailVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean canComment;
	private boolean isFavorate;
	private String shortUrl;
	private String wapUrl;
	private String title;
	private String summary;
	private String id;

	private String adId;
	private String adUrl;
	private String adTitle;
	private String adIconUrl;

	private String cFrom;
	private String cTime;
	private String content;

	private String iconUrl;
	private String forwardId;
	private int commentNum;
	private String rootId;
	private LinkedHashMap<String, String> imgs;

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

	public String getForwardId() {
		return forwardId;
	}

	public void setForwardId(String forwardId) {
		this.forwardId = forwardId;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public boolean isCanComment() {
		return canComment;
	}

	public void setCanComment(boolean canComment) {
		this.canComment = canComment;
	}

	public boolean isFavorate() {
		return isFavorate;
	}

	public void setFavorate(boolean isFavorate) {
		this.isFavorate = isFavorate;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public String getWapUrl() {
		return wapUrl;
	}

	public void setWapUrl(String wapUrl) {
		this.wapUrl = wapUrl;
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

}
