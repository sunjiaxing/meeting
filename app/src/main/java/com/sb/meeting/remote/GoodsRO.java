package com.sb.meeting.remote;

import android.content.Context;

import com.sb.meeting.common.BonConstants;
import com.sb.meeting.common.Utils;
import com.sb.meeting.exeception.AppException;
import com.sb.meeting.remote.dto.GoodsDetailDto;
import com.sb.meeting.remote.dto.GoodsDto;
import com.sb.meeting.remote.dto.NameAndValueDto;
import com.sb.meeting.ui.vo.GoodsVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        GET_VALID_TIME(IParam.GET_VALID_TIME), PUBLISH(IParam.PUBLISH_GOODS),
        DETAIL(IParam.DETAIL), ATTENTION(IParam.ATTENTION),
        PUBLISHED_GOODS(IParam.MY_LIST), ATTENTION_GOODS(IParam.MY_ATTENTION), UPDATE_GOODS(IParam.UPDATE_GOODS);

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
     *
     * @param goodsVO 物品对象
     * @param userId  用户id
     * @param mobile  手机号
     * @param imgJson 图片+描述 json
     * @return
     * @throws JSONException
     */
    public int publishGoods(GoodsVO goodsVO, String userId, String mobile, String imgJson) throws JSONException {
        String url = getServerUrl() + RemoteGoodsURL.PUBLISH.getURL();
        Map<String, Object> params = new HashMap<>();
        params.put(IParam.USER_ID, userId);
        params.put(IParam.USER_CONTACT, mobile);
        params.put(IParam.GOODS_NAME, goodsVO.getName());
        params.put(IParam.COVER_URL, goodsVO.getCoverUrl());
        params.put(IParam.CATEGORY, goodsVO.getCategory().getId());
        params.put(IParam.MARKET_PRICE, goodsVO.getMarketPrice());
        params.put(IParam.EXCHANGE_PRICE, goodsVO.getExchangePrice());
        params.put(IParam.VALID_TIME, goodsVO.getValidTime().getId());
        params.put(IParam.COUNT, goodsVO.getCount());
        params.put(IParam.IMAGES, imgJson);
        if (goodsVO.getNeedCategory() != null) {
            params.put(IParam.NEED_CATEGORY, goodsVO.getNeedCategory().getId());
        }
        String result = httpPostRequest(url, null, params);
        JSONObject json = new JSONObject(result);
        if (json.getInt(IParam.STATUS) == 1) {
            return json.getInt(IParam.GOODS_ID);
        } else {
            throw new AppException(json.getInt(IParam.ERROR_CODE));
        }
    }

    /**
     * 获取物品列表
     *
     * @param userId    用户id
     * @param pageIndex 页码
     * @param limit     每页数量
     * @return
     * @throws JSONException
     */
    public List<GoodsDto> getGoodsList(String userId, int pageIndex, int limit) throws JSONException {
        String url = getServerUrl() + RemoteGoodsURL.LIST.getURL()
                + IParam.WENHAO + IParam.USER_ID + IParam.EQUALS_STRING + userId
                + IParam.AND + IParam.PAGE_INDEX + IParam.EQUALS_STRING + pageIndex
                + IParam.AND + IParam.LIMIT + IParam.EQUALS_STRING + limit;
        String result = httpGetRequest(url, null);
        JSONObject json = new JSONObject(result);
        if (json.getInt(IParam.STATUS) == 1) {
            JSONArray array = json.getJSONArray(IParam.LIST);
            List<GoodsDto> web = new ArrayList<>();
            GoodsDto dto;
            for (int i = 0; i < array.length(); i++) {
                dto = new GoodsDto();
                dto.parseJson(array.getJSONObject(i));
                web.add(dto);
            }
            return web;
        } else {
            throw new AppException(json.getInt(IParam.ERROR_CODE));
        }
    }

    /**
     * 获取 物品详情
     *
     * @param goodsId 物品id
     * @param userId  用户id
     * @return
     * @throws JSONException
     */
    public GoodsDetailDto getGoodsDetail(int goodsId, String userId) throws JSONException {
        String url = getServerUrl() + RemoteGoodsURL.DETAIL.getURL()
                + IParam.WENHAO + IParam.GOODS_ID + IParam.EQUALS_STRING + goodsId
                + IParam.AND + IParam.USER_ID + IParam.EQUALS_STRING + userId;
        String result = httpGetRequest(url, null);
        JSONObject json = new JSONObject(result);
        if (json.getInt(IParam.STATUS) == 1) {
            GoodsDetailDto dto = new GoodsDetailDto();
            dto.parseJson(json.getJSONObject(IParam.DETAIL));
            return dto;
        } else {
            throw new AppException(json.getInt(IParam.ERROR_CODE));
        }
    }

    /**
     * 关注
     *
     * @param goodsId 物品id
     * @param userId  用户id
     * @param option  0关注   1取消关注
     * @return
     */
    public boolean attention(int goodsId, String userId, int option) throws JSONException {
        String url = getServerUrl() + RemoteGoodsURL.ATTENTION.getURL()
                + IParam.WENHAO + IParam.GOODS_ID + IParam.EQUALS_STRING + goodsId
                + IParam.AND + IParam.USER_ID + IParam.EQUALS_STRING + userId
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
     * 获取发布的物品列表
     *
     * @param userId    用户id
     * @param pageIndex 页码
     * @param limit     每页条数
     * @return
     * @throws JSONException
     */
    public List<GoodsDto> getPublishedGoods(String userId, int pageIndex, int limit) throws JSONException {
        String url = getServerUrl() + RemoteGoodsURL.PUBLISHED_GOODS.getURL()
                + IParam.WENHAO + IParam.USER_ID + IParam.EQUALS_STRING + userId
                + IParam.AND + IParam.PAGE_INDEX + IParam.EQUALS_STRING + pageIndex
                + IParam.AND + IParam.LIMIT + IParam.EQUALS_STRING + limit;
        String result = httpGetRequest(url, null);
        JSONObject json = new JSONObject(result);
        if (json.getInt(IParam.STATUS) == 1) {
            List<GoodsDto> data = null;
            JSONArray array = json.getJSONArray(IParam.LIST);
            if (array.length() > 0) {
                data = new ArrayList<>();
                GoodsDto dto;
                for (int i = 0; i < array.length(); i++) {
                    dto = new GoodsDto();
                    dto.parseJson(array.getJSONObject(i));
                    data.add(dto);
                }
            }
            return data;
        } else {
            throw new AppException(json.getInt(IParam.ERROR_CODE));
        }
    }

    /**
     * 获取 我关注的物品列表
     *
     * @param userId    用户id
     * @param pageIndex 页码
     * @param limit     每页数量
     * @return
     * @throws JSONException
     */
    public List<GoodsDto> getAttentionGoods(String userId, int pageIndex, int limit) throws JSONException {
        String url = getServerUrl() + RemoteGoodsURL.ATTENTION_GOODS.getURL()
                + IParam.WENHAO + IParam.USER_ID + IParam.EQUALS_STRING + userId
                + IParam.AND + IParam.PAGE_INDEX + IParam.EQUALS_STRING + pageIndex
                + IParam.AND + IParam.LIMIT + IParam.EQUALS_STRING + limit;
        String result = httpGetRequest(url, null);
        JSONObject json = new JSONObject(result);
        if (json.getInt(IParam.STATUS) == 1) {
            List<GoodsDto> data = null;
            JSONArray array = json.getJSONArray(IParam.LIST);
            if (array.length() > 0) {
                data = new ArrayList<>();
                GoodsDto dto;
                for (int i = 0; i < array.length(); i++) {
                    dto = new GoodsDto();
                    dto.parseJson(array.getJSONObject(i));
                    data.add(dto);
                }
            }
            return data;
        } else {
            throw new AppException(json.getInt(IParam.ERROR_CODE));
        }
    }

    /**
     * 发布物品
     *
     * @param goodsVO 物品对象
     * @param userId  用户id
     * @param mobile  手机号
     * @param imgJson 图片+描述 json
     * @return
     * @throws JSONException
     */
    public boolean updateGoods(GoodsVO goodsVO, String userId, String mobile, String imgJson) throws JSONException {
        String url = getServerUrl() + RemoteGoodsURL.UPDATE_GOODS.getURL();
        Map<String, Object> params = new HashMap<>();
        params.put(IParam.USER_ID, userId);
        params.put(IParam.GOODS_ID, goodsVO.getId());
        if (!Utils.isEmpty(mobile)) {
            params.put(IParam.USER_CONTACT, mobile);
        }
        if (!Utils.isEmpty(goodsVO.getName())) {
            params.put(IParam.GOODS_NAME, goodsVO.getName());
        }
        if (!Utils.isEmpty(goodsVO.getCoverUrl())) {
            params.put(IParam.COVER_URL, goodsVO.getCoverUrl());
        }
        if (goodsVO.getCategory() != null && goodsVO.getCategory().getId() != 0) {
            params.put(IParam.CATEGORY, goodsVO.getCategory().getId());
        }
        if (goodsVO.getMarketPrice() > 0) {
            params.put(IParam.MARKET_PRICE, goodsVO.getMarketPrice());
        }
        if (goodsVO.getExchangePrice() > 0) {
            params.put(IParam.EXCHANGE_PRICE, goodsVO.getExchangePrice());
        }
        if (goodsVO.getValidTime() != null && goodsVO.getValidTime().getId() != 0) {
            params.put(IParam.VALID_TIME, goodsVO.getValidTime().getId());
        }
        if (goodsVO.getCount() > 0) {
            params.put(IParam.COUNT, goodsVO.getCount());
        }
        if (!Utils.isEmpty(imgJson)) {
            params.put(IParam.IMAGES, imgJson);
        }
        if (goodsVO.getNeedCategory() != null && goodsVO.getNeedCategory().getId() > 0) {
            params.put(IParam.NEED_CATEGORY, goodsVO.getNeedCategory().getId());
        }
        String result = httpPostRequest(url, null, params);
        JSONObject json = new JSONObject(result);
        if (json.getInt(IParam.STATUS) == 1) {
            return true;
        } else {
            throw new AppException(json.getInt(IParam.ERROR_CODE));
        }
    }
}
