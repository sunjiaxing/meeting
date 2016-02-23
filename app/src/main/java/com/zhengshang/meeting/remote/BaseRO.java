package com.zhengshang.meeting.remote;

import java.util.HashMap;

import java.util.Map;


import android.content.Context;
import android.util.Log;

import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.BonConstants;
import com.zhengshang.meeting.http.HttpUtilsApache;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.exeception.AppException;


/**
 * 引擎类基类
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
     * @param serverUrl url
     * @param headerParams 头信息
     * @return 响应信息
     */
    protected String httpGetRequest(String serverUrl,
                                    Map<String, String> headerParams) {
        if (!Utils.isNetworkValidate(mContext)) {
            throw new AppException(mContext.getString(R.string.netconnecterror));
        }
        String res = HttpUtilsApache.get(serverUrl, headerParams);
        if (debug) {
            Log.e("===============", "url:" + serverUrl + " **res:" + res);
        }
        if (Utils.isEmpty(res)) {
            throw new AppException(mContext.getString(R.string.netconnecterror));
        }
        return res;
    }

    /**
     * post请求
     *
     * @param url url
     * @param headerParams  请求头信息
     * @param params 参数
     * @return 响应信息
     */
    protected String httpPostRequest(String url,
                                     Map<String, String> headerParams, Map<String, Object> params) {
        if (!Utils.isNetworkValidate(mContext)) {
            throw new AppException(mContext.getString(R.string.netconnecterror));
        }
        String res = HttpUtilsApache.post(url, params, headerParams);
        if (debug) {
            Log.e("===============", "url:" + url + " **res:" + res);
        }
        if (Utils.isEmpty(res)) {
            throw new AppException(mContext.getString(R.string.netconnecterror));
        }
        return res;
    }

    /**
     * 获取server链接
     *
     * @return base url
     */
    protected String getServerUrl() {

        return BonConstants.SERVER_URL;
    }

}
