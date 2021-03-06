package com.sb.meeting.remote;

import android.content.Context;

import com.sb.meeting.common.BonConstants;
import com.sb.meeting.common.Utils;
import com.sb.meeting.exeception.AppException;
import com.sb.meeting.remote.dto.FavoriteDto;
import com.sb.meeting.remote.dto.UserDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户 服务器交互 RO
 * Created by sun on 2016/1/11.
 */
public class UserRO extends BaseRO {

    public UserRO(Context context) {
        super(context);
    }

    public enum RemoteUserURL implements IBaseURL {
        LOGIN(IParam.LOGIN), ADD_FAVORITE(IParam.ADD_FAVORITE),
        FAVORITE_LIST(IParam.FAVORITE), DELETE_FAVORITE(IParam.DELETE_FAVORITE),
        DELETE_ALL_FAVORITE(IParam.DELETE_ALL_FAVORITE), REGISTER(IParam.REGISTER),
        GET_CODE(IParam.CODE);
        private static final String NAMESPACE = IParam.USER;
        private String url;

        RemoteUserURL(String mapping) {
            url = NAMESPACE + BonConstants.SLASH + mapping;
        }

        @Override
        public String getURL() {
            return url;
        }
    }

    /**
     * 登录
     *
     * @param userName       用户名
     * @param password       密码
     * @param registrationID JPush单点推送 使用的id
     * @return
     * @throws JSONException
     */
    public UserDto login(String userName, String password, String registrationID) throws JSONException {
        String url = getServerUrl() + RemoteUserURL.LOGIN.getURL();
        Map<String, Object> params = new HashMap<>();
        params.put(IParam.USER_NAME, userName);
        params.put(IParam.PASSWORD, password);
        params.put(IParam.REGISTRATION_ID, registrationID);
        String result = httpPostRequest(url, null, params);
        JSONObject json = new JSONObject(result);
        if (json.getInt(IParam.STATUS) == 1) {
            UserDto userDto = new UserDto();
            userDto.parseJson(json.getJSONObject(IParam.USER));
            return userDto;
        } else {
            throw new AppException(json.getInt(IParam.ERROR_CODE));
        }
    }

    /**
     * 添加新闻收藏
     *
     * @param userId 用户id
     * @param newsId 新闻id
     * @return
     * @throws JSONException
     */
    public boolean addFavorite(String userId, String newsId) throws JSONException {
        String url = getServerUrl() + RemoteUserURL.ADD_FAVORITE.getURL()
                + IParam.WENHAO + IParam.USER_ID + IParam.EQUALS_STRING + userId
                + IParam.AND + IParam.NEWS_ID + IParam.EQUALS_STRING + newsId;
        String result = httpGetRequest(url, null);
        JSONObject json = new JSONObject(result);
        if (json.getInt(IParam.STATUS) == 1) {
            return true;
        } else {
            throw new AppException(json.getInt(IParam.ERROR_CODE));
        }
    }

    /**
     * 获取收藏列表
     *
     * @param userId 用户id
     * @return
     * @throws JSONException
     */
    public List<FavoriteDto> getFavoriteList(String userId) throws JSONException {
        String url = getServerUrl() + RemoteUserURL.FAVORITE_LIST.getURL()
                + IParam.WENHAO + IParam.USER_ID + IParam.EQUALS_STRING + userId;
        String result = httpGetRequest(url, null);
        JSONObject json = new JSONObject(result);
        if (json.getInt(IParam.STATUS) == 1) {
            JSONArray array = json.getJSONArray(IParam.LIST);
            List<FavoriteDto> list = null;
            if (array.length() > 0) {
                list = new ArrayList<>();
                FavoriteDto dto;
                for (int i = 0; i < array.length(); i++) {
                    dto = new FavoriteDto();
                    dto.parseJson(array.getJSONObject(i));
                    list.add(dto);
                }
            }
            return list;
        } else {
            throw new AppException(json.getInt(IParam.ERROR_CODE));
        }
    }

