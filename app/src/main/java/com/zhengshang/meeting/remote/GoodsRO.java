package com.zhengshang.meeting.remote;

import android.content.Context;

import com.zhengshang.meeting.common.BonConstants;
import com.zhengshang.meeting.exeception.AppException;
import com.zhengshang.meeting.remote.dto.NameAndValueDto;
import com.zhengshang.meeting.ui.vo.GoodsVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.NameValuePair;

/**
 * 易物 RO
 * Created by sun on 2016/2/23.
 */
public class GoodsRO extends BaseRO {

    public GoodsRO(Context context) {
        super(context);
    }

    public enum RemoteGoodsURL implements IBaseURL {
        LIST(IParam.LIST), CATEGORIES(IParam.CATEGORIES),
        GET_VALID_TIME(IParam.GET_VALID_TIME), PUBLISH(IParam.PUBLISH_GOODS);

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
     *
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

    /**
     * 发布物品
     * @param goodsVO
     * @param userId
     * @param mobile
     * @param imgJson
     * @return
     * @throws JSONException
     */
    public boolean publishGoods(GoodsVO goodsVO, String userId, String mobile, String imgJson) throws JSONException {
        String url = getServerUrl() + RemoteGoodsURL.PUBLISH.getURL();
        Map<String, Object> params = new HashMap<>();
        params.put(IParam.USER_ID, userId);
        params.put(IParam.USER_CONTACT, mobile);
        params.put(IParam.NAME, goodsVO.getName());
        params.put(IParam.COVER_URL, goodsVO.getCoverUrl());
        params.put(IParam.CATEGORY, goodsVO.getCategory().getId());
        params.put(IParam.MARKET_PRICE, goodsVO.getMarketPrice());
        params.put(IParam.EXCHANGE_PRICE, goodsVO.getExchangePrice());
        params.put(IParam.VALID_TIME, goodsVO.getValidTime().getId());
        params.put(IParam.IMAGES, imgJson);
        params.put(IParam.NEED_CATEGORY, goodsVO.getNeedCategory().getId());
        String result = httpPostRequest(url, null, params);
        JSONObject json = new JSONObject(result);
        if (json.getInt(IParam.STATUS) == 1) {
            return true;
        } else {
            throw new AppException(json.getInt(IParam.ERROR_CODE));
        }
    }
}
