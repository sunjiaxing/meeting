package com.zhengshang.meeting.http;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import android.util.Log;

import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.BonConstants;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.exeception.AppException;

import cz.msebera.android.httpclient.Consts;
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
 */
public class HttpUtilsApache {

    private static final int connectionTimeout = 5000;
    private static final int readTimeout = 15000;
    private static final String TAG = "HttpUtilsApache";

    /**
     * 创建连接
     *
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
     * @param url    url
     * @param header 请求头信息
     * @return
     * @throws IOException
     */
    public static String get(String url,
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
            return execute(httpGet);
        } catch (Exception e) {
            // 仅供测试使用
            if (BonConstants.DEBUG) {
                Log.e(TAG, "get请求异常，url:" + url);
            }
            throw new AppException("网络连接异常, 请稍后再试！");
        }
    }

    /**
     * post 上传图片请求
     *
     * @param url    url
     * @param params 参数
     * @param header 请求头信息
     * @param file   要上传的文件
     * @return
     */
    public static String postFile(String url, Map<String, Object> params, Map<String, String> header,
                                  File file) {
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

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            if (params != null) {
                Iterator<?> iterator = params.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    builder.addTextBody(key, params.get(key) != null ? params
                            .get(key).toString() : "");
                }
            }
            if (file != null) {
                builder.addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, file.getName());
            }
            httpPost.setEntity(builder.build());
            return execute(httpPost);
        } catch (Exception e) {
            // 仅供测试使用
            if (BonConstants.DEBUG) {
                Log.d(TAG, "post请求异常，url:" + url);
            }
            throw new AppException("网络连接异常, 请稍后再试！");
        }
    }

    /**
     * 单独的post请求
     *
     * @param url    url
     * @param params 参数
     * @param header 请求头信息
     * @return
     */
    public static String post(String url,
                              Map<String, Object> params, Map<String, String> header) {
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
            List<NameValuePair> requestParams = new ArrayList<>();
            if (params != null) {
                Iterator<?> iterator = params.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    requestParams.add(new BasicNameValuePair(key, params.get(key) != null ? params
                            .get(key).toString() : ""));
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(requestParams, Consts.UTF_8));
            return execute(httpPost);
        } catch (Exception e) {
            // 仅供测试使用
            if (BonConstants.DEBUG) {
                Log.e(TAG, "post请求异常，url:" + url);
            }
            throw new AppException("网络连接异常, 请稍后再试！");
        }
    }

    /**
     * 执行 请求
     *
     * @param request 请求
     * @return
     * @throws Exception
     */
    private static String execute(HttpUriRequest request)
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
                    throw new AppException("网络连接异常, 请稍后再试！");
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
     * @param httpClient httpclient
     * @param request    请求
     * @param response   响应
     */
    private static void releaseConnection(CloseableHttpClient httpClient,
                                          HttpUriRequest request, CloseableHttpResponse response) {
        try {
            if (response != null) {
                response.close();
            }
            if (request != null) {
                request.abort();
            }
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
