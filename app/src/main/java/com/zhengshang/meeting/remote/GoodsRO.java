package com.zhengshang.meeting.remote;

import android.content.Context;

import com.zhengshang.meeting.common.BonConstants;
import com.zhengshang.meeting.exeception.AppException;
import com.zhengshang.meeting.remote.dto.NameAndValueDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 易物 RO
 * Created by sun on 2016/2/23.
 */
public class GoodsRO extends BaseRO {

    public GoodsRO(Context context) {
        super(context);
    }

    public enum RemoteGoodsURL implements IBaseURL {
        LIST(IParam.LIST), CATEGORIES(IParam.CATEGORIES), GET_VALID_TIME(IParam.GET_VALID_TIME);

        private static final String NAMESPACE = IParam.GOODS;
        private String url;

        RemoteGoodsURL(String mapping) {
            url = NAMESPACE + BonConstants.SLASH + mapping;
        }

        @Override
        public String getURL() {

            return url;
        }
    }

    /**
     * 获取物品分类信息
     *
     * @return
     * @throws JSONException
     */
    public List<NameAndValueDto> getGoodsCategories() throws JSONException {
        String url = getServerUrl() + RemoteGoodsURL.CATEGORIES.getURL();
        String result = httpGetRequest(url, null);
        JSONObject json = new JSONObject(result);
        if (json.getInt(IParam.STATUS) == 1) {
            JSONArray array = json.getJSONArray(IParam.LIST);
            List<NameAndValueDto> data = new ArrayList<>();
            NameAndValueDto dto;
            for (int i = 0; i < array.length(); i++) {
                dto = new NameAndValueDto();
                dto.parseJson(array.getJSONObject(i));
                data.add(dto);
            }
            return data;
        } else {
            throw new AppException(json.getInt(IParam.ERROR_CODE));
        }
    }

    /**
     * 获取有效时间
     * @return
     * @throws JSONException
     */
    public List<NameAndValueDto> getValidTime() throws JSONException {
        String url = getServerUrl() + RemoteGoodsURL.GET_VALID_TIME.getURL();
        String result = httpGetRequest(url, null);
        JSONObject json = new JSONObject(result);
        if (json.getInt(IParam.STATUS) == 1) {
            JSONArray array = json.getJSONArray(IParam.LIST);
            List<NameAndValueDto> data = new ArrayList<>();
            NameAndValueDto dto;
            for (int i = 0; i < array.length(); i++) {
                dto = new NameAndValueDto();
                dto.parseJson(array.getJSONObject(i));
                data.add(dto);
            }
            return data;
        } else {
            throw new AppException(json.getInt(IParam.ERROR_CODE));
        }
    }
}
