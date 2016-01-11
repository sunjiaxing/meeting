package com.zhengshang.meeting.remote;

import android.content.Context;

import com.zhengshang.meeting.common.BonConstants;
import com.zhengshang.meeting.exeception.AppException;
import com.zhengshang.meeting.remote.dto.UserDto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sun on 2016/1/11.
 */
public class UserRO extends BaseRO {

    public UserRO(Context context) {
        super(context);
    }

    public enum RemoteUserURL implements IBaseURL {
        LOGIN(IParam.LOGIN);
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

    public UserDto login(String userName, String password) throws JSONException {
        String url = getServerUrl() + RemoteUserURL.LOGIN.getURL();
        Map<String, Object> params = new HashMap<>();
        params.put(IParam.USER_NAME, userName);
        params.put(IParam.PASSWORD, password);
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
}
