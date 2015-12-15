package com.zhengshang.meeting.remote;

import java.util.HashMap;

import java.util.Map;


import android.content.Context;
import android.util.Log;

import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.BonConstants;
import com.zhengshang.meeting.common.HttpUtilsApache;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.exeception.AppException;


/**
 * 引擎类基类
 * 
 * @author sun
 * 
 */
public class BaseRO {
	protected Context mContext;
	protected HashMap<String, String> headerParam;
	private boolean debug = false;

	protected HashMap<String, String> getHeaderParam(String key, String value) {
		headerParam = new HashMap<>();
		headerParam.put(key, value);
		return headerParam;
	}

	public BaseRO(Context context) {
		this.mContext = context;
	}

	public interface IBaseURL {
		String getURL();
	}

	/**
	 * get请求
	 * 
	 * @author sun 2015年8月13日13:38:38
	 * @param serverUrl
	 * @param headerParams
	 *            头信息
	 * @return
	 */
	protected String httpGetRequest(String serverUrl,
			Map<String, String> headerParams) {
		if (!Utils.isNetworkValidate(mContext)) {
			throw new AppException(mContext.getString(R.string.netconnecterror));
		}
		String res = HttpUtilsApache.get(mContext, serverUrl, headerParams);
		if (debug) {
			Log.e("===============", "url:" + serverUrl + " **res:" + res);
		}
		return res;
	}

	/**
	 * post请求
	 * 
	 * @author sun 2015年8月13日13:38:52
	 * @param url
	 * @param headerParams
	 * @param params
	 * @return
	 */
	protected String httpPostRequest(String url,
			Map<String, String> headerParams, Map<String, Object> params) {
		if (!Utils.isNetworkValidate(mContext)) {
			throw new AppException(mContext.getString(R.string.netconnecterror));
		}
		String res = HttpUtilsApache.post(mContext, url, params, headerParams);
		if (debug) {
			Log.e("===============", "url:" + url + " **res:" + res);
		}
		return res;
	}

	/**
	 * 获取server链接
	 * @return
	 */
	protected String getServerUrl() {

		return BonConstants.SERVER_URL;
	}

}
