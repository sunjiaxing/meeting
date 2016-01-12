package com.zhengshang.meeting.http;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import android.content.Context;
import android.util.Log;

import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.exeception.AppException;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.config.RequestConfig;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Apache httpCLient辅助类
 *
 * @author sun
 */
public class HttpUtilsApache {

    private static final int connectionTimeout = 5000 * 10;
    private static final int readTimeout = 15000 * 10;
    private static final Boolean DEBUG = false;
    private static final String TAG = "HttpUtilsApache";

    /**
     * 创建连接
     *
     * @return
     * @author sun
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
     * @param context
     * @param url
     * @param header
     * @return
     * @throws IOException
     * @author sun
     */
    public static String get(Context context, String url,
                             Map<String, String> header) {
        try {
            HttpGet httpGet = new HttpGet(url);
            if (header != null) {
                Iterator<?> iterator = header.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    httpGet.setHeader(key, header.get(key));
                }
            }
            return execute(context, httpGet);
        } catch (Exception e) {
            // 仅供测试使用
            if (DEBUG) {
                Log.d(TAG, "get请求异常，url:" + url);
            }
            throw new AppException(context.getString(R.string.netconnecterror));
        }
    }

    /**
     * post 上传图片请求
     *
     * @param context
     * @param url
     * @param params
     * @param header
     * @return
     * @author sun
     */
    public static String post(Context context, String url,
                              Map<String, Object> params, Map<String, String> header,
                              List<File> files, boolean isHead) {
        try {
            HttpPost httpPost = new HttpPost(url);
            // 处理header
            if (header != null) {
                Iterator<?> iterator = header.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    httpPost.setHeader(key, header.get(key));
                }
            }
//            HttpEntity requestEntity;
            // 处理params
//            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            List<NameValuePair> nvps = new ArrayList<>();
            if (params != null) {
                Iterator<?> iterator = params.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
//                    builder.addTextBody(key, params.get(key) != null ? params
//                            .get(key).toString() : "");
                    nvps.add(new BasicNameValuePair(key, params.get(key) != null ? params
                            .get(key).toString() : ""));
                }
            }
//            if (files != null) {
//                for (File file : files) {
//                    builder.addBinaryBody(
//                            isHead ? "avatarFile" : "pictureFile", file,
//                            ContentType.DEFAULT_BINARY, file.getName());
//                }
//            }
//            requestEntity = builder.build();
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf8"));
            return execute(context, httpPost);
        } catch (Exception e) {
            // 仅供测试使用
            if (DEBUG) {
                Log.d(TAG, "post请求异常，url:" + url);
            }
            throw new AppException(context.getString(R.string.netconnecterror));
        }
    }

    /**
     * 单独的post请求
     *
     * @param context
     * @param url
     * @param params
     * @param header
     * @return
     * @author sun
     */
    public static String post(Context context, String url,
                              Map<String, Object> params, Map<String, String> header) {
        return post(context, url, params, header, null, false);
    }

    /**
     * 执行 请求
     *
     * @param context
     * @param request
     * @return
     * @throws Exception
     * @author sun
     */
    private static String execute(Context context, HttpUriRequest request)
            throws Exception {
        String result = null;
        CloseableHttpClient client = createClient();
        CloseableHttpResponse response = null;
        try {
            response = client.execute(request);
            if (response != null
                    && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity());
                if (Utils.isEmpty(result)) {
                    throw new AppException(
                            context.getString(R.string.netconnecterror));
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
     * 释放连接
     *
     * @param httpClient
     * @param request
     * @param response
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
