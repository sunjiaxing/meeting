package com.zhengshang.meeting.common;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;


import org.json.JSONObject;

import android.content.Context;
import android.os.Build;

import com.zhengshang.meeting.R;
import com.zhengshang.meeting.exeception.AppException;
import com.zhengshang.meeting.remote.IParam;
import com.zhengshang.meeting.ui.activity.BaseActivity;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.client.config.RequestConfig;
import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Apache httpCLient辅助类
 * 
 * @author sun 2015年8月12日17:02:21
 * 
 */
public class HttpUtilsApache {

	private static final int connectionTimeout = 5000;
	private static final int readTimeout = 15000;

	/**
	 * 创建连接
	 * 
	 * @author sun 2015年8月12日18:18:09
	 * @return
	 */
	private static CloseableHttpClient createClient() {
		try {
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(connectionTimeout)
					.setSocketTimeout(readTimeout)
					.setConnectionRequestTimeout(readTimeout).build();
			return HttpClients.custom().setDefaultRequestConfig(requestConfig)
					.setMaxConnTotal(200)
					.setSslcontext(SSLContext.getDefault()).build();
		} catch (Exception e) {
			return HttpClients.createSystem();
		}

	}

	/**
	 * get 方法 使用 Apache httpCLient
	 * 
	 * @author sun 2015年8月12日16:15:55
	 * @param context
	 * @param url
	 * @param header
	 * @return
	 * @throws IOException
	 */
	public static String get(Context context, String url,
			Map<String, String> header) {
		try {
			HttpGet httpGet = new HttpGet(url);
			header = addOtherParamToHeader(header);
			if (header != null) {
				Iterator<?> iterator = header.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next().toString();
					httpGet.setHeader(key, header.get(key));
				}
			}
			return execute(context, httpGet);
		} catch (Exception e) {
			// TODO 仅供测试使用
			// DouliApplication.getInstance().collectDeviceInfo(context);
			// DouliApplication.getInstance().saveCrashInfo2File(e,
			// "get请求异常，url:" + url);
			throw new AppException(context.getString(R.string.netconnecterror));
		}
	}

	/**
	 * post 上传图片请求
	 * 
	 * @author sun 2015年8月12日16:34:35
	 * @param context
	 * @param url
	 * @param params
	 * @param header
	 * @return
	 */
	public static String post(Context context, String url,
			Map<String, Object> params, Map<String, String> header,
			List<File> files, boolean isHead) {
		try {
			HttpPost httpPost = new HttpPost(url);
			header = addOtherParamToHeader(header);
			// 处理header
			if (header != null) {
				Iterator<?> iterator = header.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next().toString();
					httpPost.setHeader(key, header.get(key));
				}
			}
			HttpEntity requestEntity = null;
			// 处理params
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			if (params != null) {
				Iterator<?> iterator = params.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next().toString();
					builder.addTextBody(key, params.get(key) != null ? params
							.get(key).toString() : "");
				}
			}
			if (files != null) {
				for (File file : files) {
					builder.addBinaryBody(
							isHead ? "avatarFile" : "pictureFile", file,
							ContentType.DEFAULT_BINARY, file.getName());
				}
			}
			requestEntity = builder.build();
			httpPost.setEntity(requestEntity);
			return execute(context, httpPost);
		} catch (Exception e) {
			// TODO 仅供测试使用
			// DouliApplication.getInstance().collectDeviceInfo(context);
			// DouliApplication.getInstance().saveCrashInfo2File(e,
			// "post请求异常，url:" + url);
			throw new AppException(context.getString(R.string.netconnecterror));
		}
	}

	/**
	 * 单独的post请求
	 * 
	 * @author sun 2015年8月12日17:01:51
	 * @param context
	 * @param url
	 * @param params
	 * @param header
	 * @return
	 */
	public static String post(Context context, String url,
			Map<String, Object> params, Map<String, String> header) {
		return post(context, url, params, header, null, false);
	}

	/**
	 * 执行 请求
	 * 
	 * @author sun 2015年8月12日16:53:15
	 * @param context
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private static String execute(Context context, HttpUriRequest request)
			throws Exception {
		int refreshCount = 0;
		boolean isOK = false;
		String result = null;
		CloseableHttpClient client = createClient();
		CloseableHttpResponse response = null;
		try {
			while (refreshCount < 2 && !isOK) {
				response = client.execute(request);
				if (response != null
						&& response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					result = EntityUtils.toString(response.getEntity());
					if (Utils.isEmpty(result)) {
						throw new AppException(
								context.getString(R.string.netconnecterror));
					}
					JSONObject json = new JSONObject(result);
					if (json.has(IParam.ERROR_CODE)
							&& json.getInt(IParam.ERROR_CODE) == 22000) {
						// token 过期
						response.close();
						response = null;
						request.setHeader(IParam.TOKEN, refreshToken(context));
						refreshCount++;
					} else if (json.has(IParam.ERROR_CODE)
							&& json.getInt(IParam.ERROR_CODE) == 23000) {
//						TODO ((BaseActivity) context).stopLoading();
						// refreshToken 无效
						removeToken(context);
						// 跳转到登录界面
//						TODO ((BaseActivity) context).returnToMainWithLogin();
						break;
					} else {
						isOK = true;
					}
				} else {
					throw new AppException("网络请求异常！"
							+ response.getStatusLine().getStatusCode());
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			releaseConnection(client, request, response);
		}
		return result;
	}

	/**
	 * 将其他属性 写入header中
	 * 
	 * @author sun 2015年8月12日15:57:28
	 * @param header
	 * @throws UnsupportedEncodingException
	 */
	private static Map<String, String> addOtherParamToHeader(
			Map<String, String> header) throws UnsupportedEncodingException {
		if (header == null) {
			header = new HashMap<>();
		}
		header.put("model", Build.MODEL);
		header.put("manufacturer", Build.MANUFACTURER);
		header.put("sdkVersion", Build.VERSION.RELEASE);
		// TODO header中存放应用信息
//		header.put("appVersionName",
//				Utils.getVersionName(DouliApplication.getInstance()));
//		header.put("appVersionCode",
//				Utils.getVersionCode(DouliApplication.getInstance()));
//		header.put("simType", URLEncoder.encode(
//				Utils.getSimType(DouliApplication.getInstance()), "utf-8"));
//		header.put("networkState",
//				Utils.getNetworkState(DouliApplication.getInstance()));
//		header.put(IParam.PHONE_NUM,
//				MasterDao.getInstance(DouliApplication.getInstance())
//						.getMaster().getIdentifyId());
		return header;
	}

	/**
	 * 刷新token
	 * 
	 * @author sun 2014年5月8日15:39:59
	 * @param act
	 * @return
	 */
	private static String refreshToken(Context act) {
//		try {
//			MasterDao masterDao = MasterDao.getInstance(act);
//			TokenDto tokenDto = new UserRO(act).refreshToken(masterDao
//					.getMaster().getRefreshToken());
//			masterDao.updateToken(tokenDto.getToken(),
//					tokenDto.getRefreshToken(), tokenDto.getExpiresIn());
//			return tokenDto.getToken();
//		} catch (Exception e) {
//			LogHelper.addLog("refreshToken Exception:" + e.getMessage());
//			throw new AppException(e.getMessage());
//		}
		// TODO 重新实现 刷新token方法
		return "";
	}

	/**
	 * 移除token
	 * 
	 * @author sun 2014年5月8日15:43:29
	 * @param act
	 */
	private static void removeToken(Context act) {
//		MasterDao.getInstance(act).removeToken();
	}

	/**
	 * 释放连接
	 * 
	 * @param httpClient
	 * @param request
	 * @param response
	 * @author sun 2015年8月12日16:33:48
	 */
	private static void releaseConnection(CloseableHttpClient httpClient,
			HttpUriRequest request, CloseableHttpResponse response) {
		try {
			if (response != null) {
				response.close();
				response = null;
			}
			if (request != null) {
				request.abort();
				request = null;
			}
			if (httpClient != null) {
				httpClient.close();
				httpClient = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
