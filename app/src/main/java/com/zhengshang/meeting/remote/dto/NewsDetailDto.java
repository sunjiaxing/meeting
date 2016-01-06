package com.zhengshang.meeting.remote.dto;

import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.remote.IParam;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author sun
 * 新闻详情 dto
 */
public class NewsDetailDto {
	private String title;// 标题
	private String content;
	private long cTime;
	private String cFrom;
	private String contentUrl;

	/** 推广信息Id */
	private String adId;
	/** 推广信息的显示内容 */
	private String adTitle;
	/** 推广信息的url */
	private String adUrl;
	/** 推广信息icon图标url */
	private String adIconUrl;

	/** 分享用的列表图 */
	private String iconUrl;
	private String summary;
	private String longUrl;
	private String shortUrl;

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public long getcTime() {
		return cTime;
	}

	public String getcFrom() {
		return cFrom;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public String getAdId() {
		return adId;
	}

	public String getAdTitle() {
		return adTitle;
	}

	public String getAdUrl() {
		return adUrl;
	}

	public String getAdIconUrl() {
		return adIconUrl;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public String getSummary() {
		return summary;
	}

	public String getLongUrl() {
		return longUrl;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	/**
	 * json解析
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public void paserJson(JSONObject json) throws JSONException {
		// 页面主体显示的内容
		if (json.has(IParam.TITLE)) {
			this.title = json.getString(IParam.TITLE);
		}
		if (json.has(IParam.CONTENT)) {
			this.content = json.getString(IParam.CONTENT);
		}
		if (json.has(IParam.CONTENT_URL)) {
			this.contentUrl = json.getString(IParam.CONTENT_URL);
		}
		if (json.has(IParam.NEWS_FROM)) {
			this.cFrom = json.getString(IParam.NEWS_FROM);
		}
		if (json.has(IParam.CREATE_TIME)) {
			this.cTime = Utils.formateTimeFromPhpToJava(json.getLong(IParam.CREATE_TIME));
		}

		// 用于对外分享的内容
		if (json.has(IParam.SUMMARY)) {
			this.summary = json.getString(IParam.SUMMARY);
		}
		if (json.has(IParam.WAP_URL)) {
			this.longUrl = json.getString(IParam.WAP_URL);
		}
		if (json.has(IParam.SHORT_URL)) {
			this.shortUrl = json.getString(IParam.SHORT_URL);
		}
		if (json.has(IParam.ICON_URL)) {
			this.iconUrl = json.getString(IParam.ICON_URL);
		}

		// 广告的内容
		if (json.has(IParam.AD_ID)) {
			this.adId = json.getString(IParam.AD_ID);
		}
		if (json.has(IParam.AD_TITLE)) {
			this.adTitle = json.getString(IParam.AD_TITLE);
		}
		if (json.has(IParam.AD_URL)) {
			this.adUrl = json.getString(IParam.AD_URL);
		}
		if (json.has(IParam.AD_IMG_URL)) {
			this.adIconUrl = json.getString(IParam.AD_IMG_URL);
		}
	}
}