    /**
     * 删除 所有收藏
     *
     * @param userId 用户id
     * @return
     * @throws JSONException
     */
    public boolean deleteAllFavorite(String userId) throws JSONException {
        String url = getServerUrl() + RemoteUserURL.DELETE_ALL_FAVORITE.getURL()
                + IParam.WENHAO + IParam.USER_ID + IParam.EQUALS_STRING + userId;
        String result = httpGetRequest(url, null);
        JSONObject json = new JSONObject(result);
        if (json.getInt(IParam.STATUS) == 1) {
            return true;
        } else {
            throw new AppException(json.getInt(IParam.ERROR_CODE));
        }
    }

    /**
     * 删除指定 收藏
     *
     * @param userId 用户id
     * @param id     收藏id
     * @return
     * @throws JSONException
     */
    public boolean deleteFavoriteById(String userId, String id) throws JSONException {
        String url = getServerUrl() + RemoteUserURL.DELETE_FAVORITE.getURL()
                + IParam.WENHAO + IParam.USER_ID + IParam.EQUALS_STRING + userId
                + IParam.AND + IParam.FAVORITE_ID + IParam.EQUALS_STRING + id;
        String result = httpGetRequest(url, null);
        JSONObject json = new JSONObject(result);
        if (json.getInt(IParam.STATUS) == 1) {
            return true;
        } else {
            throw new AppException(json.getInt(IParam.ERROR_CODE));
        }
    }

    /**
     * 注册
     *
     * @param phone    手机号
     * @param code     验证码
     * @param password 密码
     * @return
     * @throws JSONException
     */
    public boolean register(String phone, String code, String password) throws JSONException {
        String url = getServerUrl() + RemoteUserURL.REGISTER.getURL();
        Map<String, Object> params = new HashMap<>();
        params.put(IParam.MOBILE, phone);
        params.put(IParam.CODE, code);
        params.put(IParam.PASSWORD, password);
        String result = httpPostRequest(url, null, params);
        JSONObject json = new JSONObject(result);
        if (json.getInt(IParam.STATUS) == 1) {
            return true;
        } else {
            throw new AppException(json.getInt(IParam.ERROR_CODE));
        }
    }

    /**
     * 获取验证码
     *
     * @param phone  手机号
     * @param option 操作符
     * @return
     * @throws JSONException
     */
    public boolean getCode(String phone, int option) throws JSONException {
        String url = getServerUrl() + "common/message"
                + IParam.WENHAO + IParam.MOBILE + IParam.EQUALS_STRING + phone
                + IParam.AND + IParam.OPTION + IParam.EQUALS_STRING + option;
        String result = httpGetRequest(url, null);
        JSONObject json = new JSONObject(result);
        if (json.getInt(IParam.STATUS) == 1) {
            return true;
        } else {
            throw new AppException(json.getInt(IParam.ERROR_CODE));
        }
    }

    /**
     * 版本更新
     *
     * @throws JSONException
     */
    public Map<String, Object> updateVersion() throws JSONException {
        String result = httpGetRequest("http://update.com", null);
        JSONObject json = new JSONObject(result);
        if (json.has(IParam.VERSION_CODE)) {
            String versionNub = json.getString(IParam.VERSION_CODE);
            if (!Utils.isEmpty(versionNub)
                    && versionNub.compareTo(Utils.getVersionCode(mContext)) > 0) {
                // 获取更新标题
                String title = "更新提示";
                if (json.has(IParam.TITLE)) {
                    title = json.getString(IParam.TITLE);
                }
                // 获取更新信息的描述
                String desc = "有新版本，是否更新？";
                if (json.has(IParam.DESCRIPTION)) {
                    desc = json.getString(IParam.DESCRIPTION);
                }
                // 获取应用的下载地址
                String url = json.getString(IParam.URL);
                boolean focus = false;
                if (json.has(IParam.FOCUS)) {
                    focus = json.getBoolean(IParam.FOCUS);
                }
                Map<String, Object> data = new HashMap<>();
                data.put(IParam.TITLE, title);
                data.put(IParam.DESC, desc);
                data.put(IParam.URL, url);
                data.put(IParam.FOCUS, focus);

                return data;
            }
        }
        return null;
    }
}
